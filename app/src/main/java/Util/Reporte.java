package Util;

/**
 * Created by Angel Aparicio on 24/09/2017.
 */

public class Reporte {
    private int id;
    private String fecha;
    private String dispositivo;
    private String docente;
    private String perisferico;
    private String cantidad;
    private String autor;
    private String referencia;

    @Override
    public String toString() {
        return "Reporte{" +
                "id=" + id +
                ", fecha='" + fecha + '\'' +
                ", dispositivo='" + dispositivo + '\'' +
                ", docente='" + docente + '\'' +
                ", perisferico='" + perisferico + '\'' +
                ", cantidad='" + cantidad + '\'' +
                ", autor='" + autor + '\'' +
                ", referencia='" + referencia + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getDispositivo() {
        return dispositivo;
    }

    public void setDispositivo(String dispositivo) {
        this.dispositivo = dispositivo;
    }

    public String getDocente() {
        return docente;
    }

    public void setDocente(String docente) {
        this.docente = docente;
    }

    public String getPerisferico() {
        return perisferico;
    }

    public void setPerisferico(String perisferico) {
        this.perisferico = perisferico;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
}
