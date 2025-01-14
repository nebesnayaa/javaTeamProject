package model;

import java.util.Optional;
import java.util.function.Function;

public class ResumeDtoMapper implements Function<Resume, ResumeDTO> {

	@Override
  public ResumeDTO apply(Resume t) {
    return new ResumeDTO(
      t.getId(),
      t.getTemplateId(),
      t.getCreatedAt(),
      t.getUpdatedAt(),
      Optional.ofNullable(t.getUser()),
      t.getFullName(),
      t.getPosition(),
      t.getObjective(),
      t.getEducation(),
      t.getWorkExperience(),
      t.getSkillsAndAwards(),
      t.getLanguages(),
      t.getRecommendations(),
      t.getHobbiesAndInterests()
    );
  }

}
