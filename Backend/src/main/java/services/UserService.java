package services;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import interfaces.IUserService;
import io.vertx.core.Future;
import model.UserDTO;
import repository.UserRepository;

/**
 * Service for managing users.
 * Implements the {@link IUserService} interface and provides methods to interact with the user repository.
 */
public record UserService(UserRepository repository) implements IUserService {

    /**
     * Updates user information.
     * Checks if the current user is the owner of the account being updated.
     *
     * @param principal {@link Principal} object representing the current user.
     * @param userDTO {@link UserDTO} object containing the updated user data.
     * @return {@link Future} containing the updated user data.
     * @throws RuntimeException if the user is not found.
     * @throws NotOwnerException if the current user is not the owner of the account being updated.
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
     * Removes a user by their ID.
     * Checks if the current user is the owner of the account being deleted.
     *
     * @param principal {@link Principal} object representing the current user.
     * @param id the unique identifier of the user to be deleted.
     * @return {@link Future} indicating successful user deletion.
     * @throws RuntimeException if the user is not found.
     * @throws NotOwnerException if the current user is not the owner of the account being deleted.
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
     * Creates a new user.
     *
     * @param user {@link UserDTO} object containing the data to create a new user.
     * @return {@link Future} containing the created user.
     */
    @Override
    public Future<UserDTO> createUser(UserDTO user) {
        return repository.createUser(user);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the unique identifier of the user.
     * @return {@link Future} containing an {@link Optional} with the found user or an empty value.
     */
    @Override
    public Future<Optional<UserDTO>> findUserById(UUID id) {
        return repository.findUserById(id);
    }

    /**
     * Updates user information.
     *
     * @param user {@link UserDTO} object containing the updated user data.
     * @return {@link Future} containing the updated user data.
     */
    @Override
    public Future<UserDTO> updateUser(UserDTO user) {
        return repository.updateUser(user);
    }

    /**
     * Removes a user by their ID.
     *
     * @param id the unique identifier of the user to be deleted.
     * @return {@link Future} indicating successful user deletion.
     */
    @Override
    public Future<Void> removeUser(UUID id) {
        return repository.removeUser(id);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address of the user.
     * @return {@link Future} containing an {@link Optional} with the found user or an empty value.
     */
    @Override
    public Future<Optional<UserDTO>> findUserByEmail(String email) {
        return repository.findUserByEmail(email);
    }

}
