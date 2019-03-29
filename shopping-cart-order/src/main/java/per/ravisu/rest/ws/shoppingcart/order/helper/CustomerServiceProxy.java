package per.ravisu.rest.ws.shoppingcart.order.helper;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient(name="shopping-cart-customer")
@RibbonClient(name="shopping-cart-customer")
public interface CustomerServiceProxy {
	
	@GetMapping("/shopping-cart/customer/{id}") 
	public CustomerProxyObj retrieveCustomer(@PathVariable Long id) ;
	
	@PostMapping("/shopping-cart/customer")
	public CustomerProxyObj createCustomer(@Valid @RequestBody CustomerProxyObj customer);
}



