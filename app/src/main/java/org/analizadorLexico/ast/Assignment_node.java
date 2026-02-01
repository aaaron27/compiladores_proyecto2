package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

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
    public void checkSemantics(TablaSimbolos ts) {
        String id = this.id; // Lado izquierdo (Variable)

        if (!ts.existe(id)) {
            System.err.println("Error Semantico: Variable '" + id + "' no declarada.");
            return;
        }

        this.hijos.get(0).checkSemantics(ts);

    }

    public String getId() {
        return this.id;
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        String id = this.id;

        String valor = this.hijos.get(0).generateCode(gi);

        gi.agregarCuarteto("=", valor, null, id);

        return id;
    }
    public String getId() {
        return this.id;
    }
    @Override
    public String toString() {
        return "Asignacion: " + id;
    }
}