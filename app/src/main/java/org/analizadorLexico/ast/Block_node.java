package org.analizadorLexico.ast;

public class Block_node extends NodoAST {

    public Block_node() {
        super();
    }

    public Block_node(NodoAST sentencias) {
        super();
        if (sentencias != null) {
            this.agregarHijo(sentencias);
        }
    }

    @Override
    public String toString() {
        return "Bloque";
    }
}