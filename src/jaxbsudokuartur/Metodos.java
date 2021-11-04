/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxbsudokuartur;

import generated.Historiales;
import generated.Sudokus;
import generated.Usuarios.Usuario;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alu2017363
 */
public class Metodos {
    /**
     * Dice si un sudoku de la lista general y otro sudoku del historial de un usuario son el mismo
     * @param sudokugeneral Sudoku de la lista general
     * @param sudokuhistorial Sudoku del historial del usuario
     * @return Indica si los sudokus son iguales o no
     */
    public static boolean sudokusiguales(Sudokus.Sudoku sudokugeneral, Historiales.Usuario.Sudoku sudokuhistorial) {
        boolean resultado;
        //Se comparan los valores
        if (sudokugeneral.getLevel().equals(sudokuhistorial.getLevel()) && sudokugeneral.getDescription().equals(sudokuhistorial.getDescription()) && sudokugeneral.getProblem().equals(sudokuhistorial.getProblem()) && sudokugeneral.getSolved().equals(sudokuhistorial.getSolved())) {
            resultado = true;
        } else {
            resultado = false;
        }
        return resultado;
    }
    
      /**
     * Crea una lista de números ordenados empezando con el 0 hasta llegar a un máximo
     * @param maximo Valor máximo
     * @return Lista de números
     */
    public static ArrayList<Integer> crealista(int maximo) {
        ArrayList<Integer> resultado;
        resultado = new ArrayList<Integer>();
        for (int cont = 0; cont < maximo; cont++) {
            resultado.add(cont);
        }
        return resultado;
    }

    
    /**
     * Indica si un archivo existe
     * @param nombre Nombre del archivo
     * @return True si existe False si no existe.
     */
    public static boolean existearchivo(String nombre) {
        File archivo = new File(nombre);
        if (archivo.exists() && archivo.isFile()) {
            return true;
        } else {
            return false;
        }
    }

    
    
   
}
