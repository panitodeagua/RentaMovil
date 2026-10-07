import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class RentaMovil {
    private final ArrayList<Vehiculo> vehiculos = new ArrayList<>();
    private final ArrayList<Cliente> clientes = new ArrayList<>();
    private final ArrayList<Alquiler> alquileres = new ArrayList<>();
    private int siguienteNumeroAlquiler = 1;
    private int primerNumeroAlquilerEjecucion = 1;

    public RentaMovil() { this(true); }

    public RentaMovil(boolean cargarDatos) { if (cargarDatos) cargarDatosIniciales(); }
    public boolean registrarVehiculo(Vehiculo vehiculo) {
        if (vehiculo == null) throw new IllegalArgumentException("Vehículo obligatorio.");
        if (buscarVehiculo(vehiculo.getPlaca()) != null) return false;
        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) throw new IllegalArgumentException("El vehículo debe iniciar disponible.");
        vehiculos.add(vehiculo); return true;
    }
    public boolean registrarCliente(Cliente cliente) {
        if (cliente == null) throw new IllegalArgumentException("Cliente obligatorio.");
        if (buscarCliente(cliente.getIdentificador()) != null) return false;
        clientes.add(cliente); return true;
    }
    public Vehiculo buscarVehiculo(String placa) {
        String clave = Validacion.texto(placa, "Placa");
        for (Vehiculo v : vehiculos) if (v.getPlaca().equalsIgnoreCase(clave)) return v;
        return null;
    }
    public Cliente buscarCliente(String identificador) {
        String clave = Validacion.texto(identificador, "Identificador");
        for (Cliente c : clientes) if (c.getIdentificador().equalsIgnoreCase(clave)) return c;
        return null;
    }
    public int contarAlquileresActivos(Cliente cliente) {
        int cantidad = 0;
        for (Alquiler a : alquileres) if (a.perteneceACliente(cliente) && a.isActivo()) cantidad++;
        return cantidad;
    }
    public int contarAlquileresConfirmados(Cliente cliente) { return obtenerHistorialCliente(cliente).size(); }
    private void validarDatos(Cliente cliente, Vehiculo vehiculo, int dias) {
        Validacion.positivo(dias, "Días");
        if (cliente == null || !clientes.contains(cliente)) throw new IllegalArgumentException("Cliente inexistente en el sistema.");
        if (vehiculo == null || !vehiculos.contains(vehiculo)) throw new IllegalArgumentException("Vehículo inexistente en el sistema.");
        if ((long) vehiculo.getDiasAcumulados() + dias > Integer.MAX_VALUE)
            throw new IllegalArgumentException("Los días exceden el rango permitido.");
    }
    private ArrayList<String> razones(Cliente cliente, Vehiculo vehiculo) {
        ArrayList<String> razones = new ArrayList<>();
        if (vehiculo.getEstado() != EstadoVehiculo.DISPONIBLE) razones.add("Vehículo no disponible.");
        if (!vehiculo.validarLicencia(cliente.getLicencias())) razones.add("Licencia inadecuada.");
        if (contarAlquileresActivos(cliente) >= cliente.obtenerLimiteAlquileresActivos()) razones.add("Límite de alquileres activos alcanzado.");
        return razones;
    }
    public String cotizar(Cliente cliente, Vehiculo vehiculo, int dias) {
        validarDatos(cliente, vehiculo, dias);
        double subtotal = vehiculo.calcularSubtotal(dias);
        double descuento = cliente.calcularDescuento(subtotal, contarAlquileresConfirmados(cliente));
        double total = Validacion.dinero(subtotal - descuento);
        ArrayList<String> impedimentos = razones(cliente, vehiculo);
        return String.format(Locale.US, "%s%nCliente: %s | %d días%nSubtotal: Q%.2f%nDescuento: Q%.2f%nTotal: Q%.2f%n%s",
            vehiculo, cliente.getNombre(), dias, subtotal, descuento, total,
            impedimentos.isEmpty() ? "Puede alquilar: sí." : "Puede alquilar: no. " + String.join(" ", impedimentos));
    }
    public Alquiler confirmarAlquiler(Cliente cliente, Vehiculo vehiculo, int dias) {
        validarDatos(cliente, vehiculo, dias);
        ArrayList<String> impedimentos = razones(cliente, vehiculo);
        if (!impedimentos.isEmpty()) throw new IllegalStateException(String.join(" ", impedimentos));
        double subtotal = vehiculo.calcularSubtotal(dias);
        double descuento = cliente.calcularDescuento(subtotal, contarAlquileresConfirmados(cliente));
        Alquiler alquiler = new Alquiler(siguienteNumeroAlquiler, cliente, vehiculo, dias,
            subtotal, descuento, Validacion.dinero(subtotal - descuento));
      
        int siguiente = Math.addExact(siguienteNumeroAlquiler, 1);
        vehiculo.marcarAlquilado();
        alquileres.add(alquiler);
        siguienteNumeroAlquiler = siguiente;
        return alquiler;
    }
    public boolean registrarDevolucion(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (vehiculo == null) throw new IllegalArgumentException("Vehículo inexistente.");
        if (vehiculo.getEstado() != EstadoVehiculo.ALQUILADO) return false;
        for (Alquiler a : alquileres) if (a.isActivo() && a.getVehiculo() == vehiculo) {
            vehiculo.registrarDevolucion(a.getDias()); a.finalizar(); return true;
        }
        throw new IllegalStateException("No se encontró un alquiler activo para ese vehículo.");
    }
    public boolean finalizarMantenimiento(String placa) {
        Vehiculo vehiculo = buscarVehiculo(placa);
        if (vehiculo == null) throw new IllegalArgumentException("Vehículo inexistente.");
        if (vehiculo.getEstado() != EstadoVehiculo.MANTENIMIENTO) return false;
        vehiculo.finalizarMantenimiento(); return true;
    }
    public ArrayList<Vehiculo> obtenerVehiculos() { return new ArrayList<>(vehiculos); }
    public ArrayList<Cliente> obtenerClientes() { return new ArrayList<>(clientes); }
    public String generarReporteVehiculos() {
        Map<String, Integer> categorias = new LinkedHashMap<>();
        Map<EstadoVehiculo, Integer> estados = new LinkedHashMap<>();
        for (EstadoVehiculo e : EstadoVehiculo.values()) estados.put(e, 0);
        for (Vehiculo v : vehiculos) {
            categorias.merge(v.getCategoria(), 1, Integer::sum);
            estados.merge(v.getEstado(), 1, Integer::sum);
        }
        return "Por categoría: " + categorias + "\nPor estado: " + estados;
    }
    public String generarIngresosPorCategoria() {
        Map<String, Double> ingresos = new LinkedHashMap<>();
        for (Vehiculo v : vehiculos) ingresos.putIfAbsent(v.getCategoria(), 0.0);
        for (Alquiler a : alquileres) if (a.getNumero() >= primerNumeroAlquilerEjecucion)
            ingresos.merge(a.getVehiculo().getCategoria(), a.getTotal(), Double::sum);
        StringBuilder reporte = new StringBuilder("Ingresos de esta ejecución por categoría:\n");
        ingresos.forEach((categoria, total) -> reporte.append(String.format(Locale.US, "%s: Q%.2f%n", categoria, total)));
        return reporte.toString();
    }
    public double calcularIngresosTotales() {
        double total = 0;
        for (Alquiler a : alquileres) if (a.getNumero() >= primerNumeroAlquilerEjecucion) total += a.getTotal();
        return Validacion.dinero(total);
    }
    public double calcularDescuentosTotales() {
        double total = 0;
        for (Alquiler a : alquileres) if (a.getNumero() >= primerNumeroAlquilerEjecucion) total += a.getDescuento();
        return Validacion.dinero(total);
    }
    public ArrayList<Alquiler> obtenerAlquileresActivos() {
        ArrayList<Alquiler> resultado = new ArrayList<>();
        for (Alquiler a : alquileres) if (a.isActivo()) resultado.add(a);
        return resultado;
    }
    public ArrayList<Alquiler> obtenerHistorialCliente(Cliente cliente) {
        ArrayList<Alquiler> resultado = new ArrayList<>();
        for (Alquiler a : alquileres) if (a.perteneceACliente(cliente)) resultado.add(a);
        return resultado;
    }
    /** Incluye el historial inicial, a diferencia del ingreso de esta ejecución. */
    public double calcularTotalPagadoCliente(Cliente cliente) {
        double total = 0;
        for (Alquiler a : obtenerHistorialCliente(cliente)) total += a.getTotal();
        return Validacion.dinero(total);
    }

    //Los datos iniciales nos los hizo ChatGPT, era un monton, nos ahorramos tiempo aquí
    private void cargarDatosIniciales() {
        registrarVehiculo(new Automovil("P001", "Toyota", "Corolla", 200, 5, false));
        registrarVehiculo(new Automovil("P002", "Honda", "Civic", 200, 5, true));
        registrarVehiculo(new Motocicleta("M001", "Honda", "CB250", 100, 250));
        registrarVehiculo(new Motocicleta("M002", "Yamaha", "MT03", 100, 321));
        registrarVehiculo(new CamionetaCarga("C001", "Toyota", "Hilux", 200, 1.5));
        registrarVehiculo(new CamionetaCarga("C002", "Isuzu", "NPR", 300, 3));
        registrarVehiculo(new Microbus("B001", "Toyota", "Hiace", 450, 15, true));
        registrarVehiculo(new Microbus("B002", "Hyundai", "H1", 350, 12, false));
        Cliente frecuente = new ClienteIndividual("1234567890123", "Ana López", new ArrayList<>(Arrays.asList(TipoLicencia.C)));
        registrarCliente(frecuente);
        registrarCliente(new ClienteIndividual("9876543210123", "Luis Pérez", new ArrayList<>(Arrays.asList(TipoLicencia.M))));
        registrarCliente(new ClienteCorporativo("1234567-8", "Transportes del Valle", new ArrayList<>(Arrays.asList(TipoLicencia.A, TipoLicencia.M)), "Transportes del Valle", "María Ruiz"));
        registrarCliente(new ClienteCorporativo("7654321-0", "Servicios Centro", new ArrayList<>(Arrays.asList(TipoLicencia.B)), "Servicios Centro", "Carlos Díaz"));
        for (int dias : new int[] {10, 10, 9}) {
            confirmarAlquiler(frecuente, buscarVehiculo("P001"), dias);
            registrarDevolucion("P001");
        }
        primerNumeroAlquilerEjecucion = siguienteNumeroAlquiler;
    }
}
