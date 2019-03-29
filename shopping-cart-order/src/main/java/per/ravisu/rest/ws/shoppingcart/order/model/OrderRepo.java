package per.ravisu.rest.ws.shoppingcart.order.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepo extends JpaRepository<Order, Long> {
	
	List<Order> findAll();

}
