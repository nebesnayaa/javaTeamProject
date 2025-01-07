package javaTeamProject.starterjavaTeamProject;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import model.ResumeDTO;
import model.ResumesList;
import model.UserDTO;
import services.ResumeService;
import services.UserService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
	void findUserByIdExistsTest (Vertx vertx, VertxTestContext context) {
		UserDTO user = new UserDTO (1,"email", "password", new Date(), new Date());
		Mockito.when(userService.findUserById(1)).thenReturn(Future.succeededFuture(Optional.of(user)));
		context.verify(()->{
			client.getAbs("http://localhost:8080/users/one/1").send()
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
		UserDTO user = new UserDTO (1,"email", "password", new Date(), new Date());
		Mockito.when(userService.findUserById(1)).thenReturn(Future.succeededFuture(Optional.empty()));
		context.verify(()->{
			client.getAbs("http://localhost:8080/users/one/1").send()
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
    UserDTO user = new UserDTO(1, "email1", "password1", new Date(), new Date());
    List<ResumeDTO> resumes = List.of(
      new ResumeDTO(1 ,"content 1", 1, new Date(), new Date(), Optional.of(user)),
      new ResumeDTO(2 ,"content 2", 2, new Date(), new Date(), Optional.of(user)),
      new ResumeDTO(3, "content 3", 1, new Date(), new Date(), Optional.of(user)),
      new ResumeDTO(4 ,"content 4", 1, new Date(), new Date(), Optional.of(user))
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
}
