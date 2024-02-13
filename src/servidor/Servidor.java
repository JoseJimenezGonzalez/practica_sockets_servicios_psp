package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor extends Thread {
    private Socket clienteSocket;

    public Servidor(Socket socket) {
        this.clienteSocket = socket;
    }

    public void run() {
        try {
            BufferedReader entrada = new BufferedReader(new InputStreamReader(clienteSocket.getInputStream()));
            PrintWriter salida = new PrintWriter(clienteSocket.getOutputStream(), true);

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

    public static void main(String[] args) {
        try {
            // Crea un ServerSocket para aceptar conexiones de clientes
            ServerSocket serverSocket = new ServerSocket(55555);
            System.out.println("Servidor Formateo de Cadenas iniciado. Esperando conexiones...");

            while (true) {
                // Acepta una conexión de cliente y crea un hilo para manejarla
                Socket clienteSocket = serverSocket.accept();
                System.out.println("Cliente conectado desde " + clienteSocket.getInetAddress());
                Servidor hiloCliente = new Servidor(clienteSocket);
                hiloCliente.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

