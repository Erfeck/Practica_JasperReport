package com.felipe.flotaespacial.gui.enums;

public enum EstadoMision {
    COMPLETADA("Completada"),
    FALLIDA("Fallida"),
    EN_PROGRESO("En progreso");

    private final String nombre;

    EstadoMision(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
