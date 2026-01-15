package org.analizadorLexico.generator;

import java.io.File;

public final class Generar {
    private Generar() {

    }

    public static void main(String[] args) {
        final File sourceDir = PathResolver.resolve();
        final Generator generator = new JFlexCupGenerator(sourceDir);
        generator.generate();
    }
}