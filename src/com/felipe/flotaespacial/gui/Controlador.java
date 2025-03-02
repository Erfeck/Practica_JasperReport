package com.felipe.flotaespacial.gui;

import com.felipe.flotaespacial.gui.enums.RangoTripulante;
import com.felipe.flotaespacial.gui.vistaseliminar.VistaEliminar;
import com.felipe.flotaespacial.jasper.ReportGenerator;
import com.felipe.flotaespacial.util.Util;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;

import javax.help.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Controlador implements ActionListener {

    Modelo modelo;
    Vista vista;
    VistaEliminar vistaEliminar;

    public Controlador(Modelo modelo, Vista vista) {
        this.modelo = modelo;
        this.vista = vista;
        this.vistaEliminar = new VistaEliminar();

        modelo.setValues();
        modelo.conectar();

        addActionListeners(this);
        addKeyListener();
        addWindowClosingListener();

        rellenarComboBoxNave();
    }

    private void addActionListeners(ActionListener listener) {
        //Atributos Nave
        vista.bNaveAgregar.addActionListener(listener);
        vista.bNaveEliminar.addActionListener(listener);
        //Atributos Tripulante
        vista.bTripulanteAgregar.addActionListener(listener);
        vista.bTripulanteEliminar.addActionListener(listener);
        //Atributos Misión
        vista.bMisionAgregar.addActionListener(listener);
        vista.bMisionEliminar.addActionListener(listener);
        //Vista Eliminar
        vistaEliminar.bVistaEliminar.addActionListener(listener);
        //Botones Informe
        vista.bInformeNaves.addActionListener(listener);
        vista.bInformeTripulantes.addActionListener(listener);
        vista.bInformeTripulantesPorNave.addActionListener(listener);
        vista.bInformeMisionesPorEstado.addActionListener(listener);
        vista.bInformeMisionesDeUnaNave.addActionListener(listener);
        vista.bInformeTripulacionPorRango.addActionListener(listener);
        //Botones Informe
        vista.bGrafico1.addActionListener(listener);
        vista.bGrafico2.addActionListener(listener);
    }
    private void addKeyListener() {
        vista.panel1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_F1) {
                    mostrarAyuda();
                }
            }
        });
    }
    private void addWindowClosingListener() {
        vista.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                int opcionParaSalir = JOptionPane.showConfirmDialog(
                        null,
                        "¿Seguro que quieres salir?",
                        "Salir App",
                        JOptionPane.YES_NO_OPTION);

                if (opcionParaSalir == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()) {
            case "AgregarNave":
                if (hayCamposVaciosEnNave()) {
                    Util.showErrorAlert("Debes rellenar todos los campos de la nave");
                    return;
                }
                String nombreNave = vista.tfNaveNombre.getText();
                String clase = String.valueOf(vista.cbNaveClase.getSelectedItem());

                String textoCapacidad = String.valueOf(vista.tfNaveCapacidad.getText());
                if (!validarCampoNumeros(textoCapacidad, "Capacidad")) {
                    return;
                }
                int capacidad = Integer.parseInt(textoCapacidad);

                modelo.insertarNave(nombreNave, clase, capacidad);
                limpiarCamposNave();
                rellenarComboBoxNave();
                break;
            case "AgregarTripulante":
                if (hayCamposVaciosEnTripulante()) {
                    Util.showErrorAlert("Debes rellenar todos los campos del tripulante");
                    return;
                }
                String nombreTripulante = vista.tfTripulanteNombre.getText();
                String rango = String.valueOf(vista.cbTripulanteRango.getSelectedItem());

                String nave = String.valueOf(vista.cbTripulanteNaveAsignada.getSelectedItem());
                int idNave = extraerId(nave);

                modelo.insertarTripulante(nombreTripulante, rango, idNave);
                limpiarCamposTripulante();
                break;
            case "AgregarMision":
                if (hayCamposVaciosEnMision()) {
                    Util.showErrorAlert("Debes rellenar todos los campos de la misión");
                    return;
                }
                String descripcion = vista.taMisionDescripcion.getText();
                String estado = String.valueOf(vista.cbMisionEstado.getSelectedItem());

                nave = String.valueOf(vista.cbMisionNaveAsignada.getSelectedItem());
                idNave = extraerId(nave);

                modelo.insertarMision(descripcion, estado, idNave);
                limpiarCamposMision();
                break;
            case "EliminarNave":
                Map<String, String> listaNaves = null;
                try {
                    listaNaves = modelo.consultarNaves();
                } catch (SQLException ex) {
                    System.out.println("Error al listar naves para eliminar: " + e);
                    break;
                }
                vistaEliminar.rellenarVistaEliminarNaves("Lista Naves", listaNaves);
                break;
            case "EliminarTripulante":
                Map<String, String> listaTripulantes = null;
                try {
                    listaTripulantes = modelo.consultarTripulantes();
                } catch (SQLException ex) {
                    System.out.println("Error al listar tripulantes para eliminar: " + e);
                }
                vistaEliminar.rellenarVistaEliminarTripulantes("Lista Tripulantes", listaTripulantes);
                break;
            case "EliminarMision":
                Map<String, String> listaMisiones = null;
                try {
                    listaMisiones = modelo.consultarMisiones();
                } catch (SQLException ex) {
                    System.out.println("Error al listar misiones para eliminar: " + e);
                }
                vistaEliminar.rellenarVistaEliminarMision("Lista Misiones", listaMisiones);
                break;
            case "ConfirmarEliminar":
                String entidad = String.valueOf(vistaEliminar.cbVistaEliminar.getSelectedItem());
                int idEntidad = extraerId(entidad);

                switch (vistaEliminar.tipoEliminacion) {
                    case "Nave":
                        modelo.eliminar("nave_espacial", idEntidad);
                        break;
                    case "Tripulante":
                        modelo.eliminar("tripulante", idEntidad);
                        break;
                    case "Mision":
                        modelo.eliminar("mision", idEntidad);
                        break;
                }
                vistaEliminar.dispose();
                break;
            case "ListaNaves":
                generarReporte("Informe1", "Informe1_Naves_Espaciales.jasper", new HashMap<>());
                break;
            case "ListaTripulantes":
                generarReporte("Informe2", "Informe2_Tripulantes.jasper", new HashMap<>());
                break;
            case "ListaTripulantesPorNave":
                generarReporte("Informe3", "Informe3_TripulantesPorNave.jasper", new HashMap<>());
                break;
            case "ListaMisionesPorEstado":
                generarReporte("Informe4", "Informe4_MisionesPorEstado.jasper", new HashMap<>());
                break;
            case "ListaMisionesDeUnaNave":
                String mensaje = null;
                mensaje = JOptionPane.showInputDialog("Introduce el ID de la nave");
                if (mensaje != null && !mensaje.isEmpty()) {
                    if (!validarCampoNumeros(mensaje, "ID Nave")) {
                        break;
                    }
                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("Nave", mensaje);
                    generarReporte("Informe5", "Informe5_MisionesDeUnaNave.jasper", parametros);
                }
                break;
            case "ListaTripulaciónPorRango":
                mensaje = null;
                mensaje = JOptionPane.showInputDialog("Introduce el Rango del tripulante");
                if (mensaje != null && !mensaje.isEmpty()) {
                    if (!validarCampoRangoTripulante(mensaje)) {
                        Util.showErrorAlert("El rango no existe");
                        break;
                    }
                    Map<String, Object> parametros = new HashMap<>();
                    parametros.put("Rango", mensaje);
                    generarReporte("Informe6", "Informe6_TripulantesPorRango.jasper", parametros);
                }
                break;
            case "Grafico1":
                generarReporte("Grafico1", "Grafico1_RangoTripulantes.jasper", new HashMap<>());
                break;
            case "Grafico2":
                generarReporte("Grafico2", "Grafico2_EstadoMisiones.jasper", new HashMap<>());
                break;
        }
    }

    private void limpiarCamposNave() {
        vista.tfNaveNombre.setText(null);
        vista.cbNaveClase.setSelectedIndex(0);
        vista.tfNaveCapacidad.setText(null);
    }
    private void limpiarCamposTripulante() {
        vista.tfTripulanteNombre.setText(null);
        vista.cbTripulanteRango.setSelectedIndex(0);
        vista.cbTripulanteNaveAsignada.setSelectedIndex(0);
    }
    private void limpiarCamposMision() {
        vista.cbMisionEstado.setSelectedIndex(0);
        vista.cbMisionNaveAsignada.setSelectedIndex(0);
        vista.taMisionDescripcion.setText(null);
    }

    private boolean hayCamposVaciosEnNave() {
        return vista.tfNaveNombre.getText().isEmpty() ||
                vista.cbNaveClase.getSelectedIndex() == 0 ||
                vista.tfNaveCapacidad.getText().isEmpty();
    }
    private boolean hayCamposVaciosEnTripulante() {
        return vista.tfTripulanteNombre.getText().isEmpty() ||
                vista.cbTripulanteRango.getSelectedIndex() == 0 ||
                vista.cbTripulanteNaveAsignada.getSelectedIndex() == 0;
    }
    private boolean hayCamposVaciosEnMision() {
        return vista.cbMisionNaveAsignada.getSelectedIndex() == 0  ||
                vista.cbMisionEstado.getSelectedIndex() == 0 ||
                vista.taMisionDescripcion.getText().isEmpty();
    }

    private boolean validarCampoNumeros(String posibleNumero, String nombreCampo) {
        try {
            Integer.parseInt(posibleNumero);
            return true;
        } catch (Exception e) {
            Util.showErrorAlert("El campo " + nombreCampo + " debe ser un número entero");
        }
        return false;
    }
    private boolean validarCampoRangoTripulante(String posibleRango) {
        for (RangoTripulante unRango : RangoTripulante.values()) {
            if (posibleRango.equalsIgnoreCase(unRango.getNombre())) {
                return true;
            }
        }
        return false;
    }

    private void rellenarComboBoxNave() {
        vista.cbTripulanteNaveAsignada.removeAllItems();
        vista.cbTripulanteNaveAsignada.addItem("Elegir nave");

        vista.cbMisionNaveAsignada.removeAllItems();
        vista.cbMisionNaveAsignada.addItem("Elegir nave");

        try {
            for (Map.Entry<String , String> entrada : modelo.consultarNaves().entrySet()) {
                String id = entrada.getKey();
                String nombre = entrada.getValue();

                String nave = id + " - " + nombre;

                vista.cbTripulanteNaveAsignada.addItem(nave);
                vista.cbMisionNaveAsignada.addItem(nave);
            }
        } catch (SQLException e) {
            System.out.println("Error al rellenar los combobox de las naves: " + e);
        }

    }

    private int extraerId(String entidad) {
        String[] partesEntidad = entidad.split(" - ");
        return Integer.parseInt(partesEntidad[0]);
    }

    private void generarReporte(String nombreDocumento, String nombreInforme, Map<String, Object> parametros) {
        String ubicacionInforme = "informes/" + nombreInforme;
        JasperPrint informeLleno = ReportGenerator.generarInforme(ubicacionInforme, parametros, modelo.conectarJasper());

        File pdfFile = new File("reporte.pdf");
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(informeLleno));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));
        try {
            exporter.exportReport();
        } catch (JRException e) {
            System.out.println("Error pdf:");
            throw new RuntimeException(e);
        }

        try {
            Desktop.getDesktop().browse(pdfFile.toURI());
        } catch (IOException e) {
            System.out.println("Error navegador:");
            throw new RuntimeException(e);
        }
    }
    private void generarReporte(String nombreInforme, Map<String, Object> parametros) {
        JasperPrint informeLleno = ReportGenerator.generarInforme(nombreInforme, parametros, modelo.conectarJasper());

        File pdfFile = new File("reporte.pdf");
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(informeLleno));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(pdfFile));
        try {
            exporter.exportReport();
        } catch (JRException e) {
            System.out.println("Error pdf:");
            throw new RuntimeException(e);
        }
        try {
            Desktop.getDesktop().browse(pdfFile.toURI());
        } catch (IOException e) {
            System.out.println("Error navegador:");
            throw new RuntimeException(e);
        }
    }

    private void mostrarAyuda() {
        try {
            File fichero = new File("src/com/felipe/flotaespacial/help/help_set.hs");
            URL hsURL = fichero.toURI().toURL();

            HelpSet helpset = new HelpSet(getClass().getClassLoader(), hsURL);
            HelpBroker hb = helpset.createHelpBroker();

            hb.setDisplayed(true);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}