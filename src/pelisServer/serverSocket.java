package pelisServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class serverSocket {
    public static final int puerto = 4000;

    public static void main(String[] args) throws IOException {
        System.out.println("      APLICACIÓN DE SERVIDOR CON HILOS     ");
        System.out.println("-------------------------------------------");	
        boolean entrada = true;

        try (ServerSocket server = new ServerSocket()) {
            DAO dao = new DAO();
            try {
                dao.inicializarBaseDatos();
            } catch (Exception e) {
                System.out.println("Error al inicializar la base de datos:");
                e.printStackTrace();
                return; // No continuar si no se puede inicializar la base de datos
            }

            // Asociar dirección y puerto
            InetSocketAddress direccion = new InetSocketAddress(puerto);
            server.bind(direccion);
            System.out.println("Esperando peticiones en el puerto " + puerto);

            while (entrada) {
                try {
                	System.out.println("esperando cliente");
                    Socket alCliente = server.accept();
                    System.out.println("Cliente recibido");
                    new peliculasHilos(alCliente); // Manejar conexión en un hilo
                    System.out.println("Ha salido");
                } catch (IOException e) {
                    System.out.println("Error al aceptar cliente:");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor:");
            e.printStackTrace();
        }
    }
}
