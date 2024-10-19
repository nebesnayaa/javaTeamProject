package javaTeamProject.starterjavaTeamProject;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    // Создаем роутер
    Router router = Router.router(vertx);

    // Настраиваем маршруты
    router.get("/").handler(this::handleRoot);

    // Создаем HTTP сервер и назначаем роутер
    vertx.createHttpServer().requestHandler(router).listen(8888).onComplete(http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }

  // Обработчик для корневого маршрута "/"
  private void handleRoot(RoutingContext context) {
    context.response()
      .putHeader("content-type", "text/plain")
      .end("Welcome to the Vert.x server!");
  }

}
