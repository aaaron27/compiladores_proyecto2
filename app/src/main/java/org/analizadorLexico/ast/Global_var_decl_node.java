package org.analizadorLexico.ast;

public final class Global_var_decl_node extends NodoAST {
    public String id;

    // Constructor 1: Solo declaración
    public Global_var_decl_node(TypeNode tipo, String id) {
        super();
        this.id = id;
        if (tipo != null) this.agregarHijo(tipo);
    }

    // Constructor 2: Asignación
    public Global_var_decl_node(TypeNode tipo, String id, NodoAST expresion) {
        super();
        this.id = id;
        if (tipo != null) this.agregarHijo(tipo);
        if (expresion != null) this.agregarHijo(expresion);
    }

    // Constructor 3: Arreglos
    public Global_var_decl_node(TypeNode tipo, String id, NodoAST dimensiones, NodoAST arrayInit) {
        super();
        this.id = id;
        if (tipo != null) this.agregarHijo(tipo);
        if (dimensiones != null) this.agregarHijo(dimensiones);
        if (arrayInit != null) this.agregarHijo(arrayInit);
    }

    @Override
    public String toString() {
        return "Var Global: " + id;
    }
}