package dao;

import model.Venta;
import model.VentaCliente;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface IVentaDao extends IDao<Venta, Integer> {
    boolean registrarVentaConStock(Venta venta) throws SQLException;
    List<VentaCliente> consultarVentasCliente(int idCliente) throws SQLException;
    BigDecimal consultarTotalGastadoCliente(int idCliente) throws SQLException;
}
