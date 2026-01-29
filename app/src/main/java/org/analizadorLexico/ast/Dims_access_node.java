package org.analizadorLexico.ast;

import org.w3c.dom.Node;

public class Dims_access_node extends NodoAST {
    private NodoAST indexI;
    private NodoAST indexJ;

    public Dims_access_node(NodoAST indexI, NodoAST indexJ) {
        this.indexI = indexI;
        this.indexJ = indexJ;
    }
    @Override
    public String toString() {
        return "Dims_access[" + indexI.toString() + "][" + indexJ.toString() + "]";
    }
}