package org.analizadorLexico.ast;

public class Dims_decl_node extends NodoAST {
    private int filas;
    private int columnas;

    public Dims_decl_node(int filas, int columnas) {
        super();
        this.filas = filas;
        this.columnas = columnas;
    }

    public int getFilas() { return filas; }
    public int getColumnas() { return columnas; }

    @Override
    public String toString() {
        return "Dimensiones [" + filas + "][" + columnas + "]";
    }
}