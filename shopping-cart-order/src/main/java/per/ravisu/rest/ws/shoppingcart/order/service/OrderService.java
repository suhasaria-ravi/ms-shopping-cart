package per.ravisu.rest.ws.shoppingcart.order.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
import per.ravisu.rest.ws.shoppingcart.order.exception.OrderServiceException;
import per.ravisu.rest.ws.shoppingcart.order.helper.CustomerProxyObj;
import per.ravisu.rest.ws.shoppingcart.order.helper.CustomerServiceProxy;
import per.ravisu.rest.ws.shoppingcart.order.helper.ItemProxyObject;
import per.ravisu.rest.ws.shoppingcart.order.helper.ItemServiceProxy;
import per.ravisu.rest.ws.shoppingcart.order.model.CustomerOrder;
import per.ravisu.rest.ws.shoppingcart.order.model.Order;
import per.ravisu.rest.ws.shoppingcart.order.model.OrderRepo;
import javax.persistence.LockModeType;

@Configuration
@RestController
public class OrderService {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private Environment environment;

	@Autowired
	private ItemServiceProxy itemProxy;

	@Autowired
	private CustomerServiceProxy customerProxy;

	@Autowired
	private OrderRepo repository;

	@Autowired
	private EntityManagerFactory emf;
	
	
	private static EntityManager emgr;
	
    @Value("${localhost.uri.ravisu}")
    private static String localhostvalue;
    
    @Value("{spring.rabbitmq.host}")
    private static String rabbitMQValue;

	@GetMapping("/shopping-cart/orders")
	public List<Order> retrieveAllOrders() {
		
		logger.info("Localhost value read as : {}", localhostvalue);
		logger.info("rabbitMQValue value read as : {}", rabbitMQValue);

		List<Order> order = repository.findAll();

		logger.info("retrieveAllOrders: {}", order);

		return order;
	}

	@PostMapping("/shopping-cart/order")
	public ResponseEntity<Object> createOrder(@Valid @RequestBody Order order) {
		Order savedOrd = null;
		try {
			ItemProxyObject item = itemProxy.retrieveItem(order.getItemSku());

			logger.info("value of ItemProxyObject: {}", item.toString());

			CustomerProxyObj customer = customerProxy.retrieveCustomer(order.getCustId());

			logger.info("value of CustomerProxyObj: {}", customer.toString());

			// ravisu Order lockedOrder = em.find(Order.class,
			// order.getOrderId(),LockModeType.PESSIMISTIC_READ);
			// ravisu em.persist(entity);
			// ravisu em.flush();
			savedOrd = repository.save(order);

		} catch (Exception e) {
			throw new OrderServiceException(
					"For create order :" + order.toString() + " Exception Message : " + e.getMessage());
		}

		logger.info("createOrder: {}", savedOrd);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(savedOrd.getOrderId()).toUri();

		// To-do (ravisu xx ravisu) -- after create order call ItemService's orderItem
		// method.

		return ResponseEntity.created(location).build();

	}

	@GetMapping("/shopping-cart/order/{id}")
	public Order retrieveOrder(@PathVariable Long id) {
		Order ord = null;

		Optional<Order> order = repository.findById(id);

		if (order == null)
			throw new OrderServiceException("id-" + id);

		try {

			ord = order.get();

		} catch (Exception e) {
			throw new OrderServiceException("For get Order Id:" + id + " Exception Message : " + e.getMessage());
		}

		logger.info("retrieveOrder: {}", ord);

		return ord;
	}

