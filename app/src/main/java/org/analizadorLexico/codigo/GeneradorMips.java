package org.analizadorLexico.codigo;
import org.analizadorLexico.codigo.Cuarteto;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneradorMips {
    private List<Cuarteto> cuartetos;

    // SEPARAMOS DATA Y TEXTO
    private StringBuilder mipsData;
    private StringBuilder mipsCode;

    private Map<String, Integer> mapaMemoria;
    private Map<String, String> stringPool; // Para guardar literales: "Hola" -> str_1
    private int stackPointerOffset;
    private int paramCounter = 0;
    private int stringLabelCounter = 0;

    public GeneradorMips(List<Cuarteto> cuartetos) {
        this.cuartetos = cuartetos;
        this.mipsData = new StringBuilder();
        this.mipsCode = new StringBuilder();
        this.mapaMemoria = new HashMap<>();
        this.stringPool = new HashMap<>();
        this.stackPointerOffset = -4;
    }

    public void generarArchivo(String rutaSalida) {
        mipsData.append(".data\n");
        mipsData.append("newline: .asciiz \"\\n\"\n");
        mipsData.append("true_msg: .asciiz \"true\"\n");
        mipsData.append("false_msg: .asciiz \"false\"\n");

        // Inicializar Encabezado .text
        mipsCode.append("\n.text\n");
        mipsCode.append(".globl main\n\n");
        // Traducir Cuartetos
        for (Cuarteto c : cuartetos) {
            traducirCuarteto(c);
        }

        // Agregar rutina de impresión
        agregarRutinasSistema();

        // Escribir archivo
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaSalida))) {
            writer.write(mipsData.toString());
            writer.write(mipsCode.toString());
            System.out.println(">> Archivo MIPS generado exitosamente: " + rutaSalida);
        } catch (IOException e) {
            System.err.println("Error escribiendo MIPS: " + e.getMessage());
        }
    }

    private void traducirCuarteto(Cuarteto c) {
        mipsCode.append("# ").append(c.toString()).append("\n");

        switch (c.op) {
            case "LABEL":
                if (c.res.equalsIgnoreCase("main")) {
                    mipsCode.append("main:\n");
                    // Prólogo del main
                    mipsCode.append("    move $fp, $sp\n");
                    mipsCode.append("    sub $sp, $sp, 500\n"); // Reservar espacio grande (Stack)
                } else {
                    mipsCode.append(c.res).append(":\n");
                    if (c.res.startsWith("func_")) {
                        mipsCode.append("    sw $ra, 0($sp)\n"); // Guardar Return Address
                        mipsCode.append("    addiu $sp, $sp, -4\n");
                    }
                }
                break;

            case "GOTO":
                mipsCode.append("    j ").append(c.res).append("\n");
                break;

            case "IF_FALSE": // if_false condicion goto etiqueta
                cargarOperando(c.arg1, "$t0");
                mipsCode.append("    beqz $t0, ").append(c.res).append("\n");
                break;

            case "IF": // if condicion goto etiqueta
                cargarOperando(c.arg1, "$t0");
                mipsCode.append("    bnez $t0, ").append(c.res).append("\n");
                break;

            // --- ARITMÉTICA ---
            case "+": traducirAritmetica("add", c); break;
            case "-": traducirAritmetica("sub", c); break;
            case "*": traducirAritmetica("mul", c); break;
            case "/": traducirAritmetica("div", c); break; // Ojo: div en mips es distinto, pero pseudoinstrucción div $t, $t, $t funciona
            case "%": traducirAritmetica("rem", c); break;

            // --- LÓGICA ---
            case "AND": traducirAritmetica("and", c); break;
            case "OR":  traducirAritmetica("or",  c); break;
            case "NOT":
                cargarOperando(c.arg1, "$t0");
                mipsCode.append("    xori $t0, $t0, 1\n"); // Invertir booleano 0/1
                guardarResultado(c.res, "$t0");
                break;

            // --- RELACIONALES ---
            case "<":  traducirAritmetica("slt", c); break; // Set Less Than
            case ">":
                // x > y es lo mismo que y < x
                cargarOperando(c.arg1, "$t0"); // x
                cargarOperando(c.arg2, "$t1"); // y
                mipsCode.append("    slt $t2, $t1, $t0\n");
                guardarResultado(c.res, "$t2");
                break;
            case "==":
                cargarOperando(c.arg1, "$t0");
                cargarOperando(c.arg2, "$t1");
                mipsCode.append("    seq $t2, $t0, $t1\n");
                guardarResultado(c.res, "$t2");
                break;
            case "!=":
                cargarOperando(c.arg1, "$t0");
                cargarOperando(c.arg2, "$t1");
                mipsCode.append("    sne $t2, $t0, $t1\n");
                guardarResultado(c.res, "$t2");
                break;
            case ">=":
                cargarOperando(c.arg1, "$t0");
                cargarOperando(c.arg2, "$t1");
                mipsCode.append("    sge $t2, $t0, $t1\n");
                guardarResultado(c.res, "$t2");
                break;
            case "<=":
                cargarOperando(c.arg1, "$t0");
                cargarOperando(c.arg2, "$t1");
                mipsCode.append("    sle $t2, $t0, $t1\n");
                guardarResultado(c.res, "$t2");
                break;

            // --- ASIGNACIÓN ---
            case "=":
                cargarOperando(c.arg1, "$t0");
                guardarResultado(c.res, "$t0");
                break;
            case "DECLARE_ARRAY":
                String nombreArr = c.arg1;
                int sizeBytes = Integer.parseInt(c.arg2);

                int baseOffset = getOffsetMemoria(nombreArr);

                stackPointerOffset -= (sizeBytes - 4);

                mipsCode.append("    # Reservado espacio para array '").append(nombreArr)
                        .append("': ").append(sizeBytes).append(" bytes\n");
                break;

            case "ARR_STORE":
                int offsetBase = getOffsetMemoria(c.arg1); // Offset base del array
                cargarOperando(c.arg2, "$t0"); // El offset calculado (0, 4, 8...)

                mipsCode.append("    addiu $t1, $fp, ").append(offsetBase).append("\n");
                mipsCode.append("    sub $t1, $t1, $t0\n");

                cargarOperando(c.res, "$t2");
                mipsCode.append("    sw $t2, 0($t1)\n");
                break;

            case "ARR_LOAD":
                int offBaseLoad = getOffsetMemoria(c.arg1);
                cargarOperando(c.arg2, "$t0");

                mipsCode.append("    addiu $t1, $fp, ").append(offBaseLoad).append("\n");
                mipsCode.append("    sub $t1, $t1, $t0\n");

                mipsCode.append("    lw $t2, 0($t1)\n");
                guardarResultado(c.res, "$t2");
                break;

            // --- FUNCIONES ---
            case "PARAM":
                cargarOperando(c.arg1, "$a" + paramCounter);
                paramCounter++;
                break;

            case "CALL":
                paramCounter = 0; // Resetear para la próxima
                mipsCode.append("    jal ").append(c.arg1).append("\n");
                // Al volver, el resultado está en $v0
                guardarResultado(c.res, "$v0");
                break;

            case "RETURN":
                if (c.arg1 != null) {
                    cargarOperando(c.arg1, "$v0");
                }
                mipsCode.append("    addiu $sp, $sp, 4\n");
                mipsCode.append("    lw $ra, 0($sp)\n");    // Recuperar RA
                mipsCode.append("    jr $ra\n");
                break;

            case "EXIT":
                mipsCode.append("    li $v0, 10\n");
                mipsCode.append("    syscall\n");
                break;

            case "READ_INT":
                mipsCode.append("    li $v0, 5\n");
                mipsCode.append("    syscall\n");

                guardarResultado(c.res, "$v0");
                break;

            case "READ_FLOAT":
                mipsCode.append("    li $v0, 6\n");
                mipsCode.append("    syscall\n");

                mipsCode.append("    mfc1 $t0, $f0\n");

                guardarResultado(c.res, "$t0");
                break;

            case "PRINT":
                manejarPrint(c.arg1);
                imprimirNuevaLinea();
                break;
        }
    }

    private void manejarPrint(String arg) {
        //String Literal
        if (arg.startsWith("\"")) {
            String etiqueta = obtenerEtiquetaString(arg);
            mipsCode.append("    li $v0, 4\n");         // Syscall 4: Print String
            mipsCode.append("    la $a0, ").append(etiqueta).append("\n");
            mipsCode.append("    syscall\n");
        }
        // Booleanos
        else if (arg.equals("true")) {
            mipsCode.append("    li $v0, 4\n");
            mipsCode.append("    la $a0, true_msg\n");
            mipsCode.append("    syscall\n");
        }
        else if (arg.equals("false")) {
            mipsCode.append("    li $v0, 4\n");
            mipsCode.append("    la $a0, false_msg\n");
            mipsCode.append("    syscall\n");
        }
        // Float Literal
        else if (arg.contains(".")) {
            mipsCode.append("    li $v0, 2\n");         // Syscall 2: Print Float
            mipsCode.append("    li.s $f12, ").append(arg).append("\n");
            mipsCode.append("    syscall\n");
        }
        // Variables
        else {
            cargarOperando(arg, "$a0");
            mipsCode.append("    li $v0, 1\n");         // Syscall 1: Print Int
            mipsCode.append("    syscall\n");
        }
    }

    private void imprimirNuevaLinea() {
        mipsCode.append("    li $v0, 4\n");
        mipsCode.append("    la $a0, newline\n");
        mipsCode.append("    syscall\n");
    }

    // Gestiona el String Pool: Si llega "Hola", crea str_1: .asciiz "Hola"
    private String obtenerEtiquetaString(String literal) {
        if (stringPool.containsKey(literal)) {
            return stringPool.get(literal);
        }

        String etiqueta = "str_" + stringLabelCounter++;
        stringPool.put(literal, etiqueta);

        // Agregamos al segmento .data inmediatamente
        mipsData.append(etiqueta).append(": .asciiz ").append(literal).append("\n");

        return etiqueta;
    }

    private void traducirAritmetica(String instruccion, Cuarteto c) {
        cargarOperando(c.arg1, "$t0");
        cargarOperando(c.arg2, "$t1");
        mipsCode.append("    ").append(instruccion).append(" $t2, $t0, $t1\n");
        guardarResultado(c.res, "$t2");
    }

    private void cargarOperando(String operando, String registroDestino) {
        if (esNumero(operando)) {
            mipsCode.append("    li ").append(registroDestino).append(", ").append(operando).append("\n");
        } else if (operando.equals("true")) {
            mipsCode.append("    li ").append(registroDestino).append(", 1\n");
        } else if (operando.equals("false")) {
            mipsCode.append("    li ").append(registroDestino).append(", 0\n");
        } else if (operando.startsWith("\"")) {
            mipsCode.append("    # String loading not fully implemented inline\n");
        } else {
            // Es una variable o temporal
            int offset = getOffsetMemoria(operando);
            mipsCode.append("    lw ").append(registroDestino).append(", ").append(offset).append("($fp)\n");
        }
    }

    private void guardarResultado(String variable, String registroFuente) {
        int offset = getOffsetMemoria(variable);
        mipsCode.append("    sw ").append(registroFuente).append(", ").append(offset).append("($fp)\n");
    }

    private int getOffsetMemoria(String variable) {
        if (!mapaMemoria.containsKey(variable)) {
            stackPointerOffset -= 4;
            mapaMemoria.put(variable, stackPointerOffset);
        }
        return mapaMemoria.get(variable);
    }

    private boolean esNumero(String str) {
        if (str == null) return false;
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void agregarRutinasSistema() {
    }
}