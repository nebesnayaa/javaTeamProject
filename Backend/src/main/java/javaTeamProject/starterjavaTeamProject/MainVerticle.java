package javaTeamProject.starterjavaTeamProject;

import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import services.UserService;

public class MainVerticle extends AbstractVerticle {

	private final UserService userService;

	public MainVerticle(UserService userService) {
	
	if(userService != null) System.out.println("USER SERVICE HAS BEEN RECEIVED");
		this.userService = userService;
	}

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);

		router.get("/users/one/:id").handler(context -> {
			System.out.println("Received request: " + context.request().absoluteURI());
			try {
				Integer id = Integer.parseInt(context.pathParam("id"));
				System.out.println("Parsed ID: " + id);

				userService.findUserById(id).onSuccess(result -> {
					System.out.println("Service call successful for ID: " + id);
					if (result.isPresent()) {
						JsonObject body = JsonObject.mapFrom(result.get());
						System.out.println("User found: " + body.encode());
						context.response().setStatusCode(200).end(body.encode());
					} else {
						System.out.println("User not found for ID: " + id);
						context.response().setStatusCode(404).end();
					}
				}).onFailure(err -> {
					System.out.println("Error during service call: " + err.getMessage());
					context.response().setStatusCode(500).end(err.getMessage());
				});
			} catch (NumberFormatException e) {
				System.out.println("Invalid ID format: " + context.pathParam("id"));
				context.response().setStatusCode(400).end("Invalid ID format");
			}
		});

		JsonObject config = config();
		Dotenv dotenv = Dotenv.load();
		Integer port = 8080;// Integer.parseInt(dotenv.get("PORT"), 8080);//config.getInteger("port");

		server.requestHandler(router).listen(port).onSuccess(result -> startPromise.complete())
				.onFailure(err -> startPromise.fail(err));
	}

}
