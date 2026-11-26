package app;

import dao.IDao;
import database.ConexionBD;
import factory.DaoFactory;
import model.Cliente;
import model.Venta;
import model.Videojuego;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner teclado = new Scanner(System.in);

    private static final IDao<Videojuego, Integer> videojuegoDao = DaoFactory.getVideojuegoDao();
    private static final IDao<Cliente, Integer> clienteDao = DaoFactory.getClienteDao();
    private static final IDao<Venta, Integer> ventaDao = DaoFactory.getVentaDao();

    public static void main(String[] args) {
        try {
            prepararBaseDeDatos();
            menuPrincipal();
        } catch (SQLException error) {
            System.out.println("No se ha podido conectar con la base de datos: " + error.getMessage());
        } finally {

            ConexionBD.cerrarConexion();
        }
    }

    private static void prepararBaseDeDatos() throws SQLException {
        videojuegoDao.crearTablaSiNoExiste();
        clienteDao.crearTablaSiNoExiste();
        ventaDao.crearTablaSiNoExiste();
    }



    private static void menuPrincipal() {
        int opcion;
        do {
            System.out.println();
            System.out.println("========= TIENDA DE VIDEOJUEGOS =========");
            System.out.println("1. Gestión de videojuegos");
            System.out.println("2. Gestión de clientes");
            System.out.println("3. Gestión de ventas");
            System.out.println("0. Salir");
            opcion = leerEntero("Elige una opción: ");

            switch (opcion) {
                case 1 -> menuVideojuegos();
                case 2 -> menuClientes();
                case 3 -> menuVentas();
                case 0 -> System.out.println("¡Hasta pronto!");
                default -> System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
    }



    private static void menuVideojuegos() {
        int opcion;
        do {
            System.out.println();
            System.out.println("------ GESTIÓN DE VIDEOJUEGOS ------");
            System.out.println("1. Listar videojuegos");
            System.out.println("2. Buscar videojuego por ID");
            System.out.println("3. Añadir videojuego");
            System.out.println("4. Modificar videojuego");
            System.out.println("5. Eliminar videojuego");
            System.out.println("0. Volver");
            opcion = leerEntero("Elige una opción: ");


            try {
                switch (opcion) {
                    case 1 -> listarVideojuegos();
                    case 2 -> buscarVideojuego();
                    case 3 -> anadirVideojuego();
                    case 4 -> modificarVideojuego();
                    case 5 -> eliminarVideojuego();
                    case 0 -> { }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException error) {
                mostrarErrorBaseDeDatos(error);
            }
        } while (opcion != 0);
    }

    private static void listarVideojuegos() throws SQLException {
        List<Videojuego> videojuegos = videojuegoDao.listarTodos();
        if (videojuegos.isEmpty()) {
            System.out.println("No hay videojuegos registrados.");
            return;
        }

        for (Videojuego videojuego : videojuegos) {
            System.out.println(videojuego);
        }
    }

    private static void buscarVideojuego() throws SQLException {
        int id = leerEntero("ID del videojuego: ");
        Videojuego videojuego = videojuegoDao.buscarPorId(id);
        System.out.println(videojuego != null ? videojuego : "No existe ningún videojuego con ese ID.");
    }

    private static void anadirVideojuego() throws SQLException {
        String titulo = leerTexto("Título: ");
        String plataforma = leerTexto("Plataforma: ");
        String genero = leerTexto("Género: ");
        double precio = leerDecimalNoNegativo("Precio: ");
        int stock = leerEnteroNoNegativo("Stock: ");

        Videojuego nuevoVideojuego = new Videojuego(titulo, plataforma, genero, precio, stock);
        if (videojuegoDao.insertar(nuevoVideojuego)) {

            System.out.println("Videojuego añadido con ID " + nuevoVideojuego.getId() + ".");
        }
    }

    private static void modificarVideojuego() throws SQLException {
        int id = leerEntero("ID del videojuego a modificar: ");
        Videojuego videojuego = videojuegoDao.buscarPorId(id);
        if (videojuego == null) {
            System.out.println("No existe ningún videojuego con ese ID.");
            return;
        }

        System.out.println("Datos actuales: " + videojuego);
        System.out.println("(Pulsa Intro sin escribir nada para dejar un campo como está)");

        videojuego.setTitulo(leerTextoOpcional("Título", videojuego.getTitulo()));
        videojuego.setPlataforma(leerTextoOpcional("Plataforma", videojuego.getPlataforma()));
        videojuego.setGenero(leerTextoOpcional("Género", videojuego.getGenero()));
        videojuego.setPrecio(leerDecimalOpcional("Precio", videojuego.getPrecio()));
        videojuego.setStock(leerEnteroOpcional("Stock", videojuego.getStock()));

        if (videojuegoDao.modificar(videojuego)) {
            System.out.println("Videojuego modificado.");
        }
    }

    private static void eliminarVideojuego() throws SQLException {
        int id = leerEntero("ID del videojuego a eliminar: ");
        if (!confirmar("¿Seguro que quieres eliminarlo? (s/n): ")) {
            System.out.println("Operación cancelada.");
            return;
        }
        boolean eliminado = videojuegoDao.eliminar(id);
        System.out.println(eliminado ? "Videojuego eliminado." : "No existe ningún videojuego con ese ID.");
    }



    private static void menuClientes() {
        int opcion;
        do {
            System.out.println();
            System.out.println("------ GESTIÓN DE CLIENTES ------");
            System.out.println("1. Listar clientes");
            System.out.println("2. Buscar cliente por ID");
            System.out.println("3. Añadir cliente");
            System.out.println("4. Modificar cliente");
            System.out.println("5. Eliminar cliente");
            System.out.println("0. Volver");
            opcion = leerEntero("Elige una opción: ");

            try {
                switch (opcion) {
                    case 1 -> listarClientes();
                    case 2 -> buscarCliente();
                    case 3 -> anadirCliente();
                    case 4 -> modificarCliente();
                    case 5 -> eliminarCliente();
                    case 0 -> { }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException error) {
                mostrarErrorBaseDeDatos(error);
            }
        } while (opcion != 0);
    }

    private static void listarClientes() throws SQLException {
        List<Cliente> clientes = clienteDao.listarTodos();
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes registrados.");
            return;
        }
        for (Cliente cliente : clientes) {
            System.out.println(cliente);
        }
    }

    private static void buscarCliente() throws SQLException {
        int id = leerEntero("ID del cliente: ");
        Cliente cliente = clienteDao.buscarPorId(id);
        System.out.println(cliente != null ? cliente : "No existe ningún cliente con ese ID.");
    }

    private static void anadirCliente() throws SQLException {
        String nombre = leerTexto("Nombre: ");
        String apellidos = leerTexto("Apellidos: ");
        String email = leerTexto("Email: ");
        String telefono = leerTexto("Teléfono: ");

        Cliente nuevoCliente = new Cliente(nombre, apellidos, email, telefono);
        if (clienteDao.insertar(nuevoCliente)) {
            System.out.println("Cliente añadido con ID " + nuevoCliente.getId() + ".");
        }
    }

    private static void modificarCliente() throws SQLException {
        int id = leerEntero("ID del cliente a modificar: ");
        Cliente cliente = clienteDao.buscarPorId(id);
        if (cliente == null) {
            System.out.println("No existe ningún cliente con ese ID.");
            return;
        }

        System.out.println("Datos actuales: " + cliente);
        System.out.println("(Pulsa Intro sin escribir nada para dejar un campo como está)");

        cliente.setNombre(leerTextoOpcional("Nombre", cliente.getNombre()));
        cliente.setApellidos(leerTextoOpcional("Apellidos", cliente.getApellidos()));
        cliente.setEmail(leerTextoOpcional("Email", cliente.getEmail()));
        cliente.setTelefono(leerTextoOpcional("Teléfono", cliente.getTelefono()));

        if (clienteDao.modificar(cliente)) {
            System.out.println("Cliente modificado.");
        }
    }

    private static void eliminarCliente() throws SQLException {
        int id = leerEntero("ID del cliente a eliminar: ");
        if (!confirmar("¿Seguro que quieres eliminarlo? (s/n): ")) {
            System.out.println("Operación cancelada.");
            return;
        }
        boolean eliminado = clienteDao.eliminar(id);
        System.out.println(eliminado ? "Cliente eliminado." : "No existe ningún cliente con ese ID.");
    }



    private static void menuVentas() {
        int opcion;
        do {
            System.out.println();
            System.out.println("------ GESTIÓN DE VENTAS ------");
            System.out.println("1. Listar ventas");
            System.out.println("2. Buscar venta por ID");
            System.out.println("3. Registrar venta");
            System.out.println("4. Modificar venta");
            System.out.println("5. Eliminar venta");
            System.out.println("0. Volver");
            opcion = leerEntero("Elige una opción: ");

            try {
                switch (opcion) {
                    case 1 -> listarVentas();
                    case 2 -> buscarVenta();
                    case 3 -> registrarVenta();
                    case 4 -> modificarVenta();
                    case 5 -> eliminarVenta();
                    case 0 -> { }
                    default -> System.out.println("Opción no válida.");
                }
            } catch (SQLException error) {
                mostrarErrorBaseDeDatos(error);
            }
        } while (opcion != 0);
    }

    private static void listarVentas() throws SQLException {
        List<Venta> ventas = ventaDao.listarTodos();
        if (ventas.isEmpty()) {
            System.out.println("No hay ventas registradas.");
            return;
        }
        for (Venta venta : ventas) {
            mostrarVentaConNombres(venta);
        }
    }

    private static void buscarVenta() throws SQLException {
        int id = leerEntero("ID de la venta: ");
        Venta venta = ventaDao.buscarPorId(id);
        if (venta == null) {
            System.out.println("No existe ninguna venta con ese ID.");
        } else {
            mostrarVentaConNombres(venta);
        }
    }

    private static void registrarVenta() throws SQLException {
        int idCliente = leerEntero("ID del cliente: ");
        if (clienteDao.buscarPorId(idCliente) == null) {
            System.out.println("No existe ningún cliente con ese ID.");
            return;
        }
        int idVideojuego = leerEntero("ID del videojuego: ");
        int unidades = leerEnteroPositivo("Unidades: ");
        LocalDate fecha = leerFecha("Fecha (AAAA-MM-DD, Intro = hoy): ", LocalDate.now());

        Venta nuevaVenta = new Venta(idCliente, idVideojuego, fecha, unidades);
        if (ventaDao.insertar(nuevaVenta)) {
            System.out.printf("Venta registrada con ID %d. Importe total: %.2f €%n",
                    nuevaVenta.getId(), nuevaVenta.getImporteTotal());
        }
    }

    private static void modificarVenta() throws SQLException {
        int id = leerEntero("ID de la venta a modificar: ");
        Venta venta = ventaDao.buscarPorId(id);
        if (venta == null) {
            System.out.println("No existe ninguna venta con ese ID.");
            return;
        }

        System.out.print("Datos actuales: ");
        mostrarVentaConNombres(venta);
        System.out.println("(Pulsa Intro sin escribir nada para dejar un campo como está)");

        venta.setIdCliente(leerEnteroOpcional("ID del cliente", venta.getIdCliente()));
        venta.setIdVideojuego(leerEnteroOpcional("ID del videojuego", venta.getIdVideojuego()));
        venta.setUnidades(leerEnteroOpcional("Unidades", venta.getUnidades()));
        venta.setFechaVenta(leerFecha("Fecha [" + venta.getFechaVenta() + "]: ", venta.getFechaVenta()));

        if (venta.getUnidades() <= 0) {
            System.out.println("Las unidades deben ser mayores que 0. No se ha modificado nada.");
            return;
        }

        if (ventaDao.modificar(venta)) {
            System.out.printf("Venta modificada. Nuevo importe: %.2f €%n", venta.getImporteTotal());
        }
    }

    private static void eliminarVenta() throws SQLException {
        int id = leerEntero("ID de la venta a eliminar: ");
        if (!confirmar("¿Seguro que quieres eliminarla? Las unidades volverán al stock (s/n): ")) {
            System.out.println("Operación cancelada.");
            return;
        }
        boolean eliminada = ventaDao.eliminar(id);
        System.out.println(eliminada ? "Venta eliminada y stock devuelto." : "No existe ninguna venta con ese ID.");
    }

    private static void mostrarVentaConNombres(Venta venta) throws SQLException {
        Cliente cliente = clienteDao.buscarPorId(venta.getIdCliente());
        Videojuego videojuego = videojuegoDao.buscarPorId(venta.getIdVideojuego());

        String nombreCliente = (cliente != null) ? cliente.getNombre() + " " + cliente.getApellidos() : "¿?";
        String tituloVideojuego = (videojuego != null) ? videojuego.getTitulo() : "¿?";

        System.out.printf("[%d] %s - %s compró %d x \"%s\" - Importe: %.2f €%n",
                venta.getId(), venta.getFechaVenta(), nombreCliente,
                venta.getUnidades(), tituloVideojuego, venta.getImporteTotal());
    }




    private static void mostrarErrorBaseDeDatos(SQLException error) {
        String codigoError = error.getSQLState();

        if ("23503".equals(codigoError)) {

            System.out.println("Error: hay datos relacionados. Por ejemplo, no se puede borrar un cliente o "
                    + "videojuego que tiene ventas, ni registrar una venta de un cliente que no existe.");
        } else if ("23505".equals(codigoError)) {

            System.out.println("Error: ya existe un registro con ese valor (por ejemplo, ese email ya está registrado).");
        } else {

            System.out.println("Error: " + error.getMessage());
        }
    }




    private static String leerTexto(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine().trim();
            if (!texto.isEmpty()) {
                return texto;
            }
            System.out.println("Este campo no puede estar vacío.");
        }
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {

                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número entero.");
            }
        }
    }

    private static int leerEnteroNoNegativo(String mensaje) {
        while (true) {
            int numero = leerEntero(mensaje);
            if (numero >= 0) {
                return numero;
            }
            System.out.println("No puede ser negativo.");
        }
    }

    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            int numero = leerEntero(mensaje);
            if (numero > 0) {
                return numero;
            }
            System.out.println("Tiene que ser mayor que 0.");
        }
    }

    private static double leerDecimalNoNegativo(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double numero = Double.parseDouble(teclado.nextLine().trim().replace(',', '.'));
                if (numero >= 0) {
                    return numero;
                }
                System.out.println("No puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número (por ejemplo 19.99).");
            }
        }
    }

    private static String leerTextoOpcional(String campo, String valorActual) {
        System.out.print(campo + " [" + valorActual + "]: ");
        String texto = teclado.nextLine().trim();
        return texto.isEmpty() ? valorActual : texto;
    }

    private static int leerEnteroOpcional(String campo, int valorActual) {
        while (true) {
            System.out.print(campo + " [" + valorActual + "]: ");
            String texto = teclado.nextLine().trim();
            if (texto.isEmpty()) {
                return valorActual;
            }
            try {
                int numero = Integer.parseInt(texto);
                if (numero >= 0) {
                    return numero;
                }
                System.out.println("No puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número entero.");
            }
        }
    }

    private static double leerDecimalOpcional(String campo, double valorActual) {
        while (true) {
            System.out.print(campo + " [" + valorActual + "]: ");
            String texto = teclado.nextLine().trim();
            if (texto.isEmpty()) {
                return valorActual;
            }
            try {
                double numero = Double.parseDouble(texto.replace(',', '.'));
                if (numero >= 0) {
                    return numero;
                }
                System.out.println("No puede ser negativo.");
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número (por ejemplo 19.99).");
            }
        }
    }

    private static LocalDate leerFecha(String mensaje, LocalDate fechaPorDefecto) {
        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine().trim();
            if (texto.isEmpty()) {
                return fechaPorDefecto;
            }
            try {
                return LocalDate.parse(texto);
            } catch (DateTimeParseException e) {
                System.out.println("Formato incorrecto. Ejemplo: 2026-11-21");
            }
        }
    }

    private static boolean confirmar(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim().equalsIgnoreCase("s");
    }
}
