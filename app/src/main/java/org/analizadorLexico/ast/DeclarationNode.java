package org.analizadorLexico.ast;

public class DeclarationNode extends NodoAST {
    public DeclarationNode(NodoAST hijo) {
        super();
        this.agregarHijo(hijo);
    }
}
