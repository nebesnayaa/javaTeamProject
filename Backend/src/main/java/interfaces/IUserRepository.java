package interfaces;

import java.util.Optional;

import io.vertx.core.Future;
import model.UserDTO;

public interface IUserRepository {
	
	Future<UserDTO> createUser(UserDTO user);
	
	Future<UserDTO> updateUser(UserDTO user);
	
	Future<Void> removeUser(Integer id);
	
	Future<Optional<UserDTO>> findUserById (Integer id);
	
}
