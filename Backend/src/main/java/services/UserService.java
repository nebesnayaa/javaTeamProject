package services;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import interfaces.IUserService;
import io.vertx.core.Future;
import model.UserDTO;
import repository.UserRepository;

/**
 * Сервис для управления пользователями.
 * Реализует интерфейс {@link IUserService} и предоставляет методы для работы с репозиторием пользователей.
 */
public record UserService(UserRepository repository) implements IUserService {

    /**
     * Обновляет информацию о пользователе.
     * Проверяет, является ли текущий пользователь владельцем обновляемого аккаунта.
     *
     * @param principal объект {@link Principal}, представляющий текущего пользователя.
     * @param userDTO объект {@link UserDTO}, содержащий обновленные данные пользователя.
     * @return объект {@link Future}, содержащий обновленные данные пользователя.
     * @throws RuntimeException если пользователь не найден.
     * @throws NotOwnerException если текущий пользователь не является владельцем обновляемого аккаунта.
     */
    @Override
    public Future<UserDTO> updateUser(Principal principal, UserDTO userDTO) {
        UUID principalId = principal.Id();
        return repository.findUserById(principalId).compose(result -> {
            if(result.isEmpty()) {
                return Future.failedFuture(new RuntimeException());
            }
            UserDTO userPrincipal = result.get();
            if(Objects.equals(userPrincipal.id(), userDTO.id())) {
                return repository.updateUser(userDTO);
            }
            else {
                return Future.failedFuture(new NotOwnerException());
            }
        });
    }

    /**
     * Удаляет пользователя по идентификатору.
     * Проверяет, является ли текущий пользователь владельцем удаляемого аккаунта.
     *
     * @param principal объект {@link Principal}, представляющий текущего пользователя.
     * @param id уникальный идентификатор пользователя, которого нужно удалить.
     * @return объект {@link Future}, сигнализирующий об успешном удалении пользователя.
     * @throws RuntimeException если пользователь не найден.
     * @throws NotOwnerException если текущий пользователь не является владельцем удаляемого аккаунта.
     */
    @Override
    public Future<Void> removeUser(Principal principal, UUID id) {
        UUID principalId = principal.Id();
        return repository.findUserById(principalId).compose(result -> {
            if(result.isEmpty()) {
                return Future.failedFuture(new RuntimeException());
            }
            UserDTO userPrincipal = result.get();
            if(Objects.equals(userPrincipal.id(), id)) {
                return repository.removeUser(id);
            }
            else {
                return Future.failedFuture(new NotOwnerException());
            }
        });
    }

    /**
     * Создает нового пользователя.
     *
     * @param user объект {@link UserDTO}, содержащий данные для создания нового пользователя.
     * @return объект {@link Future}, содержащий созданного пользователя.
     */
    @Override
    public Future<UserDTO> createUser(UserDTO user) {
        return repository.createUser(user);
    }

    /**
     * Находит пользователя по идентификатору.
     *
     * @param id уникальный идентификатор пользователя.
     * @return объект {@link Future}, содержащий {@link Optional} с найденным пользователем или пустое значение.
     */
    @Override
    public Future<Optional<UserDTO>> findUserById(UUID id) {
        return repository.findUserById(id);
    }

    /**
     * Обновляет информацию о пользователе.
     *
     * @param user объект {@link UserDTO}, содержащий обновленные данные пользователя.
     * @return объект {@link Future}, содержащий обновленные данные пользователя.
     */
    @Override
    public Future<UserDTO> updateUser(UserDTO user) {
        return repository.updateUser(user);
    }

    /**
     * Удаляет пользователя по идентификатору.
     *
     * @param id уникальный идентификатор пользователя, которого нужно удалить.
     * @return объект {@link Future}, сигнализирующий об успешном удалении пользователя.
     */
    @Override
    public Future<Void> removeUser(UUID id) {
        return repository.removeUser(id);
    }

    /**
     * Находит пользователя по email.
     *
     * @param email адрес электронной почты пользователя.
     * @return объект {@link Future}, содержащий {@link Optional} с найденным пользователем или пустое значение.
     */
    @Override
    public Future<Optional<UserDTO>> findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }

}
