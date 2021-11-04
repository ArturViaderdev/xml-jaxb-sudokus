/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jaxbsudokuartur;

import generated.Historiales;
import generated.Sudokus;
import generated.Usuarios;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * Clase que interactua con el XML y guarda los datos en memoria.
 *
 * @author alu2017363
 */
public class Manejador {

    //Contiene los sudokus que se leen del txt o el xml y se guardan en xml.
    private Sudokus sudokus;
    //Contiene los usuarios que pueden iniciar sesión. Se leen y graban en xml.
    private Usuarios usuarios;
    //Contiene los historiales de sudokus de los usuarios.
    private Historiales historial;
    //Guarda el usuario que ha iniciado sesión.
    private String login;
    //Posición en la lista del sudoku que se ha dado aleatoriamente al usuario.
    private int sudokualeatorio;
    //Indica si ha habido cambios en el historial y debe grabarse.
    private boolean cambioshistorial;
    //Indica si ha habido cambios en la lista de usuarios y debe grabarse.
    private boolean cambiosusuarios;

    /**
     * Get de la variable que indica si hay cambios en el historial y debe
     * grabarse.
     *
     * @return
     */
    public boolean isCambioshistorial() {
        return cambioshistorial;
    }

    /**
     * Get de la variable que indica si hay cambios en los usuarios y deben
     * grabarse.
     *
     * @return
     */
    public boolean isCambiosusuarios() {
        return cambiosusuarios;
    }

    /**
     * Devuelve el nombre de usuario del usuario que ha iniciado sesión.
     *
     * @return
     */
    public String getLogin() {
        return login;
    }
    JAXBContext contexto;

    /**
     * Variables estáticas que contienen los nombres de los archivos.
     */
    private static final String sudokusfile = "sudokus.xml";
    private static final String historialxml = "historial.xml";
    private static final String sudokustxt = "sudokus.txt";
    private static final String usuariosxml = "usuarios.xml";

    /**
     * Añade al historial del usuario el sudoku aleatorio con el tiempo que ha
     * tardado en resolverlo.
     *
     * @param tiempo Tiempo que ha tardado en resolverlo.
     */
    public void settiemposudoku(int tiempo) {
        //Se obtiene la posición en el historial de usuarios del usuario que ha iniciado sesión.
        int posusuariohistorial = dameposusuariohistorial(login);
        if (posusuariohistorial == -1) {
            //Si el usuario estaba en el historial se añade.
            posusuariohistorial = creausuariohistorial();
        }
        //Se añade el sudoku aleatorio con el tiempo que ha tardado.
        creasudokuhistorial(posusuariohistorial, tiempo);
        //Reinicio la variable del sudoku aleatorio porque ahora se debería obtener otro antes de poner un tiempo.
        sudokualeatorio = -1;
    }

    /**
     * Añade al historial del usuario el sudoku aleatorio y el tiempo que ha
     * tardado en resolverlo.
     *
     * @param posusuariohistorial Posición del usuario en el historial.
     * @param tiempo Tiempo que ha tardado en resolverlo.
     */
    private void creasudokuhistorial(int posusuariohistorial, int tiempo) {
        //Se crea un nuevo sudoku en el historial
        Historiales.Usuario.Sudoku sudokuhistorial = new Historiales.Usuario.Sudoku();
        //Se le copian los valores del sudoku aleatorio a este nuevo sudoku en el historial, que vienen a ser el mismo.
        sudokuhistorial.setProblem(sudokus.getSudoku().get(sudokualeatorio).getProblem());
        sudokuhistorial.setSolved(sudokus.getSudoku().get(sudokualeatorio).getSolved());
        sudokuhistorial.setLevel(sudokus.getSudoku().get(sudokualeatorio).getLevel());
        sudokuhistorial.setDescription(sudokus.getSudoku().get(sudokualeatorio).getDescription());
        //Se añade el tiempo que ha tardado en resolverlo.
        sudokuhistorial.setTime(BigInteger.valueOf(tiempo));
        //Se añade el sudoku al historial del usuario.
        historial.getUsuario().get(posusuariohistorial).getSudoku().add(sudokuhistorial);
        //Se recuerda que ha habido cambios para guardar en el xml al cerrar la aplicación.
        cambioshistorial = true;
    }

