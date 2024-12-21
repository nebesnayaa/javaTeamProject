package javaTeamProject.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.hibernate.reactive.stage.Stage.SessionFactory;

import interfaces.IUserRepository;
import io.vertx.core.Future;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import javaTeamProject.model.Resume;
import javaTeamProject.model.ResumeDTO;
import javaTeamProject.model.User;
import javaTeamProject.model.UserDTO;
import javaTeamProject.model.UserDtoMapper;
import javaTeamProject.model.UserEntityMapper;

public record UserRepository(SessionFactory sessionFactory) implements IUserRepository {

	@Override
	public Future<UserDTO> createUser(UserDTO user) {
		UserEntityMapper entityMapper = new UserEntityMapper();
		User entity = entityMapper.apply(user);
		
		CompletionStage<Void> result = sessionFactory.withTransaction((s,t)-> s.persist(entity));
		Future<UserDTO> future = Future.fromCompletionStage(result).map(e -> new UserDtoMapper().apply(entity));
		
		return future;
	}

	@Override
	public Future<UserDTO> updateUser(UserDTO user) {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
		CriteriaUpdate<User> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(User.class);
		Root<User> root = criteriaUpdate.from(User.class);
		Predicate predicate = criteriaBuilder.equal(root.get("id"), user.id());
		
		criteriaUpdate.set("email", user.email());
		criteriaUpdate.set("password", user.password());
		criteriaUpdate.set("updatedAt", LocalDateTime.now());
		
		criteriaUpdate.where(predicate);
		CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaUpdate).executeUpdate());
		Future<UserDTO> future = Future.fromCompletionStage(result).map(r -> user);
		return future;
	}

	@Override
	public Future<Void> removeUser(Integer id) {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
		CriteriaDelete<User> criteriaDelete = criteriaBuilder.createCriteriaDelete(User.class);
		Root<User> root = criteriaDelete.from(User.class);
		Predicate predicate = criteriaBuilder.equal(root.get("id"), id);
		criteriaDelete.where(predicate);
		
		CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaDelete).executeUpdate());
		Future<Void> future = Future.fromCompletionStage(result).compose(r -> Future.succeededFuture());
		return future;
	}

	@Override
	public Future<Optional<UserDTO>> findUserById(Integer id) {
		UserDtoMapper dtoMapper = new UserDtoMapper();
		CompletionStage<User> result = sessionFactory.withTransaction((s,t) -> s.find(User.class, id));
		Future<Optional<UserDTO>> future = Future.fromCompletionStage(result).map(r -> Optional.ofNullable(r)).map(r -> r.map(dtoMapper));
		return future;
	}

}
