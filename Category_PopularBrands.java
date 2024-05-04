package application;

public class Category_PopularBrands {
	private String categoryID;
	private String brand;

	public Category_PopularBrands(String categoryID, String brand) {
		setCategoryID(categoryID);
		setBrand(brand);
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	@Override
	public String toString() {
		return "Category_PopularBrands [CategoryID=" + categoryID + ", brand=" + brand + "]";
	}
}