package pelisServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.value.Value;

public class DAO {
	public void inicializarBaseDatos() throws SQLException {
		// Consulta SQL para crear la tabla si no existe
		String sqlCrearTabla = """
				    CREATE TABLE IF NOT EXISTS PELICULAS (
				        id INT PRIMARY KEY,
				        titulo VARCHAR(100),
				        director VARCHAR(100),
				        precio DOUBLE
				    )
				""";

		// Consultas SQL para insertar las películas predefinidas
		String[] peliculas = {
				"INSERT INTO PELICULAS (id, titulo, director, precio) VALUES (1, 'Inception', 'Nolan', 10.99)",
				"INSERT INTO PELICULAS (id, titulo, director, precio) VALUES (2, 'Matrix', 'Sisters', 9.99)",
				"INSERT INTO PELICULAS (id, titulo, director, precio) VALUES (3, 'Interstellar', 'Nolan', 12.99)",
				"INSERT INTO PELICULAS (id, titulo, director, precio) VALUES (4, 'Parasite', 'Bong', 8.99)",
				"INSERT INTO PELICULAS (id, titulo, director, precio) VALUES (5, 'Godfather', 'Francis', 14.99)" };

		// Crear conexión y ejecutar las consultas
		Connection conexion = DriverManager.getConnection("jdbc:h2:C:\\Users\\metoj\\Desktop\\base de datos\\psp.db");
		Statement stmt = conexion.createStatement();

		// Crear la tabla
		stmt.execute(sqlCrearTabla);
		System.out.println("Tabla 'peliculas' creada o ya existente.");

		// Insertar películas predefinidas
		for (String pelicula : peliculas) {
			stmt.executeUpdate(pelicula);
		}
		System.out.println("5 películas predefinidas han sido insertadas.");

		// Cerrar recursos manualmente
		stmt.close();
		conexion.close();
	}

	public synchronized void guardarPeli(Socket socketAlCliente) throws SQLException, IOException {
		Connection conexion = DriverManager.getConnection("jdbc:h2:C:\\Users\\metoj\\Desktop\\base de datos\\psp.db");
		PreparedStatement sentencia;
		ResultSet result;
		ResultSet cantidad;
		PrintStream enviar = new PrintStream(socketAlCliente.getOutputStream());
		InputStreamReader recibir = new InputStreamReader(socketAlCliente.getInputStream());
		BufferedReader leer = new BufferedReader(recibir);

		enviar.println("Dime el titulo de la peli");
		leer = new BufferedReader(recibir);
		String titulo = leer.readLine();

		enviar.println("Dime el director");
		leer = new BufferedReader(recibir);
		String director = leer.readLine();

		enviar.println("Dime el el precio");
		leer = new BufferedReader(recibir);
		String precioStr = leer.readLine();
		double precio = Double.parseDouble(precioStr);

		sentencia = conexion.prepareStatement("SELECT COUNT(*) AS TotalFilas FROM PELICULAS");
		cantidad = sentencia.executeQuery();
		int id = 1;
		if (cantidad.next()) {
			id = cantidad.getInt("TotalFilas") + 1;
		}
		System.out.println("-----");

		sentencia = conexion.prepareStatement("INSERT INTO PELICULAS VALUES(?, ?, ?, ?)");
		sentencia.setInt(1, id);
		sentencia.setString(2, titulo);
		sentencia.setString(3, director);
		sentencia.setDouble(4, precio);
		sentencia.executeUpdate();
		enviar.println("Pelicula creada con éxito");

	}

