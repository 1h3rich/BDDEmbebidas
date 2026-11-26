package model;

import java.time.LocalDate;

public class Venta {

    private int id;
    private int idCliente;
    private int idVideojuego;
    private LocalDate fechaVenta;
    private int unidades;
    private double importeTotal;

    public Venta() {
    }

    public Venta(int idCliente, int idVideojuego, LocalDate fechaVenta, int unidades) {
        this.idCliente = idCliente;
        this.idVideojuego = idVideojuego;
        this.fechaVenta = fechaVenta;
        this.unidades = unidades;
    }

    public Venta(int id, int idCliente, int idVideojuego, LocalDate fechaVenta, int unidades, double importeTotal) {
        this(idCliente, idVideojuego, fechaVenta, unidades);
        this.id = id;
        this.importeTotal = importeTotal;
    }

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
