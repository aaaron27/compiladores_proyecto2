package org.analizadorLexico.ast;

public class Binary_op_node extends NodoAST {
    private String operador;

    public Binary_op_node(NodoAST izq, String operador, NodoAST der) {
        super();
        this.operador = operador;
        this.agregarHijo(izq);
        this.agregarHijo(der);
    }

    @Override
    public String toString() {
        return "Operacion Binaria: " + operador;
    }
}