package pkg;

import java.io.File;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Main {

	static final String rutaConfig = "..\\Proyecto1_LigaFutbol_salasA\\Config.xml";
	static String rutaEquipos;
	static String rutaJugadores;
	static String rutaDB;

	public static void main(String[] args) {
		obtenerRutas();
		crearTablaEquipos();
		crearTablaJugadores();
		insertarEquipos();
		insertarJugadores();
		menu();

	}

	public static void obtenerRutas() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(rutaConfig));

			NodeList rutas = doc.getElementsByTagName("Ruta");

			for (int i = 0; i < rutas.getLength(); i++) {
				Element element = (Element) rutas.item(i);
				if (element.hasAttribute("equipos")) {
					rutaEquipos = (element.getAttribute("equipos"));
				}
				if (element.hasAttribute("jugadores")) {
					rutaJugadores = (element.getAttribute("jugadores"));
				}
				if (element.hasAttribute("bd")) {
					rutaDB = (element.getAttribute("bd"));
				}

			}

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void connectar() {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(rutaDB);
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		} finally {
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException ex) {
				System.out.println(ex.getMessage());
			}
		}
	}

	public static void crearTablaEquipos() {
		String sql = "CREATE TABLE IF NOT EXISTS Equipos (ID_Equipo integer PRIMARY KEY, Nombre_Equipo text(50));";

		try {
			Connection conn = DriverManager.getConnection(rutaDB);
			Statement stmt = conn.createStatement();
			stmt.execute(sql);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void crearTablaJugadores() {
		String sql = "CREATE TABLE IF NOT EXISTS Jugadores (ID_Jugador integer PRIMARY KEY, Nombre_Jugador text(50), "
				+ "Posicion text(50), ID_Equipo integer, Nombre_Equipo text(50),"
				+ " FOREIGN KEY (ID_Equipo) REFERENCES Equipos(ID_Equipo),"
				+ " FOREIGN KEY (Nombre_Equipo) REFERENCES Equipos(Nombre_Equipo));";

		try {
			Connection conn = DriverManager.getConnection(rutaDB);
			Statement stmt = conn.createStatement();
			stmt.execute(sql);

		} catch (Exception e) {
			System.out.println(e);
		}
	}

	public static void insertarEquipos() {
		Connection conn = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(rutaEquipos));

			String sql1 = "SELECT * FROM EQUIPOS";
			String sql2 = "INSERT INTO Equipos(ID_Equipo,Nombre_Equipo) VALUES(?,?)";

			conn = DriverManager.getConnection(rutaDB);
			PreparedStatement ps = conn.prepareStatement(sql1);

			ResultSet rs = ps.executeQuery();

			if (rs.next() == false) {
				ps = conn.prepareStatement(sql2);

				NodeList equipo = doc.getElementsByTagName("Equipo");

				for (int i = 0; i < equipo.getLength(); i++) {
					NodeList nl1 = doc.getElementsByTagName("ID_Equipo");
					ps.setInt(1, Integer.parseInt(nl1.item(i).getTextContent()));
					NodeList nl2 = doc.getElementsByTagName("Nombre_Equipo");
					ps.setString(2, nl2.item(i).getTextContent());

					ps.executeUpdate();
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
	}

	public static void insertarJugadores() {
		Connection conn = null;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(rutaJugadores));

			String sql1 = "SELECT * FROM JUGADORES";
			String sql2 = "INSERT INTO Jugadores(ID_Jugador,Nombre_Jugador,Posicion,ID_Equipo,Nombre_Equipo) VALUES(?,?,?,?,?)";

			conn = DriverManager.getConnection(rutaDB);
			PreparedStatement ps = conn.prepareStatement(sql1);

			ResultSet rs = ps.executeQuery();

			if (rs.next() == false) {
				ps = conn.prepareStatement(sql2);

				NodeList jugador = doc.getElementsByTagName("Jugador");

				for (int i = 0; i < jugador.getLength(); i++) {
					NodeList nl1 = doc.getElementsByTagName("ID_Jugador");
					ps.setInt(1, Integer.parseInt(nl1.item(i).getTextContent()));
					NodeList nl2 = doc.getElementsByTagName("Nombre_Jugador");
					ps.setString(2, nl2.item(i).getTextContent());
					NodeList nl3 = doc.getElementsByTagName("Posicion");
					ps.setString(3, nl3.item(i).getTextContent());
					NodeList nl4 = doc.getElementsByTagName("ID_Equipo");
					ps.setInt(4, Integer.parseInt(nl4.item(i).getTextContent()));
					NodeList nl5 = doc.getElementsByTagName("Nombre_Equipo");
					ps.setString(5, nl5.item(i).getTextContent());

					ps.executeUpdate();
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
	}

	public static void mostrarEquiposLiga() {
		Connection conn = null;
		try {

			String sql1 = "SELECT * FROM EQUIPOS";

			conn = DriverManager.getConnection(rutaDB);
			PreparedStatement ps = conn.prepareStatement(sql1);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.print("ID: " + rs.getObject(1) + " - " + rs.getObject(2));
				System.out.println();
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
	}

	public static void mostrarJugadoresPorEquipo() {
		Connection conn = null;
		try {
			System.out.println("Introduce el nombre del equipo: ");
			Scanner lector = new Scanner(System.in);
			String nEquipo = lector.nextLine();

			String sql1 = "SELECT * FROM JUGADORES WHERE Nombre_Equipo LIKE '" + nEquipo + "'";

			conn = DriverManager.getConnection(rutaDB);
			PreparedStatement ps = conn.prepareStatement(sql1);

			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.print(rs.getObject(1) + " " + rs.getObject(2) + " " + rs.getObject(3) + " " + rs.getObject(4)
						+ " " + rs.getObject(5));
				System.out.println();
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
	}

	public static void validarJugadores() {
		Connection conn = null;
		try {

			String sql1 = "SELECT COUNT(Nombre_Equipo) FROM EQUIPOS";

			conn = DriverManager.getConnection(rutaDB);
			PreparedStatement ps = conn.prepareStatement(sql1);

			ResultSet rs = ps.executeQuery();

			int numEquipos = rs.getInt(1);
			System.out.println(numEquipos);

			String sql2 = "SELECT COUNT(ID_Jugador) FROM JUGADORES WHERE ID_EQUIPO = ?";

			ps = conn.prepareStatement(sql2);

			for (int i = 1; i < numEquipos; i++) {
				ps.setInt(1, i);
				rs = ps.executeQuery();
				int countJugadores = rs.getInt(1);
				if (countJugadores < 5) {
					System.err.println("El Equipo con ID " + i + " tiene menos de 5 Jugadores.");
				}
			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
	}

	public static void borrarJugador() {
		Connection conn = null;
		System.out.println("Introduce la id del jugador a borrar: ");
		Scanner lector = new Scanner(System.in);
		int idJugador = lector.nextInt();
		try {

			String sql1 = "DELETE FROM JUGADORES WHERE ID_Jugador = ?";

			conn = DriverManager.getConnection(rutaDB);
			PreparedStatement ps = conn.prepareStatement(sql1);
			ps.setInt(1, idJugador);

			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
	}

	public static void introducirJugador() {
		Connection conn = null;
		Scanner lectorString = new Scanner(System.in);
		Scanner lectorInt = new Scanner(System.in);

		System.out.println("Introduce la id del jugador: ");
		int idJugador = lectorInt.nextInt();
		System.out.println("Introduce el nombre del jugador: ");
		String nJugador = lectorString.nextLine();
		System.out.println("Introduce la posicion del jugador: ");
		String posJugador = lectorString.nextLine();
		System.out.println("Introduce el id del equipo: ");
		int idEquipo = lectorInt.nextInt();
		System.out.println("Introduce el nombre del equipo: ");
		String nEquipo = lectorString.nextLine();

		try {
			String sql1 = "INSERT INTO JUGADORES (ID_Jugador,Nombre_Jugador,Posicion,ID_Equipo,Nombre_Equipo) "
					+ "VALUES (?,?,?,?,?);";

			conn = DriverManager.getConnection(rutaDB);
			PreparedStatement ps = conn.prepareStatement(sql1);
			ps.setInt(1, idJugador);
			ps.setString(2, nJugador);
			ps.setString(3, posJugador);
			ps.setInt(4, idEquipo);
			ps.setString(5, nEquipo);

			ps.executeUpdate();

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
	}

	public static void intercambiarJugadores() {
		Connection conn = null;
		Scanner lector = new Scanner(System.in);
		System.out.println("Introduce la id del primer jugador: ");
		int idUno = lector.nextInt();
		System.out.println("Introduce la id del segundo jugador: ");
		int idDos = lector.nextInt();

		try {
			String sql1 = "SELECT * FROM JUGADORES WHERE ID_JUGADOR = ?";
			conn = DriverManager.getConnection(rutaDB);
			PreparedStatement ps = conn.prepareStatement(sql1);
			ps.setInt(1, idUno);

			ResultSet rs = ps.executeQuery();

			int idEquipo1 = (int) rs.getObject(4);
			String nombreEquipo1 = (String) rs.getObject(5);

			ps.setInt(1, idDos);
			rs = ps.executeQuery();

			int idEquipo2 = (int) rs.getObject(4);
			String nombreEquipo2 = (String) rs.getObject(5);

			String queryUpdate1 = "UPDATE JUGADORES SET ID_EQUIPO = ?, NOMBRE_EQUIPO = ? WHERE ID_JUGADOR = ?;";
			ps = conn.prepareStatement(queryUpdate1);

			ps.setObject(1, idEquipo2);
			ps.setObject(2, nombreEquipo2);
			ps.setObject(3, idUno);

			ps.executeUpdate();

			ps.setObject(1, idEquipo1);
			ps.setObject(2, nombreEquipo1);
			ps.setObject(3, idDos);

			ps.executeUpdate();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}
		}
	}

	public static void generarFicherosTexto() {
		Connection conn = null;
		File file = null;
		PrintWriter pw = null;

		try {
			String sql1 = "SELECT COUNT(Nombre_Equipo) FROM EQUIPOS";

			conn = DriverManager.getConnection(rutaDB);
			PreparedStatement ps = conn.prepareStatement(sql1);

			ResultSet rs = ps.executeQuery();

			int numEquipos = rs.getInt(1);

			String sql2 = "SELECT * FROM JUGADORES WHERE ID_EQUIPO = ?";
			ps = conn.prepareStatement(sql2);

			for (int i = 1; i <= numEquipos; i++) {

				ps.setInt(1, i);
				rs = ps.executeQuery();
				file = new File("Equipo" + i + ".txt");
				pw = new PrintWriter(file);

				while (rs.next()) {
					pw.println(rs.getObject(1) + " " + rs.getObject(2) + " " + rs.getObject(3) + " " + rs.getObject(4)
							+ " " + rs.getObject(5));
					pw.println();
				}
				
				pw.close();

			}

		} catch (Exception e) {
			System.out.println(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {
					System.out.println(e);
				}
			}

			if (pw != null) {
				pw.close();
			}
		}
	}

	public static void menu() {
		Scanner sn = new Scanner(System.in);
		boolean salir = false;
		int opcion;

		while (!salir) {
			System.out.println();
			System.out.println("1. Mostrar Equipos de la Liga");
			System.out.println("2. Mostrar Jugadores por Equipos");
			System.out.println("3. Validar Jugadores");
			System.out.println("4. Introducir nuevo Jugador");
			System.out.println("5. Borrar Jugador existente");
			System.out.println("6. Intercambiar Jugadores entre Equipos");
			System.out.println("7. Guardar información de los Equipos en ficheros de texto");
			System.out.println("8. Salir");
			System.out.println();

			try {

				System.out.println("Escribe una de las opciones");
				opcion = sn.nextInt();

				switch (opcion) {
				case 1:
					mostrarEquiposLiga();
					break;
				case 2:
					mostrarJugadoresPorEquipo();
					break;
				case 3:
					validarJugadores();
					break;
				case 4:
					introducirJugador();
					break;
				case 5:
					borrarJugador();
					break;
				case 6:
					intercambiarJugadores();
					break;
				case 7:
					generarFicherosTexto();
					break;
				case 8:
					salir = true;
					break;
				default:
					System.out.println("Opciones validas entre 1 y 8");
				}
			} catch (InputMismatchException e) {
				System.out.println("Debes insertar un número");
				sn.next();
			}

		}

	}

}
