package factory;

import dao.ClienteDaoImpl;
import dao.IDao;
import dao.VentaDaoImpl;
import dao.VideojuegoDaoImpl;
import model.Cliente;
import model.Venta;
import model.Videojuego;

/**
 * =====================================================================
 *  PATRÓN FACTORY (Factoría): DaoFactory
 * =====================================================================
 *
 * ¿QUÉ ES?
 *   Una "fábrica" de objetos. En vez de crear los DAO con "new" por todo el programa,
 *   se los pedimos a esta clase:
 *
 *       IDao<Videojuego, Integer> videojuegoDao = DaoFactory.getVideojuegoDao();
 *
 * ¿POR QUÉ NO HACER DIRECTAMENTE "new VideojuegoDaoImpl()"?
 *   1) El Main solo conoce la INTERFAZ (IDao), no la clase concreta (VideojuegoDaoImpl).
 *      Así el Main no depende de CÓMO se guardan los datos.
 *   2) Si mañana cambiamos de base de datos y creamos, por ejemplo, "VideojuegoDaoMongo",
 *      solo hay que cambiar UNA línea aquí, y el Main sigue funcionando sin tocarlo.
 *   3) La creación de objetos queda en un único sitio, fácil de encontrar y de cambiar.
 *
 * El enunciado lo exige: "Las clases de la aplicación no podrán crear directamente
 * objetos DaoImpl. Estos deberán obtenerse mediante la factoría."
 *
 * Los métodos son "static" para poder usarlos sin crear un objeto DaoFactory.
 */
public class DaoFactory {

    /** Constructor privado: esta clase solo tiene métodos static, no tiene sentido crear objetos de ella. */
    private DaoFactory() {
    }

    /**
     * Fíjate en el tipo que devuelve: IDao<Videojuego, Integer> (la interfaz),
     * aunque por dentro el objeto real sea un VideojuegoDaoImpl.
     */
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
