package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Param_node extends NodoAST {
    public String id;

    public Param_node(TypeNode tipo, String id) {
        super();
        this.id = id;
        if (tipo != null) {
            this.agregarHijo(tipo);
        }
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        String nombreTipo = "unknown";
        if (!this.hijos.isEmpty()) {
            nombreTipo = this.hijos.getFirst().toString();
        }
        ts.agregar(this.id, nombreTipo);
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        return null;
    }
    @Override
    public String toString() {
        return "Parametro: " + id;
    }
}