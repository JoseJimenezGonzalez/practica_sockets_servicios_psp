package servidor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Servidor extends Thread {
    private Socket clienteSocket;

    public Servidor(Socket socket) {
        this.clienteSocket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Arrancando hilo nuevo");
            //Crea sus flujos E/S para la comunicacion
            InputStream entrada = clienteSocket.getInputStream();
            OutputStream salida = clienteSocket.getOutputStream();
            //Vamos a recibir el mensaje del cliente
            byte[] buffer = new byte[1024];
            int bytesRead;
            while((bytesRead = entrada.read(buffer)) != -1){
                //Se crea mensajeCompleto a partir de los bytes en el array buffer, empezando desde
                //el primer byte (índice 0) y tomando tantos bytes como indique bytesRead.
                String mensajeCompleto = new String(buffer, 0, bytesRead).trim();

                // Dividir la cadena usando el delimitador "*|*"
                String[] partes = mensajeCompleto.split("\\*\\|\\*");
                if(partes.length == 2){
                    //Va bien el programa
                    String mensajeRecibido = partes[0];
                    String modoUsoRecibido = partes[1];
                    respuestaServidor(mensajeRecibido, modoUsoRecibido, salida);
                }else{
                    System.out.println("Formato no valido: " + mensajeCompleto);
                }

            }
            // Cierra los flujos y el socket del cliente
            entrada.close();
            salida.close();
            clienteSocket.close();
        } catch (IOException ioe) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ioe);
        }finally {
            System.out.println("Hilo terminado");
        }
    }

    private void respuestaServidor(String mensajeRecibido, String modoUsoRecibido, OutputStream salida) throws IOException {
        String mensajeEnviar = "";
        if(modoUsoRecibido.equals("1")){
            //Mayus
            mensajeEnviar = mensajeRecibido.toUpperCase();
        }else if(modoUsoRecibido.equals("2")){
            //Minus
            mensajeEnviar = mensajeRecibido.toLowerCase();
        }else if(modoUsoRecibido.equals("3")){
            //Damos la vuelta
            // Creamos un objeto StringBuilder con la cadena original
            StringBuilder builder = new StringBuilder(mensajeRecibido);
            // Utilizamos el método reverse() para dar la vuelta a la cadena
            builder.reverse();
            // Convertimos el StringBuilder de nuevo a String y lo retornamos
            mensajeEnviar = builder.toString();
        }else{
            mensajeEnviar = "No se reconoce esa opcion";
        }
        salida.write(mensajeEnviar.getBytes());
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Creando Socket Servidor");

        ServerSocket server = new ServerSocket();
        InetSocketAddress dir = new InetSocketAddress("localhost", 55555);
        server.bind(dir);

        System.out.println("Aceptando conexiones");

        while(server != null)
        {
            //Acepta la conexion y crea un subSocket
            Socket nuevo = server.accept();
            System.out.println("He recibido una petición");

            Servidor hilo = new Servidor(nuevo);
            hilo.start();
        }
        System.out.println("Ha finalizado la aplicación");
    }
}

