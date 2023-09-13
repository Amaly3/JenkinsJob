package com.example.demo.controllers;

import java.util.Optional;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;

	//private static final Logger log = LoggerFactory.getLogger(CartController.class);
	Logger log = LogManager.getLogger(CartController.class);

	@PostMapping("/addToCart")
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		log.info("Cart Controller : addTocart : Response is ");

		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.error("user is not found : Status Code : "+ResponseEntity.status(HttpStatus.NOT_FOUND).build().getStatusCode());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("item not found : Status Code : "+ResponseEntity.status(HttpStatus.NOT_FOUND).build().getStatusCode());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.addItem(item.get()));

		try {
			cartRepository.save(cart);
		} catch (Exception e){
			e.printStackTrace();
			log.fatal("Unable to add cart. ", e);
		}
		log.info("Status Code : "+ResponseEntity.ok(cart).getStatusCode());
		log.info(" Cart ID : " + ResponseEntity.ok(cart).getBody().getId());
		log.info(" Cart UserName : " + ResponseEntity.ok(cart).getBody().getUser());
		int i;
		for (i = 0; ResponseEntity.ok(cart).getBody().getItems().size()>i; i++) {
			log.info(" Item ID : " + ResponseEntity.ok(cart).getBody().getItems().get(i).getId());
			log.info(" Item NAME : " + ResponseEntity.ok(cart).getBody().getItems().get(i).getName());
			log.info(" Item Description : " + ResponseEntity.ok(cart).getBody().getItems().get(i).getDescription());
			log.info(" Item PRICE: " + ResponseEntity.ok(cart).getBody().getItems().get(i).getPrice());

		}
		log.info(" Cart Total : " + ResponseEntity.ok(cart).getBody().getTotal());

		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/removeFromCart")
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		log.info("Cart Controller : removeFromcart : Response is ");
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			log.error("user is not found : Status Code : "+ResponseEntity.status(HttpStatus.NOT_FOUND).build().getStatusCode());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			log.error("item not found : Status Code : "+ResponseEntity.status(HttpStatus.NOT_FOUND).build().getStatusCode());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity())
			.forEach(i -> cart.removeItem(item.get()));
		log.info(" Removed Item  : ");
		log.info(" Item ID : " + item.get().getId());
		log.info(" Item NAME : " + item.get().getName());
		log.info(" Item Description : " + item.get().getDescription());
		log.info(" Item PRICE: " + item.get().getPrice());

		try {
			cartRepository.save(cart);
		} catch (Exception e){
			e.printStackTrace();
			log.fatal("Unable to save cart. ", e);
		}


		log.info("Status Code : "+ResponseEntity.ok(cart).getStatusCode());
		log.info(" Cart ID : " + ResponseEntity.ok(cart).getBody().getId());
		log.info(" Cart UserName : " + ResponseEntity.ok(cart).getBody().getUser());
		int i;
		for (i = 0; ResponseEntity.ok(cart).getBody().getItems().size()>i; i++) {
			log.info(" Item ID : " + ResponseEntity.ok(cart).getBody().getItems().get(i).getId());
			log.info(" Item NAME : " + ResponseEntity.ok(cart).getBody().getItems().get(i).getName());
			log.info(" Item Description : " + ResponseEntity.ok(cart).getBody().getItems().get(i).getDescription());
			log.info(" Item PRICE: " + ResponseEntity.ok(cart).getBody().getItems().get(i).getPrice());

		}
		log.info(" Cart Total : " + ResponseEntity.ok(cart).getBody().getTotal());
		return ResponseEntity.ok(cart);
	}
		
}
