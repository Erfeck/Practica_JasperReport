<helpset version="1.0">
	<title>Guía App Flota Espacial</title>
	<maps>
		<!-- Pagina por defecto al mostrar la ayuda -->
		<homeID>main</homeID>
		<!-- Que mapa deseamos -->
		<mapref location="map_file.jhm"/>
	</maps>
 
	<!-- Las Vistas que deseamos mostrar en la ayuda -->
	<view>
		<name>Contenidos</name>
		<label>Tabla de contenidos</label>
		<type>javax.help.TOCView</type>
		<data>toc.xml</data>
	</view>
	
	<presentation default="true">
		<name>MainWindow</name>
		<size width="800" height="600"/>
		<location x="200" y="100"/>
		<toolbar>
			<toolbarShow>false</toolbarShow> <!-- 🔹 Esto oculta la barra de herramientas -->
		</toolbar>
	</presentation>


</helpset>