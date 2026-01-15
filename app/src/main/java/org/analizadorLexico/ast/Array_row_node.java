package org.analizadorLexico.ast;

public class Array_row_node extends NodoAST{
    public Array_row_node(final NodoAST list) {
        this.agregarHijo(list);
    }
}
