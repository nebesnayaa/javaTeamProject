package model;

import java.util.function.Function;

public class ResumeEntityMapper implements Function<ResumeDTO, Resume> {

	@Override
  public Resume apply(ResumeDTO t) {
    Resume resume = new Resume();
    resume.setId(t.id());
    resume.setCreatedAt(t.createdAt());
    resume.setUpdatedAt(t.updatedAt());
    resume.setTemplateId(t.template());
    resume.setFullName(t.fullName());
    resume.setPosition(t.position());
    resume.setObjective(t.objective());
    resume.setEducation(t.education());
    resume.setWorkExperience(t.workExperience());
    resume.setSkillsAndAwards(t.skillsAndAwards());
    resume.setLanguages(t.languages());
    resume.setRecommendations(t.recommendations());
    resume.setHobbiesAndInterests(t.hobbiesAndInterests());
    t.user().ifPresent(userDTO -> {
      User user = new UserEntityMapper().apply(userDTO);
      resume.setUser(user);
    });
    return resume;
  }

}
