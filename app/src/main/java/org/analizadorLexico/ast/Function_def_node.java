package org.analizadorLexico.ast;

public final class Function_def_node extends NodoAST {

    public String id;

    public Function_def_node(NodoAST tipo, String id, NodoAST params, NodoAST bloque) {
        super();
        this.id = id;

        if (tipo != null) {
            this.agregarHijo(tipo);
        }

        if (params != null) {
            this.agregarHijo(params);
        }

        if (bloque != null) {
            this.agregarHijo(bloque);
        }
    }

    @Override
    public String toString() {
        return "Definicion Funcion: " + id;
    }
}