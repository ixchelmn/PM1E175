package com.example.pm1e175.transacciones;

import java.io.Serializable;

public class Contactos implements Serializable {

    private Integer id;
    private String nombre;
    private Integer numero;
    private String combopais;

    private String nota;

    private String imagen;

    public Contactos(Integer id, String nombre, String combopais, Integer numero,  String nota, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.combopais = combopais;
        this.numero = numero;
        this.nota = nota;
        this.imagen = imagen;

    }

    public Contactos(){}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getCombopais() {
        return combopais;
    }

    public void setCombopais(String combopais) {
        this.combopais = combopais;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
