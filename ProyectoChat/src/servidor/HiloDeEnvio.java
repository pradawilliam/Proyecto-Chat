package servidor;

import servidor.Servidor;
import java.text.SimpleDateFormat;
import java.io.*;
import java.net.*;
import java.util.Date;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class HiloDeEnvio extends Thread {
	
	//ATRIBUTOS
	private String mensajeS;
    private Socket conexion;
	private final Servidor ventanaDeServidor;
    private ObjectOutputStream salida;
    
    //CONSTRUCTOR
    public HiloDeEnvio(Socket conn, final Servidor ventana) {
        this.conexion = conn;
        this.ventanaDeServidor = ventana;
        
        ventanaDeServidor.tipiar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
            	mensajeS = event.getActionCommand();
                SQL(mensajeS);
                enviarMensaje(mensajeS); 
                ventanaDeServidor.tipiar.setText(""); 
            }
        });
    }
    
    //METODOS
    private void SQL(String mensaje) {
        try (Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Chat","postgres","williamapw12")) {
        Statement statement = conn.createStatement();
        statement.executeQuery("INSERT INTO chat (mensaje) VALUES('"+mensaje+"')");
       } catch (SQLException e) {
    }
    
    }

    private void enviarMensaje(String mensaje) {
        try {
        	Date fecha = new Date();
        	String DFormat = "hh:mm:ss a dd/MM/yy";
        	SimpleDateFormat alteracion = new SimpleDateFormat(DFormat);
            salida.writeObject("- "+ventanaDeServidor.usuario + " : " + mensaje+"\n "+alteracion.format(fecha));
            salida.flush(); 
            ventanaDeServidor.mostrarMensaje("- Yo : " + mensaje+"\n "+alteracion.format(fecha));
        } catch (IOException ioException) {
        	ventanaDeServidor.mostrarMensaje("Conexion Cliente Caida");
        }
    }
    
    public void mostrarMensaje(String mensaje) {
    	ventanaDeServidor.pantallaDeChat.append(mensaje);
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