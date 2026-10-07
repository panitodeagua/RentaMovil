
final class Validacion {
    private Validacion() { }
    static String texto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) throw new IllegalArgumentException(campo + " no puede estar vacío.");
        return valor.trim();
    }
    static int positivo(int valor, String campo) {
        if (valor <= 0) throw new IllegalArgumentException(campo + " debe ser un entero positivo.");
        return valor;
    }
    static double positivo(double valor, String campo) {
        if (!Double.isFinite(valor) || valor <= 0) throw new IllegalArgumentException(campo + " debe ser positivo y finito.");
        return valor;
    }
    static double dinero(double valor) {
        if (!Double.isFinite(valor) || valor < 0) throw new IllegalArgumentException("Monto fuera de rango.");
        return java.math.BigDecimal.valueOf(valor).setScale(2, java.math.RoundingMode.HALF_UP).doubleValue();
    }
}
