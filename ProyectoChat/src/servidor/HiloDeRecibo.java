package servidor;

import servidor.Servidor;
import java.net.*;
import java.io.*;
import java.util.logging.*;
import servidor.HiloDeRecibo;

public class HiloDeRecibo extends Thread{
	
	//ATRIBUTOS
	private final Servidor ventanaDeServidor;
	private ObjectInputStream entrada;
	private Socket cliente;
	private String mensaje;

    //CONSTRUCTOR
    public HiloDeRecibo(Socket cliente, Servidor ventana) {
        this.cliente = cliente;
        this.ventanaDeServidor = ventana;
    }
    
    //METODOS 
    public void mostrarMensaje(String mensaje) {
    	ventanaDeServidor.pantallaDeChat.append(mensaje);
    }

    public void run() {
        try {
            entrada = new ObjectInputStream(cliente.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(HiloDeRecibo.class.getName()).log(Level.SEVERE, null, ex);
            ventanaDeServidor.mostrarMensaje("Error al enviar Mensaje");
        }

        do {
            try {
                mensaje = (String) entrada.readObject();
                ventanaDeServidor.mostrarMensaje(mensaje);
            } catch (SocketException ex) {
            	ventanaDeServidor.mostrarMensaje("Se ha caido la conexion con el cliente ");
                mensaje = "!!!";
            } catch (EOFException eofException) {
            	ventanaDeServidor.mostrarMensaje("Se ha caido la conexion con el cliente");
                mensaje = "!!!";
            } catch (IOException ex) {
                Logger.getLogger(HiloDeRecibo.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException classNotFoundException) {
            	ventanaDeServidor.mostrarMensaje("Objeto no identificado");
            }
        } 
        while (!mensaje.equals("!!!"));

        try {
            entrada.close();
            cliente.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        ventanaDeServidor.mostrarMensaje("Conexion Terminada");
    }
	
}