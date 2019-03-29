package per.ravisu.rest.ws.shoppingcart.order.helper;


public class ItemProxyObject {

	private Long sku;

	private String fname;

	private Long quantity;

	public ItemProxyObject() {
		
	}	
	
	public ItemProxyObject(Long sku, String fname, Long quantity) {
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

	@Override
	public String toString() {
		return "ItemProxyObject [sku=" + sku + ", fname=" + fname + ", quantity=" + quantity + "]";
	}

	
	
}
