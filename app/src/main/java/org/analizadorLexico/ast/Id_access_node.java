package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Id_access_node extends NodoAST {
    private String id;

    // Constructor para variable simple: x
    public Id_access_node(String id) {
        super();
        this.id = id;
    }

    // Constructor para arrays: arr[i]
    public Id_access_node(String id, NodoAST dims) {
        super();
        this.id = id;
        if(dims != null) this.agregarHijo(dims);
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        if (!ts.existe(this.id)) {
            System.err.println("Error Semántico: La variable '" + this.id + "' no ha sido declarada.");
            this.tipoDato = "error";
        } else {
            this.tipoDato = ts.obtenerTipo(this.id);
        }
        super.checkSemantics(ts);
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        return this.id;
    }

    @Override
    public String toString() {
        return "Acceso ID: " + id;
    }
}