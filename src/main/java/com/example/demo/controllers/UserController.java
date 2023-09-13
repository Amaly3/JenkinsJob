package com.example.demo.controllers;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api/user")
public class UserController {

	//private static final Logger log = LoggerFactory.getLogger(UserController.class);
	Logger log = LogManager.getLogger(UserController.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@GetMapping("/id/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		log.info("User Controller : findById : Response is ");
		Optional<User> user = userRepository.findById(id);
		if(!user.isPresent()) {
			log.error("user is not found : Status Code : "+ResponseEntity.status(HttpStatus.NOT_FOUND).build().getStatusCode());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		log.info("user is found Status Code : "+ResponseEntity.of(userRepository.findById(id)).getStatusCode());
		log.info("ID : "+ResponseEntity.of(userRepository.findById(id)).getBody().getId());
		log.info("USERNAME : "+ResponseEntity.of(userRepository.findById(id)).getBody().getUsername());
		log.info("PASSWORD : "+ResponseEntity.of(userRepository.findById(id)).getBody().getPassword());
		return ResponseEntity.of(userRepository.findById(id));
	}

	@GetMapping("/{username}")
	public ResponseEntity<User> findByUserName(@PathVariable String username) {
		User user = userRepository.findByUsername(username);
		log.info("User Controller : findByUserName : Response is ");
		if (user == null) {
			log.error("User is not found : Status Code : "+ResponseEntity.notFound().build().getStatusCode());
			return ResponseEntity.notFound().build();
		}
		else{
			log.info("user is found : Status Code : "+ResponseEntity.ok(user).getStatusCode());
			log.info("ID is : "+ResponseEntity.ok(user).getBody().getId());
			log.info("USERNAME is : "+ResponseEntity.ok(user).getBody().getUsername());
			log.info("PASSWORD is : "+ResponseEntity.ok(user).getBody().getPassword());
			return ResponseEntity.ok(user);
		}
	}

	@PostMapping("/create")
	public ResponseEntity<User> createUser(@RequestBody CreateUserRequest createUserRequest) {

		log.info("User Controller : createUser Response is ");
		User user = new User();

		user.setUsername(createUserRequest.getUsername());
		Cart cart = new Cart();
		cartRepository.save(cart);
		user.setCart(cart);
		if(createUserRequest.getPassword().length()<7 ||
				!createUserRequest.getPassword().equals(createUserRequest.getConfirmPassword())){
			//System.out.println("Error - Either length is less than 7 or pass and conf pass do not match. Unable to create ",
			//		createUserRequest.getUsername());
			log.error("Error - Either length is less than 7 or pass and conf pass do not match. Unable to create user with Status Code "+ ResponseEntity.badRequest().build().getStatusCode());
			return ResponseEntity.badRequest().build();
		}
		user.setPassword(bCryptPasswordEncoder.encode(createUserRequest.getPassword()));
		try {
			userRepository.save(user);
		} catch (Exception e){
			e.printStackTrace();
			log.fatal("Unable to create user.", e);
		}
		log.info("user is Successfuly created Status Code : "+ResponseEntity.ok(user).getStatusCode());
		log.info("ID is : "+ResponseEntity.ok(user).getBody().getId());
		log.info("USERNAME is : "+ResponseEntity.ok(user).getBody().getUsername());
		log.info("PASSWORD is : "+ResponseEntity.ok(user).getBody().getPassword());
		return ResponseEntity.ok(user);
	}

}
