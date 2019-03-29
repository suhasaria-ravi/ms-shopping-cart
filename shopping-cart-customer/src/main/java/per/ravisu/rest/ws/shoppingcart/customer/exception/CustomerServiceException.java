package per.ravisu.rest.ws.shoppingcart.customer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CustomerServiceException extends RuntimeException {
	public CustomerServiceException(String message) {
		super(message);
	}
}
