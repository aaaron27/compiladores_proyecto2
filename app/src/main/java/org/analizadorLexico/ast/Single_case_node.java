package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Single_case_node extends NodoAST {

    public Single_case_node(NodoAST condicion, NodoAST bloque) {
        super();
        if (condicion != null) this.agregarHijo(condicion); // Hijo 0
        if (bloque != null)    this.agregarHijo(bloque);    // Hijo 1
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        // Verificar la condición
        NodoAST condicion = this.hijos.get(0);
        condicion.checkSemantics(ts);

        if (condicion.tipoDato != null && !condicion.tipoDato.equals("boolean") && !condicion.tipoDato.equals("error")) {
            System.err.println("Error Semántico: La condición del caso debe ser booleana, se encontró: " + condicion.tipoDato);
        }

        this.hijos.get(1).checkSemantics(ts);
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        return null;
    }

    @Override
    public String toString() {
        return "Caso (->)";
    }
}