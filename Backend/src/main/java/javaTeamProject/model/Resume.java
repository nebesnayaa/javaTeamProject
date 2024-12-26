package javaTeamProject.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "resume")
public class Resume {

	@Id @GeneratedValue
	
	Integer id;
	
	String content;
	
	Integer templateId;
	
	LocalDateTime createdAt;
	
	LocalDateTime updatedAt;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=true)
	private User user;
	
	public void setUser(User user) {
		this.user =user;
	}
	
	public UserDTO getUser() {
		return new UserDtoMapper().apply(user);
	}
	
	public Integer getId() {
		return id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getContent() {
		return this.content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Integer getTemplateId() {
		return this.templateId;
	}
	
	public void setTemplateId(Integer templateId) {
		this.templateId = templateId;
	}
	
	public LocalDateTime getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
