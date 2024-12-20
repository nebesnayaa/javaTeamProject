package javaTeamProject.repository;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

import org.hibernate.reactive.stage.Stage;

import interfaces.IResumeRepository;
import io.vertx.core.Future;
import javaTeamProject.model.Resume;
import javaTeamProject.model.ResumeDTO;
import javaTeamProject.model.ResumeDtoMapper;
import javaTeamProject.model.ResumesList;
import javaTeamProject.model.ResumeEntityMapper;

public record ResumeRepository (Stage.SessionFactory sessionFactory) implements IResumeRepository {
	
	@Override
	public Future<ResumeDTO> createResume(ResumeDTO resume) {
	    ResumeEntityMapper entityMapper = new ResumeEntityMapper();
	    Resume entity = entityMapper.apply(resume);
	    
	    return Future.fromCompletionStage(
	        sessionFactory.withTransaction((s, t) -> s.persist(entity))
	            .exceptionally(ex -> {
	                ex.printStackTrace();  // Друк винятку для налагодження
	                throw new RuntimeException("Error saving resume", ex);
	            })
	    ).map(v -> new ResumeDtoMapper().apply(entity));
	}


	@Override
	public Future<ResumeDTO> updateResume(ResumeDTO resume) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Void> removeResume(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<Optional<ResumeDTO>> findResumeById(Integer id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Future<ResumesList> findResumeByUserId(Integer userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
