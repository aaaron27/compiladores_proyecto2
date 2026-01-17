package org.analizadorLexico.ast;

public class Main_def_node extends NodoAST {

    public Main_def_node(NodoAST bloque) {
        super();
        if (bloque != null) {
            this.agregarHijo(bloque);
        }
    }

    @Override
    public String toString() {
        return "Metodo Main (Coal Navidad)";
    }
}