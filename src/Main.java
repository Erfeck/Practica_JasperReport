import com.felipe.flotaespacial.gui.Controlador;
import com.felipe.flotaespacial.gui.Modelo;
import com.felipe.flotaespacial.gui.Vista;
import com.formdev.flatlaf.FlatLightLaf;

public class Main {
    public static void main(String[] args) {
        FlatLightLaf.setup();
        Modelo modelo = new Modelo();
        Vista vista = new Vista();
        Controlador controlador = new Controlador(modelo, vista);
    }
}