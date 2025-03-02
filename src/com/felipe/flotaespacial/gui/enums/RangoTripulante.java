package com.felipe.flotaespacial.gui.enums;

public enum RangoTripulante {
    CAPITAN("Capitán"),
    INGENIERO("Ingeniero"),
    PILOTO("Piloto");

    private final String nombre;

    RangoTripulante(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
}
