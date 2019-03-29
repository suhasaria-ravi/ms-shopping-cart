package per.ravisu.rest.ws.shoppingcart.customer.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepo extends JpaRepository<Customer, Long> {
	
	List<Customer> findAll();

}
