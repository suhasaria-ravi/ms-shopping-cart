package per.ravisu.rest.ws.shoppingcart.customer.service;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import per.ravisu.rest.ws.shoppingcart.customer.exception.CustomerServiceException;
import per.ravisu.rest.ws.shoppingcart.customer.model.Customer;
import per.ravisu.rest.ws.shoppingcart.customer.model.CustomerRepo;

@RestController
public class CustomerService {

	private Logger logger = LoggerFactory.getLogger(CustomerService.class);

	@Autowired
	private Environment environment;

	@Autowired
	private CustomerRepo repository;

	private static String eurekaURL = System.getProperty("eureka.client.service-url.default-zone");

	private static String rabbitMQHost = System.getProperty("spring.rabbitmq.host");

	@GetMapping("/shopping-cart/customers")
	public List<Customer> retrieveAllCustomers() {

		logger.info("ravisuxxx rabbitMQHost value read as : {}", rabbitMQHost);
		logger.info("ravisuxxx eurekaURL value read as : {}", eurekaURL);

		List<Customer> customer = repository.findAll();

		logger.info("retrieveAllCustomers: {}", customer);

		return customer;
	}

	@PostMapping("/shopping-cart/customer")
	public Customer createCustomer(@Valid @RequestBody Customer customer) {
		Customer savedCust = null;
		try {

			savedCust = repository.save(customer);
		} catch (Exception e) {
			throw new CustomerServiceException(
					"For create customer :" + customer.toString() + " Exception Message : " + e.getMessage());
		}

		logger.info("createCustomer: {}", savedCust);

		return savedCust;

	}

	@GetMapping("/shopping-cart/customer/{id}")
	public Customer retrieveCustomer(@PathVariable Long id) {
		Customer cust = null;

		Optional<Customer> customer = repository.findById(id);

		if (customer == null)
			throw new CustomerServiceException("id-" + id);
		try {
			cust = customer.get();
		} catch (Exception e) {
			throw new CustomerServiceException("For get Customer Id:" + id + " Exception Message : " + e.getMessage());
		}

		logger.info("retrieveCustomer: {}", cust);

		return cust;
	}

}
