package org.analizadorLexico.ast;

public class Single_case_node extends NodoAST {

    public Single_case_node(NodoAST condicion, NodoAST bloque) {
        super();

        if (condicion != null) {
            this.agregarHijo(condicion);
        }

        if (bloque != null) {
            this.agregarHijo(bloque);
        }
    }

    @Override
    public String toString() {
        return "Caso";
    }
}