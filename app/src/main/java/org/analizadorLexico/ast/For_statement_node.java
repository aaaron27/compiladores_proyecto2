package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class For_statement_node extends NodoAST {

    private String tipoVar;

    public For_statement_node(NodoAST typeNode, NodoAST init, NodoAST condition, NodoAST step, NodoAST body) {
        super();

        if (typeNode != null) {
            this.tipoVar = typeNode.toString();
        } else {
            this.tipoVar = "int";
        }

        // Hijos: 0=Init, 1=Cond, 2=Step, 3=Body
        if (init != null) this.agregarHijo(init);
        if (condition != null) this.agregarHijo(condition);
        if (step != null) this.agregarHijo(step);
        if (body != null) this.agregarHijo(body);
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        ts.openScope();

        NodoAST init = this.hijos.get(0);
        if (init instanceof Assignment_node) {
            String idVar = ((Assignment_node) init).getId();

            ts.agregar(idVar, this.tipoVar);
        }

        init.checkSemantics(ts);

        if (init.tipoDato != null && !sonTiposCompatibles(this.tipoVar, init.tipoDato)) {
            System.err.println("Error Semántico: Tipo de inicialización en FOR incorrecto. Esperado: " + this.tipoVar + ", Encontrado: " + init.tipoDato);
        }

        NodoAST cond = this.hijos.get(1);
        cond.checkSemantics(ts);

        if (this.hijos.size() > 2) this.hijos.get(2).checkSemantics(ts);
        if (this.hijos.size() > 3) this.hijos.get(3).checkSemantics(ts);

        ts.closeScope();
    }

    private boolean sonTiposCompatibles(String declarado, String asignado) {
        if (declarado.equals(asignado)) return true;
        if (declarado.equals("float") && asignado.equals("int")) return true;
        return false;
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        String labelStart = gi.nuevaEtiqueta(); // Inicio del bucle
        String labelEnd = gi.nuevaEtiqueta();   // Salida del bucle


        this.hijos.get(0).generateCode(gi);

        gi.agregarCuarteto("LABEL", null, null, labelStart);

        String condTemp = this.hijos.get(1).generateCode(gi);

        gi.agregarCuarteto("IF_FALSE", condTemp, null, labelEnd);

        if (this.hijos.size() > 3) {
            this.hijos.get(3).generateCode(gi);
        }

        if (this.hijos.size() > 2) {
            this.hijos.get(2).generateCode(gi);
        }

        gi.agregarCuarteto("GOTO", null, null, labelStart);

        gi.agregarCuarteto("LABEL", null, null, labelEnd);

        return null;
    }
    public String toString() {
        return "Sentencia For";
    }
}