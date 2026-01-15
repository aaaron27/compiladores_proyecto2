package org.analizadorLexico.generator;

import java.io.File;

public final class PathResolver {
    private PathResolver() {
    }

    public static File resolve() {
        final String base = System.getProperty("user.dir");

        final File[] candidates = {
                new File(base + "/app/src/main/java/org/analizadorLexico/"),
                new File(base + "/src/main/java/org/analizadorLexico/")
        };

        for (File f : candidates) {
            if (f.exists()) return f;
        }

        throw new RuntimeException("No se pudo detectar la ruta del paquete");
    }
}
