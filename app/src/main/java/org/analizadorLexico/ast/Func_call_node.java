package org.analizadorLexico.ast;

public class Func_call_node extends NodoAST {
    public String id;

    public Func_call_node(String id, NodoAST list) {
        this.agregarHijo(list);
        this.id = id;
    }
}
