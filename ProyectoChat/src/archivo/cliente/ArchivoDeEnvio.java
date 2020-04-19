package archivo.cliente;

import java.net.*;
import java.io.*;

public class ArchivoDeEnvio extends Thread{
	
	    //ATRIBUTOS
	    byte[] vectorD;
	    int prueba;
	    Socket cliente;
	    String nombreDeArchivo;
	    String IPServidor;
	    BufferedInputStream buffer1;    
	    BufferedOutputStream buffer2;

	    //CONSTRUCTOR
	    public ArchivoDeEnvio(String chatServidor, String pat) {
	        this.IPServidor = chatServidor;
	        this.nombreDeArchivo = pat;
	    }

	    //METODO
	    public void run() {
	    	try {
	            final File localF = new File(nombreDeArchivo);

	            cliente = new Socket(IPServidor, 32768);
	            
	            buffer1 = new BufferedInputStream(new FileInputStream(localF));
	            buffer2 = new BufferedOutputStream(cliente.getOutputStream());
        
	            DataOutputStream dos = new DataOutputStream(cliente.getOutputStream());
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


