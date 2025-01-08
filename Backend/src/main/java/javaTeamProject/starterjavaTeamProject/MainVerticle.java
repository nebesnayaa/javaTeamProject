package javaTeamProject.starterjavaTeamProject;

import java.util.Objects;
import java.util.Properties;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.vertx.core.json.Json;
import io.vertx.core.json.jackson.DatabindCodec;
import org.hibernate.cfg.Configuration;
import org.hibernate.reactive.provider.ReactiveServiceRegistryBuilder;
import org.hibernate.reactive.stage.Stage;
import org.hibernate.service.ServiceRegistry;

import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Promise;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import model.Resume;
import model.User;
import repository.ResumeRepository;
import repository.UserRepository;
import services.ResumeService;
import services.UserService;

public class MainVerticle extends AbstractVerticle {

	private final UserService userService ;
	private final ResumeService resumeService;

	public MainVerticle(UserService userService, ResumeService resumeService) {

		this.userService = userService;
		this.resumeService =resumeService;
	}

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
    /*
    * Enables mapping Optional
    * Error sample
    * java.lang.IllegalArgumentException: Java 8 optional type java.util.Optional<model.UserDTO> not supported by default: add Module "com.fasterxml.jackson.datatype:jackson-datatype-jdk8" to enable handling
     */
    DatabindCodec.mapper().registerModule(new Jdk8Module());
    DatabindCodec.prettyMapper().registerModule(new Jdk8Module());

    if(this.userService == null){
      System.out.println("userService is null");
    }

		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);

		router.get("/").handler(context ->{
			context.response().setStatusCode(200).send("Hello from server!");
		});

    router.post("/users/signup").handler(context -> {
      if(!context.body().isEmpty()){

      }
      else {
        context.response().setStatusCode(400).end("Body is empty");
      }
    });

		router.get("/users/one/:id").handler(context -> {
			try {
				Integer id = Integer.parseInt(context.pathParam("id"));

        userService.findUserById(id).onSuccess(result -> {
					if (result.isPresent()) {
						JsonObject body = JsonObject.mapFrom(result.get());
						context.response().setStatusCode(200).end(body.encode());
					} else {
						context.response().setStatusCode(404).end();
					}
				}).onFailure(err -> {
					context.response().setStatusCode(500).end(err.getMessage());
				});
			} catch (NumberFormatException e) {
				context.response().setStatusCode(400).end("Invalid ID format");
			}
		});

		router.get("/resumes/userId/:userId").handler(context -> {
      try {
        Integer userId = Integer.parseInt(context.pathParam("userId"));

        resumeService.findResumeByUserId(userId).
          onSuccess(result -> {

            if(result.resumes().isEmpty()){
              context.response().setStatusCode(404).end("No resumes found.");
            }
            else{
              JsonObject body = JsonObject.mapFrom(result);
              context.response().setStatusCode(200).end(body.encode());
            }
          })
          .onFailure(err -> {
            context.response().setStatusCode(500).end(err.getMessage());
          });
      }
      catch(NumberFormatException e){
        context.response().setStatusCode(400).end(e.getMessage());
      }
		});
		Dotenv dotenv = Dotenv.load();
		Integer port = Integer.parseInt(Objects.requireNonNull(dotenv.get("PORT")));

		server.requestHandler(router).listen(port)
      .onSuccess(result -> startPromise.complete())
      .onFailure(err -> startPromise.fail(err.getMessage()));
	}

	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.out.println("Running setup...");
		Properties hibernateProps = new Properties();
		String url = "jdbc:mysql://localhost:" + dotenv.get("DB_PORT") + "/" + dotenv.get("DB_NAME") + "?useSSL=false";
		hibernateProps.put("hibernate.connection.url", url);
		hibernateProps.put("hibernate.connection.username", dotenv.get("USER"));
		hibernateProps.put("hibernate.connection.password", dotenv.get("PASSWORD"));
		hibernateProps.put("jakarta.persistence.schema-generation.database.action", "drop-and-create");
		hibernateProps.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		//hibernateProps.put("hibernate.show_sql", "true");
		//hibernateProps.put("hibernate.format_sql", "true");
		//hibernateProps.put("hibernate.use_sql_comments", "true");
		Configuration hibernateConfiguration = new Configuration();
		hibernateConfiguration.addProperties(hibernateProps);
		hibernateConfiguration.addAnnotatedClass(User.class);
		hibernateConfiguration.addAnnotatedClass(Resume.class);

		ServiceRegistry serviceRegistry = new ReactiveServiceRegistryBuilder().applySettings(hibernateConfiguration.getProperties()).build();
		Stage.SessionFactory sessionFactory = hibernateConfiguration.buildSessionFactory(serviceRegistry).unwrap(Stage.SessionFactory.class);

		ResumeRepository resumeRepository = new ResumeRepository(sessionFactory);
		UserRepository userRepository = new UserRepository(sessionFactory);

		UserService userService = new UserService(userRepository);
		ResumeService resumeService = new ResumeService(resumeRepository);

		MainVerticle verticle = new MainVerticle(userService,resumeService);

//		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject()
//			    .put("userService", userService)
//			    .put("resumeService", resumeService));

		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(verticle)
		.onFailure(err -> System.out.println(err.getMessage()))
		.onSuccess(res -> {
			System.out.println(res);
			System.out.println("Application is running");
		});
	}
}
