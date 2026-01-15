package org.analizadorLexico.ast;

public class Array_init_node extends NodoAST {
    public Array_init_node(final NodoAST row_list) {
        super();
        this.agregarHijo(row_list);
    }
}
