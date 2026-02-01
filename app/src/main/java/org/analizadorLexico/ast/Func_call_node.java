package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Func_call_node extends NodoAST {
    public String id;

    public Func_call_node(String id, NodoAST list) {
        this.agregarHijo(list);
        this.id = id;
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        if(!ts.existe(this.id)){
            System.err.println("Error Semántico: La función '" + this.id + "' no ha sido definida.");
            this.tipoDato = "error";
        }
        else{
            this.tipoDato = ts.obtenerTipo(this.id);
        }

        this.hijos.getFirst().checkSemantics(ts);
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        NodoAST params = this.hijos.getFirst();
        if(!this.hijos.isEmpty()) {
            for (NodoAST param : params.hijos) {
                String paramTemp = param.generateCode(gi);
                gi.agregarCuarteto("PARAM", paramTemp, null, null);
            }

        }

        final String res = gi.nuevaTemporal();
        gi.agregarCuarteto("CALL", id, String.valueOf(params.hijos.size()), res);
        return res;
    }

    @Override
    public String toString() {
        return "Llamada Funcion: " + id;
    }
}
