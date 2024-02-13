package servidor;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

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

            // Lee la cadena y la opción de formateo del cliente
            String cadena = entrada.readLine();
            String opcion = entrada.readLine();

            // Realiza el formateo correspondiente según la opción elegida
            String resultado = "";
            switch (opcion) {
                case "1":
                    resultado = cadena.toUpperCase(); // Convertir a mayúsculas
                    break;
                case "2":
                    resultado = cadena.toLowerCase(); // Convertir a minúsculas
                    break;
                case "3":
                    resultado = new StringBuilder(cadena).reverse().toString(); // Revertir la cadena
                    break;
                default:
                    resultado = "Opción no válida";
            }

            // Envía el resultado al cliente
            salida.println(resultado);

            // Cierra los flujos y el socket del cliente
            entrada.close();
            salida.close();
            clienteSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        System.out.println("Calculadora terminada");
    }
}

