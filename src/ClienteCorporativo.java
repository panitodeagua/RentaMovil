import java.util.ArrayList;
public class ClienteCorporativo extends Cliente {
    private final String nombreEmpresa, nombreContacto;
    public ClienteCorporativo(String identificador, String nombre, ArrayList<TipoLicencia> licencias,
                              String nombreEmpresa, String nombreContacto) {
        super(identificador, nombre, licencias);
        this.nombreEmpresa = Validacion.texto(nombreEmpresa, "Empresa");
        this.nombreContacto = Validacion.texto(nombreContacto, "Contacto");
    }
    public String getNombreEmpresa() { return nombreEmpresa; }
    public String getNombreContacto() { return nombreContacto; }
    @Override public double calcularDescuento(double subtotal, int confirmadosPrevios) { return Validacion.dinero(subtotal * 0.10); }
    @Override public int obtenerLimiteAlquileresActivos() { return 3; }
    @Override public String toString() { return "Corporativo | " + super.toString() + " | " + nombreEmpresa + " | Contacto: " + nombreContacto; }
}
