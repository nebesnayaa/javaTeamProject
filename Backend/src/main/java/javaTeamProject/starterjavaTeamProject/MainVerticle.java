package javaTeamProject.starterjavaTeamProject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.BodyHandler;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.List;

public class MainVerticle extends AbstractVerticle {
	private static final Dotenv dotenv = Dotenv.load();

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		Router router = Router.router(vertx);

		  router.route().handler(ctx -> {
		        ctx.response().putHeader("Access-Control-Allow-Origin", "http://localhost:3000");
		        ctx.response().putHeader("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
		        ctx.response().putHeader("Access-Control-Allow-Headers", "Content-Type, Authorization");
		        ctx.response().putHeader("Access-Control-Allow-Credentials", "true"); // Allow credentials if needed

		        if (ctx.request().method().name().equals("OPTIONS")) {
		            ctx.response().setStatusCode(204).end(); // Immediately respond to OPTIONS preflight requests
		        } else {
		            ctx.next(); // Pass to the next handler for other requests
		        }
		    });
		  
		router.route().handler(BodyHandler.create());

		// router.get("/").handler(this::handleRoot);
		router.post("/signup").handler(BodyHandler.create()).handler(this::addUser);
		router.post("/signin").handler(BodyHandler.create()).handler(this::signIn);
		router.post("/logout").handler(this::logout);
		router.get("/users").handler(this::getAllUsers);
		router.get("/users/:id").handler(this::getUserById);

		vertx.createHttpServer().requestHandler(router).listen(8888).onComplete(http -> {
			if (http.succeeded()) {
				startPromise.complete();
				System.out.println("HTTP server started on port 8888");
			} else {
				startPromise.fail(http.cause());
			}
		});
	}

	private void handleRoot(RoutingContext context) {
		context.response().putHeader("content-type", "text/plain").end("Welcome to the Vert.x server!");
	}

	// Add user (signup) handler
	@SuppressWarnings("deprecation")
	private void addUser(RoutingContext context) {
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
	private void signIn(RoutingContext context) {
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
	private void logout(RoutingContext context) {
		// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!session or token
		context.response().setStatusCode(200).putHeader("content-type", "application/json").end("Logout successful");
	}

	// Get all users handler
	private void getAllUsers(RoutingContext context) {
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
	private void getUserById(RoutingContext context) {
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
