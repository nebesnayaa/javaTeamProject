package model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name ="template")
public class Tameplate {
	@Id @GeneratedValue
	Integer id;
	String name;
	String layout;
	
	@OneToMany(mappedBy="template")
	List<Resume> resumes;
	
	public Integer getId() {
		return this.id;
	}
	
	public void setId(Integer id) {
		this.id = id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getLayout() {
		return this.layout;
	}
	
	public void setLayout(String layout) {
		this.layout = layout;
	}
}
