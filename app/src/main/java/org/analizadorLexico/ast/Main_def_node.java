package org.analizadorLexico.ast;

public class Main_def_node extends NodoAST {
    public Main_def_node(NodoAST bloque) {
        super();
        this.agregarHijo(bloque);
    }
}
