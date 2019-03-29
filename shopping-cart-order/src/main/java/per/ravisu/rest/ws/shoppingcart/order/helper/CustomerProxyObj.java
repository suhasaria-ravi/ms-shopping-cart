package per.ravisu.rest.ws.shoppingcart.order.helper;

public class CustomerProxyObj {
	private Long id;

	private String fname;

	private String lname;
	
	public CustomerProxyObj() {
		
	}

	public CustomerProxyObj(Long id, String fname, String lname) {
		super();
		this.id = id;
		this.fname = fname;
		this.lname = lname;
	}

	public Long getId() {
		return id;
	}

	public String getFname() {
		return fname;
	}

	public String getLname() {
		return lname;
	}

	@Override
	public String toString() {
		return "CustomerProxyObj [id=" + id + ", fname=" + fname + ", lname=" + lname + "]";
	}
	
	
}
