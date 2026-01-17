package org.analizadorLexico.ast;

public class Io_statement_node extends NodoAST{
    private String tipoOperacion; // "SHOW" o "GET"
    private String id;            // Solo para GET

    public Io_statement_node(NodoAST expresion) {
        super();
        this.tipoOperacion = "SHOW";
        if (expresion != null) {
            this.agregarHijo(expresion);
        }
    }

    public Io_statement_node(String id) {
        super();
        this.tipoOperacion = "GET";
        this.id = id;
    }

    public Io_statement_node(String id, NodoAST dimensiones) {
        super();
        this.tipoOperacion = "GET";
        this.id = id;

        if (dimensiones != null) {
            this.agregarHijo(dimensiones);
        }
    }

    @Override
    public String toString() {
        if (tipoOperacion.equals("SHOW")) {
            return "IO: Output (Show)";
        } else {
            return "IO: Input (Get) -> " + id;
        }
    }
}
