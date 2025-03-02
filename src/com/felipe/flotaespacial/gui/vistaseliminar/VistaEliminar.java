package com.felipe.flotaespacial.gui.vistaseliminar;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class VistaEliminar extends JFrame {

    static final String TITULO_FRAME = "Flota Espacial";
    public String tipoEliminacion;
    private int width;
    private int height;

    private JPanel panel1;
    public JComboBox cbVistaEliminar;
    public JButton bVistaEliminar;
    public JLabel labelVistaEliminar;

    public VistaEliminar() {
        super(TITULO_FRAME);
        iniciarFrame();
        tipoEliminacion = "";
    }

    private void iniciarFrame() {
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pack();
        panel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
        setSize(new Dimension(this.getWidth() + 20, this.getHeight() + 50));
        width = this.getWidth();
        height = this.getHeight();

        setLocationRelativeTo(null);
    }

    public void rellenarVistaEliminarNaves(String nombreLista, Map<String, String> listaNaves) {
        setSize(new Dimension(width + 20, height + 50));
        cbVistaEliminar.removeAllItems();
        labelVistaEliminar.setText(nombreLista);

        for (Map.Entry<String, String> entrada : listaNaves.entrySet()) {
            String id = entrada.getKey();
            String nombre = entrada.getValue();
            String nave = id + " - " + nombre;
            cbVistaEliminar.addItem(nave);
        }
        setVisible(true);
        tipoEliminacion = "Nave";
    }
    public void rellenarVistaEliminarTripulantes(String nombreLista, Map<String, String> listaTripulantes) {
        setSize(new Dimension( width + 20, height + 50));
        cbVistaEliminar.removeAllItems();
        labelVistaEliminar.setText(nombreLista);

        for (Map.Entry<String, String> entrada : listaTripulantes.entrySet()) {
            String id = entrada.getKey();
            String nombres = entrada.getValue();
            String tripulantePorNave = id + " - " + nombres;
            cbVistaEliminar.addItem(tripulantePorNave);
        }
        setVisible(true);
        tipoEliminacion = "Tripulante";
    }
    public void rellenarVistaEliminarMision(String nombreLista, Map<String, String> listaMisiones) {
        setSize(new Dimension(width + 400, height + 50));
        setLocationRelativeTo(null);
        cbVistaEliminar.removeAllItems();
        labelVistaEliminar.setText(nombreLista);

        for (Map.Entry<String, String> entrada : listaMisiones.entrySet()) {
            String nombreConEstado = entrada.getKey();
            String descripcion = entrada.getValue();
            cbVistaEliminar.addItem(nombreConEstado + ": " + descripcion);
        }
        setVisible(true);
        tipoEliminacion = "Mision";
    }
}