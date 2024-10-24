package javaTeamProject.starterjavaTeamProject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.handler.BodyHandler;


public class MainVerticle extends AbstractVerticle {
	private static final Dotenv dotenv = Dotenv.load();

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Router router = Router.router(vertx);

    router.get("/").handler(this::handleRoot);
    router.post("/signup").handler(BodyHandler.create()).handler(this::addUser);

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
    context.response()
      .putHeader("content-type", "text/plain")
      .end("Welcome to the Vert.x server!");
  }
  

  private void  addUser(RoutingContext context) {
	  
	  //таблица выглядит как-то так. !!!!Создавать в mysql. Оракл мне не пошёл как-то
	  //Table: users
	  //Columns:
	  //id int AI PK 
	  //username varchar(50) 
	  //password varchar(50) 
	  //email varchar(45)
	  System.out.println("BODY");
	
	  JsonObject body = context.getBodyAsJson();
	  System.out.println(body);
      
      if (body == null) {
          context.response()
                 .setStatusCode(400)
                 .putHeader("content-type", "application/json")
                 .end("Неверный формат данных");
          return;
      }

      String username = body.getString("username");
      String password = body.getString("password");
      String email = body.getString("email");

      if (username == null || password == null || email == null) {
          context.response()
                 .setStatusCode(400)
                 .putHeader("content-type", "application/json")
                 .end("Все поля обязательны");
          return;
      }

      String tableName = dotenv.get("USERS_TABLE_NAME");
      String[] fields = {"username", "password", "email"};
      String[] params = {username, password, email};

      try {
          DbOperations.insertData(tableName, fields, params);
          context.response()
                 .setStatusCode(201)
                 .putHeader("content-type", "application/json")
                 .end( "Пользователь успешно добавлен");
      } catch (Exception e) {
          e.printStackTrace();
          context.response()
                 .setStatusCode(500)
                 .putHeader("content-type", "application/json")
                 .end( "Ошибка при добавлении пользователя");
      }
  }


}
