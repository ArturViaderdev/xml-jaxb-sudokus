/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxbsudokuartur;

import generated.Sudokus;
import generated.Sudokus.Sudoku;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Clase principal
 * @author alu2017363
 */
public class Jaxbsudokuartur {
    /**
     * Se ejecuta al inicio del programa. Método main.
     * @param args 
     */
    public static void main(String[] args) {
        //Se declara una variable de la clase aplicación.
        Aplicacion programa = new Aplicacion();
    }

    
}
