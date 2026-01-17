package org.analizadorLexico.ast;
public class Id_access_node extends NodoAST {
    private String id;
    public Id_access_node(String id) { this.id = id; }
    public Id_access_node(String id, NodoAST dims) {
        this.id = id;
        if(dims != null) this.agregarHijo(dims);
    }
    @Override public String toString() { return "Acceso ID: " + id; }
}