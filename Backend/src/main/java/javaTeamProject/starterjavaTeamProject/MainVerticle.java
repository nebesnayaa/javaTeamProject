package javaTeamProject.starterjavaTeamProject;

import java.util.Properties;

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

	private  UserService userService;
	private  ResumeService resumeService;
	
	public MainVerticle() {
		this.userService = null;
		this.resumeService = null;
		//this.userService = config().getJsonObject("userService").mapTo(UserService.class);
	   // this.resumeService = config().getJsonObject("resumeService").mapTo(ResumeService.class);
	}

	public MainVerticle(UserService userService, ResumeService resumeService) {
	
		this.userService = userService;
		this.resumeService =resumeService;
	}

	@Override
	public void start(Promise<Void> startPromise) throws Exception {
		if (this.userService == null || this.resumeService == null) {
		    JsonObject config = config();
		    this.resumeService = (ResumeService) config.getValue("resumeService");
		    this.userService = (UserService) config.getValue("userService");
		}

		 
	        
		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);
		
		router.get("/").handler(context ->{
			context.response().setStatusCode(200).send("Hello from server!");
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
			Integer userId = Integer.parseInt(context.pathParam("userId"));
			
		});
		Dotenv dotenv = Dotenv.load();
		Integer port = 8080;// Integer.parseInt(dotenv.get("PORT"), 8080);//config.getInteger("port");

		server.requestHandler(router).listen(port).onSuccess(result -> startPromise.complete())
				.onFailure(err -> startPromise.fail(err));
	}
	
	public static void main(String[] args) {
		Dotenv dotenv = Dotenv.load();
		System.out.println("Running setup...");
		Properties hibernateProps = new Properties();
		String url = "jdbc:mysql://localhost:" + dotenv.get("PORT") + "/" + dotenv.get("DB_NAME") + "?useSSL=false";
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
		
		MainVerticle verticle = new MainVerticle();
		
		DeploymentOptions options = new DeploymentOptions().setConfig(new JsonObject()
			    .put("userService", userService)
			    .put("resumeService", resumeService));
		
		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(verticle, options)
		.onFailure(err -> System.out.println(err.getMessage()))
		.onSuccess(res -> {
			System.out.println(res);
			System.out.println("Application is running");
		});
	}

}
