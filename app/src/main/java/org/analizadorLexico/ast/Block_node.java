package org.analizadorLexico.ast;

public class Block_node extends NodoAST {
    public Block_node() {
        super();
    }

    public Block_node(NodoAST sentencias) {
        super();
        this.agregarHijo(sentencias);
    }
}
