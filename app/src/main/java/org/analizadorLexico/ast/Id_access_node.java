package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Id_access_node extends NodoAST {
    private String id;

    // IMPORTANTE: Si tus arreglos tienen tamaño variable,
    // deberías recuperar esto de la TablaSimbolos.
    // Basado en tu ejemplo "_arr_[3][3]", asumimos 3 columnas por defecto.
    private static final int COLUMNAS_POR_DEFECTO = 3;

    public Id_access_node(String id) {
        super();
        this.id = id;
    }

    // Constructor para arrays: arr[i][j]
    // "dims" contendrá la lista de índices
    public Id_access_node(String id, NodoAST dims) {
        super();
        this.id = id;
        if(dims != null) this.agregarHijo(dims);
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        // 1. Validar existencia
        if (!ts.existe(this.id)) {
            System.err.println("Error Semántico: La variable '" + this.id + "' no ha sido declarada.");
            this.tipoDato = "error";
            return;
        } else {
            this.tipoDato = ts.obtenerTipo(this.id);
        }

        // Validar acceso a arreglo (Siempre 2 Dimensiones)
        if (!this.hijos.isEmpty()) {
            NodoAST nodoDimensiones = this.hijos.get(0);

            // Verificar que sea un arreglo (su tipo termina en [])
            if (!this.tipoDato.contains("[]")) {
                System.err.println("Error Semántico: La variable '" + this.id + "' no es un arreglo.");
            }

            // Verificar que tenga EXACTAMENTE 2 índices
            if (nodoDimensiones.hijos.size() != 2) {
                System.err.println("Error Semántico: El arreglo '" + this.id + "' es de 2 dimensiones, se recibieron " + nodoDimensiones.hijos.size() + " índices.");
            }

            // Verificar que ambos índices sean INT
            for (NodoAST indice : nodoDimensiones.hijos) {
                indice.checkSemantics(ts);
                if (indice.tipoDato != null && !indice.tipoDato.equals("int")) {
                    System.err.println("Error Semántico: Los índices deben ser enteros. Encontrado: " + indice.tipoDato);
                }
            }

            this.tipoDato = this.tipoDato.replace("[]", "");
        }
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        if (this.hijos.isEmpty()) {
            return this.id;
        }

        NodoAST nodoDimensiones = this.hijos.get(0);

        if (nodoDimensiones.hijos.size() >= 2) {
            String fila = nodoDimensiones.hijos.get(0).generateCode(gi);
            String col = nodoDimensiones.hijos.get(1).generateCode(gi);

            String tempMult = gi.nuevaTemporal();
            String tempSuma = gi.nuevaTemporal();

            gi.agregarCuarteto("*", fila, String.valueOf(COLUMNAS_POR_DEFECTO), tempMult);

            gi.agregarCuarteto("+", tempMult, col, tempSuma);

            String offsetTotal = gi.nuevaTemporal();
            gi.agregarCuarteto("*", tempSuma, "4", offsetTotal);

            String resultado = gi.nuevaTemporal();
            gi.agregarCuarteto("ARR_LOAD", this.id, offsetTotal, resultado);

            return resultado;
        }

        return null;
    }

    @Override
    public String toString() {
        return "Acceso Matriz: " + id;
    }
}