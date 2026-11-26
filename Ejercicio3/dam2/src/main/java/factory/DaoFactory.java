package factory;

import dao.ClienteDaoImpl;
import dao.IDao;
import dao.VentaDaoImpl;
import dao.VideojuegoDaoImpl;
import model.Cliente;
import model.Venta;
import model.Videojuego;

public class DaoFactory {

    private DaoFactory() {
    }

    public static IDao<Videojuego, Integer> getVideojuegoDao() {
        return new VideojuegoDaoImpl();
    }

    public static IDao<Cliente, Integer> getClienteDao() {
        return new ClienteDaoImpl();
    }

    public static IDao<Venta, Integer> getVentaDao() {
        return new VentaDaoImpl();
    }
}
