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
 * Репозиторий для работы с пользователями.
 * Реализует интерфейс {@link IUserRepository} и предоставляет методы для создания, обновления, удаления и поиска пользователей.
 */
public record UserRepository(SessionFactory sessionFactory) implements IUserRepository {

    /**
     * Создает нового пользователя.
     *
     * @param user объект {@link UserDTO}, содержащий данные пользователя.
     * @return объект {@link Future}, содержащий созданного пользователя.
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
     * Обновляет данные пользователя.
     *
     * @param user объект {@link UserDTO}, содержащий обновленные данные пользователя.
     * @return объект {@link Future}, содержащий обновленные данные пользователя.
     */
    @Override
    public Future<UserDTO> updateUser(UserDTO user) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaUpdate<User> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(User.class);
        Root<User> root = criteriaUpdate.from(User.class);
        Predicate predicate = criteriaBuilder.equal(root.get("id"), user.id());

        criteriaUpdate.set("email", user.email());
        criteriaUpdate.set("password", user.password());
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
     * Удаляет пользователя по идентификатору.
     *
     * @param id уникальный идентификатор пользователя.
     * @return объект {@link Future}, сигнализирующий об успешном удалении пользователя.
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
     * Находит пользователя по идентификатору.
     *
     * @param id уникальный идентификатор пользователя.
     * @return объект {@link Future}, содержащий {@link Optional} с найденным пользователем или пустое значение.
     */
    @Override
    public Future<Optional<UserDTO>> findUserById(UUID id) {
        UserDtoMapper dtoMapper = new UserDtoMapper();
        CompletionStage<User> result = sessionFactory.withTransaction((s,t) -> s.find(User.class, id));
        Future<Optional<UserDTO>> future = Future.fromCompletionStage(result).map(r -> Optional.ofNullable(r)).map(r -> r.map(dtoMapper));
        return future;
    }

    /**
     * Находит пользователя по адресу электронной почты.
     *
     * @param email адрес электронной почты пользователя.
     * @return объект {@link Future}, содержащий {@link Optional} с найденным пользователем или пустое значение.
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

// Для запуска javadoc -d docs UserRepository.java
//gradle javadoc - для запуска через gradle 
//mvn javadoc:javadoc - для запуска через maven