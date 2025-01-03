package model;

import java.time.LocalDateTime;
import java.util.Date;

import org.hibernate.sql.Template;

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
	
	Date createdAt;
	
	Date updatedAt;
	
	@ManyToOne
	@JoinColumn(name="user_id", nullable=true)
	private User user;
	
	/*@ManyToOne
	@JoinColumn(name= "template_id", nullable=false)
	private Template template;*/
	
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
