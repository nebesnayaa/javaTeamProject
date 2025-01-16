package repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
import model.Resume;
import model.ResumeDTO;
import model.ResumeDtoMapper;
import model.ResumeEntityMapper;
import model.ResumesList;

/**
 * Repository for working with resumes.
 * Implements the {@link IResumeRepository} interface and provides methods for creating, updating, deleting, and retrieving resumes.
 */
public record ResumeRepository (Stage.SessionFactory sessionFactory) implements IResumeRepository {

    /**
     * Creates a new resume.
     *
     * @param resume the {@link ResumeDTO} object containing resume data.
     * @return a {@link Future} containing the created resume.
     */
    @Override
    public Future<ResumeDTO> createResume(ResumeDTO resume) {
        ResumeEntityMapper entityMapper = new ResumeEntityMapper();
        Resume entity = entityMapper.apply(resume);

        return Future.fromCompletionStage(
            sessionFactory.withTransaction((s, t) -> s.persist(entity))
                .exceptionally(ex -> {
                    ex.printStackTrace();  // Print exception for debugging
                    throw new RuntimeException("Error saving resume", ex);
                })
        ).map(v -> new ResumeDtoMapper().apply(entity));
    }

    /**
     * Updates a resume.
     *
     * @param resume the {@link ResumeDTO} object containing the updated resume data.
     * @return a {@link Future} containing the updated resume data.
     */
    @Override
    public Future<ResumeDTO> updateResume(ResumeDTO resume) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaUpdate<Resume> criteriaUpdate = criteriaBuilder.createCriteriaUpdate(Resume.class);
        Root<Resume> root = criteriaUpdate.from(Resume.class);

        Predicate predicate = criteriaBuilder.equal(root.get("id"), resume.id());

        // Update necessary fields
        criteriaUpdate.set("fullName", resume.fullName());
        criteriaUpdate.set("position", resume.position());
        criteriaUpdate.set("objective", resume.objective());
        criteriaUpdate.set("education", resume.education());
        criteriaUpdate.set("workExperience", resume.workExperience());
        criteriaUpdate.set("skillsAndAwards", resume.skillsAndAwards());
        criteriaUpdate.set("languages", resume.languages());
        criteriaUpdate.set("recommendations", resume.recommendations());
        criteriaUpdate.set("hobbiesAndInterests", resume.hobbiesAndInterests());
        criteriaUpdate.set("template", resume.template());
        criteriaUpdate.set("updatedAt", new Date());

        criteriaUpdate.where(predicate);
        CompletionStage<Integer> result = sessionFactory.withTransaction((s, t) -> s.createQuery(criteriaUpdate).executeUpdate());

        Future<ResumeDTO> future = Future.fromCompletionStage(result).map(r -> resume);
        return future;
    }

    /**
     * Deletes a resume by its ID.
     *
     * @param id the unique identifier of the resume.
     * @return a {@link Future} indicating successful deletion of the resume.
     */
    @Override
    public Future<Void> removeResume(UUID id) {
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaDelete<Resume> criteriaDelete = criteriaBuilder.createCriteriaDelete(Resume.class);
        Root<Resume> root = criteriaDelete.from(Resume.class);
        Predicate predicate = criteriaBuilder.equal(root.get("id"), id); //id == [id]
        criteriaDelete.where(predicate);

        CompletionStage<Integer> result = sessionFactory.withTransaction((s,t) -> s.createQuery(criteriaDelete).executeUpdate());
        Future<Void> future = Future.fromCompletionStage(result).compose(r -> Future.succeededFuture());
        return future;
    }

    /**
     * Finds a resume by its ID.
     *
     * @param id the unique identifier of the resume.
     * @return a {@link Future} containing an {@link Optional} with the found resume or an empty value.
     */
    @Override
    public Future<Optional<ResumeDTO>> findResumeById(UUID id) {
        ResumeDtoMapper dtoMapper = new ResumeDtoMapper();
        CompletionStage<Resume> result = sessionFactory().withTransaction((s,t) -> s.find(Resume.class, id));
        Future<Optional<ResumeDTO>> future = Future.fromCompletionStage(result)
                                                    .map(r -> Optional.ofNullable(r))
                                                    .map(r -> r.map(dtoMapper));
        return future;
    }

    /**
     * Finds all resumes for a user by their ID.
     *
     * @param userId the unique identifier of the user.
     * @return a {@link Future} containing a list of resumes {@link ResumesList}.
     */
    @Override
    public Future<ResumesList> findResumeByUserId(UUID userId) {
        ResumeDtoMapper dtoMapper = new ResumeDtoMapper();
        CriteriaBuilder criteriaBuilder = sessionFactory.getCriteriaBuilder();
        CriteriaQuery<Resume> criteriaQuery = criteriaBuilder.createQuery(Resume.class);
        Root<Resume> root = criteriaQuery.from(Resume.class);
        Predicate predicate = criteriaBuilder.equal(root.get("user").get("id"), userId);
        criteriaQuery.where(predicate);
        CompletionStage<List<Resume>> result = sessionFactory().withTransaction((s,t)-> s.createQuery(criteriaQuery).getResultList());
        Future<ResumesList> future = Future.fromCompletionStage(result).map(list -> list.stream().map(dtoMapper).collect(Collectors.toList()))
                .map(list -> new ResumesList(list));
        return future;
    }

}
