package co.edu.uis.conectacampo;

/**
 * Created by jufea on 11/23/2017.
 */

public class Producto {

    private String nombre;
    private String precio;
    private String imagen;
    private String user;

    public Producto(String nombre, String precio, String imagen, String user) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.user = user;
    }

    public Producto() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
