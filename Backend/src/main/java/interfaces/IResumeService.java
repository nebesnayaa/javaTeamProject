package interfaces;

import java.util.Optional;

import io.vertx.core.Future;
import model.ResumeDTO;
import model.ResumesList;
import services.Principal;

public interface IResumeService {
	//Future<ResumeDTO> updateResume(Principal principal, ResumeDTO Resume);
	
	//Future<Void> removeResume(Principal principal, Integer id);
	
	Future<ResumeDTO> createResume(ResumeDTO Resume);
	
	Future<Optional<ResumeDTO>> findResumeById(Integer id);
	
	Future<ResumesList> findResumeByUserId(Integer userId);
	
	Future<ResumeDTO> updateResume( ResumeDTO Resume);
	
	Future<Void> removeResume( Integer id);
}
