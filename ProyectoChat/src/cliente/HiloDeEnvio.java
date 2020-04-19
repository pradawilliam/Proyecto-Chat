package cliente;

import cliente.Cliente;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Date;
import java.net.Socket;
import java.net.SocketException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;

public class HiloDeEnvio extends Thread {
	
	//ATRIBUTOS
	private final Cliente ventanaDeCliente;
	private ObjectOutputStream salida;
	private String mensajeC;
	private Socket conexion;
 	static Statement statement;
 	static int id;    

 	//CONSTRUCTOR
	public HiloDeEnvio(Socket conn, final Cliente ventana) {
	        this.conexion = conn;
	        this.ventanaDeCliente = ventana;

	
	        ventanaDeCliente.tipiar.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent event) {
	                mensajeC = event.getActionCommand();
	                SQL(mensajeC);
	                enviarMensaje(mensajeC); 
	                ventanaDeCliente.tipiar.setText(""); 
	            }
	        });
	    }
	    
	    //METODOS
	    private void SQL(String mensaje) {
	        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Chat","postgres","williamapw12")) {
	        statement = conn.createStatement();
	        statement.executeQuery("INSERT INTO chat (mensaje) VALUES('"+mensaje+"')");
	       } catch (SQLException e) {
	    }
	    
	    }
 
	    private void enviarMensaje(String mensaje) {
	        try {
	        	 Date fecha = new Date();
	        	String DFormat = "hh:mm:ss a dd/MM/yy";
	        	SimpleDateFormat alteracion = new SimpleDateFormat(DFormat);
	        	salida.writeObject("- Cliente" + " : " + mensaje+"\n "+alteracion.format(fecha));
	            salida.flush(); 
	            ventanaDeCliente.mostrarMensaje("- Yo : " + mensaje+"\n "+alteracion.format(fecha));
	        } catch (IOException ioException) {
	        	ventanaDeCliente.mostrarMensaje("Servidor Perdido");
	        }
	    }

	    public void mostrarMensaje(String mensaje) {
	    	ventanaDeCliente.pantallaDeChat.append(mensaje);
	    }

	    public void run() {
	        try {
	            salida = new ObjectOutputStream(conexion.getOutputStream());
	            salida.flush();
	        } catch (SocketException ex) {
	        	System.out.println("Problemas de red");
	        } catch (IOException ioException) {
	            ioException.printStackTrace();
	        } catch (NullPointerException ex) {
	        }
	    }
}

