package cliente;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) {
        try {
            // Establece la conexión con el servidor
            Socket socket = new Socket("localhost", 55555);
            PrintWriter salida = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedReader teclado = new BufferedReader(new InputStreamReader(System.in));

            // Lee la cadena a formatear y la opción del usuario
            System.out.print("Introduce una cadena de texto: ");
            String cadena = teclado.readLine();
            System.out.println("Elije una opción de formateo:");
            System.out.println("1. Convertir a mayúsculas");
            System.out.println("2. Convertir a minúsculas");
            System.out.println("3. Revertir la cadena");
            System.out.print("Opción: ");
            String opcion = teclado.readLine();

            // Envía la cadena y la opción al servidor
            salida.println(cadena);
            salida.println(opcion);

            // Lee la respuesta del servidor y la muestra al usuario
            String resultado = entrada.readLine();
            System.out.println("Resultado del formateo: " + resultado);

            // Cierra los flujos y el socket
            salida.close();
            entrada.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

