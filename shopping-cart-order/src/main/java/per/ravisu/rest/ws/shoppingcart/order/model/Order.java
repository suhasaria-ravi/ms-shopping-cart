package per.ravisu.rest.ws.shoppingcart.order.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All details about the order.")
@Table(name = "order_table")
@Entity
public class Order {

	@Id
	@GeneratedValue
	@ApiModelProperty(notes = "Auto generated value, will be ignored for POST requests")
	@Column(name = "order_id")
	private Long orderId;

	@NotNull
	@ApiModelProperty(notes = "Should hava a value")
	@Column(name = "cust_id")
	private Long custId;
	
	@NotNull
	@ApiModelProperty(notes = "Should hava a value")
	@Column(name = "item_sku")
	private Long itemSku;
	
	@Positive(message = "Should be greater than 0")
	@ApiModelProperty(notes = "Should be greater than 0")
	@Column(name = "ord_quantity")
	private Long ordQuantity;

	public Long getOrderId() {
		return orderId;
	}

	public Long getCustId() {
		return custId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public void setCustId(Long custId) {
		this.custId = custId;
	}

	public void setItemSku(Long itemSku) {
		this.itemSku = itemSku;
	}

	public void setOrdQuantity(Long ordQuantity) {
		this.ordQuantity = ordQuantity;
	}

	public Long getItemSku() {
		return itemSku;
	}

	public Long getOrdQuantity() {
		return ordQuantity;
	}
	
	public Order() {
		
	}

	public Order(Long orderId, @NotNull Long custId, @NotNull Long itemSku,
			@Positive(message = "Should be greater than 0") Long ordQuantity) {
		super();
		this.orderId = orderId;
		this.custId = custId;
		this.itemSku = itemSku;
		this.ordQuantity = ordQuantity;
	}

	
	
}
