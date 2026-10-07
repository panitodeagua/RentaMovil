import java.util.ArrayList;
public class ClienteIndividual extends Cliente {
    public ClienteIndividual(String identificador, String nombre, ArrayList<TipoLicencia> licencias) {
        super(identificador, nombre, licencias);
        if (!getIdentificador().matches("[0-9]{13}")) throw new IllegalArgumentException("El DPI debe tener exactamente 13 dígitos.");
    }
    @Override public double calcularDescuento(double subtotal, int confirmadosPrevios) {
        Validacion.dinero(subtotal);
        return Validacion.dinero(confirmadosPrevios >= 3 ? subtotal * 0.05 : 0);
    }
    @Override public int obtenerLimiteAlquileresActivos() { return 1; }
    @Override public String toString() { return "Individual | " + super.toString(); }
}
