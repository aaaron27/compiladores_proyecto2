package org.analizadorLexico.codigo;

public class Cuarteto {
    public String op;   // +, -, *, /, =, IF, GOTO, LABEL, PRINT
    public String arg1;
    public String arg2;
    public String res;

    public Cuarteto(String op, String arg1, String arg2, String res) {
        this.op = op;
        this.arg1 = arg1;
        this.arg2 = arg2;
        this.res = res;
    }

    @Override
    public String toString() {
        return String.format("%-10s %-10s %-10s %-10s", op, arg1, (arg2 == null ? "" : arg2), res);
    }
}
