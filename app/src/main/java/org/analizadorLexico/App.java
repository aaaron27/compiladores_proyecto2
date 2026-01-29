package org.analizadorLexico;

import org.analizadorLexico.analyzer.LexicalAnalyzer;
import org.analizadorLexico.ast.NodoAST;
import java.io.Reader;
import java.io.FileReader;

public final class App {
    private static final String TEST_FILE_PATH = "/home/aaaron27/compiladores/proyecto2/prueba.txt";

    public static void main(String[] args) {
        try {
            LexicalAnalyzer.analyze(TEST_FILE_PATH);

            Reader reader = new FileReader(TEST_FILE_PATH);
            Lexer lexer = new Lexer(reader);
            Parser parser = new Parser(lexer);

            System.out.println("Analizando sintaxis...");
            Object result = parser.parse().value;

            if (result instanceof NodoAST) {
                NodoAST raiz = (NodoAST) result;
                System.out.println("\n--- ARBOL DE SINTAXIS ABSTRACTA (AST) ---");
                raiz.print("", true);
                parser.getTablaSimbolos().imprimirTabla();
                System.out.println("\nAnalisis finalizado con exito!");
            } else {
                System.out.println("El analisis termino pero no se genero un arbol valido.");
            }

        } catch (Exception e) {
            System.err.println("\n[ERROR] Durante el análisis:");
            e.printStackTrace();
        }
    }
}