package repository;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletionStage;

import jakarta.persistence.criteria.*;
import org.hibernate.reactive.stage.Stage.SessionFactory;

import interfaces.IUserRepository;
import io.vertx.core.Future;
import model.Resume;
import model.ResumeDTO;
import model.User;
import model.UserDTO;
import model.UserDtoMapper;
import model.UserEntityMapper;

import javax.swing.text.html.Option;

/**
 * Repository for managing users.
 * Implements the {@link IUserRepository} interface and provides methods for creating, updating, deleting, and finding users.
 */
public record UserRepository(SessionFactory sessionFactory) implements IUserRepository {

    /**
     * Creates a new user.
     *
     * @param user {@link UserDTO} object containing user data.
     * @return {@link Future} containing the created user.
     */
    @Override
    public Future<UserDTO> createUser(UserDTO user) {
        UserEntityMapper entityMapper = new UserEntityMapper();
        User entity = entityMapper.apply(user);

        CompletionStage<Void> result = sessionFactory.withTransaction((s,t)-> s.persist(entity));
        Future<UserDTO> future = Future.fromCompletionStage(result).map(e -> new UserDtoMapper().apply(entity));

        return future;
    }

    /**
     * Updates the user data.
     *
     * @param user {@link UserDTO} object containing updated user data.
     * @return {@link Future} containing the updated user.
     */
    @Override
    public Future<UserDTO> updateUser(UserDTO user) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaUpdate<User> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(User.class);
        Root<User> root = criteriaUpdate.from(User.class);
        Predicate predicate = criteriaBuilder.equal(root.get("id"), user.id());

        criteriaUpdate.set("email", user.email());
        if(user.password() != null){
          criteriaUpdate.set("password", user.password());
          System.out.println("Password was changed");
        }
        criteriaUpdate.set("gender", user.gender());
        criteriaUpdate.set("phone", user.phone());
        criteriaUpdate.set("age", user.age());
        criteriaUpdate.set("updatedAt", LocalDateTime.now());

        criteriaUpdate.where(predicate);
        CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaUpdate).executeUpdate());
        Future<UserDTO> future = Future.fromCompletionStage(result).map(r -> user);
        return future;
    }

    /**
     * Removes a user by its ID.
     *
     * @param id the unique identifier of the user.
     * @return {@link Future} indicating successful removal of the user.
     */
    @Override
    public Future<Void> removeUser(UUID id) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaDelete<User> criteriaDelete = criteriaBuilder.createCriteriaDelete(User.class);
        Root<User> root = criteriaDelete.from(User.class);
        Predicate predicate = criteriaBuilder.equal(root.get("id"), id);
        criteriaDelete.where(predicate);

        CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaDelete).executeUpdate());
        Future<Void> future = Future.fromCompletionStage(result).compose(r -> Future.succeededFuture());
        return future;
    }

    /**
     * Finds a user by its ID.
     *
     * @param id the unique identifier of the user.
     * @return {@link Future} containing an {@link Optional} with the found user or an empty value.
     */
    @Override
    public Future<Optional<UserDTO>> findUserById(UUID id) {
        UserDtoMapper dtoMapper = new UserDtoMapper();
        CompletionStage<User> result = sessionFactory.withTransaction((s,t) -> s.find(User.class, id));
        Future<Optional<UserDTO>> future = Future.fromCompletionStage(result).map(r -> Optional.ofNullable(r)).map(r -> r.map(dtoMapper));
        return future;
    }

    /**
     * Finds a user by email address.
     *
     * @param email the email address of the user.
     * @return {@link Future} containing an {@link Optional} with the found user or an empty value.
     */
    @Override
    public Future<Optional<UserDTO>> findUserByEmail(String email) {
        UserDtoMapper mapper = new UserDtoMapper();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<User> criteriaQuery = criteriaBuilder.createQuery(User.class);
        Root<User> root = criteriaQuery.from(User.class);
        Predicate predicate = criteriaBuilder.equal(root.get("email"), email);
        criteriaQuery.where(predicate);
        CompletionStage<User> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaQuery).getSingleResultOrNull());
        Future<Optional<UserDTO>> future = Future.fromCompletionStage(result).map(Optional::ofNullable).map(r -> r.map(mapper));
        return future;
    }

}
