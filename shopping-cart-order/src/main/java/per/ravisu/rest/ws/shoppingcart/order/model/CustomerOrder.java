package per.ravisu.rest.ws.shoppingcart.order.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModelProperty;
import per.ravisu.rest.ws.shoppingcart.order.helper.CustomerProxyObj;

public class CustomerOrder {


	@Size(min = 1, message = "First Name should have atleast 1 character")
	@ApiModelProperty(notes = "First Name should have atleast 1 character")
	@Column(name = "first_name")
	private String fname;

	@Size(min = 1, message = "Last Name should have atleast 1 character")
	@ApiModelProperty(notes = "Last Name should have atleast 1 character")
	@Column(name = "last_name")
	private String lname;
	
	@NotNull
	@ApiModelProperty(notes = "Should hava a value")
	@Column(name = "item_sku")
	private Long itemSku;
	
	@Positive(message = "Should be greater than 0")
	@ApiModelProperty(notes = "Should be greater than 0")
	@Column(name = "ord_quantity")
	private Long ordQuantity;

	public String getFname() {
		return fname;
	}

	public String getLname() {
		return lname;
	}

	public Long getItemSku() {
		return itemSku;
	}

	public Long getOrdQuantity() {
		return ordQuantity;
	}

	public void setFname(String fname) {
		this.fname = fname;
	}

	public void setLname(String lname) {
		this.lname = lname;
	}

	public void setItemSku(Long itemSku) {
		this.itemSku = itemSku;
	}

	public void setOrdQuantity(Long ordQuantity) {
		this.ordQuantity = ordQuantity;
	}
	
	public CustomerOrder() {
		
	}

	public CustomerOrder(@Size(min = 1, message = "First Name should have atleast 1 character") String fname,
			@Size(min = 1, message = "Last Name should have atleast 1 character") String lname, @NotNull Long itemSku,
			@Positive(message = "Should be greater than 0") Long ordQuantity) {
		super();
		this.fname = fname;
		this.lname = lname;
		this.itemSku = itemSku;
		this.ordQuantity = ordQuantity;
	}
	
	
}
