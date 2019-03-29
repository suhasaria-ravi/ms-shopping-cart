package per.ravisu.rest.ws.shoppingcart.item.model;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepo extends JpaRepository<Item, Long> {
	
	List<Item> findAll();

}
