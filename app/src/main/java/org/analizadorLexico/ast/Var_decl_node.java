package org.analizadorLexico.ast;

public class Var_decl_node extends NodoAST {
    private String id;

    // Caso 1: Declaración simple
    public Var_decl_node(TypeNode tipo, String id) {
        super();
        this.agregarHijo(tipo);
        this.id = id;
    }

    // Caso 2: Asignación simple
    public Var_decl_node(TypeNode tipo, String id, NodoAST expresion) {
        super();
        this.agregarHijo(tipo);
        this.id = id;
        this.agregarHijo(expresion);
    }

    // Caso arreglos
    public Var_decl_node(TypeNode tipo, String id, NodoAST dimensiones, NodoAST arrayInit) {
        super();
        this.agregarHijo(tipo);
        this.id = id;

        if (dimensiones != null) {
            this.agregarHijo(dimensiones);
        }

        if (arrayInit != null) {
            this.agregarHijo(arrayInit);
        }
    }
}
