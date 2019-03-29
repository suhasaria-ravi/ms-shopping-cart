package per.ravisu.rest.ws.shoppingcart.customer.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "All details about the customer.")
@Entity
public class Customer {

	@Id
	@GeneratedValue
	@ApiModelProperty(notes = "Auto generated value, will be ignored for POST requests")
	@Column(name = "customer_id")
	private Long id;

	@Size(min = 1, message = "First Name should have atleast 1 character")
	@ApiModelProperty(notes = "First Name should have atleast 1 character")
	@Column(name = "first_name")
	private String fname;

	@Size(min = 1, message = "Last Name should have atleast 1 character")
	@ApiModelProperty(notes = "Last Name should have atleast 1 character")
	@Column(name = "last_name")
	private String lname;

	public Long getId() {
		return id;
	}

	public Customer() {

	}

	public Customer(Long id, String fname, String lname) {
		super();
		this.id = id;
		this.fname = fname;
		this.lname = lname;
	}

	public String getFname() {
		return fname;
	}

	public String getLname() {
		return lname;
	}

	@Override
	public String toString() {
		return "[" + "Customer First Name=" + fname + ", Last Name=" + lname + "]";
	}

}
