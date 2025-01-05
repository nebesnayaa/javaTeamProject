package javaTeamProject.starterjavaTeamProject;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import model.UserDTO;
import services.ResumeService;
import services.UserService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
