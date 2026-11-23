package model;

/**
 * MODELO: Cliente
 *
 * Igual que Videojuego: una clase que solo guarda datos.
 * Cada objeto Cliente = una fila de la tabla "cliente".
 */
public class Cliente {

    // ---------- Atributos (= columnas de la tabla) ----------
    private int id;           // Clave primaria, autogenerada por la base de datos
    private String nombre;
    private String apellidos;
    private String email;
    private String telefono;  // Lo guardamos como texto: puede llevar "+34", espacios, etc.

    // ---------- Constructores ----------

    public Cliente() {
    }

    /** Para clientes nuevos (todavía sin id). */
    public Cliente(String nombre, String apellidos, String email, String telefono) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.telefono = telefono;
    }

    /** Para clientes leídos de la base de datos (ya tienen id). */
    public Cliente(int id, String nombre, String apellidos, String email, String telefono) {
        this(nombre, apellidos, email, telefono);
        this.id = id;
    }

    // ---------- Getters y setters ----------

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s - %s - Tel: %s", id, nombre, apellidos, email, telefono);
    }
}
