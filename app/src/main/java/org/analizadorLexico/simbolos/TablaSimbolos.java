package org.analizadorLexico.simbolos;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class TablaSimbolos {
    private final Stack<Map<String, String>> scopes;
    private final Stack<Map<String, String>> otros;
    private int currentLevel;

    public TablaSimbolos() {
        this.scopes = new Stack<>();
        this.otros = new Stack<>();
        this.currentLevel = 0;
        openScope();
    }

    public void openScope() {
        System.out.println("Abriendo scope");
        scopes.push(new HashMap<>());
        currentLevel++;
    }

    public void closeScope() {
        if (scopes.size() > 1) {
            final Map<String, String> scope = scopes.pop();
            otros.push(scope);
            currentLevel--;

            System.out.println("Cerrando scope");
        } else {
            System.err.println("Advertencia: No se puede cerrar el scope global");
        }
    }

    public void agregar(final String nombre, final String tipo) {
        final Map<String, String> scope = scopes.peek();

        if (scope.containsKey(nombre)) {
            System.err.println("Error Semantico: La variable '" + nombre + "' ya existe.");
        } else {
            scope.put(nombre, tipo);
            System.out.println("Simbolo agregado: " + nombre + " (" + tipo + ")");
        }
    }

    public boolean existe(final String nombre) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            if (scopes.get(i).containsKey(nombre)) {
                return true;
            }
        }
        return false;
    }

    public void imprimirTabla() {
        System.out.println("\n=== CONTENIDO DE LA TABLA DE SÍMBOLOS ===");
        System.out.println("Niveles de scope: " + otros.size());

        for (int i = otros.size() - 1; i >= 0; i--) {
            final Map<String, String> scope = otros.get(i);
            System.out.println("\n--- Scope nivel " + (i + 1) + " ---");

            if (scope.isEmpty()) {
                System.out.println("  (vacío)");
            } else {
                System.out.printf("  %-20s | %-15s | %-10s%n", "Nombre", "Tipo", "Nivel");
                System.out.println("  " + "-".repeat(50));
                scope.forEach((nombre, tipo) -> System.out.printf("%-20s | %-10s%n", nombre, tipo));
            }
        }
        System.out.println("==========================================\n");
    }

    public String obtenerTipo(final String nombre) {
        for (int i = scopes.size() - 1; i >= 0; i--) {
            final String tipo = scopes.get(i).get(nombre);
            if (tipo != null) {
                return tipo;
            }
        }
        return null;
    }
}