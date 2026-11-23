package model;

import java.time.LocalDate;

/**
 * MODELO: Venta
 *
 * Una venta une a un CLIENTE con un VIDEOJUEGO:
 * "el cliente X compró N unidades del videojuego Y en tal fecha".
 *
 * Por eso guarda los ids de ambos (idCliente e idVideojuego).
 * En la base de datos esos campos son CLAVES FORÁNEAS (FOREIGN KEY):
 * la base de datos no deja guardar una venta de un cliente o juego que no existe.
 */
public class Venta {

    // ---------- Atributos (= columnas de la tabla) ----------
    private int id;               // Clave primaria, autogenerada
    private int idCliente;        // Quién compra  -> apunta a cliente.id
    private int idVideojuego;     // Qué compra    -> apunta a videojuego.id
    private LocalDate fechaVenta; // Cuándo (LocalDate = solo fecha, sin hora)
    private int unidades;         // Cuántas copias
    private double importeTotal;  // precio del juego * unidades (lo calcula el DAO al registrar la venta)

    // ---------- Constructores ----------

    public Venta() {
    }

    /**
     * Para ventas nuevas. Fíjate que NO pedimos el importe:
     * lo calcula VentaDaoImpl a partir del precio real del videojuego,
     * así evitamos que alguien escriba un importe equivocado.
     */
    public Venta(int idCliente, int idVideojuego, LocalDate fechaVenta, int unidades) {
        this.idCliente = idCliente;
        this.idVideojuego = idVideojuego;
        this.fechaVenta = fechaVenta;
        this.unidades = unidades;
    }

    /** Para ventas leídas de la base de datos (con todos sus datos). */
    public Venta(int id, int idCliente, int idVideojuego, LocalDate fechaVenta, int unidades, double importeTotal) {
        this(idCliente, idVideojuego, fechaVenta, unidades);
        this.id = id;
        this.importeTotal = importeTotal;
    }

    // ---------- Getters y setters ----------

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCliente() { return idCliente; }
    public void setIdCliente(int idCliente) { this.idCliente = idCliente; }

    public int getIdVideojuego() { return idVideojuego; }
    public void setIdVideojuego(int idVideojuego) { this.idVideojuego = idVideojuego; }

    public LocalDate getFechaVenta() { return fechaVenta; }
    public void setFechaVenta(LocalDate fechaVenta) { this.fechaVenta = fechaVenta; }

    public int getUnidades() { return unidades; }
    public void setUnidades(int unidades) { this.unidades = unidades; }

    public double getImporteTotal() { return importeTotal; }
    public void setImporteTotal(double importeTotal) { this.importeTotal = importeTotal; }

    @Override
    public String toString() {
        return String.format("[%d] Fecha: %s - Cliente: %d - Videojuego: %d - Unidades: %d - Importe: %.2f €",
                id, fechaVenta, idCliente, idVideojuego, unidades, importeTotal);
    }
}
