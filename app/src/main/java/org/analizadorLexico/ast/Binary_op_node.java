package org.analizadorLexico.ast;

import org.analizadorLexico.simbolos.TablaSimbolos;
import org.analizadorLexico.codigo.GeneradorIntermedio;

public class Binary_op_node extends NodoAST {
    private String operador;

    public Binary_op_node(NodoAST izq, String operador, NodoAST der) {
        super();
        this.operador = operador;
        this.agregarHijo(izq);
        this.agregarHijo(der);
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        this.hijos.get(0).checkSemantics(ts);
        this.hijos.get(1).checkSemantics(ts);

        String tipoIzq = this.hijos.get(0).tipoDato;
        String tipoDer = this.hijos.get(1).tipoDato;

        if (tipoIzq == null || tipoDer == null) {
            this.tipoDato = "error";
            return;
        }

        // Logica de tipos
        switch (operador) {
            case "+": case "-": case "*": case "/": case "%":
                if (tipoIzq.equals("int") && tipoDer.equals("int")) {
                    this.tipoDato = "int";
                } else if (tipoIzq.equals("float") || tipoDer.equals("float")) {
                    this.tipoDato = "float";
                } else {
                    this.tipoDato = "error";
                    System.err.println("Error Semántico: Operación aritmética incompatible: " + tipoIzq + " " + operador + " " + tipoDer);
                }
                break;

            case ">": case "<": case ">=": case "<=": case "==": case "!=":
                // Las comparaciones siempre devuelven boolean
                this.tipoDato = "boolean";
                break;

            case "&&": case "||":
                if (tipoIzq.equals("boolean") && tipoDer.equals("boolean")) {
                    this.tipoDato = "boolean";
                } else {
                    this.tipoDato = "error";
                    System.err.println("Error Semántico: Operación lógica requiere booleanos.");
                }
                break;

            default:
                this.tipoDato = "unknown";
        }
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        String izq = this.hijos.get(0).generateCode(gi);
        String der = this.hijos.get(1).generateCode(gi);
        String temporal = gi.nuevaTemporal();
        gi.agregarCuarteto(this.operador, izq, der, temporal);
        return temporal;
    }
    @Override
    public String toString() {
        return "Operacion Binaria: " + operador;
    }
}