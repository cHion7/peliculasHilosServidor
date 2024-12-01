package piedra_papel__tijeras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
	public static final int puerto = 3000;

	public static void main(String[] args) throws IOException {
		System.out.println("------------------Servidor------------------");

		InputStreamReader recibir = null;
		PrintStream enviar = null;
		InputStreamReader recibir2 = null;
		PrintStream enviar2 = null;
		Socket socketAlCliente = null;
		Socket socketAlCliente2 = null;
		BufferedReader bf = null;

		InetSocketAddress direccion = new InetSocketAddress(puerto);

	

		try (ServerSocket serverSoc = new ServerSocket()) {
			serverSoc.bind(direccion);

			System.out.println("Esperando al primer jugador");
			socketAlCliente = serverSoc.accept();
			enviar = new PrintStream(socketAlCliente.getOutputStream());
			enviar.println("Te has conectado");
			System.out.println("Jugador encontrado");

			System.out.println("Esperando al segundo jugador");
			socketAlCliente2 = serverSoc.accept();
			enviar2 = new PrintStream(socketAlCliente2.getOutputStream());
			enviar2.println("Te has conectado");

			///
			enviar.println("La partida va a comenzar");
			enviar2.println("La partida va a comenzar");

			int jugador1Wins = 0;
			int jugador2Wins = 0;
			
			while (jugador1Wins < 3 && jugador2Wins < 3) {


				enviar.println("Elije piedra = 1, papel = 2  o tijeras = 3");

				enviar2.println("Elije piedra = 1, papel = 2  o tijeras = 3");

				recibir = new InputStreamReader(socketAlCliente.getInputStream());
				bf = new BufferedReader(recibir);
				String jugador1 = bf.readLine();
				//1 = piedra
				//2 = papel
				//3 = tijera

				recibir2 = new InputStreamReader(socketAlCliente2.getInputStream());
				bf = new BufferedReader(recibir2);
				String jugador2 = bf.readLine();

				  if (jugador1.equals(jugador2)) {
					  enviar.println("Ha habido un empate. La puntuación es " + jugador1Wins + "-" + jugador2Wins);
					  enviar2.println("Ha habido un empate. La puntuación es " + jugador2Wins + "-" + jugador1Wins);
	                } else if ((jugador1.equals("1") && jugador2.equals("3")) ||
	                           (jugador1.equals("2") && jugador2.equals("1")) ||
	                           (jugador1.equals("3") && jugador2.equals("2"))) {
	                    
	                    jugador1Wins++;
	                    enviar.println("Has ganado esta partida. La puntuación es " + jugador1Wins + "-" + jugador2Wins);
	                    enviar2.println("Has perdido esta partida. La puntuación es " + jugador2Wins + "-" + jugador1Wins);
	                } else {
	                   
	                    jugador2Wins++;
	                    enviar.println("Has perdido esta partida. La puntuación es " + jugador1Wins + "-" + jugador2Wins);
	                    enviar2.println("Has ganado esta partida. La puntuación es " + jugador2Wins + "-" + jugador1Wins);
	                }
			}
			enviar.println("FIN");
			enviar2.println("FIN");
			
			  if (jugador1Wins > jugador2Wins) {
				  enviar.println("Has ganado la copa chinChonchun");
				  enviar2.println("Has perdido, ya sabes quien gana gana y quien no no");
	            } else {
	            	enviar.println("Has perdido, ya sabes quien gana gana y quien no no");
	            	enviar2.println("Has ganado la copa chinChonchun");
	            }

			  socketAlCliente.close();
			  socketAlCliente2.close();
			
		} catch (Exception e) {
			System.err.println("CLIENTE: Error -> " + e);
			e.printStackTrace();
		}

	}
}

/*package piedra_papel__tijeras;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    public static final int puerto = 3000;

    public static void main(String[] args) throws IOException {
        System.out.println("------------------Servidor------------------");

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            System.out.println("Esperando al primer jugador...");
            Socket socketJugador1 = serverSocket.accept();
            PrintStream salidaJugador1 = new PrintStream(socketJugador1.getOutputStream());
            BufferedReader entradaJugador1 = new BufferedReader(new InputStreamReader(socketJugador1.getInputStream()));
            salidaJugador1.println("Te has conectado");
            System.out.println("Jugador 1 conectado");

            System.out.println("Esperando al segundo jugador...");
            Socket socketJugador2 = serverSocket.accept();
            PrintStream salidaJugador2 = new PrintStream(socketJugador2.getOutputStream());
            BufferedReader entradaJugador2 = new BufferedReader(new InputStreamReader(socketJugador2.getInputStream()));
            salidaJugador2.println("Te has conectado");
            System.out.println("Jugador 2 conectado");

            salidaJugador1.println("La partida va a comenzar");
            salidaJugador2.println("La partida va a comenzar");

            int jugador1Wins = 0;
            int jugador2Wins = 0;

            while (jugador1Wins < 3 && jugador2Wins < 3) {
                salidaJugador1.println("Elije piedra = 1, papel = 2 o tijeras = 3");
                salidaJugador2.println("Elije piedra = 1, papel = 2 o tijeras = 3");

                String opcionJugador1 = entradaJugador1.readLine();
                String opcionJugador2 = entradaJugador2.readLine();

                if (opcionJugador1 == null || opcionJugador2 == null) {
                    System.out.println("Un jugador se ha desconectado.");
                    break;
                }

                // Lógica del juego
                if (opcionJugador1.equals(opcionJugador2)) {
                    salidaJugador1.println("Ha habido un empate. La puntuación es " + jugador1Wins + "-" + jugador2Wins);
                    salidaJugador2.println("Ha habido un empate. La puntuación es " + jugador2Wins + "-" + jugador1Wins);
                } else if ((opcionJugador1.equals("1") && opcionJugador2.equals("3")) ||
                           (opcionJugador1.equals("2") && opcionJugador2.equals("1")) ||
                           (opcionJugador1.equals("3") && opcionJugador2.equals("2"))) {
                    // Jugador 1 gana
                    jugador1Wins++;
                    salidaJugador1.println("Has ganado esta partida. La puntuación es " + jugador1Wins + "-" + jugador2Wins);
                    salidaJugador2.println("Has perdido esta partida. La puntuación es " + jugador2Wins + "-" + jugador1Wins);
                } else {
                    // Jugador 2 gana
                    jugador2Wins++;
                    salidaJugador1.println("Has perdido esta partida. La puntuación es " + jugador1Wins + "-" + jugador2Wins);
                    salidaJugador2.println("Has ganado esta partida. La puntuación es " + jugador2Wins + "-" + jugador1Wins);
                }
            }

            // Mensaje final
            if (jugador1Wins > jugador2Wins) {
                salidaJugador1.println("Has ganado la copa chinChonchun");
                salidaJugador2.println("Has perdido, ya sabes quien gana gana y quien no no");
            } else {
                salidaJugador1.println("Has perdido, ya sabes quien gana gana y quien no no");
                salidaJugador2.println("Has ganado la copa chinChonchun");
            }

            // Cerrar conexiones
            socketJugador1.close();
            socketJugador2.close();
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

*/