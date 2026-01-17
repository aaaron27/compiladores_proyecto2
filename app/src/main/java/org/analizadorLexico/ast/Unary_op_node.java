package org.analizadorLexico.ast;

public class Unary_op_node extends NodoAST {
    private String operador;
    private String tipo;

    public Unary_op_node(String operador, NodoAST expresion, String tipo) {
        super();
        this.operador = operador;
        this.tipo = tipo;
        this.agregarHijo(expresion);
    }

    @Override
    public String toString() {
        return "Operacion Unaria (" + tipo + "): " + operador;
    }
}