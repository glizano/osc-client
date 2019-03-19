package com.example.osc_client;

public class Plato {

    private Long id;
    private String titulo;
    private String descripcion;

    public Plato(Long id, String titulo, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
    }

    public Plato(){

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
}