	@PostMapping("/shopping-cart/order-for-existing-customer")
	public Order checkOutExistingCustomer(@Valid @RequestBody Order order) {
		Order savedOrder=null;
		URI location = null;
		CustomerProxyObj customer = null;
		ItemProxyObject itemLocked = null;
		Order lockedOrder = null;
		EntityManager em=getEntityManager();		

		try {
			// validate customer exists
			customer = customerProxy.retrieveCustomer(order.getCustId());

			// order item and obtain lock on the item
			itemLocked = itemProxy.orderItemLock(order.getItemSku(), order.getOrdQuantity());

			// save and lock the order
			if(!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			savedOrder = repository.save(order);
			lockedOrder = em.find(Order.class, savedOrder.getOrderId(), LockModeType.PESSIMISTIC_READ);

			if ((customer == null) || (lockedOrder == null) || (itemLocked == null)) {
				if (customer == null) {
					throw new OrderServiceException("For create order :" + order.toString() + " Exception Message : "
							+ "Customer :" + order.getCustId() + " does not exists");
				}

				if (lockedOrder == null)
					throw new OrderServiceException("For create order :" + order.toString() + " Exception Message : "
							+ "Order :" + order.toString() + " could not be saved");

				if (itemLocked == null)
					throw new OrderServiceException("For create order :" + order.toString() + " Exception Message : "
							+ "Item :" + order.getItemSku() + " could not be saved");
			} else {

				logger.info("value of validated CustomerProxyObj: {}", customer.toString());
				logger.info("value of locked ItemProxyObject: {}", itemLocked.toString());
				logger.info("value of locked Order: {}", lockedOrder.toString());

				// commit all , in case of issues, roll-back all.
				// commit the item order
				ItemProxyObject itemCommited = itemProxy.orderItemCommit(itemLocked);
				logger.info("item commited: {}", itemCommited.toString());

				// commit the order
				em.merge(lockedOrder);				
				em.flush();
				em.getTransaction().commit(); 
				
				logger.info("order commited: {}", lockedOrder.toString());

				return lockedOrder;

			}

		} catch (Exception e) {
			
			if(em.getTransaction()!=null)
			{
				em.getTransaction().rollback();
			}
			
			if (itemLocked != null) {
				// roll back the item order
				ItemProxyObject itemRolledBack = itemProxy.orderItemRollback(itemLocked); 
				logger.info("item itemRolledBack: {}", itemRolledBack.toString());
			}

			if (lockedOrder != null) {
				// roll back the order
				em.getTransaction().rollback();
				logger.info("Order RolledBack: {}", lockedOrder.toString());
			}
			
			if (savedOrder != null) {
				// delete the saved order
				repository.delete(savedOrder);
				logger.info("Order Deleted: {}", savedOrder.toString());
			}

			throw new OrderServiceException(
					"For create order :" + order.toString() + " Exception Message : " + e.getMessage());
		}

		return null;

	}

	
	@PostMapping("/shopping-cart/order-for-new-customer")
	public Order checkOutNewCustomer(@Valid @RequestBody CustomerOrder customerOrder) {
		Order savedOrder=null;
		Order order=null;
		URI location = null;
		CustomerProxyObj customer = null;
		ItemProxyObject itemLocked = null;
		Order lockedOrder = null;
		EntityManager em=getEntityManager();		

		try {
			
			//get customer value
			customer = customerProxy.createCustomer(createCustomerObj(customerOrder));
			
			//get order value
			order = createOrderObj(customerOrder);
			
			if (customer == null) {
				throw new OrderServiceException("For create order :" + order.toString() + " Exception Message : "
						+ "Customer :" + order.getCustId() + " does not exists");
			}

			
			order.setCustId(customer.getId());
			
			// order item and obtain lock on the item
			itemLocked = itemProxy.orderItemLock(order.getItemSku(), order.getOrdQuantity());

			// save and lock the order
			if(!em.getTransaction().isActive()) {
				em.getTransaction().begin();
			}
			savedOrder = repository.save(order);
			lockedOrder = em.find(Order.class, savedOrder.getOrderId(), LockModeType.PESSIMISTIC_READ);

			if ((lockedOrder == null) || (itemLocked == null)) {				

				if (lockedOrder == null)
					throw new OrderServiceException("For create order :" + order.toString() + " Exception Message : "
							+ "Order :" + order.toString() + " could not be saved");

				if (itemLocked == null)
					throw new OrderServiceException("For create order :" + order.toString() + " Exception Message : "
							+ "Item :" + order.getItemSku() + " could not be saved");
			} else {

				logger.info("value of validated CustomerProxyObj: {}", customer.toString());
				logger.info("value of locked ItemProxyObject: {}", itemLocked.toString());
				logger.info("value of locked Order: {}", lockedOrder.toString());

				// commit all , in case of issues, roll-back all.
				// commit the item order
				ItemProxyObject itemCommited = itemProxy.orderItemCommit(itemLocked);
				logger.info("item commited: {}", itemCommited.toString());

				// commit the order
				em.merge(lockedOrder);				
				em.flush();
				em.getTransaction().commit(); 
				
				logger.info("order commited: {}", lockedOrder.toString());				

				return lockedOrder;

			}

		} catch (Exception e) {
			
			if(em.getTransaction()!=null)
			{
				em.getTransaction().rollback();
			}
			
			if (itemLocked != null) {
				// roll back the item order
				ItemProxyObject itemRolledBack = itemProxy.orderItemRollback(itemLocked);
				logger.info("item itemRolledBack: {}", itemRolledBack.toString());
			}

			if (lockedOrder != null) {
				// roll back the order
				em.getTransaction().rollback();
				logger.info("Order RolledBack: {}", lockedOrder.toString());
			}
			
			if (savedOrder != null) {
				// delete the saved order
				repository.delete(savedOrder);
				logger.info("Order Deleted: {}", savedOrder.toString());
			}

			throw new OrderServiceException(
					"For create order :" + order.toString() + " Exception Message : " + e.getMessage());
		}

		return null;

	}

	
	private Order createOrderObj(@Valid CustomerOrder customerOrder) {
		Order o = new Order(null,null,customerOrder.getItemSku(),customerOrder.getOrdQuantity());
		return o;
	}

	private @Valid CustomerProxyObj createCustomerObj(@Valid CustomerOrder customerOrder) {
		CustomerProxyObj c = new CustomerProxyObj(null,customerOrder.getFname(),customerOrder.getLname());		
		return c;
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
