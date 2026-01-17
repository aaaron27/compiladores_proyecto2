package org.analizadorLexico.ast;

public class Param_node extends NodoAST {
    public String id;

    public Param_node(TypeNode tipo, String id) {
        super();
        this.id = id;
        if (tipo != null) {
            this.agregarHijo(tipo);
        }
    }

    @Override
    public String toString() {
        return "Parametro: " + id;
    }
}