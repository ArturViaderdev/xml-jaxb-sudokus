/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxbsudokuartur;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase que contiene toda la interacción con el usuario y controla la
 * aplicación.
 *
 * @author alu2017363
 */
public class Aplicacion {

    //El manejador sirve para interactuar con el XML
    private static Manejador manejador;

    /**
     * Cuando se cierra la aplicación se guardan los datos en los XML. Si la
     * aplicación se cierra forzadamente no se guardarán los cambios. He optado
     * por esto ya que cada vez se graban todos los datos.
     */
    private void cerrar() {
        //Solo se guardan los cambios si ha habido cambios
        if (manejador.isCambiosusuarios()) {
            manejador.grabausuarios();
        } else {
            System.out.println("No se han grabado los usuarios porque no ha habido cambios.");
        }

        if (manejador.isCambioshistorial()) {
            manejador.grabahistorial();
        } else {
            System.out.println("No se ha grabado el historial porque no han habido cambios.");
        }
    }

    /**
     * Constructor. Se crea un manejador y se abre el menú principal
     */
    public Aplicacion() {
        //El manejador leerá todos los datos necesarios al ser creado.
        manejador = new Manejador();
        //Se abre el menú principal
        menuprincipal();
    }

    /**
     * Menú principal - Se muestra al usuario
     */
    private void menuprincipal() {
        Scanner entrada = new Scanner(System.in);
        int opcion = -1;

        do {
            System.out.println("1-Registrar nuevo usuario.");
            System.out.println("2-Login de usuario.");
            System.out.println("3-Obtener ranking de todos los usuarios.");
            System.out.println("0- Salir.");
            try {
                opcion = entrada.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println("Error de entrada.");
                entrada.next();
            }
            switch (opcion) {
                case 1:
                    registrausuario();
                    break;
                case 2:
                    loginusuario();
                    break;
                case 3:
                    obtenerranking();
                    break;
                case 0:
                    System.out.println("Adios.");
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0);
        cerrar();
    }

    /**
     * Se registra un usuario nuevo mediante interacción por teclado.
     */
    private void registrausuario() {
        try {
            BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
            String nombre, username, pass, passv;
            System.out.println("Introduce tu nombre completo.");
            nombre = lector.readLine();
            System.out.println("Introduce el nombre de usuario.");
            username = lector.readLine();
            System.out.println("Introduce el password.");
            pass = lector.readLine();
            System.out.println("Confirma el password");
            passv = lector.readLine();
            if (pass.equals(passv)) {
                if (manejador.anadeusuario(nombre, username, pass)) {
                    System.out.println("Usuario registrado. Se guardará en el xml al salir de la aplicación.");
                } else {
                    System.out.println("Error. El usuario ya existe.");
                }
            } else {
                System.out.println("Confirmación de password incorrecta.");
            }

        } catch (IOException ex) {
            Logger.getLogger(Metodos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * El usuario inicia sesión introduciendo su login y password.
     */
    private void loginusuario() {
        try {
            String username, pass;
            BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Introduce nombre de usuario");
            username = lector.readLine();
            System.out.println("Introduce el password");
            pass = lector.readLine();
            if (manejador.compruebalogin(username, pass)) {
                menudeusuario();
            } else {
                System.out.println("Login incorrecto.");
            }
        } catch (IOException ex) {
            Logger.getLogger(Jaxbsudokuartur.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * El usuario modifica su password actual.
     */
    private void modificarpassword() {
        try {
            String passactual, passnuevo, passnuevov;
            BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Introduce password actual.");
            passactual = lector.readLine();

            System.out.println("Introduce password nuevo.");
            passnuevo = lector.readLine();
            System.out.println("Confirma password nuevo.");
            passnuevov = lector.readLine();
            if (passnuevo.equals(passnuevov)) {
                if (manejador.modificapassword(passactual, passnuevo)) {
                    System.out.println("Password modificado.");
                } else {
                    System.out.println("Password actual incorrecto.");
                }
            } else {
                System.out.println("Confirmación de password incorrecta.");
            }

        } catch (IOException ex) {
            Logger.getLogger(Jaxbsudokuartur.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * El usuario introduce el tiempo que ha tardado en realizar el sudoku
     * aleatorio que se le ha dado anteriormente.
     */
    private void registrartiempo() {
        String sudoku;
        Scanner entrada = new Scanner(System.in);
        sudoku = manejador.muestraelsudoku();
        int tiempo;
        try {
            if (sudoku.equals("nosudokus")) {
                System.out.println("No hay sudoku seleccionado.");
            } else {
                System.out.println(sudoku);
                System.out.println("Introduce el tiempo");
                tiempo = entrada.nextInt();
                manejador.settiemposudoku(tiempo);
                System.out.println("Tiempo guardado.");
            }
        } catch (InputMismatchException ex) {
            System.out.println("Error de entrada.");
        }

    }

    /**
     * Se muestra su tiempo medio al usuario
     */
    private void vermitiempomedio() {
        double tiempo = manejador.gettiempomediousuario();
        if (tiempo == -1) {
            System.out.println("No hay datos en el historial de tu usuario.");
        } else {
            System.out.println("Tu tiempo medio es de: " + manejador.gettiempomediousuario());
        }
    }

    /**
     * Se muestra el ranking al usuario.
     */
    private void obtenerranking() {
        System.out.println(manejador.getrankingusuariosstring());
    }

    /**
     * Obtiene y muestra un sudoku aleatorio que no esté en el historial del
     * usuario que ha iniciado sesión
     */
    private void muestrasudokualeatorio() {
        String opcion, nivel;
        String sudoku = "";
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        boolean leido = false;
        try {
            System.out.println("¿Quieres especificar un nivel? s/n");
            opcion = lector.readLine();
            if (opcion.charAt(0) == 's' || opcion.charAt(0) == 'S') {
                System.out.println("Introduce el nivel: Easy, Medium, Hard");
                nivel = lector.readLine();
                sudoku = manejador.obtenersudokualeatorio(true, nivel);
                leido = true;
            } else if (opcion.charAt(0) == 'n' || opcion.charAt(0) == 'N') {
                sudoku = manejador.obtenersudokualeatorio(false,"");
                leido = true;
            } else {
                System.out.println("Opción incorrecta.");
            }
        } catch (IOException ex) {
            System.out.println("Error de entrada.");
        }
        if (leido) {
            if (sudoku.equals("nosudokus")) {
                System.out.println("No existen sudokus.");
            } else {
                System.out.println(sudoku);
            }
        }
    }
    
    /**
     * Interacción con el usuario para eliminar su usuario. 
     */
    private void eliminarmiusuario()
    {
        String opcion;
        BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
        try
        {
            System.out.println("¿Estás seguro de que quieres eliminar tu usuario? s/n");
         opcion = lector.readLine();
            if (opcion.charAt(0) == 's' || opcion.charAt(0) == 'S') {
                manejador.borramiusuario();
                System.out.println("Usuario eliminado.");
            } else if (opcion.charAt(0) == 'n' || opcion.charAt(0) == 'N') {
                System.out.println("Operación cancelada.");
            }
        }
        catch(IOException ex)
        {
            System.out.println("Error de entrada.");
        }
                
        
    }
    
    /**
     * Menú de usuario. Solo se mostrará cuando un usuario inicie sesión.
     */
    private void menudeusuario() {
        Scanner entrada = new Scanner(System.in);
        int opcion = -1;
        System.out.println("Menú de usuario.");
        do {
            System.out.println("1-Modificar password.");
            System.out.println("2-Obtener sudoku aleatorio.");
            System.out.println("3-Registrar tiempo del sudoku obtenido.");
            System.out.println("4-Ver mi tiempo medio");
            System.out.println("5-Eliminar mi usuario");
            System.out.println("0-Salir");
            try {

                opcion = entrada.nextInt();
            } catch (InputMismatchException ex) {
                System.out.println("Error de entrada. " + ex.getStackTrace());
            }
            switch (opcion) {
                case 1:
                    modificarpassword();
                    break;
                case 2:
                    muestrasudokualeatorio();
                    break;
                case 3:
                    registrartiempo();
                    break;
                case 4:
                    vermitiempomedio();
                    break;
                case 5:
                    eliminarmiusuario();
                    break;
                default:
                    System.out.println("Opción incorrecta.");
            }
        } while (opcion != 0 && opcion!=5);
        System.out.println("Adios.");
    }
}
