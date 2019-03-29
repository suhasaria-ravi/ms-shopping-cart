package per.ravisu.rest.ws.shoppingcart.order.helper;

import org.springframework.cloud.openfeign.FeignClient;

import javax.validation.Valid;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@FeignClient(name="shopping-cart-item")
@RibbonClient(name="shopping-cart-item")
public interface ItemServiceProxy {
	
	@GetMapping("/shopping-cart/item/{id}") 
	public ItemProxyObject retrieveItem(@PathVariable Long id);
	
	@PostMapping("/shopping-cart/item-lock/{id}/quantity/{quant}")
	public ItemProxyObject orderItemLock(@PathVariable Long id, @Valid @PathVariable  Long quant);
	
	@PostMapping("/shopping-cart/item-commit")
	public ItemProxyObject orderItemCommit(@Valid @RequestBody ItemProxyObject item);

	@PostMapping("/shopping-cart/item-roll-back")
	public ItemProxyObject orderItemRollback(@Valid @RequestBody ItemProxyObject item);

}



