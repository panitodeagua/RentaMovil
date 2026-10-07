import java.util.Locale;
public class Alquiler {
    private final int numero, dias;
    private final Cliente cliente;
    private final Vehiculo vehiculo;
    private final double subtotal, descuento, total;
    private boolean activo = true;
    public Alquiler(int numero, Cliente cliente, Vehiculo vehiculo, int dias, double subtotal, double descuento, double total) {
        this.numero = Validacion.positivo(numero, "Número");
        this.dias = Validacion.positivo(dias, "Días");
        if (cliente == null || vehiculo == null) throw new IllegalArgumentException("Cliente y vehículo obligatorios.");
        this.cliente = cliente; this.vehiculo = vehiculo;
        this.subtotal = Validacion.dinero(subtotal); this.descuento = Validacion.dinero(descuento); this.total = Validacion.dinero(total);
        if (this.descuento > this.subtotal || Math.abs(Validacion.dinero(this.subtotal - this.descuento) - this.total) > 0.001)
            throw new IllegalArgumentException("Montos inconsistentes.");
    }
    public int getNumero() { return numero; }
    public Cliente getCliente() { return cliente; }
    public Vehiculo getVehiculo() { return vehiculo; }
    public int getDias() { return dias; }
    public double getSubtotal() { return subtotal; }
    public double getDescuento() { return descuento; }
    public double getTotal() { return total; }
    public boolean isActivo() { return activo; }
    public void finalizar() {
        if (!activo) throw new IllegalStateException("Alquiler ya finalizado.");
        activo = false;
    }
    public boolean perteneceACliente(Cliente cliente) { return cliente != null && this.cliente.getIdentificador().equals(cliente.getIdentificador()); }
    @Override public String toString() {
        return String.format(Locale.US, "#%d | %s | %s | %d días | Subtotal Q%.2f | Descuento Q%.2f | Total Q%.2f | %s",
            numero, cliente.getNombre(), vehiculo.getPlaca(), dias, subtotal, descuento, total, activo ? "ACTIVO" : "FINALIZADO");
    }
}
