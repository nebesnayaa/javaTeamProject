package javaTeamProject.starterjavaTeamProject;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import kotlin.Pair;
import model.ResumeDTO;
import model.ResumesList;
import model.UserDTO;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import services.ResumeService;
import services.UserService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;

@ExtendWith(VertxExtension.class)
@ExtendWith(MockitoExtension.class)
public class MainVerticleTest {

	@Mock private UserService userService;
	@Mock private ResumeService resumeService;
	@InjectMocks private MainVerticle verticle;

	WebClient client;

	@BeforeEach
	void setup(Vertx vertx, VertxTestContext context) {
		DeploymentOptions options = new DeploymentOptions();
		JsonObject verticleConfig = new JsonObject();
		verticleConfig.put("port", 8080);
		options.setConfig(verticleConfig);

		client = WebClient.create(vertx);

		vertx.deployVerticle(verticle, options)
			.onSuccess(result -> context.completeNow())
			.onFailure(err -> context.failNow(err));
	}

  @Test
  void signupTest(Vertx vertx, VertxTestContext context){
    UserDTO user = new UserDTO(UUID.randomUUID(),"username", "email", "password","gender", "phone", 21, new Date(),new Date());
    Mockito.when(userService.createUser(Mockito.any(UserDTO.class))).thenReturn(Future.succeededFuture(user));
    context.verify(()->{
      JsonObject requestBody = JsonObject.mapFrom(user);
      client.postAbs("http://localhost:8080/users/signup").sendJsonObject(requestBody)
        .onFailure(err -> context.failNow(err.getMessage()))
        .onSuccess(result-> {
          Assertions.assertEquals(201, result.statusCode());
          context.completeNow();
        });
    });
  }

	@Test
	void findUserByIdExistsTest (Vertx vertx, VertxTestContext context) {
		UserDTO user = new UserDTO(UUID.randomUUID(),"username", "email", "password","gender", "phone", 21, new Date(),new Date());
		Mockito.when(userService.findUserById(user.id())).thenReturn(Future.succeededFuture(Optional.of(user)));
		context.verify(()->{
			client.getAbs("http://localhost:8080/users/one/"+user.id()).send()
			.onFailure(err -> {
				err.printStackTrace();
                context.failNow(err);
			})
			.onSuccess(result -> {
				int statusCode = result.statusCode();
				Assertions.assertEquals(200, statusCode);
				context.completeNow();
			});
		});
	}

	@Test
	void findUserByIdDoesNotExistsTest (Vertx vertx, VertxTestContext context) {
		UserDTO user = new UserDTO(UUID.randomUUID(), "username","email", "password","gender", "phone", 21, new Date(),new Date());
		Mockito.when(userService.findUserById(user.id())).thenReturn(Future.succeededFuture(Optional.empty()));
		context.verify(()->{
			client.getAbs("http://localhost:8080/users/one/"+user.id()).send()
			.onFailure(err -> {
				err.printStackTrace();
                context.failNow(err);
			})
			.onSuccess(result -> {
				int statusCode = result.statusCode();
				Assertions.assertEquals(404, statusCode);
				context.completeNow();
			});
		});
	}

