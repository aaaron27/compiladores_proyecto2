package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Main_def_node extends NodoAST {

    public Main_def_node(NodoAST bloque) {
        super();
        if (bloque != null) {
            this.agregarHijo(bloque);
        }
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        ts.openScope();

        super.checkSemantics(ts); // Revisar el bloque interno

        ts.closeScope();
    }

    public String generateCode(GeneradorIntermedio gi) {
        // Esto le dice al GeneradorMips que escriba "main:" y configure el $fp
        gi.agregarCuarteto("LABEL", null, null, "main");

        // 2. Generar el código del bloque interno
        if (!this.hijos.isEmpty()) {
            this.hijos.get(0).generateCode(gi);
        }

        // Esto genera el syscall 10 para terminar el programa correctamente
        gi.agregarCuarteto("EXIT", null, null, null);

        return null;
    }

    @Override
    public String toString() {
        return "Metodo Main (Coal Navidad)";
    }
}