package org.analizadorLexico.ast;

public class Return_statement_node extends NodoAST {

    public Return_statement_node(NodoAST expresion) {
        super();
        if (expresion != null) {
            this.agregarHijo(expresion);
        }
    }

    public Return_statement_node() {
        super();
    }

    @Override
    public String toString() {
        if (hijos.isEmpty()) {
            return "Return (Void)";
        } else {
            return "Return";
        }
    }
}