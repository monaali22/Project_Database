package application;

public class Customer_PhoneNumber {
	private String customerID;
	private String phoneNumber;

	public Customer_PhoneNumber(String customerID, String phoneNumber) {
		setCustomerID(customerID);
		setPhoneNumber(phoneNumber);
	}

	public String getCustomerID() {
		return customerID;
	}

	public void setCustomerID(String customerID) {
		this.customerID = customerID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		if (phoneNumber == null
				|| (phoneNumber.length() == 10 && (phoneNumber.startsWith("059") || phoneNumber.startsWith("056"))))
			this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Customer_PhoneNumber [customerID=" + customerID + ", phoneNumber=" + phoneNumber + "]";
	}
}