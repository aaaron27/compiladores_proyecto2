package org.analizadorLexico.ast;

public class Array_row_node extends NodoAST {

    public Array_row_node(final NodoAST list) {
        super();
        if (list != null) {
            this.agregarHijo(list);
        }
    }

    @Override
    public String toString() {
        return "Fila Array";
    }
}