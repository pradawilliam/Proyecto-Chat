package servidor;

import servidor.Servidor;
import servidor.HiloDeEnvio;
import servidor.HiloDeRecibo;
import archivo.servidor.ArchivoDeEnvio;
import archivo.servidor.ArchivoDeRecibo;
import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.logging.*;

public class Servidor extends JFrame {
	
	    //ATRIBUTOS
	    public JTextArea pantallaDeChat;
	    public JTextField tipiar;
	    public JMenuItem anexar;
	    private static ServerSocket servidor;
	    private static Socket cliente ;
	    private static String IPCliente = "10.255.255.255";
	    public static String usuario = "Servidor";                    
	    public static Servidor ventanaDeServidor;
	    String dir_recibo = "C:\\ServidorArchivo";

	    //CONSTRUCTOR
	    public Servidor() {
	    	setTitle("Servidor" );
	        getContentPane().setLayout(null);
	   
	        tipiar = new JTextField();
	        tipiar.setBackground(Color.WHITE);
	        tipiar.setBounds(0, 380, 300, 40);     
	        tipiar.setEditable(false);
	        getContentPane().add(tipiar);

	        pantallaDeChat = new JTextArea();
	        pantallaDeChat.setFont(new Font("Arial", Font.PLAIN, 15));
	        pantallaDeChat.setDisabledTextColor(Color.BLACK);
	        pantallaDeChat.setSelectionColor(Color.BLACK);
	        pantallaDeChat.setCaretColor(Color.BLACK);
	        pantallaDeChat.setEditable(false);
	        JScrollPane scrollPane = new JScrollPane(pantallaDeChat);
	        scrollPane.setBounds(0, 0, 300, 380);
	        getContentPane().add(scrollPane);
	        pantallaDeChat.setBackground(Color.LIGHT_GRAY);
	        pantallaDeChat.setForeground(Color.BLACK);
	        tipiar.setForeground(Color.BLACK);

	        JMenuItem finalizar = new JMenuItem("Finalizar");
	        finalizar.setForeground(Color.WHITE);
	        finalizar.setBackground(Color.BLACK);
	        anexar = new JMenuItem("Insertar Archivo");
	        anexar.setBackground(Color.BLACK);
	        anexar.setForeground(Color.WHITE);
	        anexar.setEnabled(false);
	        JMenuBar barra = new JMenuBar();
	        setJMenuBar(barra);
	        barra.add(anexar);
	        barra.add(finalizar);

	        //TERMINAR CONEXION
	        finalizar.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                System.exit(0); 
	            }
	        });

	        //INGRESO DE ARCHIVO
	        anexar.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                javax.swing.JFileChooser selecVentana = new javax.swing.JFileChooser();
	                int seleccion = selecVentana.showOpenDialog(selecVentana);
	                String path = selecVentana.getSelectedFile().getAbsolutePath();

	             if (seleccion == JFileChooser.APPROVE_OPTION) {
	                	JOptionPane.showMessageDialog(null, path);
	                	ventanaDeServidor.mostrarMensaje("Enviando Archivo...");
	                    ArchivoDeRecibo obtArchivo = new ArchivoDeRecibo(path , usuario, 32768, "localhost");
	                    obtArchivo.start();
	                    ArchivoDeEnvio envArchivo = new ArchivoDeEnvio("localhost", path);																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																																											
	                    envArchivo.start();
	                    ventanaDeServidor.mostrarMensaje("Archivo Enviado Correctamente");
	                }
	            }
	        });
	        setSize(312, 480);
	        setVisible(true); 
	        setResizable(false);

	    }

	    public static void main(String[] args) {
	    
	    	ventanaDeServidor = new Servidor();
	    	ventanaDeServidor.setLocation(650, 150);
	    	ventanaDeServidor.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        try {
	            
	            servidor = new ServerSocket(11111, 100);
	            ventanaDeServidor.mostrarMensaje("Esperando a un Cliente .....");
	       
	            while (true) {
	                try {
	                 
	                    cliente = servidor.accept();
	                    ventanaDeServidor.mostrarMensaje("Conectado a : " + cliente.getInetAddress().getHostName());
	                    ventanaDeServidor.habilitar(true);
	                    
	                    HiloDeEnvio hilo1 = new HiloDeEnvio(cliente, ventanaDeServidor);
	                    hilo1.start();
	                    HiloDeRecibo hilo2 = new HiloDeRecibo(cliente, ventanaDeServidor);
	                    hilo2.start();
	                } catch (IOException ex) {
	                    Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
	                    ventanaDeServidor.mostrarMensaje("No se conectó con el cliente");
	                }
	            }
	        } catch (IOException ex) {
	            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
	            ventanaDeServidor.mostrarMensaje("IP del Servidor no encontrado");
	        }
	    }

	    //METODOS
	    public void mostrarMensaje(String mensaje) {
	        pantallaDeChat.append(mensaje + "\n");
	    }

	    public void habilitar(boolean edit) {
	    	tipiar.setEditable(edit);
	        anexar.setEnabled(edit);
	    }
}