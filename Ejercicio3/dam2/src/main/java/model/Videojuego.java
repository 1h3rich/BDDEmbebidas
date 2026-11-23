package model;

/**
 * MODELO: Videojuego
 *
 * Un "modelo" (también llamado entidad, POJO o DTO) es una clase muy sencilla
 * que solo sirve para GUARDAR DATOS. No sabe nada de bases de datos ni de menús.
 *
 * Cada objeto Videojuego representa UNA FILA de la tabla "videojuego",
 * y cada atributo representa UNA COLUMNA de esa tabla.
 *
 *   Tabla videojuego:
 *   | id | titulo        | plataforma | genero   | precio | stock |
 *   |----|---------------|------------|----------|--------|-------|
 *   | 1  | Zelda TOTK    | Switch     | Aventura | 59.99  | 10    |   <- esto es un objeto Videojuego
 */
public class Videojuego {

    // ---------- Atributos (= columnas de la tabla) ----------
    private int id;            // Clave primaria. La genera la base de datos automáticamente.
    private String titulo;     // Nombre del juego
    private String plataforma; // PS5, Xbox, Switch, PC...
    private String genero;     // Acción, Aventura, Deportes...
    private double precio;     // Precio de una unidad
    private int stock;         // Unidades disponibles en la tienda

    // ---------- Constructores ----------

    /** Constructor vacío: útil cuando vamos a rellenar el objeto poco a poco con los setters. */
    public Videojuego() {
    }

    /**
     * Constructor SIN id: se usa cuando vamos a crear un videojuego nuevo.
     * Todavía no tiene id porque se lo pondrá la base de datos al insertarlo.
     */
    public Videojuego(String titulo, String plataforma, String genero, double precio, int stock) {
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.precio = precio;
        this.stock = stock;
    }

    /** Constructor COMPLETO: se usa cuando leemos un videojuego que ya existe en la base de datos. */
    public Videojuego(int id, String titulo, String plataforma, String genero, double precio, int stock) {
        this(titulo, plataforma, genero, precio, stock); // reutiliza el constructor de arriba
        this.id = id;
    }

    // ---------- Getters y setters (para leer y cambiar los atributos privados) ----------

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getPlataforma() { return plataforma; }
    public void setPlataforma(String plataforma) { this.plataforma = plataforma; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    /** toString: define cómo se ve el objeto cuando lo imprimimos con System.out.println(). */
    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - %s - %.2f € - Stock: %d",
                id, titulo, plataforma, genero, precio, stock);
    }
}
