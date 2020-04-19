package archivo.servidor;

import java.io.*;
import java.net.*;

public class ArchivoDeEnvio extends Thread{
	    
	    //ATRIBUTOS
	    byte[] vectorD;    
	    int prueba;
	    Socket servidor;
	    String IPCliente;
	    String nombreDeArchivo;
	    BufferedInputStream buffer1;    
	    BufferedOutputStream buffer2;

	    //CONSTRUCTOR
	    public ArchivoDeEnvio(String chatCliente, String pat) {
	        this.IPCliente = chatCliente;
	        this.nombreDeArchivo = pat;
	    }

	    //METODO
	    public void run() {
	    	try {
	            final File localF = new File(nombreDeArchivo);

	            servidor = new Socket("localhost", 32768);
	            
	            buffer1 = new BufferedInputStream(new FileInputStream(localF));
	            buffer2 = new BufferedOutputStream(servidor.getOutputStream());
        
	            DataOutputStream dos = new DataOutputStream(servidor.getOutputStream());
	            dos.writeUTF(localF.getName());

	            vectorD = new byte[8192];
	
	            while ((prueba = buffer1.read(vectorD)) != -1) {
	
	                buffer2.write(vectorD, 0, prueba);
	            }

	            buffer1.close();    
	            buffer2.close();    

	        } catch (Exception e) {
	        }
	    }
	}