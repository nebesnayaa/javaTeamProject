package model;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

import org.hibernate.sql.Template;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import javax.naming.Name;

@Entity
@Table(name = "resume")
public class Resume {

	@Id @GeneratedValue

  UUID id;

  String fullName;

  String position;

  String objective;

  String education;

  String workExperience;

  String skillsAndAwards;

  String languages;

  String recommendations;

  String hobbiesAndInterests;

	private Integer template;

	Date createdAt;

	Date updatedAt;

	@ManyToOne
	@JoinColumn(name="user_id", nullable=true)
	private User user;

	public void setUser(User user) {
		this.user =user;
	}

	public UserDTO getUser() {
		return new UserDtoMapper().apply(user);
	}

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public String getPosition() {
    return position;
  }

  public void setPosition(String position) {
    this.position = position;
  }

  public String getObjective() {
    return objective;
  }

  public void setObjective(String objective) {
    this.objective = objective;
  }

  public String getEducation() {
    return education;
  }

  public void setEducation(String education) {
    this.education = education;
  }

  public String getWorkExperience() {
    return workExperience;
  }

  public void setWorkExperience(String workExperience) {
    this.workExperience = workExperience;
  }

  public String getSkillsAndAwards() {
    return skillsAndAwards;
  }

  public void setSkillsAndAwards(String skillsAndAwards) {
    this.skillsAndAwards = skillsAndAwards;
  }

  public String getLanguages() {
    return languages;
  }

  public void setLanguages(String languages) {
    this.languages = languages;
  }

  public String getRecommendations() {
    return recommendations;
  }

  public void setRecommendations(String recommendations) {
    this.recommendations = recommendations;
  }

  public String getHobbiesAndInterests() {
    return hobbiesAndInterests;
  }

  public void setHobbiesAndInterests(String hobbiesAndInterests) {
    this.hobbiesAndInterests = hobbiesAndInterests;
  }

  public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Integer getTemplateId() {
		return this.template;
	}

	public void setTemplateId(Integer template) {
		this.template = template;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}
}
