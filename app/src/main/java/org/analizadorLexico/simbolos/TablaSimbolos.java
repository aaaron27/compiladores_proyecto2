package org.analizadorLexico.simbolos;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class TablaSimbolos {
    private final Stack<Map<String, InfoSimbolo>> scopes;
    private final Stack<Map<String, InfoSimbolo>> otros;
    private int currentLevel;

    private int offsetActual = 0;

    public TablaSimbolos() {
        this.scopes = new Stack<>();
        this.otros = new Stack<>();
        this.currentLevel = 0;
    }

    public void openScope() {
        scopes.push(new HashMap<>());
        currentLevel++;
    }

    public void closeScope() {
        if (scopes.isEmpty()) return;

        final Map<String, InfoSimbolo> scope = scopes.pop();
        otros.push(scope);
        currentLevel--;
    }

    public void agregar(final String nombre, final String tipo) {
        if (scopes.isEmpty()) {
            System.err.println("Error Crítico: Intentando agregar variable '" + nombre + "' sin un scope abierto.");
            return;
        }

        final Map<String, InfoSimbolo> scope = scopes.peek();

        if (scope.containsKey(nombre)) {
            System.err.println("Error Semántico: La variable '" + nombre + "' ya existe en este ámbito.");
        } else {
            offsetActual -= 4;
            scope.put(nombre, new InfoSimbolo(tipo, offsetActual));

        }
    }

    public boolean existe(final String nombre) {
        return obtenerInfo(nombre) != null;
    }

    public InfoSimbolo obtenerInfo(final String nombre) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(nombre)) {
                return scopes.get(i).get(nombre);
            }
        }
        return null;
    }

    public String obtenerTipo(final String nombre) {
        InfoSimbolo info = obtenerInfo(nombre);
        return (info != null) ? info.tipo : null;
    }

    public int obtenerOffset(final String nombre) {
        InfoSimbolo info = obtenerInfo(nombre);
        return (info != null) ? info.offset : 0;
    }

    public void resetOffset() {
        this.offsetActual = 0;
    }

    public void imprimirTabla() {
        System.out.println("\n=== HISTORIAL DE TABLA DE SÍMBOLOS (Scopes Cerrados) ===");
        System.out.println("Cantidad de scopes registrados: " + otros.size());

        for (int i = 0; i < otros.size(); i++) {
            final Map<String, InfoSimbolo> scope = otros.get(i);
            System.out.println("\n--- Scope #" + (i + 1) + " ---");

            if (scope.isEmpty()) {
                System.out.println("  (vacío)");
            } else {
                System.out.printf("  %-15s | %-15s | %-10s%n", "Nombre", "Tipo", "Offset");
                System.out.println("  " + "-".repeat(46));
                scope.forEach((nombre, info) ->
                        System.out.printf("  %-15s | %-15s | %-10d%n", nombre, info.tipo, info.offset)
                );
            }
        }
        System.out.println("========================================================\n");
    }
}
