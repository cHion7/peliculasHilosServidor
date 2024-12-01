package pelisServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
	public static final int puerto = 4000;
	public static final String IP_SERVER = "localhost";

	public static void main(String[] args) throws IOException {
		System.out.println("------------------Cliente------------------");
		InetSocketAddress direccionServidor = new InetSocketAddress(IP_SERVER, puerto);
		Scanner S = new Scanner(System.in);

		try (Socket socketAlServer = new Socket()) {
			socketAlServer.connect(direccionServidor);
			PrintStream enviar = new PrintStream(socketAlServer.getOutputStream());

			InputStreamReader recibir = new InputStreamReader(socketAlServer.getInputStream());
			BufferedReader bf = new BufferedReader(recibir);
			String recibido = "";
//			String resultado = bf.readLine();
			boolean salida = true;
			while (salida) {
				System.out.println("\nMenú:");
				System.out.println("1. Consultar película por ID");
				System.out.println("2. Consultar película por titulo");
				System.out.println("3. Consultar películas por director ");
				System.out.println("4. Agregar película");
				System.out.println("5. Salir de la aplicación");
				System.out.print("Seleccione una opción: ");

				int opcion = S.nextInt();

				switch (opcion) {
				case 1:
					// se envia al switch
					enviar.println(1);

					recibido = bf.readLine();
					System.out.println(recibido);
					int id = S.nextInt();
					enviar.println(id);
					System.out.println("hola");
					recibido = bf.readLine();
					System.out.println(recibido);
					break;

				case 2:
					// se envia al switch
					enviar.println(3);
					recibido = bf.readLine();
					System.out.println(recibido);
					String titulo = S.next();
					enviar.println(titulo);
					recibido = bf.readLine();
					System.out.println(recibido);
					break;
			
				case 3:
					int filasNum = 0;
					// se envia al switch
					enviar.println(2);

					recibido = bf.readLine();
					System.out.println(recibido);
					String director = S.next();
					enviar.println(director);

					recibido = bf.readLine(); 
					System.out.println("Número de filas recibidas: " + recibido);
					filasNum = Integer.parseInt(recibido, 10);

					
					for (int i = 0; i < filasNum; i++) {
					    recibido = bf.readLine();
					    if ("FIN".equals(recibido)) {
					        break; 
					    }
					    System.out.println(recibido);
					}
					break;

				case 4:
					
					enviar.println(4); 
					System.out.println(bf.readLine()); 
					String tituloPeli = S.next();
					enviar.println(tituloPeli);

					System.out.println(bf.readLine()); 
					String directorPeli = S.next();
					enviar.println(directorPeli);

					System.out.println(bf.readLine()); 
					double precioPeli = S.nextDouble();
					enviar.println(String.valueOf(precioPeli));

					System.out.println(bf.readLine());
					break;

				case 5:
					enviar.print(5);
					recibido = bf.readLine();
					System.out.println(recibido);
					System.out.println("Te has descomnectado del servidor");
					socketAlServer.close();
					salida = false;
					return;

				default:
					System.out.println("Opción no válida.");
					return;
				}

			}
		}

	}
}
