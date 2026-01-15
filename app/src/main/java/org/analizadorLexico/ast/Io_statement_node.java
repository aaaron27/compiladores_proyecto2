package org.analizadorLexico.ast;

public class Io_statement_node extends NodoAST{
    public String id;

    public Io_statement_node(final NodoAST expr) {
        this.agregarHijo(expr);
    }

    public Io_statement_node(String id) {
        this.id = id;
    }

    public Io_statement_node(String id, NodoAST dimensiones) {
        this.id = id;

        if (dimensiones != null) {
            this.agregarHijo(dimensiones);
        }
    }
}
