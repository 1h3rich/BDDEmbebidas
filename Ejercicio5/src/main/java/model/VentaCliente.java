package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class VentaCliente {
    private final int idVenta;
    private final String videojuego;
    private final LocalDate fecha;
    private final int unidades;
    private final BigDecimal importe;

    public VentaCliente(int idVenta, String videojuego, LocalDate fecha, int unidades, BigDecimal importe) {
        this.idVenta = idVenta;
        this.videojuego = videojuego;
        this.fecha = fecha;
        this.unidades = unidades;
        this.importe = importe;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public String getVideojuego() {
        return videojuego;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getUnidades() {
        return unidades;
    }

    public BigDecimal getImporte() {
        return importe;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s - %d unidades - %.2f €", idVenta, fecha, videojuego, unidades, importe);
    }
}
