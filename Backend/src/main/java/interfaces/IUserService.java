package interfaces;

import java.util.Optional;

import io.vertx.core.Future;
import model.UserDTO;
import services.Principal;

public interface IUserService {
	Future<UserDTO> updateUser(Principal principal, UserDTO user);
	
	Future<Void> removeUser(Principal principal, Integer id);
	
	Future<UserDTO> createUser(UserDTO user);
	
	Future<Optional<UserDTO>> findUserById(Integer id);
	
	Future<UserDTO> updateUser( UserDTO user);
	
	Future<Void> removeUser( Integer id);
	
}
