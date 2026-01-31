package org.analizadorLexico.ast;

import org.analizadorLexico.simbolos.TablaSimbolos;
import org.analizadorLexico.codigo.GeneradorIntermedio;

public class Literal_node extends NodoAST {
    private String tipo;
    private String valor;

    public Literal_node(String tipo, String valor) {
        super();
        this.tipo = tipo;
        this.valor = valor;
        this.tipoDato = tipo;
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        this.tipoDato = this.tipo;
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        return this.valor;
    }

    @Override
    public String toString() { return "Literal (" + tipo + "): " + valor; }
}