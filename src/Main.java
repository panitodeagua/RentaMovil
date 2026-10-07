import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.Scanner;

//LA VISTAAAAAAAAAAAA

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final RentaMovil rentaMovil = new RentaMovil();
    public static void main(String[] args) {
        System.out.println("RentaMovil | Datos de demostración cargados. Ingresos de esta ejecución: Q0.00");
        boolean continuar = true;
        while (continuar) {
            try {
                mostrarMenu();
                switch (entero("Opción: ")) {
                    case 1: registrarVehiculo(); break;
                    case 2: registrarCliente(); break;
                    case 3: consultarVehiculos(); break;
                    case 4: consultarClientes(); break;
                    case 5: cotizarAlquiler(); break;
                    case 6: confirmarAlquiler(); break;
                    case 7: registrarDevolucion(); break;
                    case 8: finalizarMantenimiento(); break;
                    case 9: mostrarReportes(); break;
                    case 0: continuar = false; break;
                    default: System.out.println("Opción inválida.");
                }
            } catch (NoSuchElementException e) {
                System.out.println("\nFin de entrada. Saliendo."); continuar = false;
            } catch (IllegalArgumentException | IllegalStateException | ArithmeticException e) {
                System.out.println("No se realizó la operación: " + e.getMessage());
            }
        }
    }
    private static void mostrarMenu() {
        System.out.println("\n1. Registrar vehículo\n2. Registrar cliente\n3. Consultar flota\n4. Consultar clientes\n5. Cotizar\n6. Confirmar o cancelar solicitud de alquiler\n7. Registrar devolución\n8. Finalizar mantenimiento\n9. Reportes\n0. Salir");
    }
    private static String texto(String mensaje) { System.out.print(mensaje); return scanner.nextLine().trim(); }
    private static int entero(String mensaje) {
        while (true) {
            try { return Integer.parseInt(texto(mensaje)); }
            catch (NumberFormatException e) { System.out.println("Ingrese un número entero válido."); }
        }
    }
    private static double decimal(String mensaje) {
        while (true) {
            try {
                double valor = Double.parseDouble(texto(mensaje));
                if (!Double.isFinite(valor)) throw new NumberFormatException();
                return valor;
            } catch (NumberFormatException e) { System.out.println("Ingrese un número válido; use punto para decimales."); }
        }
    }
    private static boolean siNo(String mensaje) {
        while (true) {
            String respuesta = texto(mensaje + " (s/n): ");
            if (respuesta.equalsIgnoreCase("s")) return true;
            if (respuesta.equalsIgnoreCase("n")) return false;
            System.out.println("Responda s o n.");
        }
    }
    private static ArrayList<TipoLicencia> leerLicencias() {
        ArrayList<TipoLicencia> resultado = new ArrayList<>();
        String entrada = texto("Licencias (A B C M, separadas por espacios o comas): ").toUpperCase(Locale.ROOT);
        for (String token : entrada.split("[,\\s]+")) {
            try {
                TipoLicencia licencia = TipoLicencia.valueOf(token);
                if (!resultado.contains(licencia)) resultado.add(licencia);
            } catch (IllegalArgumentException e) { throw new IllegalArgumentException("Licencia inválida: " + token); }
        }
        return resultado;
    }
    private static void registrarVehiculo() {
        int opcion = entero("1. Automóvil | 2. Motocicleta | 3. Camioneta de carga | 4. Microbús: ");
        if (opcion < 1 || opcion > 4) throw new IllegalArgumentException("Categoría inválida.");
        String placa = texto("Placa: "), marca = texto("Marca: "), modelo = texto("Modelo: ");
        double tarifa = decimal("Tarifa diaria Q: ");
        Vehiculo vehiculo;
        switch (opcion) {
            case 1: vehiculo = new Automovil(placa, marca, modelo, tarifa, entero("Pasajeros: "), siNo("¿Transmisión automática?")); break;
            case 2: vehiculo = new Motocicleta(placa, marca, modelo, tarifa, entero("Cilindraje: ")); break;
            case 3: vehiculo = new CamionetaCarga(placa, marca, modelo, tarifa, decimal("Capacidad en toneladas: ")); break;
            default: vehiculo = new Microbus(placa, marca, modelo, tarifa, entero("Pasajeros: "), siNo("¿Incluye piloto?"));
        }
        System.out.println(rentaMovil.registrarVehiculo(vehiculo) ? "Vehículo registrado." : "Placa ya registrada.");
    }
    private static void registrarCliente() {
        int opcion = entero("1. Individual | 2. Corporativo: ");
        if (opcion != 1 && opcion != 2) throw new IllegalArgumentException("Tipo de cliente inválido.");
        String id = texto("DPI o NIT: "), nombre = texto("Nombre: ");
        ArrayList<TipoLicencia> licencias = leerLicencias();
        Cliente cliente = opcion == 1 ? new ClienteIndividual(id, nombre, licencias)
            : new ClienteCorporativo(id, nombre, licencias, texto("Empresa: "), texto("Contacto: "));
        System.out.println(rentaMovil.registrarCliente(cliente) ? "Cliente registrado." : "Identificador ya registrado.");
    }
    private static void consultarVehiculos() { rentaMovil.obtenerVehiculos().forEach(System.out::println); }
    private static void consultarClientes() { rentaMovil.obtenerClientes().forEach(System.out::println); }
    private static Cliente pedirCliente() {
        Cliente cliente = rentaMovil.buscarCliente(texto("DPI o NIT del cliente: "));
        if (cliente == null) throw new IllegalArgumentException("Cliente inexistente.");
        return cliente;
    }
    private static Vehiculo pedirVehiculo() {
        Vehiculo vehiculo = rentaMovil.buscarVehiculo(texto("Placa: "));
        if (vehiculo == null) throw new IllegalArgumentException("Vehículo inexistente.");
        return vehiculo;
    }
    private static void cotizarAlquiler() {
        Cliente cliente = pedirCliente(); Vehiculo vehiculo = pedirVehiculo();
        System.out.println(rentaMovil.cotizar(cliente, vehiculo, entero("Días: ")));
    }
    private static void confirmarAlquiler() {
        Cliente cliente = pedirCliente(); Vehiculo vehiculo = pedirVehiculo(); int dias = entero("Días: ");
        System.out.println(rentaMovil.cotizar(cliente, vehiculo, dias));
        if (!siNo("¿Confirmar el alquiler con este cobro?")) {
            System.out.println("Solicitud cancelada. No se modificó el sistema."); return;
        }
        System.out.println("Alquiler confirmado: " + rentaMovil.confirmarAlquiler(cliente, vehiculo, dias));
    }
    private static void registrarDevolucion() {
        String placa = texto("Placa: ");
        if (!rentaMovil.registrarDevolucion(placa)) throw new IllegalStateException("El vehículo no está alquilado.");
        System.out.println("Devolución registrada. " + rentaMovil.buscarVehiculo(placa));
    }
    private static void finalizarMantenimiento() {
        if (!rentaMovil.finalizarMantenimiento(texto("Placa: "))) throw new IllegalStateException("El vehículo no está en mantenimiento.");
        System.out.println("Mantenimiento finalizado. Vehículo disponible y acumulado en cero.");
    }
    private static void mostrarReportes() {
        System.out.println(rentaMovil.generarReporteVehiculos());
        System.out.println(rentaMovil.generarIngresosPorCategoria());
        System.out.printf(Locale.US, "Ingresos de esta ejecución: Q%.2f%nDescuentos de esta ejecución: Q%.2f%n", rentaMovil.calcularIngresosTotales(), rentaMovil.calcularDescuentosTotales());
        System.out.println("Alquileres activos:");
        rentaMovil.obtenerAlquileresActivos().forEach(System.out::println);
        if (siNo("¿Consultar historial de un cliente?")) {
            Cliente cliente = pedirCliente();
            System.out.println("Historial completo (incluye datos anteriores al inicio):");
            rentaMovil.obtenerHistorialCliente(cliente).forEach(System.out::println);
            System.out.printf(Locale.US, "Total pagado histórico: Q%.2f%n", rentaMovil.calcularTotalPagadoCliente(cliente));
        }
    }
}
