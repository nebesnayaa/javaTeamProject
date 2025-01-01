package javaTeamProject.starterjavaTeamProject;

import java.time.LocalDateTime;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import javaTeamProject.model.UserDTO;
import javaTeamProject.repository.UserRepository;
import services.Principal;
import services.UserService;

@ExtendWith(VertxExtension.class)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	
	@Mock private UserRepository repository;
	@InjectMocks private UserService service;
	
	@Test
	void updateUserIsOwnerTest(Vertx vertx, VertxTestContext context) {
		UserDTO user = new UserDTO(1, "email", "password", LocalDateTime.now(),LocalDateTime.now());
		Mockito.when(repository.findUserById(user.id())).thenReturn(Future.succeededFuture(Optional.of(user)));
		Mockito.when(repository.updateUser(user)).thenReturn(Future.succeededFuture(user));
		Principal principal = new Principal(user.id());
		context.verify(()->{
			service.updateUser(principal, user)
			.onSuccess(result -> context.completeNow())
			.onFailure(err -> context.failNow(err));
		});
	}
	
	@Test
	void updateUserIsNotOwner(Vertx vertx, VertxTestContext context) {
		UserDTO user = new UserDTO(1, "email", "password", LocalDateTime.now(),LocalDateTime.now());
		Principal principal = new Principal(user.id());
		Mockito.when(repository.findUserById(user.id())).thenReturn(Future.succeededFuture(Optional.of(new UserDTO(2,"email", "password", LocalDateTime.now(),LocalDateTime.now()))));
		context.verify(()->{
			service.updateUser(principal, user)
			.onSuccess(result -> context.failNow(new RuntimeException()))
			.onFailure(err -> context.completeNow());
		});
	}
	
	@Test
	void removeUserIsOwnerTest(Vertx vertx, VertxTestContext context){
		UserDTO user = new UserDTO(1, "email", "password", LocalDateTime.now(),LocalDateTime.now());
		Mockito.when(repository.findUserById(user.id())).thenReturn(Future.succeededFuture(Optional.of(user)));
		Mockito.when(repository.removeUser(user.id())).thenReturn(Future.succeededFuture());
		Principal principal = new Principal(user.id());
		context.verify(()-> {
			service.removeUser(principal, user.id())
			.onSuccess(result -> context.completeNow())
			.onFailure(err -> context.failNow(err));
		});
	}
	
	@Test
	void removeUserIsNotOwnerTest(Vertx vertx, VertxTestContext context){
		UserDTO user = new UserDTO(1, "email", "password", LocalDateTime.now(),LocalDateTime.now());
		Mockito.when(repository.findUserById(user.id())).thenReturn(Future.succeededFuture(Optional.of(new UserDTO(2, "email", "password", LocalDateTime.now(),LocalDateTime.now()))));
		Principal principal = new Principal(user.id());
		context.verify(()-> {
			service.removeUser(principal, user.id())
			.onSuccess(result -> context.failNow(new RuntimeException()))
			.onFailure(err -> context.completeNow());
		});
	}
}
