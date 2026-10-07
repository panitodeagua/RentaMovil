# RentaMovil
## Compilar y ejecutar

Abra una terminal dentro de la carpeta `RentaMovil`.

```sh
javac -encoding UTF-8 -d out src/*.java
java -cp out Main
```

## Datos iniciales (generados con ayuda de IA)

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
- Validacion: clase auxiliar añadida para centralizar validaciones sencillas y redondeo monetario.




