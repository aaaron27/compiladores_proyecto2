package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class If_statement_node extends NodoAST {

    public If_statement_node(NodoAST listaCasos, NodoAST elsePart) {
        super();
        if (listaCasos != null) {
            this.agregarHijo(listaCasos);
        }
        if (elsePart != null) {
            this.agregarHijo(elsePart);
        }
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        for (NodoAST hijo : this.hijos) {
            hijo.checkSemantics(ts);
        }
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        String labelSalida = gi.nuevaEtiqueta();
        NodoAST listaCasos = this.hijos.get(0);

        for (NodoAST caso : listaCasos.hijos) {
            String labelSiguienteCaso = gi.nuevaEtiqueta();

            NodoAST condicion = caso.hijos.getFirst();
            String temporalCond = condicion.generateCode(gi);

            gi.agregarCuarteto("IF_FALSE", temporalCond, null, labelSiguienteCaso);

            NodoAST bloque = caso.hijos.get(1);
            bloque.generateCode(gi);

            gi.agregarCuarteto("GOTO", null, null, labelSalida);

            gi.agregarCuarteto("LABEL", null, null, labelSiguienteCaso);
        }

        if (this.hijos.size() > 1) {
            NodoAST elsePart = this.hijos.get(1);
            elsePart.generateCode(gi);
        }

        gi.agregarCuarteto("LABEL", null, null, labelSalida);

        return null;
    }

    @Override
    public String toString() {
        return "Sentencia Decide Of";
    }
}