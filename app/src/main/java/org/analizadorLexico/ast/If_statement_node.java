package org.analizadorLexico.ast;

public class If_statement_node extends NodoAST {
    public If_statement_node(NodoAST listaCasos, NodoAST elsePart) {
        super();
        this.agregarHijo(listaCasos);

        if (elsePart != null) {
            this.agregarHijo(elsePart);
        }
    }
}
