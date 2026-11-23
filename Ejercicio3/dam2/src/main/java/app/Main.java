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

/**
 * =====================================================================
 *  MAIN: la aplicación de consola (menús)
 * =====================================================================
 *
 * Esta clase solo se encarga de HABLAR CON EL USUARIO: mostrar menús,
 * pedir datos por teclado y enseñar resultados.
 *
 * Fíjate en lo que NO hace:
 *   - No tiene ni una línea de SQL (todo el SQL está en los DAO).
 *   - No hace "new VideojuegoDaoImpl()" (los DAO se piden a DaoFactory).
 *   - No abre ni gestiona la conexión (eso es cosa del Singleton ConexionBD).
 *
 * Así cada clase tiene UNA responsabilidad, y si cambia la base de datos,
 * esta clase no hay que tocarla.
 *
 * Esquema de cómo se relaciona todo:
 *
 *    Main  --pide DAO-->  DaoFactory  --crea-->  VideojuegoDaoImpl / ClienteDaoImpl / VentaDaoImpl
 *     |                                                   |
 *     +----- usa los métodos de IDao ------------------->+---pide la conexión---> ConexionBD (Singleton)
 *                                                                                      |
 *                                                                                  Base de datos
 */
public class Main {

    // Un único Scanner para leer del teclado en todo el programa
    private static final Scanner teclado = new Scanner(System.in);

