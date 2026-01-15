package org.analizadorLexico.ast;

public class Param_node extends NodoAST {
    public String id;

    public Param_node(TypeNode tipo, String id) {
        super();
        this.agregarHijo(tipo);
        this.id = id;
    }
}
