package application;

//import java.util.Date;

public class Order {

	// all parameters for Order class :-
		private String orderID;
		private String customerID;
		private String orderDate;
		private String orderStatus;

		// Parameterised constructor:-
		public Order(String orderID, String customerID, String orderDate, String orderStatus) {
			super();
			setOrderID(orderID);
			setCustomerID(customerID);
			setOrderDate(orderDate);
			setOrderStatus(orderStatus);
		}

		// Getters and setters :-
		public String getOrderID() {
			return orderID;
		}

		public void setOrderID(String orderID) {
			this.orderID = orderID;
		}

		public String getCustomerID() {
			return customerID;
		}

		public void setCustomerID(String customerID) {
			this.customerID = customerID;
		}

		public String getOrderDate() {
			return orderDate;
		}

		public void setOrderDate(String orderDate) {
			this.orderDate = orderDate;
		}

		public String getOrderStatus() {
			return this.orderStatus;
		}

		public void setOrderStatus(String orderStatus) {
			this.orderStatus = orderStatus;
		}

		@Override
		public String toString() {
			return "Order [orderID=" + orderID + ", customerID=" + customerID + ", orderDate=" + orderDate
					+ ", orderStatus=" + orderStatus + "]";
		}

	}