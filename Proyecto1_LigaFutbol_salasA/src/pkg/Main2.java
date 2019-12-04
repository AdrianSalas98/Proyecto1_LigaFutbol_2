package pkg;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Main2 {

	public static void main(String[] args) {
		Main.obtenerRutas();
		procedureCrearTablas();
		procedureInsertarEquipos();
		procedureInsertarJugadores();
		procedureInsertarClasificacion();
		procedureInsertarPartidos();
		simularJornada();
		obtenerClasificacion();
	}

	public static Connection conectarDB() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/futbol_db", "root", "");
		} catch (Exception e) {
			System.out.print(e);
		}

		return conn;
	}

	public static void procedureCrearTablas() {
		Connection con = conectarDB();
		CallableStatement cs = null;
		try {
			cs = con.prepareCall("{call CrearTablas}");
			cs.execute();
		} catch (SQLException e) {

		} finally {
			if (cs != null) {
				try {
					cs.close();
				} catch (SQLException e) {

				}
			}
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	public static void procedureInsertarEquipos() {
		Connection con = conectarDB();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(Main.rutaEquipos));

			String sql1 = "SELECT * FROM EQUIPOS";
			PreparedStatement ps = con.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();

			if (rs.next() == false) {
				CallableStatement callableStatement = con.prepareCall("{call InsertarEquipos(?,?,?)}");

				NodeList equipo = doc.getElementsByTagName("Equipo");
				for (int i = 0; i < equipo.getLength(); i++) {
					NodeList nl1 = doc.getElementsByTagName("ID_Equipo");
					callableStatement.setInt(1, Integer.parseInt(nl1.item(i).getTextContent()));
					NodeList nl2 = doc.getElementsByTagName("Nombre_Equipo");
					callableStatement.setString(2, nl2.item(i).getTextContent());
					callableStatement.setInt(3, (int) (Math.random() * 51 + 48));
					callableStatement.executeUpdate();
				}
			}

		} catch (SQLException e) {
			System.err.println(e);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	public static void procedureInsertarJugadores() {
		Connection con = conectarDB();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document doc = builder.parse(new File(Main.rutaJugadores));

			String sql1 = "SELECT * FROM JUGADORES";
			PreparedStatement ps = con.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();

			if (rs.next() == false) {
				CallableStatement callableStatement = con.prepareCall("{call InsertarJugadores(?,?,?,?,?)}");

				NodeList jugador = doc.getElementsByTagName("Jugador");

				for (int i = 0; i < jugador.getLength(); i++) {
					NodeList nl1 = doc.getElementsByTagName("ID_Jugador");
					callableStatement.setInt(1, Integer.parseInt(nl1.item(i).getTextContent()));
					NodeList nl2 = doc.getElementsByTagName("Nombre_Jugador");
					callableStatement.setString(2, nl2.item(i).getTextContent());
					NodeList nl3 = doc.getElementsByTagName("Posicion");
					callableStatement.setString(3, nl3.item(i).getTextContent());
					NodeList nl4 = doc.getElementsByTagName("ID_Equipo");
					callableStatement.setInt(4, Integer.parseInt(nl4.item(i).getTextContent()));
					NodeList nl5 = doc.getElementsByTagName("Nombre_Equipo");
					callableStatement.setString(5, nl5.item(i).getTextContent());

					callableStatement.executeUpdate();
				}
			}

		} catch (SQLException e) {
			System.err.println(e);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	public static void procedureInsertarClasificacion() {
		Connection con = conectarDB();
		try {
			String sql1 = "SELECT * FROM CLASIFICACION";
			String sql2 = "SELECT COUNT(Nombre_Equipo) FROM EQUIPOS";
			String sql3 = "SELECT * FROM EQUIPOS";

			PreparedStatement ps = con.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();

			if (rs.next() == false) {
				CallableStatement callableStatement = con.prepareCall("{call InsertarClasificacion(?,?,?,?,?,?)}");

				ps = con.prepareStatement(sql2);
				rs = ps.executeQuery();
				int numEquipos = 0;
				if (rs.next()) {
					numEquipos = rs.getInt(1);
				}

				String[] equipos = new String[numEquipos];

				ps = con.prepareStatement(sql3);
				rs = ps.executeQuery();

				int num = 1;

				for (int i = 1; i <= equipos.length; i++) {
					while (rs.next()) {

						equipos[i] = rs.getString(2);
						callableStatement.setInt(1, num);
						callableStatement.setString(2, equipos[i].toString());
						callableStatement.setInt(3, 0);
						callableStatement.setInt(4, 0);
						callableStatement.setInt(5, 0);
						callableStatement.setInt(6, 0);

						callableStatement.executeUpdate();

						num++;
					}
				}
			}

		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	public static void procedureInsertarPartidos() {
		Connection con = conectarDB();
		try {
			String sql1 = "SELECT * FROM PARTIDOS";

			PreparedStatement ps = con.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();

			String[] arrayJornada = new String[12];
			File f1 = new File("..\\Proyecto1_LigaFutbol_salasA\\jornada1.txt");

			Scanner lector = new Scanner(f1);
			int contador = 0;

			while (lector.hasNextLine()) {
				String linea = lector.nextLine();
				String[] sep = linea.split(" ");
				for (int i = 0; i < sep.length; i++) {
					arrayJornada[contador] = sep[i];
					contador++;
				}
			}

			if (rs.next() == false) {
				CallableStatement callableStatement = con.prepareCall("{call InsertarPartidos(?,?,?,?)}");

				ps = con.prepareStatement(sql1);
				rs = ps.executeQuery();

				if (rs.next() == false) {

					callableStatement.setString(1, arrayJornada[0]);
					callableStatement.setInt(2, Integer.parseInt(arrayJornada[1]));
					callableStatement.setString(3, arrayJornada[2]);
					callableStatement.setInt(4, Integer.parseInt(arrayJornada[3]));

					callableStatement.executeUpdate();

					callableStatement.setString(1, arrayJornada[4]);
					callableStatement.setInt(2, Integer.parseInt(arrayJornada[5]));
					callableStatement.setString(3, arrayJornada[6]);
					callableStatement.setInt(4, Integer.parseInt(arrayJornada[7]));

					callableStatement.executeUpdate();

					callableStatement.setString(1, arrayJornada[8]);
					callableStatement.setInt(2, Integer.parseInt(arrayJornada[9]));
					callableStatement.setString(3, arrayJornada[10]);
					callableStatement.setInt(4, Integer.parseInt(arrayJornada[11]));

					callableStatement.executeUpdate();
				}
			}

		} catch (SQLException e) {
			System.err.println(e);
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	public static void simularJornada() {
		Connection con = conectarDB();
		try {

			String sql1 = "SELECT * FROM PARTIDOS";

			PreparedStatement ps = con.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();

			String[] jornada1 = new String[12];
			int contador = 0;

			while (rs.next()) {
				jornada1[contador] = rs.getString(1);
				jornada1[contador + 1] = rs.getString(2);
				jornada1[contador + 2] = rs.getString(3);
				jornada1[contador + 3] = rs.getString(4);

				contador = contador + 4;

			}

			String query = "UPDATE CLASIFICACION SET VICTORIAS = ?, DERROTAS = ?, EMPATES = ?, PUNTOS = ?"
					+ " WHERE NOMBRE_EQUIPO = ?";

			ps = con.prepareStatement(query);

			if (Integer.parseInt(jornada1[1]) > Integer.parseInt(jornada1[3])) {
				ps.setInt(1, 1);
				ps.setInt(2, 0);
				ps.setInt(3, 0);
				ps.setInt(4, 3);
				ps.setString(5, jornada1[0]);
				ps.executeUpdate();

				ps.setInt(1, 0);
				ps.setInt(2, 1);
				ps.setInt(3, 0);
				ps.setInt(4, 0);
				ps.setString(5, jornada1[2]);
				ps.executeUpdate();
			}

			if (Integer.parseInt(jornada1[1]) < Integer.parseInt(jornada1[3])) {
				ps.setInt(1, 0);
				ps.setInt(2, 1);
				ps.setInt(3, 0);
				ps.setInt(4, 0);
				ps.setString(5, jornada1[0]);
				ps.executeUpdate();

				ps.setInt(1, 1);
				ps.setInt(2, 0);
				ps.setInt(3, 0);
				ps.setInt(4, 3);
				ps.setString(5, jornada1[2]);
				ps.executeUpdate();
			}

			if (Integer.parseInt(jornada1[1]) == Integer.parseInt(jornada1[3])) {
				ps.setInt(1, 0);
				ps.setInt(2, 0);
				ps.setInt(3, 1);
				ps.setInt(4, 1);
				ps.setString(5, jornada1[0]);
				ps.executeUpdate();

				ps.setInt(1, 0);
				ps.setInt(2, 0);
				ps.setInt(3, 1);
				ps.setInt(4, 1);
				ps.setString(5, jornada1[2]);
				ps.executeUpdate();
			}

			if (Integer.parseInt(jornada1[5]) > Integer.parseInt(jornada1[7])) {
				ps.setInt(1, 1);
				ps.setInt(2, 0);
				ps.setInt(3, 0);
				ps.setInt(4, 3);
				ps.setString(5, jornada1[4]);
				ps.executeUpdate();

				ps.setInt(1, 0);
				ps.setInt(2, 1);
				ps.setInt(3, 0);
				ps.setInt(4, 0);
				ps.setString(5, jornada1[6]);
				ps.executeUpdate();
			}

			if (Integer.parseInt(jornada1[5]) < Integer.parseInt(jornada1[7])) {
				ps.setInt(1, 0);
				ps.setInt(2, 1);
				ps.setInt(3, 0);
				ps.setInt(4, 0);
				ps.setString(5, jornada1[4]);
				ps.executeUpdate();

				ps.setInt(1, 1);
				ps.setInt(2, 0);
				ps.setInt(3, 0);
				ps.setInt(4, 3);
				ps.setString(5, jornada1[6]);
				ps.executeUpdate();
			}

			if (Integer.parseInt(jornada1[5]) == Integer.parseInt(jornada1[7])) {
				ps.setInt(1, 0);
				ps.setInt(2, 0);
				ps.setInt(3, 1);
				ps.setInt(4, 1);
				ps.setString(5, jornada1[4]);
				ps.executeUpdate();

				ps.setInt(1, 0);
				ps.setInt(2, 0);
				ps.setInt(3, 1);
				ps.setInt(4, 1);
				ps.setString(5, jornada1[6]);
				ps.executeUpdate();
			}

			if (Integer.parseInt(jornada1[9]) > Integer.parseInt(jornada1[11])) {
				ps.setInt(1, 1);
				ps.setInt(2, 0);
				ps.setInt(3, 0);
				ps.setInt(4, 3);
				ps.setString(5, jornada1[8]);
				ps.executeUpdate();

				ps.setInt(1, 0);
				ps.setInt(2, 1);
				ps.setInt(3, 0);
				ps.setInt(4, 0);
				ps.setString(5, jornada1[10]);
				ps.executeUpdate();
			}

			if (Integer.parseInt(jornada1[9]) < Integer.parseInt(jornada1[11])) {
				ps.setInt(1, 0);
				ps.setInt(2, 1);
				ps.setInt(3, 0);
				ps.setInt(4, 0);
				ps.setString(5, jornada1[8]);
				ps.executeUpdate();

				ps.setInt(1, 1);
				ps.setInt(2, 0);
				ps.setInt(3, 0);
				ps.setInt(4, 3);
				ps.setString(5, jornada1[10]);
				ps.executeUpdate();
			}

			if (Integer.parseInt(jornada1[9]) == Integer.parseInt(jornada1[11])) {
				ps.setInt(1, 0);
				ps.setInt(2, 0);
				ps.setInt(3, 1);
				ps.setInt(4, 1);
				ps.setString(5, jornada1[8]);
				ps.executeUpdate();

				ps.setInt(1, 0);
				ps.setInt(2, 0);
				ps.setInt(3, 1);
				ps.setInt(4, 1);
				ps.setString(5, jornada1[10]);
				ps.executeUpdate();
			}

		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	public static void obtenerClasificacion() {
		Connection con = conectarDB();
		try {

			String sql1 = "SELECT * FROM CLASIFICACION ORDER BY PUNTOS DESC";

			PreparedStatement ps = con.prepareStatement(sql1);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				System.out.print(rs.getObject(2) + " || " + " Victorias: " + rs.getObject(3) + " Derrotas: " + 
			rs.getObject(4) + " Empates: " + rs.getObject(5) + " Puntos: " + rs.getObject(6));
				System.out.println();
			}

		} catch (SQLException e) {
			System.err.println(e);
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {

				}
			}
		}
	}

}