	public void buscarPorId(Socket socketAlCliente) throws SQLException, IOException {
		Connection conexion = DriverManager.getConnection("jdbc:h2:C:\\Users\\metoj\\Desktop\\base de datos\\psp.db");
		PreparedStatement sentencia;
		ResultSet result;
		ResultSet cantidad;
		PrintStream enviar = new PrintStream(socketAlCliente.getOutputStream());
		InputStreamReader recibir = new InputStreamReader(socketAlCliente.getInputStream());
		BufferedReader leer = new BufferedReader(recibir);

		enviar.println("Dime el id");
		leer = new BufferedReader(recibir);
		String idBuscar = leer.readLine();

		sentencia = conexion.prepareStatement("SELECT * FROM PELICULAS WHERE id = ?");
		sentencia.setString(1, idBuscar);
		result = sentencia.executeQuery();

		if (result.next()) {
			int id = result.getInt("id");
			String titulo = result.getString("titulo");
			String director = result.getString("director");
			double precio = result.getDouble("precio");

			// Enviar la respuesta al cliente
			enviar.println("Película encontrada:" + "ID: " + id + "Título: " + titulo + "Director: " + director
					+ "Precio: " + precio);
		} else {
			enviar.println("No se encontró ninguna película con ese ID.");
		}

	}

	// Método para buscar por Título
	public void buscarPorTitulo(Socket socketAlCliente) throws SQLException, IOException {
		Connection conexion = DriverManager.getConnection("jdbc:h2:C:\\Users\\metoj\\Desktop\\base de datos\\psp.db");
		PreparedStatement sentencia;
		ResultSet result;
		ResultSet cantidad;
		PrintStream enviar = new PrintStream(socketAlCliente.getOutputStream());
		InputStreamReader recibir = new InputStreamReader(socketAlCliente.getInputStream());
		BufferedReader leer = new BufferedReader(recibir);

		enviar.println("Dime el titulo");
		leer = new BufferedReader(recibir);
		String tituloBuscar = leer.readLine();

		sentencia = conexion.prepareStatement("SELECT * FROM PELICULAS WHERE titulo = ?");
		sentencia.setString(1, tituloBuscar);
		result = sentencia.executeQuery();

		if (result.next()) {
			System.out.println("buscra peli");
			int id = result.getInt("id");
			String titulo = result.getString("titulo");
			String director = result.getString("director");
			double precio = result.getDouble("precio");
			// Enviar la respuesta al cliente
			enviar.println("Película encontrada:" + "ID: " + id + "Título: " + titulo + "Director: " + director
					+ "Precio: " + precio);
		} else {
			enviar.println("No se encontró ninguna película con ese título.");
		}
	}

	// Método para buscar por Director
	public void buscarPorDirector(Socket socketAlCliente) throws SQLException, IOException {
		Connection conexion = DriverManager.getConnection("jdbc:h2:C:\\Users\\metoj\\Desktop\\base de datos\\psp.db");
		PreparedStatement sentencia;
		ResultSet result;
		ResultSet cantidad;
		PrintStream enviar = new PrintStream(socketAlCliente.getOutputStream());
		InputStreamReader recibir = new InputStreamReader(socketAlCliente.getInputStream());
		BufferedReader leer = new BufferedReader(recibir);

		enviar.println("Dime el Director");
		leer = new BufferedReader(recibir);
		String directorBuscar = leer.readLine();

		sentencia = conexion.prepareStatement("SELECT COUNT(*) AS TotalFilas FROM PELICULAS WHERE director = ?");
		sentencia.setString(1, directorBuscar);
		cantidad = sentencia.executeQuery();

		String filas = "";

		if (cantidad.next()) {
			filas = cantidad.getString("TotalFilas");
		}

		enviar.println(filas);
		sentencia = conexion.prepareStatement("SELECT * FROM PELICULAS WHERE director = ?");
		sentencia.setString(1, directorBuscar);
		result = sentencia.executeQuery();
		// metodo para recibir en el cliente

		
		while (result.next()) {
		    
		    int id = result.getInt("id");
		    String titulo = result.getString("titulo");
		    String director = result.getString("director");
		    double precio = result.getDouble("precio");
		    enviar.println("Película encontrada:" + "ID: " + id + "Título: " + titulo + "Director: " + director + "Precio: " + precio);
		}
		enviar.println("FIN");
	}

}