  @Test
  void findResumeByUserTest (Vertx vertx, VertxTestContext context) {
    UserDTO user = new UserDTO(UUID.randomUUID(), "username","email", "password","gender", "phone", 21, new Date(),new Date());
    List<ResumeDTO> resumes = List.of(
      new ResumeDTO(UUID.randomUUID(), 1, new Date(), new Date(), Optional.of(user), "Full Name 1", "Position 1", "Objective 1", "Education 1", "Work Experience 1", "Skills and Awards 1", "Languages 1", "Recommendations 1", "Hobbies and Interests 1"),
      new ResumeDTO(UUID.randomUUID(), 2, new Date(), new Date(), Optional.of(user), "Full Name 2", "Position 2", "Objective 2", "Education 2", "Work Experience 2", "Skills and Awards 2", "Languages 2", "Recommendations 2", "Hobbies and Interests 2"),
      new ResumeDTO(UUID.randomUUID(), 1, new Date(), new Date(), Optional.of(user), "Full Name 3", "Position 3", "Objective 3", "Education 3", "Work Experience 3", "Skills and Awards 3", "Languages 3", "Recommendations 3", "Hobbies and Interests 3"),
      new ResumeDTO(UUID.randomUUID(), 1, new Date(), new Date(), Optional.of(user), "Full Name 4", "Position 4", "Objective 4", "Education 4", "Work Experience 4", "Skills and Awards 4", "Languages 4", "Recommendations 4", "Hobbies and Interests 4")

    );
    ResumesList resumesList = new ResumesList(resumes);
    Mockito.when(resumeService.findResumeByUserId(user.id())).thenReturn(Future.succeededFuture(resumesList));
    context.verify(()-> {
      String request = "http://localhost:8080/resumes/userId/" + user.id().toString();
      System.out.println(request);
      client.getAbs(request).send()
        .onFailure(err -> context.failNow(err.getMessage()))
        .onSuccess(result -> {
          Assertions.assertEquals(200, result.statusCode());
          context.completeNow();
        });
    });
  }

  @Test
  void deleteResumeTest (Vertx vert, VertxTestContext context){
    UUID id = UUID.randomUUID();
    Mockito.when(resumeService.removeResume(id)).thenReturn(Future.succeededFuture());
    context.verify(()->{
      client.deleteAbs("http://localhost:8080/resumes/" + id).send()
        .onSuccess(result -> {
          Assertions.assertEquals(204, result.statusCode());
          context.completeNow();
        })
        .onFailure(err -> context.failNow(err.getMessage()));
    });
  }

  @Test
  void createResumeTest(Vertx vert, VertxTestContext context){
    UserDTO user = new UserDTO(UUID.randomUUID(),"username", "email", "password","gender", "phone", 21, new Date(),new Date());
    ResumeDTO resume = new ResumeDTO(UUID.randomUUID(), 1, new Date(), new Date(), Optional.of(user), "Full Name 1", "Position 1", "Objective 1", "Education 1", "Work Experience 1", "Skills and Awards 1", "Languages 1", "Recommendations 1", "Hobbies and Interests 1");
    Mockito.when(userService.findUserById(user.id())).thenReturn(Future.succeededFuture(Optional.of(user)));
    Mockito.when(resumeService.createResume(Mockito.any(ResumeDTO.class))).thenReturn(Future.succeededFuture(resume));
    context.verify(() -> {
      JsonObject reqBody = JsonObject.mapFrom(resume);
      reqBody.put("userId", user.id());
      client.postAbs("http://localhost:8080/resumes/")
        .sendJsonObject(reqBody)
        .onFailure(err -> context.failNow(err.getMessage()))
        .onSuccess(result -> {
          Assertions.assertEquals(201, result.statusCode());
          context.completeNow();
        });
    });
  }

  @Test
  void updateResumeTest(Vertx vert, VertxTestContext context){
    UserDTO user = new UserDTO(UUID.randomUUID(), "username","email", "password","gender", "phone", 21, new Date(),new Date());
    ResumeDTO resume = new ResumeDTO(UUID.randomUUID(), 1, new Date(), new Date(), Optional.of(user), "Full Name 1", "Position 1", "Objective 1", "Education 1", "Work Experience 1", "Skills and Awards 1", "Languages 1", "Recommendations 1", "Hobbies and Interests 1");
    Mockito.when(userService.findUserById(user.id())).thenReturn(Future.succeededFuture(Optional.of(user)));
    Mockito.when(resumeService.updateResume(Mockito.any(ResumeDTO.class))).thenReturn(Future.succeededFuture(resume));
    context.verify(() -> {
      JsonObject reqBody = JsonObject.mapFrom(resume);
      reqBody.put("userId", user.id());
      client.putAbs("http://localhost:8080/resumes/")
        .sendJsonObject(reqBody)
        .onFailure(err -> context.failNow(err.getMessage()))
        .onSuccess(result -> {
          Assertions.assertEquals(200, result.statusCode());
          context.completeNow();
        });
    });
  }
}
