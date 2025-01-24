package com.example.rest.webservices.restful_web_services.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
public class UserResource {

	private UserDaoService service;

	public UserResource(UserDaoService service) {
		this.service = service;
	}

	@GetMapping("users")
	public List<User> retrieveAllUsers() {
		return service.findAll();
	}

	
	// HATEOAS
	@GetMapping("users/{userId}")
	public EntityModel<User> retrieveUser(@PathVariable int userId) {
		User user = service.findOne(userId);
		if(user==null) {
			throw new UserNotFoundException("user id:"+userId);
		}
		
		// To use HATEOAS, convert the entity into EntityModel
		EntityModel<User> entityModel = EntityModel.of(user);
		
		// Create link for entityModel
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());
		
		// Add the link to the model
		entityModel.add(link.withRel("retrieve-all-users"));
		
		
		return entityModel;
	}

	@PostMapping("users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		User savedUser = service.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{userId}")
				.buildAndExpand(savedUser.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("users/{userId}")
	public void deleteUser(@PathVariable int userId) {
		service.deleteById(userId);
	}
}
