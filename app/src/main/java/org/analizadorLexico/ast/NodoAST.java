package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

import java.util.ArrayList;
import java.util.List;

public abstract class NodoAST {
    public String tipoDato;
    protected List<NodoAST> hijos = new ArrayList<>();

    public void agregarHijo(NodoAST hijo) {
        if (hijo != null) hijos.add(hijo);
    }

    public void print(String indent, boolean ultimo) {
        System.out.print(indent);
        if (ultimo) {
            System.out.print("/--");
            indent += "  ";
        } else {
            System.out.print("|-- ");
            indent += "|";
        }
        System.out.println(toString());

        for (int i = 0; i < hijos.size(); i++) {
            hijos.get(i).print(indent, i == hijos.size() - 1);
        }
    }
    public void checkSemantics(TablaSimbolos ts) {
        if (this.hijos != null) {
            for (NodoAST hijo : this.hijos) {
                hijo.checkSemantics(ts);
            }
        }
    }

    public String generateCode(GeneradorIntermedio gi) {
        if (this.hijos != null) {
            for (NodoAST hijo : this.hijos) {
                hijo.generateCode(gi);
            }
        }
        return null;
    }
}