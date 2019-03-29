package per.ravisu.rest.ws.shoppingcart.item.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All details about the item.")
@Entity
public class Item {

	@Id	
	@ApiModelProperty(notes = "SKU for the item, input value for POST requests will be taken as value")
	@Column(name = "item_sku")
	private Long sku;

	@Size(min = 1, message = "Item Name should have atleast 1 character")
	@ApiModelProperty(notes = "Item Name should have atleast 1 character")
	@Column(name = "item_name")
	private String fname;

	@PositiveOrZero(message = "Should be 0 or greater")
	@ApiModelProperty(notes = "Should be 0 or greater")
	@Column(name = "avail_quantity")
	private Long quantity;

	public Item() {

	}

	public Item(Long sku, @Size(min = 1, message = "Item Name should have atleast 1 character") String fname,
			@PositiveOrZero(message = "Should be 0 or greater") Long quantity) {
		super();
		this.sku = sku;
		this.fname = fname;
		this.quantity = quantity;
	}
	
	public Long getSku() {
		return sku;
	}

	public String getFname() {
		return fname;
	}

	public Long getQuantity() {
		return quantity;
	}

	public void setQuantity(Long quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Item [sku=" + sku + ", fname=" + fname + ", quantity=" + quantity + "]";
	}



	


}
