package per.ravisu.rest.ws.shoppingcart.item.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ItemServiceException extends RuntimeException {
	public ItemServiceException(String message) {
		super(message);
	}
}
