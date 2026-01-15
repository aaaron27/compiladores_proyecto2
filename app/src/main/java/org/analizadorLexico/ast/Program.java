package org.analizadorLexico.ast;

public final class Program extends NodoAST {

    public Program(final NodoAST declarations) {
        this.agregarHijo(declarations);
    }
}
