package org.analizadorLexico.ast;

public class For_statement_node extends NodoAST {
    public For_statement_node(final NodoAST assignment, final NodoAST expr1, final NodoAST expr2) {
        this.agregarHijo(assignment);
        this.agregarHijo(expr1);
        this.agregarHijo(expr2);
    }
}
