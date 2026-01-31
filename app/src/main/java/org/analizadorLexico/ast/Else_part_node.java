package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Else_part_node extends NodoAST {

    public Else_part_node(NodoAST bloque) {
        super();
        if (bloque != null){
            this.agregarHijo(bloque); // Hijo 0
        }
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        if (!hijos.isEmpty()) {
            hijos.getFirst().checkSemantics(ts);
        }
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        if (!hijos.isEmpty()) {
            return hijos.get(0).generateCode(gi);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Else";
    }
}