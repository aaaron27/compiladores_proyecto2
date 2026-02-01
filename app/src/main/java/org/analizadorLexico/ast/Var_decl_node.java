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
        switch (this.hijos.size()) {
            case 1:
                gi.agregarCuarteto("DECL", this.id, null, null);
                break;
            case 2:
                NodoAST expr = this.hijos.get(1);
                String value = expr.generateCode(gi);

                gi.agregarCuarteto("=", value, null, this.id);
                break;
            case 3:
                NodoAST dims = this.hijos.get(1);
                NodoAST arrayInit = this.hijos.get(2);

                String len = dims.generateCode(gi);

                gi.agregarCuarteto("DECL_ARRAY", this.id, len, null);

                if (arrayInit != null) {
                    int i = 0;
                    int j = 0;

                    for (NodoAST hijo : arrayInit.hijos) {
                        String value = hijo.generateCode(gi);
                        gi.agregarCuarteto("[][]=", value, i + "," + j, this.id);
                    }
                }
                break;
        }

        return this.id;
    }

    @Override
    public String toString() {
        return "Var Local: " + id;
    }
}