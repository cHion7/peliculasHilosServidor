package piedra_papel__tijeras;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
	public static void main(String[] args) {
		String direccionServidor = "192.168.142.233";
		int puerto = 3000;
		Scanner scanner = new Scanner(System.in);

		try (Socket socket = new Socket(direccionServidor, puerto);
				PrintStream salida = new PrintStream(socket.getOutputStream());
				BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

			System.out.println("Conexión establecida con el servidor.");
			System.out.println("Esperando instrucciones del servidor...");

			String conexion = entrada.readLine();
			System.out.println("Servidor: " + conexion);

			String comenzar = entrada.readLine();
			System.out.println("Servidor: " + comenzar);

			while (true) {

				String elegir = entrada.readLine();
				System.out.println("------------------------------------------------------------------------");
				if (elegir.contains("FIN")) {
					break;
				} else {
					System.out.println("Servidor: " + elegir);

				}

				System.out.print("Seleccione su opción: ");
				String opcion = scanner.nextLine();

				salida.println(opcion);

				String resultado = entrada.readLine();
				System.out.println("Servidor: " + resultado);

			}

			String fin = entrada.readLine();
			System.out.println("Servidor: " + fin);
			socket.close();
		} catch (Exception e) {
			System.out.println("Error en la conexión con el servidor: " + e.getMessage());
		} finally {
			scanner.close();
		}
	}
}