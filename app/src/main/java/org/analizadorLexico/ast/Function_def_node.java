package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public final class Function_def_node extends NodoAST {

    public String id;

    public Function_def_node(TypeNode tipo, String id, NodoAST params, NodoAST bloque) {
        super();
        this.id = id;

        if (tipo != null) {
            this.agregarHijo(tipo);
        }

        if (params != null) {
            this.agregarHijo(params);
        }

        if (bloque != null) {
            this.agregarHijo(bloque);
        }
    }
    @Override
    public void checkSemantics(TablaSimbolos ts) {
        String id = this.id;
        String tipo = this.hijos.getFirst().toString();

        ts.agregar(id, tipo);
        ts.pushTipoRetorno(tipo);
        // Abrir Scope para la función
        ts.openScope();

        // Registrar los parámetros dentro del nuevo scope
        NodoAST params = this.hijos.get(1);
        if (params != null) params.checkSemantics(ts);

        // Analizar el bloque de código
        NodoAST bloque = this.hijos.get(2);
        if (bloque != null) bloque.checkSemantics(ts);

        // Cerrar Scope
        ts.closeScope();
        ts.popTipoRetorno();
    }
    @Override
    public String generateCode(GeneradorIntermedio gen) {
        gen.agregarCuarteto("LABEL", null, null, "func_"+this.id);

        // Generar el código para los parámetros
        if (this.hijos.size() > 1 && this.hijos.get(1) != null) {
            this.hijos.get(1).generateCode(gen);
        }

        // Generar el código del bloque de la función (cuerpo)
        if (this.hijos.size() > 2 && this.hijos.get(2) != null) {
            this.hijos.get(2).generateCode(gen);
        }
        return null;
    }

    @Override
    public String toString() {
        return "Definicion Funcion: " + id;
    }
}