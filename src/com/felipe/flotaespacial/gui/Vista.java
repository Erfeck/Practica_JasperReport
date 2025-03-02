package com.felipe.flotaespacial.gui;

import com.felipe.flotaespacial.gui.enums.ClaseNave;
import com.felipe.flotaespacial.gui.enums.EstadoMision;
import com.felipe.flotaespacial.gui.enums.RangoTripulante;

import javax.swing.*;
import java.awt.*;

public class Vista extends JFrame {

    static final String TITULO_FRAME = "Flota Espacial";
    private JTabbedPane tabbedPane1;
    public JPanel panel1;

    //Atributos Nave
    public JTextField tfNaveNombre;
    public JComboBox cbNaveClase;
    public JTextField tfNaveCapacidad;
    public JButton bNaveAgregar;
    public JButton bNaveEliminar;

    //Atributos Tripulante
    public JTextField tfTripulanteNombre;
    public JComboBox cbTripulanteRango;
    public JComboBox cbTripulanteNaveAsignada;
    public JButton bTripulanteAgregar;
    public JButton bTripulanteEliminar;

    //Atributos Misión
    public JComboBox cbMisionNaveAsignada;
    public JComboBox cbMisionEstado;
    public JButton bMisionAgregar;
    public JButton bMisionEliminar;
    public JTextArea taMisionDescripcion;

    //Botones Informe
    public JButton bInformeNaves;
    public JButton bInformeTripulantes;
    public JButton bInformeTripulantesPorNave;
    public JButton bInformeMisionesPorEstado;
    public JButton bInformeMisionesDeUnaNave;
    public JButton bInformeTripulacionPorRango;

    //Botones Gráficos
    public JButton bGrafico1;
    public JButton bGrafico2;
    public JButton bJavaHelp;

    public Vista() {
        super(TITULO_FRAME);
        iniciarFrame();
        rellenarComboBox();
        panel1.setFocusable(true);
        panel1.requestFocusInWindow();
    }

    private void iniciarFrame() {
        setContentPane(panel1);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        pack();
        panel1.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 20));
        setSize(new Dimension(this.getWidth() + 20, this.getHeight() + 50));
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void rellenarComboBox() {
        cbNaveClase.addItem("Elegir clase");
        for (ClaseNave unaClase : ClaseNave.values()) {
            cbNaveClase.addItem(unaClase.getNombre());
        }

        cbTripulanteRango.addItem("Elegir rango");
        for (RangoTripulante unRango : RangoTripulante.values()) {
            cbTripulanteRango.addItem(unRango.getNombre());
        }

        cbMisionEstado.addItem("Elegir estado");
        for (EstadoMision unEstado : EstadoMision.values()) {
            cbMisionEstado.addItem(unEstado.getNombre());
        }
    }
}
