package pelisServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class peliculasHilos implements Runnable {
	private Thread hilo;
	private Socket socketAlCliente;

	public peliculasHilos(Socket socketAlCliente) {
		super();
		hilo = new Thread(this, "Cliente_");
		this.socketAlCliente = socketAlCliente;
		hilo.start();
	}

	@Override
	public void run() {
		PrintStream enviar = null;
		InputStreamReader recibir = null;
		BufferedReader leer = null;
		try {
			enviar = new PrintStream(socketAlCliente.getOutputStream());
			recibir = new InputStreamReader(socketAlCliente.getInputStream());

			leer = new BufferedReader(recibir);
			String texto = "";
			boolean continuar = true;
			int eleccion = 0;
			DAO dao = new DAO();
			
			while (continuar) {
//				eleccion = leer.read() - '0'; // Convierte el valor ASCII al número real
				 String input = leer.readLine();
	             eleccion = Integer.parseInt(input);
				switch (eleccion) {
				case 1: {
					
					dao.buscarPorId(socketAlCliente);
					System.out.println();
					break;
				}
				case 2: {
					dao.buscarPorDirector(socketAlCliente);
					break;
				}
				case 3: {
					dao.buscarPorTitulo(socketAlCliente);
					break;
				}
				case 4: {
					dao.guardarPeli(socketAlCliente);
					break;
				}
				case 5: {
					enviar.println("Fin");
					socketAlCliente.close();
					break;
				}
				default:
					throw new IllegalArgumentException("Unexpected value: ");
				}

			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
