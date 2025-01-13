package javaTeamProject.starterjavaTeamProject;

import java.security.NoSuchAlgorithmException;
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
import services.Principal;
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

    router.route("/*").handler(BodyHandler.create());

    router.post("/users/signup").handler(context -> {
      try {
        JsonObject body = context.getBodyAsJson();
        if (body == null) {
          context.response().setStatusCode(500).end("Internal server error");
        } else if (body.isEmpty()) {
          context.response().setStatusCode(400).end("Bad request");
        } else {
          String email = body.getString("email");
          String password = body.getString("password");
          password = Hasher.getHash(password,10);
          String gender = body.getString("gender");
          String phone = body.getString("phone");
          Integer age = Integer.parseInt(body.getString("age"));
          body.putNull("password");

          UserDTO user = new UserDTO(null, email, password,gender, phone, age, new Date(), new Date());
          userService.createUser(user)
            .onSuccess(result -> {
              body.put("id", result.id());

              String sessionId = UUID.randomUUID().toString();

              JsonObject sessionData = new JsonObject()
                .put("userId", result.id())
                .put("email", result.email());

              redisAPI.set(List.of(sessionId, sessionData.encode()))
                .compose(res -> redisAPI.expire(List.of(sessionId, "36000")))
                .onSuccess(res -> {
                  try {
                    context.response().setStatusCode(201)
                      .putHeader("Set-Cookie", "sessionId=" + AesEncryptor.encrypt(sessionId.toString()) + "; HttpOnly; SameSite=Strict")
                      .end(new JsonObject().put("sessionId", sessionId).encode());
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                })
                .onFailure(err -> {
                  context.response().setStatusCode(500).end(err.getMessage());
                });
            })
            .onFailure(err -> {
              context.response().setStatusCode(500).end(err.getMessage());
            });
        }
      }
      catch (Exception e){
        context.response().setStatusCode(500).end(e.getMessage());
      }
    });

    router.put("/users").handler(context -> {
      try{
        JsonObject body = context.getBodyAsJson();
        if (body == null) {
          context.response().setStatusCode(500).end("Internal server error");
        } else if (body.isEmpty()) {
          context.response().setStatusCode(400).end("Bad request");
        }
        else{
          String id = body.getString("id");
          String email = body.getString("email");
          String password = body.getString("password");
          password = Hasher.getHash(password,10);
          String gender = body.getString("gender");
          String phone = body.getString("phone");
          Integer age = Integer.parseInt(body.getString("age"));
          body.putNull("password");
          UserDTO user = new UserDTO(UUID.fromString(id), email, password,gender, phone, age, new Date(), new Date());
          userService.updateUser(new Principal(UUID.fromString(id)), user)
            .onSuccess(result -> {
              String sessionId = UUID.randomUUID().toString();

              JsonObject sessionData = new JsonObject()
                .put("userId", result.id())
                .put("email", result.email());

              redisAPI.set(List.of(sessionId, sessionData.encode()))
                .compose(res -> redisAPI.expire(List.of(sessionId, "36000")))
                .onSuccess(res -> {
                  try {
                    context.response().setStatusCode(200).putHeader("Set-Cookie", "sessionId=" + AesEncryptor.encrypt(sessionId.toString()) + "; HttpOnly; SameSite=Strict").end(body.encode());
                  } catch (Exception e) {
                    throw new RuntimeException(e);
                  }
                })
                .onFailure(err -> {
                  context.response().setStatusCode(500).end(err.getMessage());
                });
            })
            .onFailure(err -> {
              context.response().setStatusCode(500).end(err.getMessage());
            });

        }
      }
      catch (Exception e){
        context.response().setStatusCode(500).end(e.getMessage());
      }
    });

    router.post("/users/logout").handler(context -> {
      try {
        String encryptedSessionId = context.getCookie("sessionId").getValue();
        if (encryptedSessionId == null) {
          System.out.println("No session ID in cookie");
          context.response().setStatusCode(400).end("Session ID missing in cookie");
          return;
        }
        if (encryptedSessionId != null) {
          String sessionId = AesEncryptor.decrypt(encryptedSessionId);

          redisAPI.del(List.of(sessionId))
            .onSuccess(res -> {
              context.response()
                .setStatusCode(200)
                .putHeader("Set-Cookie", "sessionId=; Max-Age=0; HttpOnly; SameSite=Strict")
                .end("Logged out successfully");
            })
            .onFailure(err -> {
              System.out.println("Redis delete failed: " + err.getMessage());
              context.response().setStatusCode(500).end("Failed to log out: " + err.getMessage());
            });
        } else {
          context.response().setStatusCode(400).end("No session found");
        }
      } catch (Exception e) {
        System.out.println("Error during logout: " + e.getMessage());
        context.response().setStatusCode(500).end("Error during logout: " + e.getMessage());
      }
    });

    router.post("/users/login").handler(context -> {
      try {
        JsonObject body = context.getBodyAsJson();
        if (body == null || body.isEmpty()) {
          context.response().setStatusCode(400).end("Bad request");
          return;
        }

        String email = body.getString("email");
        String password = body.getString("password");

        if (email == null || password == null) {
          context.response().setStatusCode(400).end("Email and password are required");
          return;
        }

        userService.findUserByEmail(email)
          .onSuccess(user -> {
            if (user.isEmpty()) {
              context.response().setStatusCode(404).end("User not found");
              return;
            }

            try {
              if (Hasher.compareHash(user.get().password(), password)) {

                String sessionId = UUID.randomUUID().toString();

                JsonObject sessionData = new JsonObject()
                  .put("userId", user.get().id())
                  .put("email", user.get().email());

                redisAPI.set(List.of(sessionId, sessionData.encode()))
                  .compose(res -> redisAPI.expire(List.of(sessionId, "36000")))
                  .onSuccess(res -> {
                    try {
                      context.response()
                        .setStatusCode(200)
                        .putHeader("Set-Cookie", "sessionId=" + AesEncryptor.encrypt(sessionId) + "; HttpOnly; SameSite=Strict")
                        .end(JsonObject.mapFrom(user.get()).encode());
                    } catch (Exception e) {
                      context.response().setStatusCode(500).end("Failed to encrypt sessionId");
                    }
                  })
                  .onFailure(err -> {
                    context.response().setStatusCode(500).end("Failed to create session: " + err.getMessage());
                  });
              } else {
                context.response().setStatusCode(401).end("Invalid credentials");
              }
            } catch (NoSuchAlgorithmException e) {
              throw new RuntimeException(e);
            }
          })
          .onFailure(err -> {
            context.response().setStatusCode(500).end("Failed to get user: " + err.getMessage());
          });
      } catch (Exception e) {
        context.response().setStatusCode(500).end("Error during login: " + e.getMessage());
      }
    });

    router.get("/users/one/:id").handler(context -> {
			try {
				UUID id = UUID.fromString(context.pathParam("id"));

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
        UUID userId = UUID.fromString(context.pathParam("userId"));

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
      UUID id = UUID.fromString(context.pathParam("id"));
      resumeService.removeResume(id)
        .onSuccess(result -> context.response().setStatusCode(204).end())
        .onFailure(err -> context.response().setStatusCode(500).end(err.getMessage()));
    });

    router.post("/resumes").handler(context -> {
      JsonObject body = context.getBodyAsJson();
      //JsonObject user  = body.getJsonObject("user");
      UUID userId = UUID.fromString(body.getString("userId"));//user.mapTo(UserDTO.class).id();
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
      UUID id = UUID.fromString(body.getString("id"));
      UUID userId = UUID.fromString(body.getString("userId"));
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
