package interfaces;

import java.util.Optional;

import io.vertx.core.Future;
import javaTeamProject.model.ResumeDTO;
import javaTeamProject.model.ResumesList;


public interface IResumeRepository {
	
	Future<ResumeDTO> createResume(ResumeDTO resume);
	
	Future<ResumeDTO> updateResume(ResumeDTO resume);
	
	Future<Void> removeResume(Integer id);
	
	Future<Optional<ResumeDTO>> findResumeById (Integer id);
	
	Future<ResumesList> findResumeByUserId (Integer userId);
}