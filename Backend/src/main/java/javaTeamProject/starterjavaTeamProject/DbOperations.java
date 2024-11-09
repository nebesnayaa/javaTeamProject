package javaTeamProject.starterjavaTeamProject;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.core.json.JsonObject;

public class DbOperations {
	private static final Dotenv dotenv = Dotenv.load();

	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection(dotenv.get("DB_URL"), dotenv.get("USER"), dotenv.get("PASSWORD"));
	}

	private static void executeUpdate(String sql, String successMessage) {
		try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
			stmt.executeUpdate(sql);
			System.out.println(successMessage);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void insertData(String tableName, String[] fields, String[] params) {
		if (fields.length != params.length) {
			throw new IllegalArgumentException("Количество полей и значений должно совпадать.");
		}

		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO ").append(tableName).append(" (");

		for (int i = 0; i < fields.length; i++) {
			sql.append(fields[i]);
			if (i < fields.length - 1) {
				sql.append(", ");
			}
		}

		sql.append(") VALUES (");

		for (int i = 0; i < params.length; i++) {
			sql.append("'").append(params[i]).append("'");
			if (i < params.length - 1) {
				sql.append(", ");
			}
		}

		sql.append(")");

		executeUpdate(sql.toString(), "Данные успешно добавлены в таблицу.");
	}

	// Метод для аутентификации пользователя
	public static boolean authenticateUser(String username, String password) {
		String sql = "SELECT * FROM " + dotenv.get("USERS_TABLE_NAME") + " WHERE username = ? AND password = ?";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setString(1, username);
			stmt.setString(2, password);

			try (ResultSet rs = stmt.executeQuery()) {
				return rs.next(); // Возвращает true, если пользователь найден
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	// Метод для получения всех пользователей
	public static List<JsonObject> getAllUsers() {
		String sql = "SELECT * FROM " + dotenv.get("USERS_TABLE_NAME");
		List<JsonObject> users = new ArrayList<>();

		try (Connection conn = getConnection();
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				JsonObject user = new JsonObject().put("id", rs.getInt("id")).put("username", rs.getString("username"))
						.put("email", rs.getString("email"));
				users.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return users;
	}

	// Метод для получения пользователя по ID
	public static JsonObject getUserById(int id) {
		String sql = "SELECT * FROM " + dotenv.get("USERS_TABLE_NAME") + " WHERE id = ?";
		try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
			stmt.setInt(1, id);

			try (ResultSet rs = stmt.executeQuery()) {
				if (rs.next()) {
					return new JsonObject().put("id", rs.getInt("id")).put("username", rs.getString("username"))
							.put("email", rs.getString("email"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
