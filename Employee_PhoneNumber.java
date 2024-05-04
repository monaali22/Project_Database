package application;

public class Employee_PhoneNumber {
	private String employeeID;
	private String phoneNumber;

	public Employee_PhoneNumber(String employeeID, String phoneNumber) {
		super();
		setEmployeeID(employeeID);
		setPhoneNumber(phoneNumber);
	}

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		if (phoneNumber != null && phoneNumber.length() == 10 && (phoneNumber.startsWith("059") || phoneNumber.startsWith("056")))
			this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Employee_PhoneNumber [employeeID=" + employeeID + ", phoneNumber=" + phoneNumber + "]";
	}
}
