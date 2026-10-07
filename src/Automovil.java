import java.util.ArrayList;

public class Automovil extends Vehiculo {
    private final int cantidadPasajeros;
    private final boolean transmisionAutomatica;
    public Automovil(String placa, String marca, String modelo, double tarifaDiaria, int cantidadPasajeros, boolean transmisionAutomatica) {
        super(placa, marca, modelo, tarifaDiaria);
        this.cantidadPasajeros = Validacion.positivo(cantidadPasajeros, "Pasajeros");
        this.transmisionAutomatica = transmisionAutomatica;
    }
    public int getCantidadPasajeros() { return cantidadPasajeros; }
    public boolean isTransmisionAutomatica() { return transmisionAutomatica; }
    @Override public double calcularSubtotal(int dias) {
        Validacion.positivo(dias, "Días");
        return Validacion.dinero((getTarifaDiaria() + (transmisionAutomatica ? 50 : 0)) * dias);
    }
    @Override public boolean validarLicencia(ArrayList<TipoLicencia> licencias) { return licencias.contains(TipoLicencia.A) || licencias.contains(TipoLicencia.B) || licencias.contains(TipoLicencia.C); }
    @Override public int obtenerUmbralMantenimiento() { return 30; }
    @Override public String describirCaracteristicas() { return cantidadPasajeros + " pasajeros, transmisión " + (transmisionAutomatica ? "automática" : "manual"); }
    @Override public String getCategoria() { return "Automovil"; }
}
