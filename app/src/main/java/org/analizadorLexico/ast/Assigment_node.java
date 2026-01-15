package org.analizadorLexico.ast;

public class Assigment_node extends NodoAST{
    public String id;

    public Assigment_node(String id, NodoAST expresion) {
        this.id = id;
        this.agregarHijo(expresion);
    }

    // Casos del array
    public Assigment_node(String id, NodoAST dimensiones, NodoAST arrayInit) {
        this.id = id;

        if (dimensiones != null) {
            this.agregarHijo(dimensiones);
        }

        if (arrayInit != null) {
            this.agregarHijo(arrayInit);
        }
    }
}
