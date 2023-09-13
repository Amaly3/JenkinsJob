package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;

@RestController
@RequestMapping("/api/item")
public class ItemController {

	@Autowired
	private ItemRepository itemRepository;

//private static final Logger log = LoggerFactory.getLogger(ItemController.class);
Logger log = LogManager.getLogger(ItemController.class);
	@GetMapping
	public ResponseEntity<List<Item>> getItems() {
		log.info("Item Controller : getItems : Response is ");
		log.info("Status Code : "+ResponseEntity.ok(itemRepository.findAll()).getStatusCode());
		int i;
		for (i = 0; ResponseEntity.ok(itemRepository.findAll()).getBody().size()>i; i++) {
			log.info(" ID : " + ResponseEntity.ok(itemRepository.findAll()).getBody().get(i).getId());
			log.info(" NAME : " + ResponseEntity.ok(itemRepository.findAll()).getBody().get(i).getName());
			log.info(" Description : " + ResponseEntity.ok(itemRepository.findAll()).getBody().get(i).getDescription());
			log.info(" PRICE: " + ResponseEntity.ok(itemRepository.findAll()).getBody().get(i).getPrice());

		}
		return ResponseEntity.ok(itemRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Item> getItemById(@PathVariable Long id) {
		log.info("Item Controller : getItemById : Response is ");
		Optional<Item> item = itemRepository.findById(id);
		if(!item.isPresent()) {
			log.error("item not found : Status Code : "+ResponseEntity.status(HttpStatus.NOT_FOUND).build().getStatusCode());
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}

		log.info("Status Code : "+ResponseEntity.of(itemRepository.findById(id)).getStatusCode());
		log.info(" ID : " + ResponseEntity.of(itemRepository.findById(id)).getBody().getId());
		log.info(" NAME : " + ResponseEntity.of(itemRepository.findById(id)).getBody().getName());
		log.info(" Description : " + ResponseEntity.of(itemRepository.findById(id)).getBody().getDescription());
		log.info(" PRICE: " + ResponseEntity.of(itemRepository.findById(id)).getBody().getPrice());

		return ResponseEntity.of(itemRepository.findById(id));
	}
	
	@GetMapping("/name/{name}")
	public ResponseEntity<List<Item>> getItemsByName(@PathVariable String name) {
		List<Item> items = itemRepository.findByName(name);
		log.info("Item Controller : getItemsByName : Response is ");
		if (items == null || items.isEmpty()) {
			log.error("Item is not found : Status Code : "+ResponseEntity.notFound().build().getStatusCode());
			return ResponseEntity.notFound().build();
		}
		else{
			log.info(" Found Items : Status Code : "+ResponseEntity.ok(items).getStatusCode());
			int i;
			for (i = 0; ResponseEntity.ok(items).getBody().size()>i; i++) {
				log.info(" ID : " + ResponseEntity.ok(items).getBody().get(i).getId());
				log.info(" NAME : " + ResponseEntity.ok(items).getBody().get(i).getName());
				log.info(" Description : " + ResponseEntity.ok(items).getBody().get(i).getDescription());
				log.info(" PRICE: " + ResponseEntity.ok(items).getBody().get(i).getPrice());
			}
			return ResponseEntity.ok(items);
		}
			
	}
	
}
