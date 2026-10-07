import java.util.ArrayList;

public class Motocicleta extends Vehiculo {
    private final int cilindraje;
    public Motocicleta(String placa, String marca, String modelo, double tarifaDiaria, int cilindraje) {
        super(placa, marca, modelo, tarifaDiaria);
        this.cilindraje = Validacion.positivo(cilindraje, "Cilindraje");
    }
    public int getCilindraje() { return cilindraje; }
    @Override public double calcularSubtotal(int dias) {
        Validacion.positivo(dias, "Días");
        return Validacion.dinero(getTarifaDiaria() * dias + (cilindraje > 250 ? 75 : 0));
    }
    @Override public boolean validarLicencia(ArrayList<TipoLicencia> licencias) { return licencias.contains(TipoLicencia.M); }
    @Override public int obtenerUmbralMantenimiento() { return 20; }
    @Override public String describirCaracteristicas() { return cilindraje + " cc"; }
    @Override public String getCategoria() { return "Motocicleta"; }
}
