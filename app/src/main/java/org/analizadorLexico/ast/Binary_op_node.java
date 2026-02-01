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
        // Validar hijos
        this.hijos.get(0).checkSemantics(ts);
        this.hijos.get(1).checkSemantics(ts);

        String tipoIzq = this.hijos.get(0).tipoDato;
        String tipoDer = this.hijos.get(1).tipoDato;

        // Protección contra errores en cascada
        if (tipoIzq == null || tipoDer == null || tipoIzq.equals("error") || tipoDer.equals("error")) {
            this.tipoDato = "error";
            return;
        }

        switch (operador) {
            // Aritmética
            case "+": case "-": case "*": case "/": case "^":
                if (tipoIzq.equals("int") && tipoDer.equals("int")) {
                    this.tipoDato = "int";
                } else if (tipoIzq.equals("float") || tipoDer.equals("float")) {
                    this.tipoDato = "float";
                } else {
                    this.tipoDato = "error";
                    System.err.println("Error Semántico: Operación aritmética incompatible: " + tipoIzq + " " + operador + " " + tipoDer);
                }
                break;

            case "%": // Módulo
                if (tipoIzq.equals("int") && tipoDer.equals("int")) {
                    this.tipoDato = "int";
                } else {
                    this.tipoDato = "error";
                    System.err.println("Error Semántico: Módulo (%) requiere números, encontrado: " + tipoIzq + " % " + tipoDer);
                }
                break;

            // Relacionales
            case ">": case "<": case ">=": case "<=": case "==": case "!=":
                if (sonCompatibles(tipoIzq, tipoDer)) {
                    this.tipoDato = "boolean";
                } else {
                    this.tipoDato = "error";
                    System.err.println("Error Semántico: Comparación incompatible entre " + tipoIzq + " y " + tipoDer);
                }
                break;

            // Lógicos
            case "@": case "AND":
            case "~": case "OR":
            case "&&": case "||":
                if (tipoIzq.equals("boolean") && tipoDer.equals("boolean")) {
                    this.tipoDato = "boolean";
                } else {
                    this.tipoDato = "error";
                    System.err.println("Error Semántico: La operación lógica '" + operador + "' requiere booleanos.");
                }
                break;

            default:
                this.tipoDato = "unknown";
                System.err.println("Error Semántico: Operador binario desconocido '" + operador + "'");
        }
    }

    private boolean sonCompatibles(String t1, String t2) {
        if (t1.equals(t2)) return true;
        if ((t1.equals("int") || t1.equals("float")) && (t2.equals("int") || t2.equals("float"))) return true;
        return false;
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        String izq = this.hijos.get(0).generateCode(gi);
        String der = this.hijos.get(1).generateCode(gi);

        String temporal = gi.nuevaTemporal();
        String opCodigo = this.operador;
        if (opCodigo.equals("@")) opCodigo = "AND";
        if (opCodigo.equals("~")) opCodigo = "OR";

        gi.agregarCuarteto(opCodigo, izq, der, temporal);

        return temporal;
    }

    @Override
    public String toString() {
        return "Operacion Binaria: " + operador;
    }
}