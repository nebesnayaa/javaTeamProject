package javaTeamProject.starterjavaTeamProject;

import java.util.*;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import io.vertx.core.json.Json;
import io.vertx.core.json.jackson.DatabindCodec;
import io.vertx.ext.web.handler.BodyHandler;
import model.ResumeDTO;
import model.UserDTO;
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

import io.vertx.redis.client.Redis;
import io.vertx.redis.client.RedisAPI;
import io.vertx.redis.client.RedisOptions;

public class MainVerticle extends AbstractVerticle {

	private final UserService userService ;
	private final ResumeService resumeService;
  private RedisAPI redisAPI;

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

    RedisOptions redisOptions = new RedisOptions().setConnectionString("redis://localhost:6379");
    Redis redisClient = Redis.createClient(vertx, redisOptions);
    redisAPI = RedisAPI.api(redisClient);

		HttpServer server = vertx.createHttpServer();
		Router router = Router.router(vertx);

    router.route("/*").handler(BodyHandler.create());

		router.get("/").handler(context ->{
			context.response().setStatusCode(200).send("Hello from server!");
		});

    router.post("/users/signup").handler(context -> {
      JsonObject body = context.getBodyAsJson();
      if(body == null){
        context.response().setStatusCode(500).end("Internal server error");
      }
      else if(body.isEmpty()){
        context.response().setStatusCode(400).end("Bad request");
      }
      else {
        String email = body.getString("email");
        String password = body.getString("password");
        UserDTO user = new UserDTO(null, email, password, new Date(), new Date());
        userService.createUser(user)
        .onSuccess(result -> {
          String sessionId = UUID.randomUUID().toString();

          JsonObject sessionData = new JsonObject()
          .put("userId", result.id())
          .put("email", result.email());

          redisAPI.set(List.of(sessionId, sessionData.encode()))
          .compose(res -> redisAPI.expire(List.of(sessionId, "3600")))
          .onSuccess(res -> {
              context.response().setStatusCode(201).putHeader("Set-Cookie", "sessionId="+sessionId + "; HttpOnly; Secure; SameSite=Strict").end(body.encode());
          })
          .onFailure(err -> {
            context.response().setStatusCode(500).end(err.getMessage());
          });
        })
        .onFailure(err -> {
          context.response().setStatusCode(500).end(err.getMessage());
        });
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
          .onFailure(err -> context.response().setStatusCode(500).end(err.getMessage()));
      }
      catch(NumberFormatException e){
        context.response().setStatusCode(400).end(e.getMessage());
      }
		});

    router.delete("/resumes/:id").handler(context -> {
      Integer id = Integer.parseInt(context.pathParam("id"));
      resumeService.removeResume(id)
        .onSuccess(result -> context.response().setStatusCode(204).end())
        .onFailure(err -> context.response().setStatusCode(500).end(err.getMessage()));
    });

    router.post("/resumes").handler(context -> {
      JsonObject body = context.getBodyAsJson();
      //JsonObject user  = body.getJsonObject("user");
      Integer userId = body.getInteger("userId");//user.mapTo(UserDTO.class).id();
      Integer templateId = body.getInteger("templateId");
      String content = body.getString("content");
      userService.findUserById(userId)
      .onSuccess(res -> {

        ResumeDTO payload = new ResumeDTO(null, content, templateId, new Date(), new Date(), res);
        resumeService.createResume(payload)
          .onSuccess(result -> {
            JsonObject responseBody = JsonObject.mapFrom(result);
            context.response().setStatusCode(201).end(responseBody.encode());
          })
          .onFailure(err -> {
            context.response().setStatusCode(500).end(err.getMessage());
          });
      })
      .onFailure(err -> {
        context.response().setStatusCode(500).end(err.getMessage());
      });
    });

    router.put("/resumes").handler(context -> {
      JsonObject body = context.getBodyAsJson();
      Integer id = body.getInteger("id");
      Integer userId = body.getInteger("userId");
      Integer templateId = body.getInteger("templateId");
      String content = body.getString("content");
      userService.findUserById(userId)
        .onSuccess(res -> {

          ResumeDTO payload = new ResumeDTO(id, content, null, null, null, res);
          resumeService.updateResume(payload)
            .onSuccess(result -> {
              JsonObject responseBody = JsonObject.mapFrom(result);
              context.response().setStatusCode(200).end(responseBody.encode());
            })
            .onFailure(err -> {
              context.response().setStatusCode(500).end(err.getMessage());
            });
        })
        .onFailure(err -> {
          context.response().setStatusCode(500).end(err.getMessage());
        });
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

		Vertx vertx = Vertx.vertx();
		vertx.deployVerticle(verticle)
		.onFailure(err -> System.out.println(err.getMessage()))
		.onSuccess(res -> {
			System.out.println(res);
			System.out.println("Application is running");
		});
	}
}
