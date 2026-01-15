package org.analizadorLexico.ast;

public class Single_case_node extends NodoAST {
    public Single_case_node(NodoAST condicion, NodoAST bloque) {
        super();
        this.agregarHijo(condicion);
        this.agregarHijo(bloque);
    }
}
