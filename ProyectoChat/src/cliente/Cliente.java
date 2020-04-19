package cliente;

import cliente.Cliente;
import cliente.HiloDeEnvio;
import cliente.HiloDeRecibo;
import archivo.cliente.ArchivoDeEnvio;
import archivo.cliente.ArchivoDeRecibo;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.util.logging.*;
import javax.swing.*;

public class Cliente extends JFrame {
	
	    //ATRIBUTOS
	    public JTextArea pantallaDeChat;
	    public JTextField tipiar;
	    public JMenuItem anexar;
	    private static ServerSocket servidor;
	    private static Socket cliente;
	    private static String IPServidor ="127.0.0.1";
	    public static Cliente ventanaDeCliente;
	    public static String usuario;
	    public boolean recibir;
	    
	    //CONSTRUCTOR
	    public Cliente() {
	    	setTitle("Cliente");
	        getContentPane().setLayout(null);
	        
	        tipiar = new JTextField();
	        tipiar.setBounds(0, 380, 300, 40);
	        tipiar.setEditable(false);
	        getContentPane().add(tipiar);

	        pantallaDeChat = new JTextArea();
	        pantallaDeChat.setFont(new Font("Arial", Font.PLAIN, 15));
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
	                    ventanaDeCliente.mostrarMensaje("Enviando Archivo.....");
	                    ArchivoDeRecibo obtArchivo = new ArchivoDeRecibo(path, usuario, 32768, "localhost");
	                    obtArchivo.start();
	                    ArchivoDeEnvio envArchivo = new ArchivoDeEnvio(IPServidor, path);
	                    envArchivo.start();
	                    ventanaDeCliente.mostrarMensaje("Archivo Enviado Correctamente");
	                }
	            }
	        });
	        setSize(314, 483);
	        setVisible(true);
	        setResizable(false);
	    }

	    public static void main(String[] args) {
	    	ventanaDeCliente = new Cliente();
	    	ventanaDeCliente.setLocation(300, 150);
	        ventanaDeCliente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	
	        try {
	        	ventanaDeCliente.mostrarMensaje("Buscando a un Servidor .....");
	            cliente = new Socket(InetAddress.getByName(IPServidor), 11111);
	            ventanaDeCliente.mostrarMensaje("Conectado a : " + cliente.getInetAddress().getHostName());
	            ventanaDeCliente.habilitar(true);
	            
	            HiloDeEnvio hilo1 = new HiloDeEnvio(cliente, ventanaDeCliente);
	            hilo1.start();
	            HiloDeRecibo hilo2= new HiloDeRecibo(cliente, ventanaDeCliente);
	            hilo2.start();
	        } catch (IOException ex) {
	            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
	            ventanaDeCliente.mostrarMensaje("No se encuentra el servidor");
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