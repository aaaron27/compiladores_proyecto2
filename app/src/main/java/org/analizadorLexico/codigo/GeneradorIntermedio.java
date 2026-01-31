package org.analizadorLexico.codigo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GeneradorIntermedio {
    private static final String INT_OUTPUT_FILE = "int_output.txt";
    public List<Cuarteto> codigo;
    private int tempCount = 0;
    private int labelCount = 0;

    public GeneradorIntermedio() {
        codigo = new ArrayList<>();
    }

    public String nuevaTemporal() {
        return "$t" + (tempCount++);
    }

    public String nuevaEtiqueta() {
        return "L" + (labelCount++);
    }

    public void agregarCuarteto(String op, String arg1, String arg2, String res) {
        codigo.add(new Cuarteto(op, arg1, arg2, res));
    }

    public void imprimirCodigo() {
        System.out.println("\n=== CÓDIGO INTERMEDIO ===");

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(INT_OUTPUT_FILE))) {

            for (Cuarteto c : codigo) {
                String linea = c.toString();

                System.out.println(linea);

                writer.write(linea);
                writer.newLine();
            }

            System.out.println("=========================");
            System.out.println(">> Archivo generado exitosamente en: " + INT_OUTPUT_FILE);

        } catch (IOException e) {
            System.err.println("Error al guardar el codigo intermedio: " + e.getMessage());
        }
        System.out.println("\n");
    }
}