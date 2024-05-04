package application;

public class OrderProducts {
	private String orderID;
	private String productID;
	private int quantity;

	public OrderProducts(String orderID, String productID) {
		setOrderID(orderID);
		setProductID(productID);
	}

	public OrderProducts(String orderID, String productID, int quantity) {
		super();
		this.orderID = orderID;
		this.productID = productID;
		this.quantity = quantity;
	}

	public String getOrderID() {
		return orderID;
	}

	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getProductID() {
		return productID;
	}

	public void setProductID(String productID) {
		this.productID = productID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "OrderProducts [orderID=" + orderID + ", productID=" + productID + ", quantity=" + quantity + "]";
	}
}