    /**
     * Se añade el usuario que ha iniciado sesión al historial para poder
     * guardar sudokus en el.
     *
     * @return Devuelve la posición del usuario en el historial. Siempre es la
     * última ya que se añade aquí.
     */
    private int creausuariohistorial() {
        //Se obtiene la posición del usuario que ha iniciado sesión en la lista de usuarios.
        int posusuario = dameposusuario(login);
        //Crea un nuevo usuario en el historial
        Historiales.Usuario usuario = new Historiales.Usuario();
        //Copia los datos del usuario que ha iniciado sesión en el nuevo usuario del historial. Que vienen a ser el mismo y el segundo no lleva password.
        usuario.setNombre(usuarios.getUsuario().get(posusuario).getNombre());
        usuario.setUsername(usuarios.getUsuario().get(posusuario).getUsername());
        //Añade el usuario al historial
        historial.getUsuario().add(usuario);
        //Se recuerda que han habido cambios en el historial para guardar en el xml al cerrar la aplicación.
        cambioshistorial = true;
        //Se devuelve la posición del nuevo usuario en el historial. Siempre es la última ya que se ha añadido ahora.
        return historial.getUsuario().size() - 1;

    }

    /**
     * Lee usuarios del XML
     */
    private void leeusuariosxml() {
        try {
            JAXBContext contexto = JAXBContext.newInstance(Usuarios.class);
            Unmarshaller u = contexto.createUnmarshaller();
            usuarios = (Usuarios) u.unmarshal(new File(usuariosxml));
            //Se indica cuantos usuarios se han leido.
            System.out.println("Leido XML " + usuarios.getUsuario().size() + " usuarios.");
        } catch (JAXBException ex) {
            Logger.getLogger(Manejador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lee sudokus del XML
     */
    private void leexml() {
        try {
            JAXBContext contexto = JAXBContext.newInstance(Sudokus.class);
            Unmarshaller u = contexto.createUnmarshaller();
            sudokus = (Sudokus) u.unmarshal(new File(sudokusfile));
            //Indica cuantos sudokus se han leido.
            System.out.println("Leido XML " + sudokus.getSudoku().size() + " sudokus.");
        } catch (JAXBException ex) {
            Logger.getLogger(Manejador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Lee historiales de usuarios con sudokus del XML.
     */
    private void leehistoriales() {
        try {
            JAXBContext contexto = JAXBContext.newInstance(Historiales.class);
            Unmarshaller u = contexto.createUnmarshaller();
            historial = (Historiales) u.unmarshal(new File(historialxml));
            //Indica cuantos historiales se han leido.
            System.out.println("Leido XML " + historial.getUsuario().size() + " historiales de usuarios.");
        } catch (JAXBException ex) {
            Logger.getLogger(Manejador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Constructor. Inicializa las variables y lee los datos de los archivos XML
     * o txt
     */
    public Manejador() {
        sudokualeatorio = -1;
        //Si existe el archivo XML de sudokus los leo de este.
        if (Metodos.existearchivo(sudokusfile)) {
            //leo el xml
            leexml();
        } else {
            //Si no existe el archivo xml de sudokus los leo del txt.
            if (Metodos.existearchivo(sudokustxt)) {
                leearchivo();
            } else {
                //Caso en que tampoco hay el txt, tenemos 0 sudokus.
                sudokus = new Sudokus();
            }
        }

        //Si existe el xml de usuarios se leen
        if (Metodos.existearchivo(usuariosxml)) {
            leeusuariosxml();
        } else {
            //Si el xml no existe se crea un usuarios vacio que podrá albergar nuevos usuarios.
            usuarios = new Usuarios();
        }

        //Se leen los historial de los usuarios del xml si el archivo existe.
        if (Metodos.existearchivo(historialxml)) {
            leehistoriales();
        } else {
            historial = new Historiales();
        }
        cambioshistorial = false;
        cambiosusuarios = false;
    }

    /**
     * Dice si un nombre de usuario con password son correctos y si lo son
     * guarda el nombre de usuario en esta clase porque este habrá iniciado
     * sesión.
     *
     * @param username Nombre de usuario
     * @param pass Password
     * @return Booleano que indica si el login es correcto o no.
     */
    public boolean compruebalogin(String username, String pass) {
        List<Usuarios.Usuario> listausuarios = usuarios.getUsuario();
        boolean ok = false;

        int pos;
        //Se obtiene la posición del usuario en la lista de usuarios.
        pos = dameposusuario(username);
        //Si devuelve -1 es que no se ha encontrado.
        if (pos != -1) {
            //Si se ha encontrado se comprueba si el password es correcto.
            if (listausuarios.get(pos).getPass().equals(pass)) {
                //Si el password es correcto se devuelve true.
                ok = true;
                //se guarda el nombre de usuario en esta clase porque es el que ha iniciado sesión
                login = username;
                //se inicializa la variable sudokualeatorio porque todavía no se le ha dado ninguno
                sudokualeatorio = -1;
            } else {
                //Si el password no es correcto se devuelve false
                ok = false;
            }
        } else {
            //Si el usuario no se encuentra se devuelve false
            ok = false;
        }
        return ok;
    }

    /**
     * Devuelve el ranking de usuarios.
     *
     * @return Ranking de usuarios como String
     */
    public String getrankingusuariosstring() {
        String resultado = "";
        //Se obtiene el ranking
        ArrayList<Tiempousuario> ranking = getrankingusuarios();
        //Se convierte el ranking a texto en un string
        //cada resultado en una linea.
        for (Tiempousuario rankeo : ranking) {
            resultado += rankeo.toString();
            resultado += System.lineSeparator();
        }
        return resultado;
    }

    /**
     * Obtiene el ranking de usuarios.
     *
     * @return Ranking con nombres de usuario y tiempos medios como lista de
     * elementos de la clase tiempousuario
     *
     */
    private ArrayList<Tiempousuario> getrankingusuarios() {
        Double tiempo;
        String nombre;
        //Se inicializa la lista del ranking
        ArrayList<Tiempousuario> ranking = new ArrayList<>();
        //Se obtiene la lista de usuarios que están en el historial
        List<Historiales.Usuario> usuarioshistorial;
        usuarioshistorial = historial.getUsuario();
        //Para cada usuario que está en el historial
        for (int cont = 0; cont < usuarioshistorial.size(); cont++) {
            //Se obtiene el tiempo medio que ha tardado en resolver sus sudokus
            tiempo = gettiempomedio(cont);
            //Se obtiene el nombre del usuario.
            nombre = usuarioshistorial.get(cont).getNombre();
            //Se añaden estos dos datos al ranking
            ranking.add(new Tiempousuario(nombre, tiempo));
        }
        //Se ordena el ranking
        Collections.sort(ranking);
        //Devuelve el ranking
        return ranking;
    }

    /**
     * Da el tiempo medio del usuario que ha iniciado sesión.
     *
     * @return Tiempo medio del usuario que ha iniciado sesión.
     */
    public double gettiempomediousuario() {
        //Se obtiene la posición del usuario en el historial.
        int poshistorialuser = dameposusuariohistorial(login);
        //Si el usuario no está en el historial se devuelve -1
        if (poshistorialuser == -1) {
            return -1;
        } else {
            //Si el usuario si está en el historial se obtiene el tiempo medio de este al resolver sus sudokus y se devuelve.
            return gettiempomedio(poshistorialuser);
        }
    }

    /**
     * Obtiene el tiempo medio de un usuario
     *
     * @param poshistorialuser Posición en el historial del usuario para el que
     * se quiere saber el tiempo medio.
     * @return Tiempo medio
     */
    public double gettiempomedio(int poshistorialuser) {

        double resultado;
        //Se obtiene la lista de sudokus que tiene en el historial el usuario
        List<Historiales.Usuario.Sudoku> sudokususer;
        sudokususer = historial.getUsuario().get(poshistorialuser).getSudoku();
        resultado = 0;
        //Se obtiene la suma total de tiempo de todos los sudokus recorriéndolos
        for (int cont = 0; cont < sudokususer.size(); cont++) {
            resultado += sudokususer.get(cont).getTime().doubleValue();
        }
        //Si no hay sudokus se devuelve -1
        if (sudokususer.size() == 0) {
            resultado = -1;
        } else {
            //Se devuelve la suma de los tiempos de los sudokus del usuario divida entre la cantidad de sudokus.
            //Esto es el tiempo medio.
            resultado /= sudokususer.size();
        }
        return resultado;
    }

    /**
     * Se devuelve el sudoku aleatorio que se le ha dado previamente al usuario
     * como string
     *
     * @return Sudoku aleatorio convertido a string
     */
    public String muestraelsudoku() {
        if (sudokualeatorio == -1) {
            //Caso en que no se había dado un sudoku aleatorio antes.
            return "nosudokus";
        } else {
            //Se devuelve el sudoku aleatorio convertido a string.
            return (sudokus.getSudoku().get(sudokualeatorio).toString());
        }
    }

    private ArrayList<Integer> obtenlistaposicionessudokussnivel(String nivel)
    {
        List<Sudokus.Sudoku> lista = sudokus.getSudoku();
        ArrayList<Integer> resultado = new ArrayList<Integer>();
        
        for(int cont = 0; cont<lista.size();cont++)
        {
            if(lista.get(cont).getDescription().toLowerCase().equals(nivel.toLowerCase()))
            {
                resultado.add(cont);
            }
        }
        return resultado;
    }
    
    /**
     * Se obtiene un sudoku aleatorio y se recuerda para luego poder registrar
     * el tiempo que se ha tardado en resolver. Solo se dan sudokus que no se
     * han dado antes
     *
     * @return Sudoku aleatorio convertido a string.
     */
    public String obtenersudokualeatorio(boolean buscanivel, String nivel) {
        Random rand = new Random();
        int posusuariohistorial;
        ArrayList<Integer> historialnoencontrados;
        ArrayList<Integer> sudokusvalidos;
        ArrayList<Integer> listaposiciones;
        int poshistorial;
        boolean sal;
        boolean encontrado;
        boolean busca;

        List<Sudokus.Sudoku> listageneral;
        //Si existen sudokus en memoria
        if (sudokus.getSudoku().size() > 0) {
            //Se obtiene la posición del usuario que ha iniciado sesión en el historial
            posusuariohistorial = dameposusuariohistorial(login);
            if (posusuariohistorial == -1) {
                //Si el usuario no tiene sudokus en el historial se le da uno al azar del total
                //Si se están buscando los sudokus de un nivel en concreto
                if (buscanivel) {
                    //Se obtienen la lista de posiciones de los sudokus de ese nivel
                    listaposiciones = obtenlistaposicionessudokussnivel(nivel);
                    //Se da un sudoku aleatorio de los de ese nivel
                    sudokualeatorio = listaposiciones.get(rand.nextInt(listaposiciones.size()));
                } else {
                    //Sino se da un sudoku aleatorio del total
                    sudokualeatorio = rand.nextInt(sudokus.getSudoku().size());
                }
            } else {
                //Si el usuario está en el historial
                if (historial.getUsuario().get(posusuariohistorial).getSudoku().size() == 0) {
                    //En el caso de no tener sudokus en el historial se le da también uno aleatorio del total.

                    if (buscanivel) {
                        listaposiciones = obtenlistaposicionessudokussnivel(nivel);
                        sudokualeatorio = listaposiciones.get(rand.nextInt(listaposiciones.size()));
                    } else {
                        sudokualeatorio = rand.nextInt(sudokus.getSudoku().size());
                    }
                } else {
                    //Si el usuario está en el historial y tiene sudokus resueltos.
                    //Se obtiene la lista de sudokus general. Todos los sudokus
                    listageneral = sudokus.getSudoku();
                    //He resuelto el problema de modo que se ahorre en proceso y no se recorra la lista varias veces.              
                    //Primero genero una lista con números ordenados empezando por el 0 con la misma longitud que la cantidad de sudokus en el historial del usuario.
                    //Esta lista contiene la posición en el historial del usuario de cada uno de los sudokus que tiene registrados.
                    historialnoencontrados = Metodos.crealista(historial.getUsuario().get(posusuariohistorial).getSudoku().size());
                    //Creo una nueva lista vacia para guardar los sudokus válidos para entregar al usuario. Los que no se le han dado.
                    sudokusvalidos = new ArrayList<Integer>();
                    //Recorro la lista de todos los sudokus. El total.
                    for (int cont = 0; cont < listageneral.size(); cont++) {
                        //Para cada sudoku voy a recorrer los de la lista del historial del usuario para encontrar si es el mismo.
                        poshistorial = 0;
                        sal = false;
                        encontrado = false;
                        //Recorro la lista de sudokus del historial del usuario
                        //Pero lo hago siguiendo una lista que contiene las posiciones de estos en el historial
                        //y voy a ir borrando de esta lista los encontrados para ahorrar proceso de CPU.
                        busca = false;
                        //Si solo se están buscando los sudokus de un nivel
                        if (buscanivel) {
                            //Si el nivel es el que se busca
                            if (listageneral.get(cont).getDescription().toLowerCase().equals(nivel.toLowerCase())) {
                                //Se procesa el elemento
                                busca = true;
                            } else {
                                //No se proces el elemento
                                busca = false;
                            }
                        } else {
                            //Si no se están buscando por nivel se procesa siempre
                            busca = true;
                        }
                        //Se procesa el elemento
                        if (busca) {
                            do {
                                if (poshistorial < historialnoencontrados.size()) {
                                    //Si el sudoku de la lista general que se está comprobando es el mismo que el del historial del usuario que se está comprobando
                                    if (Metodos.sudokusiguales(listageneral.get(cont), historial.getUsuario().get(posusuariohistorial).getSudoku().get(historialnoencontrados.get(poshistorial)))) {
                                        sal = true;
                                        //Elimino el sudoku de la lista de posiciones de historial de usuario. Ahorro tiempo de proceso.
                                        //Este sudoku ya se ha comprobado.
                                        //Salgo del bucle de historial y paso al siguiente sudoku de la lista general
                                        historialnoencontrados.remove(poshistorial);
                                        encontrado = true;
                                    } else {
                                        poshistorial++;
                                    }
                                } else {
                                    sal = true;
                                }
                            } while (!sal);

                            //En caso de no haber encontrado el sudoku de la lista general en el historial del usuario este se añade a la lista de sudokus válidos para darle
                            if (!encontrado) {
                                sudokusvalidos.add(cont);
                            }
                        }
                    }
                    //Al haber repasado todos los sudokus
                    //Si existen sudokus válidos, que no se le hayan dado al usuario
                    if (sudokusvalidos.size() > 0) {
                        //Se obtiene la posición de un sudoku que no se le ha dado al usuario antes aleatoriamente.
                        sudokualeatorio = sudokusvalidos.get(rand.nextInt(sudokusvalidos.size()));
                    } else {
                        return "nosudokus";
                    }
                }
            }
            //Devuelve el sudoku aleatorio.
            return (sudokus.getSudoku().get(sudokualeatorio).toString());
        } else {
            return "nosudokus";
        }
    }

    /**
     * Dice si un nombre de usuario existe
     *
     * @param usuario Nombre de usuario
     * @return Indica si existe o no
     */
    private boolean existeusuario(String usuario) {
        if (dameposusuario(usuario) != -1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Da la posición en la lista de usuarios del usuario con un username
     * concreto.
     *
     * @param usuario Nombre de usuario
     * @return Posición en la lista del usuario
     */
    private int dameposusuario(String usuario) {
        boolean encontrado = false;
        boolean sal = false;
        int cont = 0;
        //Se obtiene la lista de usuarios
        List<Usuarios.Usuario> listausuarios = usuarios.getUsuario();
        //Se recorre la lista de usuarios buscando el que tiene ese nombre de usuario
        do {
            if (cont < listausuarios.size()) {
                if (listausuarios.get(cont).getUsername().equals(usuario)) {
                    encontrado = true;
                    sal = true;
                } else {
                    cont++;
                }
            } else {
                sal = true;
            }
        } while (!sal);
        //si se ha encontrado el usuario se devuelve su posición
        if (encontrado) {
            return cont;
        } else {
            //Si no se ha encontrado se devuelve -1
            return -1;
        }
    }

    /**
     * Da la posición de un usuario con un username en el historial
     *
     * @param usuario Nombre de usuario
     * @return Posición del usuario en el historial
     */
    private int dameposusuariohistorial(String usuario) {
        boolean encontrado = false;
        boolean sal = false;
        int cont = 0;
        //Se obtiene la lista de usuarios del historial
        List<Historiales.Usuario> listausuarios = historial.getUsuario();
        //Se recorre la lista buscando ese username
        do {
            if (cont < listausuarios.size()) {
                if (listausuarios.get(cont).getUsername().equals(usuario)) {
                    encontrado = true;
                    sal = true;
                } else {
                    cont++;
                }
            } else {
                sal = true;
            }
        } while (!sal);
        //Si se encuentra el usuario se devuelve la posición
        if (encontrado) {
            return cont;
        } else {
            //Si no se encuentra el usuario se devuelve -1
            return -1;
        }
    }

    /**
     * Añade un usuario
     *
     * @param nombre Nombre personal
     * @param username Nombre de usuario
     * @param pass Password
     * @return Dice si se ha añadido o no porque ya existía
     */
    public boolean anadeusuario(String nombre, String username, String pass) {
        //Si existe el usuario
        if (existeusuario(username)) {
            //No se añade y se devuelve false
            return false;
        } else {
            //Si el usuario no existe se crea el nuevo usuario
            Usuarios.Usuario usuario = new Usuarios.Usuario();
            usuario.setNombre(nombre);
            usuario.setUsername(username);
            usuario.setPass(pass);
            //Se añade el nuevo usuario a la lista de usuarios.
            usuarios.getUsuario().add(usuario);
            //Se indica que ha habido cambios en los usuarios y se debe guardar el xml al cerrar la aplicación.
            cambiosusuarios = true;
            //Se devuelve true porque se ha creado existosamente el nuevo usuario.
            return true;
        }
    }

    /**
     * Guarda el historial de usuarios con sus sudokus resueltos en el xml de
     * historial.
     */
    public void grabahistorial() {
        try {
            JAXBContext contexto = JAXBContext.newInstance(Historiales.class);
            Marshaller jaxbMarshaller = contexto.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(historial, new File(historialxml));
            //Se muestran cuantos historiales de usuarios se han grabado en el xml.
            System.out.println(historial.getUsuario().size() + " historiales de usuarios grabados en xml.");
        } catch (JAXBException ex) {
            Logger.getLogger(Manejador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Guarda los usuarios en el XML de usuarios.
     */
    public void grabausuarios() {
        try {
            JAXBContext contexto = JAXBContext.newInstance(Usuarios.class);
            Marshaller jaxbMarshaller = contexto.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            jaxbMarshaller.marshal(usuarios, new File(usuariosxml));
            System.out.println(usuarios.getUsuario().size() + " usuarios grabados en xml.");
        } catch (JAXBException ex) {
            Logger.getLogger(Manejador.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Borra el usuario que ha iniciado sesión.
     */
    public void borramiusuario()
    {
        int pos;
        pos = dameposusuario(login);
        usuarios.getUsuario().remove(pos);
        
        pos = dameposusuariohistorial(login);
        historial.getUsuario().remove(pos);
        
        cambioshistorial = true;
        cambiosusuarios = true;
    }
    
    /**
     * Modifica el password de un usuario
     *
     * @param passactual Password actual
     * @param passnuevo Password nuevo
     * @return Indica si se ha cambiado el password o no porque el actual no era
     * correcto.
     */
    public boolean modificapassword(String passactual, String passnuevo) {
        //Se obtiene la lista de usuarios.
        List<Usuarios.Usuario> listausuarios = usuarios.getUsuario();
        boolean ok = false;
        int pos;
        //Se obtiene la posición del usuario que ha iniciado sesión en la lista de usuarios
        pos = dameposusuario(login);
        if (pos != -1) {
            //Si el password actual es el correcto
            if (listausuarios.get(pos).getPass().equals(passactual)) {
                //Se cambia el password por el nuevo
                listausuarios.get(pos).setPass(passnuevo);
                //Se recuerda que ha habido cambios en los usuarios y se debe grabar el XML al cerrar la aplicación.
                cambiosusuarios = true;
                ok = true;
            } else {
                ok = false;
            }
        } else {
            ok = false;
        }
        return ok;
    }

    /**
     * Lee sudokus del archivo de texto
     */
    private void leearchivo() {
        try {
            //Se crea la lista vacia de sudokus
            sudokus = new Sudokus();
            List<Sudokus.Sudoku> listasudokus = sudokus.getSudoku();
            BufferedReader lector;
            String linea;
            String leido[];
            int contlinea;

            Sudokus.Sudoku sudoku = null;
            lector = new BufferedReader(new FileReader(sudokustxt));
            //Se prepara todo para poder guardar el XML luego
            JAXBContext contexto = JAXBContext.newInstance(Sudokus.class);
            Marshaller jaxbMarshaller = contexto.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            contlinea = 0;
            //Las lineas del archivo están distribuidas en grupos de 3
            do {
                linea = lector.readLine();

                if (linea != null) {
                    switch (contlinea) {
                        case 0:
                            //En la primera linea de cada tres
                            //Creo un objeto Sudoku nuevo para cargarle los datos.
                            sudoku = new Sudokus.Sudoku();
                            //Separo el contenido de la linea por el caracter espacio.
                            leido = linea.split(" ");
                            //Depués del primer espacio encuentro el nivel
                            sudoku.setLevel(new BigInteger(leido[1]));
                            //Despues del segundo espacio encuentro la descripción
                            sudoku.setDescription(leido[2]);
                            break;
                        case 1:
                            //En la segunda linea de cada tres
                            //Obtengo el problema del sudoku
                            sudoku.setProblem(linea);
                            break;
                        case 2:
                            //En la tercera linea de cada tres
                            //Obtendo el resultado del sudoku
                            sudoku.setSolved(linea);
                            //Añado el nuevo sudoku a la lista de sudokus.
                            listasudokus.add(sudoku);
                            break;
                        default:
                    }
                    //Cada 3 lineas vuelvo el contador de lineas a 0
                    if (contlinea == 2) {
                        contlinea = 0;
                    } else {
                        contlinea++;
                    }
                }
            } while (linea != null);
            //Finalmente guardo los sudokus en el XML.
            jaxbMarshaller.marshal(sudokus, new File(sudokusfile));
            System.out.println("Leido txt " + sudokus.getSudoku().size() + " sudokus. Y grabado en XML.");
        } catch (JAXBException ex) {
            Logger.getLogger(Manejador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Manejador.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Manejador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
