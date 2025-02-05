package com.example.rest.webservices.restful_web_services.user;

import java.time.LocalDate;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

@Entity(name = "user_details") // for h2 mapping. User is already a keyword of h2.
public class User {

	protected User() {

	}

	@Id // h2
	@GeneratedValue // h2
	private Integer id;

	@JsonProperty("user_name") // Customize this field for JSON Response
	@Size(min = 2, message = "name should have at least 2 characters")
	private String name;

	@JsonProperty("birth_date")
	@Past(message = "birth date should be in the past")
	private LocalDate birthDate;

	@OneToMany(mappedBy = "user")
	@JsonIgnore // Exclude this field from JSONResponse
	private List<Post> posts;

	public User(Integer id, String name, LocalDate birthDate) {
		super();
		this.id = id;
		this.name = name;
		this.birthDate = birthDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public List<Post> getPosts() {
		return posts;
	}

	public void setPosts(List<Post> posts) {
		this.posts = posts;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", birthDate=" + birthDate + "]";
	}

}
