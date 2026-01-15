package org.analizadorLexico.generator;

import java_cup.Main;
import jflex.exceptions.SilentExit;

import java.io.File;

public record JFlexCupGenerator(File baseDir) implements Generator {

    @Override
    public void generate() {
        System.out.println("Ruta detectada: " + baseDir.getAbsolutePath());
        generateParser();
        generateLexer();
    }

    private void generateParser() {
        final File cupFile = new File(baseDir, "Parser.cup");
        if (!cupFile.exists()) {
            System.err.println("Advertencia: No se encontró " + cupFile.getName());
            return;
        }

        final String[] args = {
                "-parser", "Parser",
                "-symbols", "sym",
                "-destdir", baseDir.getAbsolutePath(),
                cupFile.getAbsolutePath()
        };

        try {
            // ejecutar CUP
            Main.main(args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void generateLexer() {
        final File flexFile = new File(baseDir, "Lexer.flex");
        if (!flexFile.exists()) {
            System.err.println("Advertencia: No se encontró " + flexFile.getName());
            return;
        }

        final String[] args = {
                "-d", baseDir.getAbsolutePath(),
                flexFile.getAbsolutePath()
        };

        try {
            // ejecutar JFlex
            jflex.Main.generate(args);
        } catch (SilentExit e) {
            throw new RuntimeException(e);
        }
    }
}
