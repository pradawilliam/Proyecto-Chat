package archivo.servidor;

import java.io.*;
import java.net.*;

public class ArchivoDeRecibo extends Thread{
	 
	    //ATRIBUTOS
	    private String path, IPCliente, nombreDeCliente;
	    private int puerto;
	    private Socket clienteArchivos;

	    //CONSTRUCTOR
	    public ArchivoDeRecibo(String fichero, String cliente, int puerto, String IPCliente) {
	        this.path = fichero;
	        this.puerto = puerto;
	        this.nombreDeCliente = cliente;
	        this.IPCliente = IPCliente;
	    }

	    //METODO
	    public void run() {

	        ServerSocket server;   
	        Socket receptor;       
	        BufferedInputStream bis;
	        BufferedOutputStream bos;
	        byte[] receivedData;   
	        int indicador;          
	        String file;            

	        try {
	            server = new ServerSocket(32768);
	            while (true) {
	                receptor = server.accept();

	                receivedData = new byte[1024];
	                bis = new BufferedInputStream(receptor.getInputStream());
	                DataInputStream dis = new DataInputStream(receptor.getInputStream());
	                
	                file = dis.readUTF();
	                file = file.substring(file.indexOf('\\') + 1, file.length());

	                bos = new BufferedOutputStream(new FileOutputStream(file));
	                while ((indicador = bis.read(receivedData)) != -1) {
	                    bos.write(receivedData, 0, indicador);
	                }

	                bos.close();
	                dis.close();
	            }
	        } catch (Exception e) {
	        }
	    }
}