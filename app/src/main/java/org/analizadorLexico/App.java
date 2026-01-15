package org.analizadorLexico;

import static org.analizadorLexico.analyzer.LexicalAnalyzer.analyze;

public final class App {
    private static final String TEST_FILE_PATH = "C:/Users/faken/Desktop/code/proyectos/compi/1/CompiladoresProyecto1/prueba.txt";

    private App() {

    }

    public static void main(String[] args) {
        try {
            analyze(TEST_FILE_PATH);
            java.io.Reader reader = new java.io.FileReader(TEST_FILE_PATH);
            Lexer lexer = new Lexer(reader);

            Parser parser = new Parser(lexer);

            System.out.println("Analizando sintaxis...");
            parser.parse();

            System.out.println("Analisis sintactico finalizado con exito!");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error fatal durante el analisis: " + e.getMessage());
        }
    }
}
