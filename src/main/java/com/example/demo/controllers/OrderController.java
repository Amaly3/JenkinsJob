package com.example.demo.controllers;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;

@RestController
@RequestMapping("/api/order")
public class OrderController {
	
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private OrderRepository orderRepository;

	Logger log = LogManager.getLogger(OrderController.class);


	@PostMapping("/submit/{username}")
	public ResponseEntity<UserOrder> submit(@PathVariable String username) {
		log.info("Order Controller : submit : Response is ");

		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("user is not found : Status Code : "+ResponseEntity.status(HttpStatus.NOT_FOUND).build().getStatusCode());
			return ResponseEntity.notFound().build();
		}
		UserOrder order = UserOrder.createFromCart(user.getCart());
		try {
			orderRepository.save(order);
		} catch (Exception e){
			e.printStackTrace();
			log.fatal("Unable to submit order.", e);
		}
		log.info("request submitted successfuly Status Code : "+ResponseEntity.ok(order).getStatusCode());
		log.info(" Order ID : " + ResponseEntity.ok(order).getBody().getId());
		log.info(" Order UserName : " + ResponseEntity.ok(order).getBody().getUser());
		int i;
		for (i = 0; ResponseEntity.ok(order).getBody().getItems().size()>i; i++) {
			log.info(" Item ID : " + ResponseEntity.ok(order).getBody().getItems().get(i).getId());
			log.info(" Item NAME : " + ResponseEntity.ok(order).getBody().getItems().get(i).getName());
			log.info(" Item Description : " + ResponseEntity.ok(order).getBody().getItems().get(i).getDescription());
			log.info(" Item PRICE: " + ResponseEntity.ok(order).getBody().getItems().get(i).getPrice());

		}
		log.info(" Order Total : " + ResponseEntity.ok(order).getBody().getTotal());
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/history/{username}")
	public ResponseEntity<List<UserOrder>> getOrdersForUser(@PathVariable String username) {
		log.info("Order Controller : getOrdersForUser : Response is ");
		User user = userRepository.findByUsername(username);
		if(user == null) {
			log.error("user is not found : Status Code : "+ResponseEntity.status(HttpStatus.NOT_FOUND).build().getStatusCode());
			return ResponseEntity.notFound().build();
		}
		log.info("order is found Status Code : "+ResponseEntity.ok(orderRepository.findByUser(user)).getStatusCode());
		int i;
		for (i = 0; ResponseEntity.ok(orderRepository.findByUser(user)).getBody().size()>i; i++) {
			log.info(" ID : " + ResponseEntity.ok(orderRepository.findByUser(user)).getBody().get(i).getId());
			log.info(" User : " + ResponseEntity.ok(orderRepository.findByUser(user)).getBody().get(i).getUser());
			int x;
			for (x = 0; ResponseEntity.ok(orderRepository.findByUser(user)).getBody().get(i).getItems().size()>x; x++) {
				log.info(" ID : " + ResponseEntity.ok(orderRepository.findByUser(user)).getBody().get(i).getItems().get(x).getId());
				log.info(" NAME : " + ResponseEntity.ok(orderRepository.findByUser(user)).getBody().get(i).getItems().get(x).getName());
				log.info(" Description : " + ResponseEntity.ok(orderRepository.findByUser(user)).getBody().get(i).getItems().get(x).getDescription());
				log.info(" PRICE: " + ResponseEntity.ok(orderRepository.findByUser(user)).getBody().get(i).getItems().get(x).getPrice());

			}
			log.info(" PRICE: " + ResponseEntity.ok(orderRepository.findByUser(user)).getBody().get(i).getTotal());
		}


		return ResponseEntity.ok(orderRepository.findByUser(user));
	}
}
