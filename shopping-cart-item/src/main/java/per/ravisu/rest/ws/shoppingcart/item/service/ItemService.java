package per.ravisu.rest.ws.shoppingcart.item.service;


import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.LockModeType;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import per.ravisu.rest.ws.shoppingcart.item.exception.ItemServiceException;
import per.ravisu.rest.ws.shoppingcart.item.model.Item;
import per.ravisu.rest.ws.shoppingcart.item.model.ItemRepo;

@Configuration
@RestController
public class ItemService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment environment;

	@Autowired
	private ItemRepo repository;
	
	@Autowired
	private  EntityManagerFactory emf;
	
	private static EntityManager emgr;
	
    @Value("${localhost.uri.ravisu}")
    private static String localhostvalue;
    
    @Value("{spring.rabbitmq.host}")
    private static String rabbitMQValue;

	@GetMapping("/shopping-cart/items")
	public List<Item> retrieveAllItems() {
		
		logger.info("Localhost value read as : {}", localhostvalue);
		logger.info("rabbitMQValue value read as : {}", rabbitMQValue);

		List<Item> item = repository.findAll();

		logger.info("retrieveAllItems: {}", item);

		return item;
	}

	@PostMapping("/shopping-cart/item")
	public ResponseEntity<Object> createOrUpdateItem(@Valid @RequestBody Item item) {
		Item savedItem = null;
		
		try {
			
			savedItem = repository.save(item);
			
		} catch (Exception e) {
			throw new ItemServiceException(
					"For create item :" + item.toString() + " Exception Message : " + e.getMessage());
		}

		logger.info("createItem: {}", savedItem);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(savedItem.getSku())
				.toUri();

		return ResponseEntity.created(location).build();

	}

	@GetMapping("/shopping-cart/item/{id}")
	public Item retrieveItem(@PathVariable Long id) {
		Item itm = null;

		Optional<Item> item = repository.findById(id);

		if (item == null)
			throw new ItemServiceException("id-" + id);
		try {
			itm = item.get();
		} catch (Exception e) {
			throw new ItemServiceException("For get Item Id:" + id + " Exception Message : " + e.getMessage());
		}

		logger.info("retrieveItem: {}", item);

		return itm;
	}

	@PostMapping("/shopping-cart/item/{id}/quantity/{quant}")
	public Item orderItem(@PathVariable Long id, @Valid @PathVariable  Long quant) {
		
		Item itm = null;
		Item savedItem;
		
		if(quant<=0) {
			throw new ItemServiceException(" Exception Message : " + "Invalid Order Quantity in input");
		}

		Optional<Item> item = repository.findById(id);

		if (item == null)
			throw new ItemServiceException("id-" + id);
		try {
			itm = item.get();
		} catch (Exception e) {
			throw new ItemServiceException("For order Item Id:" + id + " Exception Message : " + e.getMessage());
		}
		
		logger.info("orderItem - existing Item: {}", itm);
		
		if (itm.getQuantity() <quant) {
			throw new ItemServiceException("For order Item Id :" + id + " Exception Message : Available Quantity :"
					+ itm.getQuantity() + " is Less than Qrdered Quanity :"+quant);
		}
		else {
			itm.setQuantity(itm.getQuantity()-quant);
			savedItem = repository.save(itm);
		}

		logger.info("orderItem - saved Item: {}", savedItem);

		return savedItem;
	}
	
	
	
	@PostMapping("/shopping-cart/item-lock/{id}/quantity/{quant}")
	public Item orderItemLock(@PathVariable Long id, @Valid @PathVariable  Long quant) {
				
		EntityManager em=getEntityManager();
		
		Item itm = null;
		Item lockedItem;
		
		if(quant<=0) {
			throw new ItemServiceException(" Exception Message : " + "Invalid Order Quantity in input");
		}

		Optional<Item> item = repository.findById(id);

		if (item == null)
			throw new ItemServiceException("id-" + id);
		try {
			itm = item.get();
		} catch (Exception e) {
			throw new ItemServiceException("For order Item Id:" + id + " Exception Message : " + e.getMessage());
		}
		
		logger.info("orderItem - existing Item: {}", itm);
		
		if (itm.getQuantity() <quant) {
			throw new ItemServiceException("For order Item Id :" + id + " Exception Message : Available Quantity :"
					+ itm.getQuantity() + " is Less than Qrdered Quanity :"+quant);
		}
		else {			

			if(!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			//lock the item
			lockedItem = em.find(Item.class, itm.getSku(), LockModeType.PESSIMISTIC_READ);		
			//set new values to the item
			lockedItem.setQuantity(itm.getQuantity()-quant);
		}

		logger.info("Item Locked: {}", lockedItem);

		return lockedItem;
	}

	@PostMapping("/shopping-cart/item-commit")
	public Item orderItemCommit(@Valid @RequestBody Item item) {
		
		EntityManager em=getEntityManager();
		
		//commit the locked item
		em.merge(item);
		em.flush();
		em.getTransaction().commit();
		return item; 
	}
	
	@PostMapping("/shopping-cart/item-roll-back")
	public Item orderItemRollback(@Valid @RequestBody Item item) {
		
		EntityManager em=getEntityManager();
		
		//rollback the locked item
		if(em.getTransaction()!=null)
		{
			em.getTransaction().rollback();
		}
		
		return item;
	}
	
	@Bean
	public EntityManager getEntityManager() {
		EntityManager em;
		if(emgr==null) {
			em = emf.createEntityManager();
		}
		else
		{
			em=emgr;
		}
		return em;
	}
	
}
