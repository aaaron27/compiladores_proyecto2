package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;

public class Break_statement_node extends NodoAST {
    public Break_statement_node() {
        super();
    }

    @Override
    public String toString() {
        return "Break";
    }
}