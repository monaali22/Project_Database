package application;

public class Customer {


	private String customerID;
	private String customerName;
	private String customerAddress;
	private String email;

	public Customer(String customerID, String customerName, String customerAddress, String email) {
		super();
		setCustomerID(customerID);
		setCustomerName(customerName);
		setCustomerAddress(customerAddress);
		setEmail(email);
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerAddress() {
		return customerAddress;
	}

	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "Customer [customerID=" + customerID + ", customerName=" + customerName + ", customerAddress="
				+ customerAddress + ", email=" + email + "]";
	}
}