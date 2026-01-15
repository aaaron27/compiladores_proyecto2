package org.analizadorLexico.ast;

public final class Function_def_node extends NodoAST {

    public String id;

    public Function_def_node(TypeNode tipo, String id, NodoAST params, NodoAST bloque) {
        this.agregarHijo(tipo);
        this.id = id;
        if (params != null) {
            this.agregarHijo(params);
        }
        if (bloque != null) {
            this.agregarHijo(bloque);
        }
    }
}
