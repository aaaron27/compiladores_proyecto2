package org.analizadorLexico.ast;

public class Else_part_node extends NodoAST {

    public Else_part_node(NodoAST bloque) {
        super();
        if (bloque != null){
            this.agregarHijo(bloque);
        }
    }

    @Override
    public String toString() {
        return "Else";
    }
}