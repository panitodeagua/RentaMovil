import java.util.ArrayList;
import java.util.Locale;

public abstract class Vehiculo {
    private final String placa, marca, modelo;
    private final double tarifaDiaria;
    private EstadoVehiculo estado = EstadoVehiculo.DISPONIBLE;
    private int diasAcumulados;

    protected Vehiculo(String placa, String marca, String modelo, double tarifaDiaria) {
        this.placa = Validacion.texto(placa, "Placa").toUpperCase(Locale.ROOT);
        this.marca = Validacion.texto(marca, "Marca");
        this.modelo = Validacion.texto(modelo, "Modelo");
        this.tarifaDiaria = Validacion.positivo(tarifaDiaria, "Tarifa diaria");
    }
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public double getTarifaDiaria() { return tarifaDiaria; }
    public EstadoVehiculo getEstado() { return estado; }
    public int getDiasAcumulados() { return diasAcumulados; }
    public abstract double calcularSubtotal(int dias);
    public abstract boolean validarLicencia(ArrayList<TipoLicencia> licencias);
    public abstract int obtenerUmbralMantenimiento();
    public abstract String describirCaracteristicas();
    public abstract String getCategoria();
    public void marcarAlquilado() {
        if (estado != EstadoVehiculo.DISPONIBLE) throw new IllegalStateException("Vehículo no disponible.");
        estado = EstadoVehiculo.ALQUILADO;
    }
    public void registrarDevolucion(int dias) {
        Validacion.positivo(dias, "Días");
        if (estado != EstadoVehiculo.ALQUILADO) throw new IllegalStateException("El vehículo no está alquilado.");
        int acumulado = Math.addExact(diasAcumulados, dias);
        diasAcumulados = acumulado;
        estado = acumulado >= obtenerUmbralMantenimiento() ? EstadoVehiculo.MANTENIMIENTO : EstadoVehiculo.DISPONIBLE;
    }
    public void finalizarMantenimiento() {
        if (estado != EstadoVehiculo.MANTENIMIENTO) throw new IllegalStateException("El vehículo no está en mantenimiento.");
        diasAcumulados = 0;
        estado = EstadoVehiculo.DISPONIBLE;
    }
    @Override public String toString() {
        return String.format(Locale.US, "%s | %s | %s %s | Q%.2f/día | %s | %d días acumulados | %s",
            placa, getCategoria(), marca, modelo, tarifaDiaria, estado, diasAcumulados, describirCaracteristicas());
    }
}
