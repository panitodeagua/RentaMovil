# RentaMovil

Aplicación de consola en Java basada en el análisis y UML proporcionados para los ejercicios 4 y 5 de Herencia y Polimorfismo.

## Compilar y ejecutar

Requisito: JDK 17 o posterior. Abra una terminal dentro de la carpeta `RentaMovil`.

```sh
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

En IntelliJ IDEA o VS Code puede abrir la carpeta y ejecutar `src/Main.java` con un JDK configurado. No requiere Maven, base de datos ni librerías externas. Los datos se conservan únicamente mientras el programa está abierto.

## Pruebas

```sh
javac -encoding UTF-8 -d out src/*.java pruebas/PruebasRentaMovil.java
java -cp out PruebasRentaMovil
```

`pruebas/resultados.md` contiene la tabla obtenida al ejecutar 104 comprobaciones. `pruebas/consola.md` contiene comprobaciones adicionales del menú real. Las pruebas lanzan AssertionError y terminan con código distinto de cero si un resultado no coincide.

## Datos iniciales

| Placa | Vehículo | Tarifa diaria | Características |
|---|---|---:|---|
| P001 | Automóvil | Q200.00 | Manual, 5 pasajeros, 29 días acumulados |
| P002 | Automóvil | Q200.00 | Automático, 5 pasajeros |
| M001 | Motocicleta | Q100.00 | 250 cc |
| M002 | Motocicleta | Q100.00 | 321 cc |
| C001 | Camioneta de carga | Q200.00 | 1.5 toneladas |
| C002 | Camioneta de carga | Q300.00 | 3 toneladas |
| B001 | Microbús | Q450.00 | 15 pasajeros, con piloto |
| B002 | Microbús | Q350.00 | 12 pasajeros, sin piloto |

| Identificador | Cliente | Licencias |
|---|---|---|
| 1234567890123 | Ana López, individual | C |
| 9876543210123 | Luis Pérez, individual | M |
| 1234567-8 | Transportes del Valle, corporativo | A, M |
| 7654321-0 | Servicios Centro, corporativo | B |

Todos los vehículos comienzan disponibles. Ana tiene tres alquileres históricos finalizados, de 10, 10 y 9 días, sobre P001. Su siguiente alquiler tiene descuento del 5 %. Si alquila P001 por un día, paga Q190.00 y la devolución lleva el automóvil a mantenimiento.

Se aplica la separación definida en el análisis: `primerNumeroAlquilerEjecucion` excluye el historial inicial de los ingresos y descuentos de la ejecución. Estos reportes inician en cero. El historial del cliente y su total pagado sí incluyen los alquileres históricos. El primer correlativo nuevo es 4.

## Organización y diseño

- Modelo: Vehiculo, sus cuatro subclases, Cliente, sus dos subclases, Alquiler y las enumeraciones.
- Controlador: RentaMovil administra las colecciones y coordina operaciones.
- Vista: Main recibe entradas, solicita aceptación y muestra resultados.
- Validacion: clase auxiliar añadida al UML para centralizar validaciones sencillas y redondeo monetario.

Se mantienen las firmas principales del UML. Se agregan métodos privados auxiliares, `toString()` para presentar objetos polimórficamente y el constructor `RentaMovil(boolean)` para facilitar pruebas sin datos iniciales. No hay decisiones por categoría o tipo fuera de las jerarquías, excepto para construir objetos durante el registro. Los reportes agrupan por categoría sin elegir reglas de negocio.

Las colecciones se devuelven mediante copias; los atributos de identificación y los importes confirmados son finales. Las transiciones públicas del UML comprueban el estado anterior; el menú realiza las operaciones a través del controlador.

Los importes usan `double`, como establece el UML, con redondeo HALF_UP a dos decimales del subtotal, descuento y total. No se aplican reglas de tránsito externas: las licencias siguen exclusivamente el enunciado.

## Cancelar una solicitud

En la opción 6, revise el cobro y responda `n`. No se crea un alquiler ni se consume un correlativo. No se cancela un alquiler ya confirmado, pues el caso no contempla reembolsos. La opción 5 permite cotizar incluso vehículos alquilados o en mantenimiento y presenta todas las razones de rechazo aplicables.

## Entrega

El código está listo para copiarse a un repositorio. Cree el repositorio público en su cuenta y suba `src`, `pruebas` y este README. No suba `out`. Este paquete no publica un repositorio en GitHub ni modifica el PDF de análisis. Para la entrega académica, incorpore las evidencias al PDF y actualice el UML con los auxiliares añadidos si se exige coincidencia completa.
