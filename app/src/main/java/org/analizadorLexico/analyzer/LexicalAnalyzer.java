package org.analizadorLexico.analyzer;

import java.io.*;

import java_cup.runtime.Symbol;
import org.analizadorLexico.Lexer;
import org.analizadorLexico.sym;

public final class LexicalAnalyzer {
    private static final String TOKENS_OUTPUT_FILE = "tokens_output.txt";

    public static void analyze(String path) {
        try {
            // Leer archivo fuente
            final Reader reader = new FileReader(path);
            final Lexer lexer = new Lexer(reader);

            // Escribir en un archivo los tokens
            final BufferedWriter writer = new BufferedWriter(new FileWriter(TOKENS_OUTPUT_FILE));

            System.out.println("Iniciando analisis lexico...");

            Symbol token;
            while ((token = lexer.next_token()).sym != sym.EOF) {
                final String result = "Token: " + token.sym +
                        " ||| Lexema: " + (token.value != null ? token.value.toString() : "null") +
                        " ||| Linea: " + (token.left + 1) +
                        " ||| Columna: " + (token.right + 1);

                System.out.println(result); // Salida a consola
                writer.write(result);       // Salida a archivo
                writer.newLine();
            }

            writer.close();
            System.out.println("Analisis completado. Resultados en 'tokens_output.txt'");

        } catch (FileNotFoundException e) {
            System.err.println("Error al abrir el archivo: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error al interactuar con el archivo: " + e.getMessage());
        }
    }
}
