package org.analizadorLexico.ast;

import org.analizadorLexico.simbolos.TablaSimbolos;
import org.analizadorLexico.codigo.GeneradorIntermedio;

public class Unary_op_node extends NodoAST {
    private String operador;
    private String tipo;

    public Unary_op_node(String operador, NodoAST expresion, String tipo) {
        super();
        this.operador = operador;
        this.tipo = tipo;
        if (expresion != null) {
            this.agregarHijo(expresion);
        }
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        if (this.hijos.isEmpty()) return;

        this.hijos.get(0).checkSemantics(ts);
        String tipoHijo = this.hijos.get(0).tipoDato;

        if (tipoHijo == null || tipoHijo.equals("error")) {
            this.tipoDato = "error";
            return;
        }

        switch (this.operador) {
            case "Σ": case "!": case "NOT":
                if (tipoHijo.equals("boolean")) {
                    this.tipoDato = "boolean";
                } else {
                    this.tipoDato = "error";
                    System.err.println("Error Semántico: NOT espera boolean, encontrado: " + tipoHijo);
                }
                break;

            case "-":
                if (tipoHijo.equals("int") || tipoHijo.equals("float")) {
                    this.tipoDato = tipoHijo;
                } else {
                    this.tipoDato = "error";
                    System.err.println("Error Semántico: '-' espera numero, encontrado: " + tipoHijo);
                }
                break;

            // SOPORTE PARA INCREMENTO/DECREMENTO
            case "++": case "--":
                if (tipoHijo.equals("int") || tipoHijo.equals("float")) {
                    this.tipoDato = tipoHijo;
                } else {
                    this.tipoDato = "error";
                    System.err.println("Error Semántico: '" + operador + "' espera numero, encontrado: " + tipoHijo);
                }
                break;

            default:
                this.tipoDato = "unknown";
                System.err.println("Error Semántico: Operador unario desconocido '" + operador + "'");
        }
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        if (this.hijos.isEmpty()) return null;
        String argumento = this.hijos.get(0).generateCode(gi);

        String resultado = null;

        switch (this.operador) {
            case "++":
                // Incremento:
                gi.agregarCuarteto("+", argumento, "1", argumento);
                return argumento;

            case "--":
                // Decremento:
                gi.agregarCuarteto("-", argumento, "1", argumento);
                return argumento;

            case "Σ": case "!": case "NOT":
                resultado = gi.nuevaTemporal();
                gi.agregarCuarteto("NOT", argumento, null, resultado);
                return resultado;
        }
        return null;
    }

    @Override
    public String toString() {
        return "Operacion Unaria (" + tipo + "): " + operador;
    }
}