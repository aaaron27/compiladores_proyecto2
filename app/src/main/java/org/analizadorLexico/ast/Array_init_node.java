package org.analizadorLexico.ast;

public class Array_init_node extends NodoAST {

    public Array_init_node(final NodoAST row_list) {
        super();
        if (row_list != null) {
            this.agregarHijo(row_list);
        }
    }

    @Override
    public String toString() {
        return "Inicializacion Array";
    }
}