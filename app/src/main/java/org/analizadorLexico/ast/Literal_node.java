package org.analizadorLexico.ast;
public class Literal_node extends NodoAST {
    private String tipo, valor;

    public Literal_node(String tipo, String valor) {
        this.tipo = tipo; this.valor = valor;
    }
    @Override public String toString() { return "Literal (" + tipo + "): " + valor; }
}