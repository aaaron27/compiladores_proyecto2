package org.analizadorLexico.ast;

public class Loop_statement_node extends NodoAST {
    public Loop_statement_node(NodoAST sentencias, NodoAST condicion) {
        super();
        if (sentencias != null) {
            this.agregarHijo(sentencias);
        }
        if (condicion != null) {
            this.agregarHijo(condicion);
        }
    }
}
