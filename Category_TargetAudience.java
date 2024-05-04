package application;

public class Category_TargetAudience {
	private String categoryID;
	private String audience;
	
	public Category_TargetAudience(String categoryID, String audience) {
		setCategoryID(categoryID);
		setAudience(audience);
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getAudience() {
		return audience;
	}

	public void setAudience(String audience) {
		this.audience = audience;
	}

	@Override
	public String toString() {
		return "Category_TargetAudience [CategoryID=" + categoryID + ", audience=" + audience + "]";
	}
}
