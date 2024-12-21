package javaTeamProject.repository;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.hibernate.reactive.stage.Stage.SessionFactory;

import interfaces.IUserRepository;
import io.vertx.core.Future;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
		// TODO Auto-generated method stub
		return null;
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
