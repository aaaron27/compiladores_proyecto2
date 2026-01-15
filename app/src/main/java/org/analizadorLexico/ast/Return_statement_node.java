package org.analizadorLexico.ast;

public class Return_statement_node extends NodoAST {
    public Return_statement_node(final NodoAST expr) {
        this.agregarHijo(expr);
    }
}
