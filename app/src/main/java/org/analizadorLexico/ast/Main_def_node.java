package org.analizadorLexico.ast;

import org.analizadorLexico.simbolos.TablaSimbolos;

public class Main_def_node extends NodoAST {

    public Main_def_node(NodoAST bloque) {
        super();
        if (bloque != null) {
            this.agregarHijo(bloque);
        }
    }
    @Override
    public void checkSemantics(TablaSimbolos ts) {
        ts.openScope();

        super.checkSemantics(ts); // Revisar el bloque interno

        ts.closeScope();
    }

    @Override
    public String toString() {
        return "Metodo Main (Coal Navidad)";
    }
}