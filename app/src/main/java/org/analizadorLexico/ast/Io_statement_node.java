package org.analizadorLexico.ast;

import org.analizadorLexico.simbolos.TablaSimbolos;
import org.analizadorLexico.codigo.GeneradorIntermedio;

public class Io_statement_node extends NodoAST {
    private String tipoOperacion; // "SHOW" o "GET"
    private String id;            // Solo para GET
    private String tipoDatoVariable; // INT o FLOAT

    public Io_statement_node(NodoAST expresion) {
        super();
        this.tipoOperacion = "SHOW";
        if (expresion != null) {
            this.agregarHijo(expresion);
        }
    }

    public Io_statement_node(String id) {
        super();
        this.tipoOperacion = "GET";
        this.id = id;
    }

    public Io_statement_node(String id, NodoAST dimensiones) {
        super();
        this.tipoOperacion = "GET";
        this.id = id;
        if (dimensiones != null) {
            this.agregarHijo(dimensiones);
        }
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        if (tipoOperacion.equals("SHOW")) {
            // Validar la expresión a imprimir
            if (!this.hijos.isEmpty()) {
                this.hijos.get(0).checkSemantics(ts);
            }
        } else {

            // Validar que la variable existe
            if (!ts.existe(this.id)) {
                System.err.println("Error Semántico: La variable '" + this.id + "' no existe.");
                this.tipoDatoVariable = "error";
                return;
            }

            String tipoVar = ts.obtenerTipo(this.id);

            // Si es arreglo, validar los índices
            if (!this.hijos.isEmpty()) {
                if (!tipoVar.contains("[]")) {
                    System.err.println("Error Semántico: Variable '" + this.id + "' no es un arreglo.");
                }

                NodoAST dims = this.hijos.get(0);
                for(NodoAST idx : dims.hijos) {
                    idx.checkSemantics(ts);
                    if (idx.tipoDato != null && !idx.tipoDato.equals("int")) {
                        System.err.println("Error Semántico: Índice de arreglo debe ser int.");
                    }
                }
                tipoVar = tipoVar.replace("[]", "");
            }

            this.tipoDatoVariable = tipoVar;

            if (!tipoVar.equals("int") && !tipoVar.equals("float")) {
                System.err.println("Error Semántico: GET solo permite leer int o float. Variable '" + id + "' es " + tipoVar);
            }
        }
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        if (tipoOperacion.equals("SHOW")) {
            String valor = this.hijos.get(0).generateCode(gi);

            gi.agregarCuarteto("PRINT", valor, null, null);

        } else {
            String opRead = this.tipoDatoVariable.equals("float") ? "READ_FLOAT" : "READ_INT";

            if (this.hijos.isEmpty()) {
                // Genera: READ_INT _ _ x
                gi.agregarCuarteto(opRead, null, null, this.id);

            } else {
                // Lectura en Arreglo (get(arr[i][j]))
                // matriz 2D (Fila * Cols + Col)
                final String COLUMNAS = "3";

                NodoAST dims = this.hijos.get(0);
                if (dims.hijos.size() >= 2) {
                    String fila = dims.hijos.get(0).generateCode(gi);
                    String col  = dims.hijos.get(1).generateCode(gi);

                    // Offset = (fila * 3 + col) * 4
                    String tMult = gi.nuevaTemporal();
                    String tSuma = gi.nuevaTemporal();
                    String tOff  = gi.nuevaTemporal();

                    gi.agregarCuarteto("*", fila, COLUMNAS, tMult);
                    gi.agregarCuarteto("+", tMult, col, tSuma);
                    gi.agregarCuarteto("*", tSuma, "4", tOff);

                    // Leer del teclado a un temporal
                    String valorLeido = gi.nuevaTemporal();
                    gi.agregarCuarteto(opRead, null, null, valorLeido);

                    // Guardar en el arreglo: ARR_STORE id offset valor
                    gi.agregarCuarteto("ARR_STORE", this.id, tOff, valorLeido);
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        if (tipoOperacion.equals("SHOW")) {
            return "IO: Output (Show)";
        } else {
            return "IO: Input (Get) -> " + id;
        }
    }
}