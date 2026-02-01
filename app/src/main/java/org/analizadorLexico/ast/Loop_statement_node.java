package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Loop_statement_node extends NodoAST {

    public Loop_statement_node(NodoAST sentencias, NodoAST condicion) {
        super();
        if (sentencias != null) {
            this.agregarHijo(sentencias);
        }

        if (condicion != null) {
            this.agregarHijo(condicion);
        }
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {

        NodoAST condicion = this.hijos.get(1);
        condicion.checkSemantics(ts);

        if (condicion.tipoDato != null && !condicion.tipoDato.equals("boolean") && !condicion.tipoDato.equals("error")) {
            System.err.println("Error Semántico: La condición del caso debe ser booleana, se encontró: " + condicion.tipoDato);
        }

        this.hijos.getFirst().checkSemantics(ts);
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        final String labelSalida = gi.nuevaEtiqueta();
        final String labelEnd = gi.nuevaEtiqueta();

        NodoAST sentencias = this.hijos.getFirst();

        gi.agregarCuarteto("LABEL", null, null, labelSalida);

        sentencias.generateCode(gi);

        NodoAST condicion = this.hijos.get(1);
        String temporalCond = condicion.generateCode(gi);

        gi.agregarCuarteto("IF", temporalCond, null, labelSalida);
        gi.agregarCuarteto("GOTO", null, null, labelEnd);

        gi.agregarCuarteto("LABEL", null, null, labelEnd);

        return null;
    }

    @Override
    public String toString() {
        return "Ciclo Loop";
    }
}