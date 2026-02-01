package org.analizadorLexico.simbolos;

public class InfoSimbolo {
    public String tipo;
    public int offset;

    public InfoSimbolo(String tipo, int offset) {
        this.tipo = tipo;
        this.offset = offset;
    }
}
