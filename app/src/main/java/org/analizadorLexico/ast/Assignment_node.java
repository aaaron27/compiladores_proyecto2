package org.analizadorLexico.ast;

public class Assignment_node extends NodoAST { // Corregido: 'Assigment' -> 'Assignment'
    public String id;

    // Caso Asignación simple
    public Assignment_node(String id, NodoAST expresion) {
        super();
        this.id = id;
        // Buena práctica: validar null
        if (expresion != null) {
            this.agregarHijo(expresion);
        }
    }

    // Caso Asignación masiva a arreglo
    public Assignment_node(String id, NodoAST dimensiones, NodoAST arrayInit) {
        super();
        this.id = id;

        if (dimensiones != null) {
            this.agregarHijo(dimensiones);
        }

        if (arrayInit != null) {
            this.agregarHijo(arrayInit);
        }
    }

    @Override
    public String toString() {
        return "Asignacion: " + id;
    }
}