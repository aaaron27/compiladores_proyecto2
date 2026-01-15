package org.analizadorLexico.ast;

public final class Global_var_decl_node extends NodoAST {
    public String id;

    //Caso solo declaracion
    public Global_var_decl_node(TypeNode tipo, String id) {
        this.agregarHijo(tipo);
        this.id = id;
    }

    // Casos creacion, asignacion
    public Global_var_decl_node(TypeNode tipo, String id, NodoAST expresion) {
        this.agregarHijo(tipo);
        this.id = id;
        this.agregarHijo(expresion);
    }

    // Casos del array
    public Global_var_decl_node(TypeNode tipo, String id, NodoAST dimensiones, NodoAST arrayInit) {
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
