/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxbsudokuartur;

/**
 * Clase que sirve para guardar un nombre de usuario con su tiempo medio.
 * @author arturv
 */
public class Tiempousuario implements Comparable {

    /**
     * Convierte la clase a string
     * @return Clase convertida a string.
     */
    @Override
    public String toString() {
        return "nombre=" + nombre + ", tiempo=" + tiempo;
    }
    //Nombre del usuario
    private String nombre;
    //Tiempo medio del usuario
    private Double tiempo;

    /**
     * Contructor
     * @param nombre Nombre de usuario
     * @param tiempo Tiempo medio.
     */
    public Tiempousuario(String nombre, double tiempo) {
        this.nombre = nombre;
        this.tiempo = tiempo;
    }
    
    /**
     * Devuelve el nombre del usuario.
     * @return Nombre del usuario.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve el tiempo medio del usuario
     * @return Tiempo medio del usuario.
     */
    public double getTiempo() {
        return tiempo;
    }

    /**
     * Sirve para ordenar el ranking por tiempo medio.
     * @param o
     * @return 
     */
    @Override
    public int compareTo(Object o) {
        return this.tiempo.compareTo(((Tiempousuario)o).tiempo);
    }
    
}
