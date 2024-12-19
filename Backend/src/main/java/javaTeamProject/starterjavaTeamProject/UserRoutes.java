package javaTeamProject.starterjavaTeamProject;

import java.util.List;

import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class UserRoutes {
	private static final Dotenv dotenv = Dotenv.load();
	
	// Add user (signup) handler
	@SuppressWarnings("deprecation")
	public static void addUser(RoutingContext context) {
		JsonObject body = context.getBodyAsJson();
		if (body == null) {
			context.response().setStatusCode(400).putHeader("content-type", "application/json")
					.end("Invalid data format");
			return;
		}

		String username = body.getString("username");
		String password = body.getString("password");
		String email = body.getString("email");

		if (username == null || password == null || email == null) {
			context.response().setStatusCode(400).putHeader("content-type", "application/json")
					.end("All fields are required");
			return;
		}

		String tableName = dotenv.get("USERS_TABLE_NAME");
		String[] fields = { "username", "password", "email" };
		String[] params = { username, password, email };

		try {
			DbOperations.insertData(tableName, fields, params);
			context.response().setStatusCode(201).putHeader("content-type", "application/json")
					.end("User successfully added");
		} catch (Exception e) {
			e.printStackTrace();
			context.response().setStatusCode(500).putHeader("content-type", "application/json")
					.end("Error adding user");
		}
	}

	// Sign-in handler
	@SuppressWarnings("deprecation")
	public static void signIn(RoutingContext context) {
		JsonObject body = context.getBodyAsJson();
		String username = body.getString("username");
		String password = body.getString("password");

		if (username == null || password == null) {
			context.response().setStatusCode(400).putHeader("content-type", "application/json")
					.end("Username and password are required");
			return;
		}

		try {
			boolean isAuthenticated = DbOperations.authenticateUser(username, password);
			if (isAuthenticated) {
				// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!session or token
				context.response().setStatusCode(200).putHeader("content-type", "application/json")
						.end("Login successful");
			} else {
				context.response().setStatusCode(401).putHeader("content-type", "application/json")
						.end("Invalid credentials");
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.response().setStatusCode(500).putHeader("content-type", "application/json")
					.end("Error during sign-in");
		}
	}

	// Logout handler
	public static void logout(RoutingContext context) {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!session or token
		context.response().setStatusCode(200).putHeader("content-type", "application/json").end("Logout successful");
	}

	// Get all users handler
	public static void getAllUsers(RoutingContext context) {
		try {
			List<JsonObject> users = DbOperations.getAllUsers();
			context.response().setStatusCode(200).putHeader("content-type", "application/json").end(users.toString());
		} catch (Exception e) {
			e.printStackTrace();
			context.response().setStatusCode(500).putHeader("content-type", "application/json")
					.end("Error retrieving users");
		}
	}

	// Get user by ID handler
	public static void getUserById(RoutingContext context) {
		String userId = context.pathParam("id");

		try {
			JsonObject user = DbOperations.getUserById(Integer.parseInt(userId));
			if (user != null) {
				context.response().setStatusCode(200).putHeader("content-type", "application/json").end(user.encode());
			} else {
				context.response().setStatusCode(404).putHeader("content-type", "application/json")
						.end("User not found");
			}
		} catch (Exception e) {
			e.printStackTrace();
			context.response().setStatusCode(500).putHeader("content-type", "application/json")
					.end("Error retrieving user");
		}
	}
}
