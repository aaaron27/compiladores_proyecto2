package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Case_list_node extends NodoAST {

    public Case_list_node() {
        super();
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        for (NodoAST hijo : this.hijos) {
            hijo.checkSemantics(ts);
        }
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        return null;
    }

    @Override
    public String toString() {
        return "Lista de Casos";
    }
}