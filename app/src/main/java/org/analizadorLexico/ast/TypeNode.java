package org.analizadorLexico.ast;

public class TypeNode extends NodoAST {
    public String nombreTipo;

    public TypeNode(String nombreTipo) {
        super();
        this.nombreTipo = nombreTipo;
    }

    public String getNombreTipo() {
        return this.nombreTipo;
    }
}
