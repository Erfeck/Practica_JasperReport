package com.felipe.flotaespacial.jasper;

import java.sql.Connection;
import java.util.Map;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

public class ReportGenerator {
	public static JasperPrint generarInforme(String jasper, Map<String, Object> parametros, Connection connection) {
		try {
			JasperPrint informeLleno = JasperFillManager.fillReport(jasper, parametros, connection);
			return informeLleno;
		} catch (Exception e) {
			System.out.println("Error Generando Reporte:");
			e.printStackTrace();
		}
		return null;
	}
}