    // Los DAO se piden a la FACTORÍA. Fíjate en el tipo: la INTERFAZ IDao, no la clase Impl.
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
            // Se ejecuta SIEMPRE al terminar: cerramos la única conexión del Singleton
            ConexionBD.cerrarConexion();
        }
    }

    /**
     * Crea las tablas si no existen.
     * El orden importa: "venta" tiene claves foráneas hacia "cliente" y "videojuego",
     * así que esas dos tienen que existir antes.
     */
    private static void prepararBaseDeDatos() throws SQLException {
        videojuegoDao.crearTablaSiNoExiste();
        clienteDao.crearTablaSiNoExiste();
        ventaDao.crearTablaSiNoExiste();
    }

    // =====================================================================
    //  MENÚ PRINCIPAL
    // =====================================================================

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

    // =====================================================================
    //  SUBMENÚ: VIDEOJUEGOS
    // =====================================================================

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

            // Todas las opciones pueden fallar con la base de datos, así que las
            // envolvemos en un único try/catch en vez de repetirlo en cada método.
            try {
                switch (opcion) {
                    case 1 -> listarVideojuegos();
                    case 2 -> buscarVideojuego();
                    case 3 -> anadirVideojuego();
                    case 4 -> modificarVideojuego();
                    case 5 -> eliminarVideojuego();
                    case 0 -> { } // no hacemos nada: el bucle termina y volvemos al menú principal
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
        // Recorremos la lista e imprimimos cada uno (usa el toString() de Videojuego)
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

        // Creamos el objeto (sin id) y se lo damos al DAO para que lo guarde
        Videojuego nuevoVideojuego = new Videojuego(titulo, plataforma, genero, precio, stock);
        if (videojuegoDao.insertar(nuevoVideojuego)) {
            // Después de insertar, el DAO le ha puesto el id generado
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

        // Cambiamos el objeto en memoria...
        videojuego.setTitulo(leerTextoOpcional("Título", videojuego.getTitulo()));
        videojuego.setPlataforma(leerTextoOpcional("Plataforma", videojuego.getPlataforma()));
        videojuego.setGenero(leerTextoOpcional("Género", videojuego.getGenero()));
        videojuego.setPrecio(leerDecimalOpcional("Precio", videojuego.getPrecio()));
        videojuego.setStock(leerEnteroOpcional("Stock", videojuego.getStock()));

        // ...y le pedimos al DAO que guarde esos cambios en la base de datos
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

    // =====================================================================
    //  SUBMENÚ: CLIENTES
    // =====================================================================

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

    // =====================================================================
    //  SUBMENÚ: VENTAS
    // =====================================================================

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

    /**
     * Registrar venta: aquí solo pedimos los datos.
     * La TRANSACCIÓN (insertar venta + descontar stock, con commit/rollback)
     * la hace VentaDaoImpl.insertar(). El Main no necesita saber cómo.
     */
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

        // El DAO recalcula el importe y ajusta el stock dentro de una transacción
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

    /**
     * Muestra una venta con el nombre del cliente y el título del juego,
     * que es más fácil de leer que solo los números de id.
     */
    private static void mostrarVentaConNombres(Venta venta) throws SQLException {
        Cliente cliente = clienteDao.buscarPorId(venta.getIdCliente());
        Videojuego videojuego = videojuegoDao.buscarPorId(venta.getIdVideojuego());

        String nombreCliente = (cliente != null) ? cliente.getNombre() + " " + cliente.getApellidos() : "¿?";
        String tituloVideojuego = (videojuego != null) ? videojuego.getTitulo() : "¿?";

        System.out.printf("[%d] %s - %s compró %d x \"%s\" - Importe: %.2f €%n",
                venta.getId(), venta.getFechaVenta(), nombreCliente,
                venta.getUnidades(), tituloVideojuego, venta.getImporteTotal());
    }

    // =====================================================================
    //  ERRORES
    // =====================================================================

    /**
     * Traduce los errores más típicos de la base de datos a un mensaje entendible.
     * getSQLState() devuelve un código estándar de 5 caracteres que indica el tipo de error.
     */
    private static void mostrarErrorBaseDeDatos(SQLException error) {
        String codigoError = error.getSQLState();

        if ("23503".equals(codigoError)) {
            // 23503 = violación de clave foránea
            System.out.println("Error: hay datos relacionados. Por ejemplo, no se puede borrar un cliente o "
                    + "videojuego que tiene ventas, ni registrar una venta de un cliente que no existe.");
        } else if ("23505".equals(codigoError)) {
            // 23505 = valor duplicado en un campo UNIQUE
            System.out.println("Error: ya existe un registro con ese valor (por ejemplo, ese email ya está registrado).");
        } else {
            // Cualquier otro error, incluidos los nuestros ("Stock insuficiente...")
            System.out.println("Error: " + error.getMessage());
        }
    }

    // =====================================================================
    //  MÉTODOS PARA LEER DEL TECLADO
    //  (repiten la pregunta hasta que el usuario escribe algo válido)
    // =====================================================================

    /** Lee un texto que no puede estar vacío. */
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

    /** Lee un número entero cualquiera. */
    private static int leerEntero(String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                // Leemos la línea entera y la convertimos. Así evitamos los problemas
                // típicos de mezclar nextInt() con nextLine().
                return Integer.parseInt(teclado.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Escribe un número entero.");
            }
        }
    }

    /** Lee un entero mayor o igual que 0 (por ejemplo, el stock). */
    private static int leerEnteroNoNegativo(String mensaje) {
        while (true) {
            int numero = leerEntero(mensaje);
            if (numero >= 0) {
                return numero;
            }
            System.out.println("No puede ser negativo.");
        }
    }

    /** Lee un entero mayor que 0 (por ejemplo, las unidades de una venta). */
    private static int leerEnteroPositivo(String mensaje) {
        while (true) {
            int numero = leerEntero(mensaje);
            if (numero > 0) {
                return numero;
            }
            System.out.println("Tiene que ser mayor que 0.");
        }
    }

    /** Lee un número decimal mayor o igual que 0. Acepta coma o punto (19,99 o 19.99). */
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

    /**
     * Para MODIFICAR: muestra el valor actual entre corchetes.
     * Si el usuario pulsa Intro sin escribir, se queda el valor actual.
     */
    private static String leerTextoOpcional(String campo, String valorActual) {
        System.out.print(campo + " [" + valorActual + "]: ");
        String texto = teclado.nextLine().trim();
        return texto.isEmpty() ? valorActual : texto;
    }

    /** Igual que leerTextoOpcional, pero para números enteros. */
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

    /** Igual que leerTextoOpcional, pero para números decimales. */
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

    /** Lee una fecha con formato AAAA-MM-DD. Si se deja vacío, devuelve "fechaPorDefecto". */
    private static LocalDate leerFecha(String mensaje, LocalDate fechaPorDefecto) {
        while (true) {
            System.out.print(mensaje);
            String texto = teclado.nextLine().trim();
            if (texto.isEmpty()) {
                return fechaPorDefecto;
            }
            try {
                return LocalDate.parse(texto); // LocalDate entiende directamente el formato AAAA-MM-DD
            } catch (DateTimeParseException e) {
                System.out.println("Formato incorrecto. Ejemplo: 2026-11-21");
            }
        }
    }

    /** Pregunta sí/no. Devuelve true solo si el usuario responde "s". */
    private static boolean confirmar(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim().equalsIgnoreCase("s");
    }
}
