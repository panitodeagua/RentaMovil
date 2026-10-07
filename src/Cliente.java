import java.util.ArrayList;
import java.util.Locale;

public abstract class Cliente {
    private final String identificador, nombre;
    private final ArrayList<TipoLicencia> licencias;
    protected Cliente(String identificador, String nombre, ArrayList<TipoLicencia> licencias) {
        this.identificador = Validacion.texto(identificador, "Identificador").toUpperCase(Locale.ROOT);
        this.nombre = Validacion.texto(nombre, "Nombre");
        if (licencias == null || licencias.isEmpty() || licencias.contains(null))
            throw new IllegalArgumentException("Debe presentar al menos una licencia válida.");
        this.licencias = new ArrayList<>();
        for (TipoLicencia licencia : licencias) if (!this.licencias.contains(licencia)) this.licencias.add(licencia);
    }
    public String getIdentificador() { return identificador; }
    public String getNombre() { return nombre; }
    public ArrayList<TipoLicencia> getLicencias() { return new ArrayList<>(licencias); }
    public abstract double calcularDescuento(double subtotal, int confirmadosPrevios);
    public abstract int obtenerLimiteAlquileresActivos();
    @Override public String toString() { return identificador + " | " + nombre + " | Licencias: " + licencias; }
}
