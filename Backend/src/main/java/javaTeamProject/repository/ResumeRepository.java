package javaTeamProject.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import org.hibernate.reactive.stage.Stage;

import interfaces.IResumeRepository;
import io.vertx.core.Future;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.CriteriaUpdate;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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
		CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
		CriteriaUpdate<Resume> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Resume.class);
		Root<Resume> root = criteriaUpdate.from(Resume.class);
		Predicate predicate = criteriaBuilder.equal(root.get("id"), resume.id());
		
		criteriaUpdate.set("content", resume.content());
		criteriaUpdate.set("updatedAt", LocalDateTime.now());
		
		criteriaUpdate.where(predicate);
		
		CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaUpdate).executeUpdate());
		Future<ResumeDTO> future = Future.fromCompletionStage(result).map(r -> resume);
		return future;
	}

	@Override
	public Future<Void> removeResume(Integer id) {
		CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
		CriteriaDelete<Resume> criteriaDelete = criteriaBuilder.createCriteriaDelete(Resume.class);
		Root<Resume> root = criteriaDelete.from(Resume.class);
		Predicate predicate = criteriaBuilder.equal(root.get("id"), id); //id == [id]
		criteriaDelete.where(predicate);
		
		CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaDelete).executeUpdate());
		Future<Void> future = Future.fromCompletionStage(result).compose(r -> Future.succeededFuture());
		return future;
	}

	@Override
	public Future<Optional<ResumeDTO>> findResumeById(Integer id) {
		ResumeDtoMapper dtoMapper = new ResumeDtoMapper();
		CompletionStage<Resume> result = sessionFactory().withTransaction((s,t) -> s.find(Resume.class, id));
		Future<Optional<ResumeDTO>> future = Future.fromCompletionStage(result)									
												.map(r -> Optional.ofNullable(r))
												.map(r -> r.map(dtoMapper));
		return future;
	}

	@Override
	public Future<ResumesList> findResumeByUserId(Integer userId) {
		ResumeDtoMapper dtoMapper = new ResumeDtoMapper();
		CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
		CriteriaQuery<Resume> criteriaQuery = criteriaBuilder.createQuery(Resume.class);
		Root<Resume> root = criteriaQuery.from(Resume.class);
		Predicate predicate = criteriaBuilder.equal(root.get("userId"), userId);
		criteriaQuery.where(predicate);
		CompletionStage<List<Resume>> result = sessionFactory().withTransaction((s,t)-> s.createQuery(criteriaQuery).getResultList());
		Future<ResumesList> future = Future.fromCompletionStage(result).map(list -> list.stream().map(dtoMapper).collect(Collectors.toList()))
				.map(list -> new ResumesList(list));
		return future;
	}

}
