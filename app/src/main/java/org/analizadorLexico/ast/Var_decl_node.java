package org.analizadorLexico.ast;

import org.analizadorLexico.codigo.GeneradorIntermedio;
import org.analizadorLexico.simbolos.TablaSimbolos;

public class Var_decl_node extends NodoAST {
    private String id;
    private boolean esArreglo = false;

    public Var_decl_node(TypeNode tipo, String id) {
        super();
        this.id = id;
        if (tipo != null) this.agregarHijo(tipo);
    }

    public Var_decl_node(TypeNode tipo, String id, NodoAST expresion) {
        super();
        this.id = id;
        if (tipo != null) this.agregarHijo(tipo);
        if (expresion != null) this.agregarHijo(expresion);
    }

    public Var_decl_node(TypeNode tipo, String id, NodoAST dimensiones, NodoAST arrayInit) {
        super();
        this.id = id;
        this.esArreglo = true;
        if (tipo != null) this.agregarHijo(tipo);
        if (dimensiones != null) this.agregarHijo(dimensiones);
        if (arrayInit != null) this.agregarHijo(arrayInit);
    }

    @Override
    public void checkSemantics(TablaSimbolos ts) {
        String tipoDeclarado = this.hijos.get(0).toString();

        // --- CASO A: Es un Arreglo (Matriz 2D) ---
        if (esArreglo) {
            String tipoArreglo = tipoDeclarado + "[]"; // "int[]"
            ts.agregar(this.id, tipoArreglo);

            // Validaciones de tamaño y estructura
            if (this.hijos.size() > 1) {
                NodoAST dims = this.hijos.get(1); // Dims_decl_node

                int declaredRows = 0;
                int declaredCols = 0;

                // Obtener dimensiones declaradas
                if (dims instanceof Dims_decl_node) {
                    Dims_decl_node d = (Dims_decl_node) dims;
                    declaredRows = d.getFilas();
                    declaredCols = d.getColumnas();
                }

                if (this.hijos.size() > 2) {
                    NodoAST init = this.hijos.get(2); // Array_init_node

                    if (declaredRows > 0 && declaredCols > 0) {
                        validarMatriz(init, declaredRows, declaredCols);
                    }

                    init.checkSemantics(ts);
                }
            }
        }
        else {
            ts.agregar(this.id, tipoDeclarado);

            if (this.hijos.size() > 1) {
                NodoAST expresion = this.hijos.get(1);
                expresion.checkSemantics(ts);

                String tipoExpr = expresion.tipoDato;
                if (tipoExpr != null && !tipoExpr.equals("error")) {
                    if (!sonTiposCompatibles(tipoDeclarado, tipoExpr)) {
                        System.err.println("Error Semántico: No se puede asignar '" + tipoExpr + "' a '" + this.id + "'.");
                    }
                }
            }
        }
    }
    private void validarMatriz(NodoAST init, int maxRows, int maxCols) {
        if (init.hijos.isEmpty()) return;

        NodoAST rowList = init.hijos.get(0); // Row_list_node
        int numRows = rowList.hijos.size();
        if (numRows > maxRows) {
            System.err.println("Error Semántico: Desbordamiento de filas en '" + this.id +
                    "'. Declaradas: " + maxRows + ", Inicializadas: " + numRows);
        }
        for (NodoAST row : rowList.hijos) {
            if (!row.hijos.isEmpty()) {
                NodoAST exprList = row.hijos.get(0); // Expression_list_node
                int numCols = exprList.hijos.size();

                if (numCols > maxCols) {
                    System.err.println("Error Semántico: Desbordamiento de columnas en '" + this.id +
                            "'. Declaradas: " + maxCols + ", Encontradas: " + numCols);
                }
            }
        }
    }
    private boolean sonTiposCompatibles(String declarado, String asignado) {
        if (declarado.equals(asignado)) return true;
        if (declarado.equals("float") && asignado.equals("int")) return true;
        return false;
    }

    @Override
    public String generateCode(GeneradorIntermedio gi) {
        // Generar código para inicialización de Matriz
        if (esArreglo && this.hijos.size() > 2) {
            NodoAST dims = this.hijos.get(1);
            NodoAST init = this.hijos.get(2);

            if (dims instanceof Dims_decl_node) {
                int maxCols = ((Dims_decl_node) dims).getColumnas();
                int filas = ((Dims_decl_node) dims).getFilas();
                int cols = ((Dims_decl_node) dims).getColumnas();

                int sizeBytes = filas * cols * 4;

                gi.agregarCuarteto("DECLARE_ARRAY", this.id, String.valueOf(sizeBytes), null);
                if (!init.hijos.isEmpty()) {
                    NodoAST rowList = init.hijos.get(0);

                    int i = 0;
                    for (NodoAST row : rowList.hijos) {
                        if (!row.hijos.isEmpty()) {
                            NodoAST exprList = row.hijos.get(0);

                            int j = 0;
                            for (NodoAST expr : exprList.hijos) {
                                String val = expr.generateCode(gi);

                                int offsetInt = (i * maxCols + j) * 4;


                                // ARR_STORE id, offset, valor
                                gi.agregarCuarteto("ARR_STORE", this.id, String.valueOf(offsetInt), val);

                                j++;
                            }
                        }
                        i++;
                    }
                }
            }
        }
        // Generar código para variable simple
        else if (!esArreglo && this.hijos.size() > 1) {
            String valor = this.hijos.get(1).generateCode(gi);
            gi.agregarCuarteto("=", valor, null, this.id);
        }

        return null;
    }

    @Override
    public String toString() {
        return "Var Local: " + id;
    }
}