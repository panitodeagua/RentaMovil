import java.util.ArrayList;

public class CamionetaCarga extends Vehiculo {
    private final double capacidadToneladas;
    public CamionetaCarga(String placa, String marca, String modelo, double tarifaDiaria, double capacidadToneladas) {
        super(placa, marca, modelo, tarifaDiaria);
        this.capacidadToneladas = Validacion.positivo(capacidadToneladas, "Capacidad");
    }
    public double getCapacidadToneladas() { return capacidadToneladas; }
    @Override public double calcularSubtotal(int dias) {
        Validacion.positivo(dias, "Días");
        return Validacion.dinero((getTarifaDiaria() + 100 * capacidadToneladas) * dias);
    }
    @Override public boolean validarLicencia(ArrayList<TipoLicencia> licencias) { return licencias.contains(TipoLicencia.A) || licencias.contains(TipoLicencia.B); }
    @Override public int obtenerUmbralMantenimiento() { return 15; }
    @Override public String describirCaracteristicas() { return capacidadToneladas + " toneladas"; }
    @Override public String getCategoria() { return "CamionetaCarga"; }
}
