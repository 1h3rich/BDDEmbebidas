package model;

public class Videojuego {

    private int id;
    private String titulo;
    private String plataforma;
    private String genero;
    private double precio;
    private int stock;


    public Videojuego() {
    }

    public Videojuego(String titulo, String plataforma, String genero, double precio, int stock) {
        this.titulo = titulo;
        this.plataforma = plataforma;
        this.genero = genero;
        this.precio = precio;
        this.stock = stock;
    }

    public Videojuego(int id, String titulo, String plataforma, String genero, double precio, int stock) {
        this(titulo, plataforma, genero, precio, stock);
        this.id = id;
    }

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

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) - %s - %.2f € - Stock: %d",
                id, titulo, plataforma, genero, precio, stock);
    }
}
