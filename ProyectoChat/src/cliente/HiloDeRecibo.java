package cliente;

import cliente.Cliente;
import java.io.*;
import java.net.*;
import java.util.logging.*;
import cliente.HiloDeRecibo;

public class HiloDeRecibo extends Thread {
	
	    //ATRIBUTOS
	    private final Cliente ventanaDeCliente;
	    private ObjectInputStream entrada;
	    private Socket cliente;
	    private String mensaje;

	    //CONSTRUCTOR
	    public HiloDeRecibo(Socket cliente, Cliente ventana) {
	        this.cliente = cliente;
	        this.ventanaDeCliente = ventana;
	    }
	    //METODOS
	    public void mostrarMensaje(String mensaje) {
	    	ventanaDeCliente.pantallaDeChat.append(mensaje);
	    }

	    public void run() {
	        try {
	            entrada = new ObjectInputStream(cliente.getInputStream());
	        } catch (IOException ex) {
	            Logger.getLogger(HiloDeRecibo.class.getName()).log(Level.SEVERE, null, ex);
	        }

	        do {
	            try {
	                mensaje = (String) entrada.readObject();
	                ventanaDeCliente.mostrarMensaje(mensaje);
	            } catch (SocketException ex) {
	            } catch (EOFException eofException) {
	            	ventanaDeCliente.mostrarMensaje("Se ha caido la conexion con servidor");
	                mensaje ="!!!";
	            } catch (IOException ex) {
	                Logger.getLogger(HiloDeRecibo.class.getName()).log(Level.SEVERE, null, ex);
	                ventanaDeCliente.mostrarMensaje("Se ha caido la conexion con servidor");
	                mensaje ="!!!";
	            } catch (ClassNotFoundException classNotFoundException) {
	            	ventanaDeCliente.mostrarMensaje("Objeto no identificado");
	                mensaje ="!!!";
	            }

	        } while (!mensaje.equals("!!!"));

	        try {
	            entrada.close();
	            cliente.close();
	        } catch (IOException ioException) {
	            ioException.printStackTrace();
	        }

	        ventanaDeCliente.mostrarMensaje("Conexion Terminada");
	    }
}

