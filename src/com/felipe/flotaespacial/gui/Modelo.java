package com.felipe.flotaespacial.gui;

import com.felipe.flotaespacial.util.Util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class Modelo {

    private Connection connection;
    String ip;
    String user;
    String pwd;
    String database;

    public void setValues() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("config.properties")) {
            properties.load(input);
            ip = properties.getProperty("ip");
            user = properties.getProperty("user");
            pwd = properties.getProperty("password");
            database = properties.getProperty("name");
        } catch (IOException e) {
            System.out.println("Error al cargar el archivo de configuración: " + e.getMessage());
        }
    }

    public void conectar() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database + "?useSSL=false&allowPublicKeyRetrieval=true", user, pwd);
            System.out.println("Conexión establecida correctamente");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e);
        }
    }
    public Connection conectarJasper() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + ip + ":3306/" + database + "?useSSL=false", user, pwd);
            //System.out.println("Conexión establecida correctamente");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e);
        }
        return connection;
    }
    public void desconectar() {
        try {
            connection.close();
            System.out.println("Conexión finalizada correctamente");
        } catch (SQLException e) {
            System.out.println("Error al cerrar conexión: " + e);
        }
    }

    public void insertarNave(String nombre, String clase, int capacidad) {
        String sql = "INSERT INTO nave_espacial (nombre_nave, clase, capacidad_tripulacion) " +
                "VALUES (?, ?, ?)";
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, clase);
            pst.setInt(3, capacidad);
            pst.executeUpdate();

            Util.showInfoAlert("Se agrego la nave " + nombre + " correctamente");
        } catch (SQLException e) {
            System.out.println("Error al insertar nave: " + e);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar pst-nave: " + e);
                }
            }
        }
    }
    public void insertarTripulante(String nombre, String rango, int idNave) {
        String sql = "INSERT INTO tripulante (nombre_tripulante, rango, id_nave) " +
                "VALUES (?, ?, ?)";
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, nombre);
            pst.setString(2, rango);
            pst.setInt(3, idNave);
            pst.executeUpdate();

            Util.showInfoAlert("Se agrego al tripulante " + nombre + " correctamente");
        } catch (SQLException e) {
            System.out.println("Error al insertar tripulante: " + e);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar pst-tripulante: " + e);
                }
            }
        }
    }
    public void insertarMision(String descripcion, String estado, int idNave) {
        String sql = "INSERT INTO mision (descripcion, estado, id_nave) " +
                "VALUES (?, ?, ?)";
        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(sql);
            pst.setString(1, descripcion);
            pst.setString(2, estado);
            pst.setInt(3, idNave);
            pst.executeUpdate();

            Util.showInfoAlert("Se agrego la misión correctamente");
        } catch (SQLException e) {
            System.out.println("Error al insertar misión: " + e);
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar pst-misión: " + e);
                }
            }
        }
    }

    public void eliminar(String tabla, int idABorrar) {
        String sql = "DELETE FROM " + tabla + " WHERE id = ?";

        PreparedStatement pst = null;
        try {
            pst = connection.prepareStatement(sql);
            pst.setInt(1, idABorrar);
            pst.executeUpdate();

            Util.showInfoAlert("Se elimino correctamente");
        } catch (SQLException e) {
            if (e.getSQLState().startsWith("23")) {
                Util.showErrorAlert("No puedes eliminar esta " + tabla + " porque está asociado con otra tabla");
            } else {
                System.out.println("Error al eliminar " + tabla + ": " + e);
            }
        } finally {
            if (pst != null) {
                try {
                    pst.close();
                } catch (SQLException e) {
                    System.out.println("Error al cerrar pst => eliminar " + tabla + ": " + e);
                }
            }
        }
    }

    public Map<String, String> consultarNaves() throws SQLException {
        LinkedHashMap<String, String> naves = new LinkedHashMap<>();

        String sql = "SELECT id, nombre_nave FROM nave_espacial;";

        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String id = String.valueOf(rs.getInt("id"));
            String nombre = rs.getString("nombre_nave");
            naves.put(id, nombre);
        }
        return naves;
    }
    public Map<String, String> consultarTripulantes() throws SQLException {
        LinkedHashMap<String, String> tripulantes = new LinkedHashMap<>();

        String sql = "SELECT t.id, t.nombre_tripulante, t.rango, ne.nombre_nave\n" +
                "FROM tripulante t\n" +
                "JOIN nave_espacial ne ON ne.id = t.id_nave;";

        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String id = String.valueOf(rs.getInt("id"));
            String nombre = rs.getString("nombre_tripulante");
            String nombre_nave = rs.getString("nombre_nave");

            tripulantes.put(id, nombre + " (" + nombre_nave + ")");
        }
        return tripulantes;
    }
    public Map<String, String> consultarMisiones() throws SQLException {
        LinkedHashMap<String, String> misiones = new LinkedHashMap<>();

        String sql = "SELECT m.id, ne.nombre_nave, m.estado, m.descripcion\n" +
                "FROM mision m\n" +
                "JOIN nave_espacial ne ON ne.id = m.id_nave;";

        PreparedStatement pst = connection.prepareStatement(sql);
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String id = String.valueOf(rs.getInt("id"));
            String nombre = rs.getString("nombre_nave");
            String estado = rs.getString("estado");
            String descripcion = rs.getString("descripcion");

            misiones.put(id + " - " + nombre + " (" + estado + ")", descripcion);
        }
        return misiones;
    }
}