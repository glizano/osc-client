package com.example.osc_client;

public class Plato {

    private Long id;
    private String titulo;
    private String descripcion;
    private String rutaFoto;
    private Double precio;

    public Plato(Long id, String titulo, String descripcion, String rutaFoto, Double precio) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.rutaFoto = rutaFoto;
        this.precio = precio;
    }

    public Plato() {

    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaFoto() {
        return rutaFoto;
    }

    public void setRutaFoto(String rutaFoto) {
        this.rutaFoto = rutaFoto;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }
}
