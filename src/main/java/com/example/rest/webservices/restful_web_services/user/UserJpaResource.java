package com.example.rest.webservices.restful_web_services.user;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;

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

import com.example.rest.webservices.restful_web_services.jpa.PostRepository;
import com.example.rest.webservices.restful_web_services.jpa.UserRepository;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.Valid;

@RestController
public class UserJpaResource {

	private UserRepository repository;
	private PostRepository postRepository;


	public UserJpaResource(UserRepository repository, PostRepository postRepository) {
		this.repository = repository;
		this.postRepository = postRepository;
	}

	@GetMapping("jpa/users")
	public List<User> retrieveAllUsers() {
		return repository.findAll();
	}

	// HATEOAS
	@GetMapping("jpa/users/{userId}")
	public EntityModel<User> retrieveUser(@PathVariable int userId) {

		Optional<User> user = repository.findById(userId);

		if (user.isEmpty()) {
			throw new UserNotFoundException("user id:" + userId);
		}

		// To use HATEOAS, convert the entity into EntityModel
		EntityModel<User> entityModel = EntityModel.of(user.get());

		// Create link for entityModel
		WebMvcLinkBuilder link = linkTo(methodOn(this.getClass()).retrieveAllUsers());

		// Add the link to the model
		entityModel.add(link.withRel("retrieve-all-users"));
		return entityModel;
	}

	@PostMapping("jpa/users")
	public ResponseEntity<Object> createUser(@Valid @RequestBody User user) {
		/**
		 * because of @JsonProperty("user_name") and @JsonProperty("birth_date") the
		 * json input should be "user_name" instead of "name" and "birth_date" instead
		 * of "birthDate"
		 */
		User savedUser = repository.save(user);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}")
				.buildAndExpand(savedUser.getId()).toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping("jpa/users/{userId}")
	public void deleteUser(@PathVariable int userId) {
		repository.deleteById(userId);
	}

	@GetMapping("jpa/users/{userId}/posts")
	public List<Post> retrievePostsForUser(@PathVariable int userId) {
		Optional<User> user = repository.findById(userId);

		if (user.isEmpty()) {
			throw new UserNotFoundException("user id:" + userId);
		}
		return user.get().getPosts();
	}
	
	@PostMapping("jpa/users/{userId}/posts")
	public ResponseEntity<Object> createPostForUser(@PathVariable int userId, @Valid @RequestBody Post post) {
		Optional<User> user = repository.findById(userId);

		if (user.isEmpty()) {
			throw new UserNotFoundException("user id:" + userId);
		}
		
		post.setUser(user.get());
		Post savedPost = postRepository.save(post);
		
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{userId}")
				.buildAndExpand(savedPost.getId()).toUri();
	
		return ResponseEntity.created(location).build();
	}
}
