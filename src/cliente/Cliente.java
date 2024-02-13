package cliente;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        try {
            System.out.println("Creando el socket cliente");
            Socket cliente = new Socket();

            System.out.println("Establecemos conexion");
            //Le ponemos los datos que hemos introducido en el servidor
            InetSocketAddress dircServidor = new InetSocketAddress ("localhost", 55555);
            cliente.connect(dircServidor);

            //Creamos el flujo de salida
            OutputStream salida = cliente.getOutputStream();
            InputStream entrada = cliente.getInputStream();

            System.out.println("Ya se puede enviar mensajes");
            System.out.println("Introduzca el mensaje a enviar");
            String mensaje = teclado.nextLine();
            teclado.next();
            System.out.println("¿Que tratamiento desea realizar al mensaje?");
            System.out.println("1. Convertir a mayúsculas");
            System.out.println("2. Convertir a minúsculas");
            System.out.println("3. Revertir la cadena");
            String opcion = teclado.next();
            String mensajeEnviar = mensaje + "*|*" + opcion;
            //Enviamos la respuesta al servidor
            salida.write(mensajeEnviar.getBytes());
            //Recibimos la respuesta del servidor
            byte[] respuesta = new byte[1024];
            entrada.read(respuesta);
            String mensajeRecibido = new String(respuesta).trim();
            System.out.println("El mensaje recibido es:");
            System.out.println(mensajeRecibido);
            System.out.println("Cerrando el socket Cliente");
            cliente.close();
            System.out.println("Conexión socket Cliente cerrada");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

