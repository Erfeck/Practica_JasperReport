package com.felipe.flotaespacial.gui.enums;

public enum ClaseNave {
    EXPLORACION("Exploración"),
    COMBATE("Combate"),
    TRANSPORTE("Transporte");

    private final String nombre;

    ClaseNave(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
