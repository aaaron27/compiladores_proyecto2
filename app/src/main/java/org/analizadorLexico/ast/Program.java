package org.analizadorLexico.ast;

import org.analizadorLexico.simbolos.TablaSimbolos;

public final class Program extends NodoAST {

    public Program(final NodoAST declarations) {

        this.agregarHijo(declarations);

    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        ts.openScope();

        super.checkSemantics(ts); // Revisar el bloque interno

        ts.closeScope();
    }

    @Override
    public String toString() {
        return "Program";
    }
}
