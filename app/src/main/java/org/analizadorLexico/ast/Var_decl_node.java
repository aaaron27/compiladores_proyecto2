package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

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
    public void checkSemantics(TablaSimbolos ts) {
        String tipo = this.hijos.get(0).toString();
        String id = this.id;

        ts.agregar(id, tipo);
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        return null;
    }

    @Override
    public String toString() {
        return "Var Local: " + id;
    }
}