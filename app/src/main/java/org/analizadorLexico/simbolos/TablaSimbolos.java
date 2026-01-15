package org.analizadorLexico.simbolos;

import java.util.HashMap;
import java.util.Map;

public class TablaSimbolos {
    private Map<String, String> tabla = new HashMap<>();

    public void agregar(String nombre, String tipo) {
        if (tabla.containsKey(nombre)) {
            System.err.println("Error Semantico: La variable '" + nombre + "' ya existe.");
        } else {
            tabla.put(nombre, tipo);
            System.out.println("Simbolo agregado: " + nombre + " (" + tipo + ")");
        }
    }

    public boolean existe(String nombre) {
        return tabla.containsKey(nombre);
    }

    public String obtenerTipo(String nombre) {
        return tabla.get(nombre);
    }
}