package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Return_statement_node extends NodoAST {

    public Return_statement_node(NodoAST expresion) {
        super();
        if (expresion != null) {
            this.agregarHijo(expresion);
        }
    }

    public Return_statement_node() {
        super();
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        String tipoEsperado = ts.getTipoFuncionActual();
        String tipoReal = "void";

        if (!hijos.isEmpty()) {
            hijos.get(0).checkSemantics(ts);
            tipoReal = hijos.get(0).getTipo();
        }

        if (!tipoReal.equals(tipoEsperado)) {
            throw new RuntimeException("Error: Retorno de tipo " + tipoReal +
                    " no coincide con " + tipoEsperado);
        }
    }
    @Override
    public String generateCode(GeneradorIntermedio gen) {
        String resultado = null;

        if (!hijos.isEmpty()) {
            NodoAST expresion = hijos.get(0);
            resultado = expresion.generateCode(gen);
        }

        gen.agregarCuarteto("RETURN", resultado, null, null);
        return null;
    }
    @Override
    public String toString() {
        if (hijos.isEmpty()) {
            return "Return (Void)";
        } else {
            return "Return";
        }
    }
}