package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class For_statement_node extends NodoAST {

    public For_statement_node(NodoAST init, NodoAST condicion, NodoAST paso, NodoAST bloque) {
        super();
        if (init != null) {
            this.agregarHijo(init);
        }
        if (condicion != null) {
            this.agregarHijo(condicion);
        }
        if (paso != null) {
            this.agregarHijo(paso);
        }
        if (bloque != null) {
            this.agregarHijo(bloque);
        }
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {

    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        final String for_label = gi.nuevaEtiqueta();
        final String for_end_label = gi.nuevaEtiqueta();
        final String for_condition_label = gi.nuevaEtiqueta();
        final String for_condition_end_label = gi.nuevaEtiqueta();
        final String for_body_label = gi.nuevaEtiqueta();
        final String for_body_end_label = gi.nuevaEtiqueta();
        final String for_update_label = gi.nuevaEtiqueta();
        final String for_update_end_label = gi.nuevaEtiqueta();

        gi.agregarCuarteto("LABEL", null, null, for_label);

        gi.agregarCuarteto("LABEL", null, null, for_condition_label);

        NodoAST init = this.hijos.getFirst();
        init.generateCode(gi);

        gi.agregarCuarteto("LABEL", null, null, for_condition_end_label);
        NodoAST condicion = this.hijos.getFirst();
        String temporalCond = condicion.generateCode(gi);

        gi.agregarCuarteto("IF", temporalCond, null, for_body_label);
        gi.agregarCuarteto("GOTO", null, null, for_end_label);

        gi.agregarCuarteto("LABEL", null, null, for_body_label);

        NodoAST bloque = this.hijos.get(3);
        bloque.generateCode(gi);

        gi.agregarCuarteto("LABEL", null, null, for_body_end_label);

        gi.agregarCuarteto("LABEL", null, null, for_update_label);

        NodoAST paso = this.hijos.get(2);
        paso.generateCode(gi);

        gi.agregarCuarteto("LABEL", null, null, for_update_end_label);

        gi.agregarCuarteto("LABEL", null, null, for_end_label);

        return null;
    }

    @Override
    public String toString() {
        return "Sentencia For";
    }
}