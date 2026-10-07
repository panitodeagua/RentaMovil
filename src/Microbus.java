import java.util.ArrayList;

public class Microbus extends Vehiculo {
    private final int cantidadPasajeros;
    private final boolean incluyePiloto;
    public Microbus(String placa, String marca, String modelo, double tarifaDiaria, int cantidadPasajeros, boolean incluyePiloto) {
        super(placa, marca, modelo, tarifaDiaria);
        this.cantidadPasajeros = Validacion.positivo(cantidadPasajeros, "Pasajeros");
        this.incluyePiloto = incluyePiloto;
    }
    public int getCantidadPasajeros() { return cantidadPasajeros; }
    public boolean isIncluyePiloto() { return incluyePiloto; }
    @Override public double calcularSubtotal(int dias) {
        Validacion.positivo(dias, "Días");
        return Validacion.dinero((getTarifaDiaria() + (incluyePiloto ? 250 : 0)) * dias);
    }
    @Override public boolean validarLicencia(ArrayList<TipoLicencia> licencias) { return incluyePiloto || licencias.contains(TipoLicencia.A) || licencias.contains(TipoLicencia.B); }
    @Override public int obtenerUmbralMantenimiento() { return 25; }
    @Override public String describirCaracteristicas() { return cantidadPasajeros + " pasajeros, " + (incluyePiloto ? "con piloto" : "sin piloto"); }
    @Override public String getCategoria() { return "Microbus"; }
}
