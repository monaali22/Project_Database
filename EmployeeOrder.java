package application;

public class EmployeeOrder {
	
	private String employeeID;
	private String orderID;
	
	
	
	public EmployeeOrder(String employeeID, String orderID) {
		setEmployeeID( employeeID);
		setOrderID( orderID);
	}
	
	
	


	public String getEmployeeID() {
		return employeeID;
	}


	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}


	public String getOrderID() {
		return orderID;
	}


	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}
	
	
	
	

}
