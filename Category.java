package application;

public class Category {

	private String categoryID;
	private String categoryName;
	private String mainPurpose;

	public Category(String categoryID, String categoryName, String mainPurpose) {
		super();
		setCategoryID(categoryID);
		setCategoryName(categoryName);
		setMainPurpose(mainPurpose);
	}

	public String getCategoryID() {
		return categoryID;
	}

	public void setCategoryID(String categoryID) {
		this.categoryID = categoryID;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getMainPurpose() {
		return mainPurpose;
	}

	public void setMainPurpose(String mainPurpose) {
		this.mainPurpose = mainPurpose;
	}

	@Override
	public String toString() {
		return "Category [categoryID=" + categoryID + ", categoryName=" + categoryName + ", mainPurpose=" + mainPurpose
				+ "]";
	}


}
