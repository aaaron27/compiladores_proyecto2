package org.analizadorLexico.ast;

public class For_statement_node extends NodoAST {

    public For_statement_node(NodoAST init, NodoAST condicion, NodoAST paso, NodoAST bloque) {
        super();
        if (init != null) {
            this.agregarHijo(init);
        }
        if (condicion != null) {
            this.agregarHijo(condicion);
        }
        if (paso != null) {
            this.agregarHijo(paso);
        }
        if (bloque != null) {
            this.agregarHijo(bloque);
        }
    }

    @Override
    public String toString() {
        return "Sentencia For";
    }
}