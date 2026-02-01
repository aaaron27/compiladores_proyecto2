package org.analizadorLexico.ast;

public class Dims_access_node extends NodoAST {

    public Dims_access_node(NodoAST indexI, NodoAST indexJ) {
        super();
        if (indexI != null) this.agregarHijo(indexI); // Hijo 0
        if (indexJ != null) this.agregarHijo(indexJ); // Hijo 1
    }

    @Override
    public String toString() {
        return "Indices Array [][]";
    }
}