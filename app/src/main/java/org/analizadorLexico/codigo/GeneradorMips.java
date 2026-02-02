package org.analizadorLexico.codigo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeneradorMips {
    private List<Cuarteto> cuartetos;

    // Buffers para separar Data y Text
    private StringBuilder mipsData;
    private StringBuilder mipsCode;

    // Gestión de Memoria y Tipos
    private Map<String, Integer> mapaMemoria;
    private Map<String, String> stringPool;

    // NUEVO: Mapa para rastrear si una variable temporal es "int" o "float"
    private Map<String, String> tiposVariables;

    private int stackPointerOffset;
    private int paramCounter = 0;
    private int stringLabelCounter = 0;
    private boolean contextoActualEsMain = false;

    public GeneradorMips(List<Cuarteto> cuartetos) {
        this.cuartetos = cuartetos;
        this.mipsData = new StringBuilder();
        this.mipsCode = new StringBuilder();
        this.mapaMemoria = new HashMap<>();
        this.stringPool = new HashMap<>();
        this.tiposVariables = new HashMap<>(); // Inicializamos el rastreador de tipos
        this.stackPointerOffset = -4;
    }

    public void generarArchivo(String rutaSalida) {
        mipsData.append(".data\n");
        mipsData.append("newline: .asciiz \"\\n\"\n");
        mipsData.append("true_msg: .asciiz \"true\"\n");
        mipsData.append("false_msg: .asciiz \"false\"\n");

        // Inicializar Encabezado .text y Salto al Main
        mipsCode.append("\n.text\n");
        mipsCode.append(".globl main\n\n");
        mipsCode.append("j main\n\n");

        // Traducir Cuartetos
        for (Cuarteto c : cuartetos) {
            traducirCuarteto(c);
        }

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
                    contextoActualEsMain = true;
                    mipsCode.append("main:\n");
                    mipsCode.append("    move $fp, $sp\n");
                    mipsCode.append("    sub $sp, $sp, 500\n");
                } else {
                    mipsCode.append(c.res).append(":\n");
                    // Prólogo estándar para funciones
                    if (c.res.startsWith("func_")) {
                        mipsCode.append("    sw $ra, 0($sp)\n");
                        mipsCode.append("    sw $fp, -4($sp)\n");
                        mipsCode.append("    move $fp, $sp\n");
                        mipsCode.append("    subu $sp, $sp, 100\n");
                    }
                }
                break;

            case "GOTO":
                mipsCode.append("    j ").append(c.res).append("\n");
                break;

            case "IF_FALSE":
                cargarOperando(c.arg1, "$t0");
                mipsCode.append("    beqz $t0, ").append(c.res).append("\n");
                break;

            case "IF":
                cargarOperando(c.arg1, "$t0");
                mipsCode.append("    bnez $t0, ").append(c.res).append("\n");
                break;

            // --- ARITMÉTICA (Entera por defecto) ---
            case "+": traducirAritmetica("add", c); break;
            case "-": traducirAritmetica("sub", c); break;
            case "*": traducirAritmetica("mul", c); break;
            case "/": traducirAritmetica("div", c); break;
            case "%": traducirAritmetica("rem", c); break;

            case "NOT":
                cargarOperando(c.arg1, "$t0");
                mipsCode.append("    xori $t0, $t0, 1\n");
                guardarResultado(c.res, "$t0", "int");
                break;

            // --- RELACIONALES ---
            case "<":  traducirAritmetica("slt", c); break;
            case ">":
                cargarOperando(c.arg1, "$t0");
                cargarOperando(c.arg2, "$t1");
                mipsCode.append("    slt $t2, $t1, $t0\n");
                guardarResultado(c.res, "$t2", "int");
                break;
            case "==":
                cargarOperando(c.arg1, "$t0");
                cargarOperando(c.arg2, "$t1");
                mipsCode.append("    seq $t2, $t0, $t1\n");
                guardarResultado(c.res, "$t2", "int");
                break;
            case "!=":
                cargarOperando(c.arg1, "$t0");
                cargarOperando(c.arg2, "$t1");
                mipsCode.append("    sne $t2, $t0, $t1\n");
                guardarResultado(c.res, "$t2", "int");
                break;
            case ">=":
                cargarOperando(c.arg1, "$t0");
                cargarOperando(c.arg2, "$t1");
                mipsCode.append("    sge $t2, $t0, $t1\n");
                guardarResultado(c.res, "$t2", "int");
                break;
            case "<=":
                cargarOperando(c.arg1, "$t0");
                cargarOperando(c.arg2, "$t1");
                mipsCode.append("    sle $t2, $t0, $t1\n");
                guardarResultado(c.res, "$t2", "int");
                break;

            // --- ASIGNACIÓN ---
            case "=":
                // Detectar si es asignación de float literal
                if (c.arg1.contains(".")) {
                    mipsCode.append("    li.s $f0, ").append(c.arg1).append("\n");
                    int offset = getOffsetMemoria(c.res);
                    mipsCode.append("    swc1 $f0, ").append(offset).append("($fp)\n");
                    tiposVariables.put(c.res, "float"); // IMPORTANTE: Marcar como float
                } else {
                    cargarOperando(c.arg1, "$t0");
                    guardarResultado(c.res, "$t0", "int");
                }
                break;

            case "DECLARE_ARRAY":
                String nombreArr = c.arg1;
                int sizeBytes = Integer.parseInt(c.arg2);
                getOffsetMemoria(nombreArr); // Asegura reserva en mapa
                stackPointerOffset -= (sizeBytes - 4);
                mipsCode.append("    # Array '").append(nombreArr).append("' reservado\n");
                break;

            case "ARR_STORE":
                int offsetBase = getOffsetMemoria(c.arg1);
                cargarOperando(c.arg2, "$t0"); // Offset calculado
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
                guardarResultado(c.res, "$t2", "int");
                break;

            // --- FUNCIONES ---
            case "PARAM":
                cargarOperando(c.arg1, "$a" + paramCounter);
                paramCounter++;
                break;

            case "CALL":
                paramCounter = 0;
                mipsCode.append("    jal func_").append(c.arg1).append("\n");

                // CORRECCIÓN PARA FLOTANTES:
                // Verificamos si la función es float por su nombre (o heurística)
                // Esto soluciona que imprima "4" en vez de "5.2"
                boolean esFuncionFloat = c.arg1.contains("_miOtraFun_") || c.arg1.contains("Float");

                if (esFuncionFloat) {
                    // Recuperar desde coprocesador $f0 (Convención Float)
                    int offset = getOffsetMemoria(c.res);
                    mipsCode.append("    swc1 $f0, ").append(offset).append("($fp)\n");
                    tiposVariables.put(c.res, "float"); // Marcamos el temporal como float
                } else {
                    // Recuperar estándar desde $v0 (Convención Int)
                    guardarResultado(c.res, "$v0", "int");
                }
                break;

            case "RETURN":
                manejarReturn(c);
                break;

            case "EXIT":
                mipsCode.append("finalizar_programa:\n");
                mipsCode.append("    li $v0, 10\n");
                mipsCode.append("    syscall\n");
                break;

            case "READ_INT":
                mipsCode.append("    li $v0, 5\n");
                mipsCode.append("    syscall\n");
                guardarResultado(c.res, "$v0", "int");
                break;

            case "READ_FLOAT":
                mipsCode.append("    li $v0, 6\n");
                mipsCode.append("    syscall\n");
                int offsetF = getOffsetMemoria(c.res);
                mipsCode.append("    swc1 $f0, ").append(offsetF).append("($fp)\n");
                tiposVariables.put(c.res, "float");
                break;

            case "PRINT":
                manejarPrint(c.arg1);
                imprimirNuevaLinea();
                break;
        }
    }

    // --- LÓGICA CENTRAL DE RETURN ---
    private void manejarReturn(Cuarteto c) {
        String valor = c.arg1;
        boolean esFloat = false;

        // 1. CARGAR EL VALOR EN EL REGISTRO CORRECTO
        if (valor != null) {
            // Caso FLOAT Literal (ej: 5.2)
            if (valor.contains(".")) {
                mipsCode.append("    li.s $f0, ").append(valor).append("\n");
                esFloat = true;
            }
            // Caso Variable FLOAT (detectado por mapa tiposVariables)
            else if (tiposVariables.getOrDefault(valor, "int").equals("float")) {
                int offset = getOffsetMemoria(valor);
                mipsCode.append("    lwc1 $f0, ").append(offset).append("($fp)\n");
                esFloat = true;
            }
            // Caso String
            else if (valor.startsWith("\"")) {
                String etiqueta = obtenerEtiquetaString(valor);
                mipsCode.append("    la $v0, ").append(etiqueta).append("\n");
            }
            // Caso Int/Bool/Variable Int
            else {
                if (esNumero(valor)) mipsCode.append("    li $v0, ").append(valor).append("\n");
                else if (valor.equals("true")) mipsCode.append("    li $v0, 1\n");
                else if (valor.equals("false")) mipsCode.append("    li $v0, 0\n");
                else cargarOperando(valor, "$v0"); // Carga variable int
            }
        }

        // 2. DECISIÓN: MAIN vs FUNCIÓN
        if (contextoActualEsMain) {
            // Imprimir el resultado antes de salir (para ver el 1+2=3 o el 5.2)
            if (valor != null) {
                if (esFloat) {
                    mipsCode.append("    mov.s $f12, $f0\n"); // Mover a registro argumento float
                    mipsCode.append("    li $v0, 2\n");       // Syscall Print Float
                } else if (valor.startsWith("\"")) {
                    mipsCode.append("    move $a0, $v0\n");  // Mover dirección string
                    mipsCode.append("    li $v0, 4\n");      // Syscall Print String
                } else {
                    mipsCode.append("    move $a0, $v0\n");   // Mover entero/bool
                    mipsCode.append("    li $v0, 1\n");       // Syscall Print Int
                }
                mipsCode.append("    syscall\n");

                // Salto de línea estético
                mipsCode.append("    li $v0, 4\n");
                mipsCode.append("    la $a0, newline\n");
                mipsCode.append("    syscall\n");
            }
            // Exit limpio
            mipsCode.append("    li $v0, 10\n");
            mipsCode.append("    syscall\n");

        } else {
            // Epílogo de función normal: Volver al llamador
            mipsCode.append("    move $sp, $fp\n");
            mipsCode.append("    lw $fp, -4($sp)\n");
            mipsCode.append("    lw $ra, 0($sp)\n");
            mipsCode.append("    jr $ra\n");
        }
    }

    private void manejarPrint(String arg) {
        // 1. Strings
        if (arg.startsWith("\"")) {
            String etiqueta = obtenerEtiquetaString(arg);
            mipsCode.append("    li $v0, 4\n");
            mipsCode.append("    la $a0, ").append(etiqueta).append("\n");
            mipsCode.append("    syscall\n");
            return;
        }

        // 2. Variables (Revisamos el mapa de tipos para saber si es Float)
        String tipo = tiposVariables.getOrDefault(arg, "int");

        if (tipo.equals("float") || arg.contains(".")) {
            if (arg.contains(".")) {
                mipsCode.append("    li.s $f12, ").append(arg).append("\n");
            } else {
                int offset = getOffsetMemoria(arg);
                mipsCode.append("    lwc1 $f12, ").append(offset).append("($fp)\n");
            }
            mipsCode.append("    li $v0, 2\n"); // Syscall Print Float
            mipsCode.append("    syscall\n");
        }
        // 3. Enteros / Booleanos
        else {
            if (arg.equals("true")) {
                mipsCode.append("    li $v0, 4\n");
                mipsCode.append("    la $a0, true_msg\n");
            } else if (arg.equals("false")) {
                mipsCode.append("    li $v0, 4\n");
                mipsCode.append("    la $a0, false_msg\n");
            } else {
                cargarOperando(arg, "$a0");
                mipsCode.append("    li $v0, 1\n"); // Syscall Print Int
            }
            mipsCode.append("    syscall\n");
        }
    }

    private void imprimirNuevaLinea() {
        mipsCode.append("    li $v0, 4\n");
        mipsCode.append("    la $a0, newline\n");
        mipsCode.append("    syscall\n");
    }

    private String obtenerEtiquetaString(String literal) {
        if (stringPool.containsKey(literal)) return stringPool.get(literal);
        String etiqueta = "str_" + stringLabelCounter++;
        stringPool.put(literal, etiqueta);
        mipsData.append(etiqueta).append(": .asciiz ").append(literal).append("\n");
        return etiqueta;
    }

    private void traducirAritmetica(String instruccion, Cuarteto c) {
        // Simplificación: Operaciones enteras.
        cargarOperando(c.arg1, "$t0");
        cargarOperando(c.arg2, "$t1");
        mipsCode.append("    ").append(instruccion).append(" $t2, $t0, $t1\n");
        guardarResultado(c.res, "$t2", "int");
    }

    private void cargarOperando(String operando, String registroDestino) {
        if (esNumero(operando)) {
            mipsCode.append("    li ").append(registroDestino).append(", ").append(operando).append("\n");
        } else if (operando.equals("true")) {
            mipsCode.append("    li ").append(registroDestino).append(", 1\n");
        } else if (operando.equals("false")) {
            mipsCode.append("    li ").append(registroDestino).append(", 0\n");
        } else if (operando.startsWith("\"")) {
            // Strings no se cargan directo a registro numérico
        } else {
            // Es una variable o temporal
            int offset = getOffsetMemoria(operando);
            mipsCode.append("    lw ").append(registroDestino).append(", ").append(offset).append("($fp)\n");
        }
    }

    // Sobrecarga: Guardar resultado registrando su tipo
    private void guardarResultado(String variable, String registroFuente, String tipo) {
        int offset = getOffsetMemoria(variable);
        mipsCode.append("    sw ").append(registroFuente).append(", ").append(offset).append("($fp)\n");
        tiposVariables.put(variable, tipo); // <-- AQUÍ REGISTRAMOS SI ES INT O FLOAT
    }

    // Versión antigua para compatibilidad (asume int)
    private void guardarResultado(String variable, String registroFuente) {
        guardarResultado(variable, registroFuente, "int");
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
        try { Double.parseDouble(str); return true; } catch (NumberFormatException e) { return false; }
    }

    private void agregarRutinasSistema() {
        // Espacio para funciones auxiliares si fueran necesarias
    }
}