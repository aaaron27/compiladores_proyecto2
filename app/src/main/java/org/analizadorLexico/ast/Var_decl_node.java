package org.analizadorLexico.ast;

public class Var_decl_node extends NodoAST {
    private String id;

    public Var_decl_node(TypeNode tipo, String id) {
        super();
        this.id = id;
        if (tipo != null) this.agregarHijo(tipo);
    }

    public Var_decl_node(TypeNode tipo, String id, NodoAST expresion) {
        super();
        this.id = id;
        if (tipo != null) this.agregarHijo(tipo);
        if (expresion != null) this.agregarHijo(expresion);
    }

    public Var_decl_node(TypeNode tipo, String id, NodoAST dimensiones, NodoAST arrayInit) {
        super();
        this.id = id;
        if (tipo != null) this.agregarHijo(tipo);
        if (dimensiones != null) this.agregarHijo(dimensiones);
        if (arrayInit != null) this.agregarHijo(arrayInit);
    }

    @Override
    public String toString() {
        return "Var Local: " + id;
    }
}