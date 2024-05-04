package application;

public class Product {

	 String ID;
	    String category_ID;
	    String name;
	    String type;
	    String description;
	    String brand;
	    int count;
	    float sale_Price,purchase_Price;
	    public Product(String ID,String category_ID,String name,String type,String description,String brand,int count,float sale_Price,float purchase_Price)
	    {
	        this.ID=ID;
	        this.category_ID=category_ID;
	        this.name=name;
	        this.type=type;
	        this.description=description;
	        this.brand=brand;
	        this.count=count;
	        this.sale_Price=sale_Price;
	        this.purchase_Price=purchase_Price;
	    }

	    public String getID() {
	        return ID;
	    }

	    public void setID(String ID) {
	        this.ID = ID;
	    }
	    public void setCategory_ID(String category_ID) {
	        this.category_ID = category_ID;
	    }

	    public String getCategory_ID() {
	        return category_ID;
	    }
	    public String getName() {
	        return name;
	    }

	    public void setName(String name) {
	        this.name = name;
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }

	    public String getDescription() {
	        return description;
	    }

	    public void setDescription(String description) {
	        this.description = description;
	    }

	    public String getBrand() {
	        return brand;
	    }

	    public void setBrand(String brand) {
	        this.brand = brand;
	    }

	    public int getCount() {
	        return count;
	    }

	    public void setCount(int count) {
	        this.count = count;
	    }

	    public float getSale_Price() {
	        return sale_Price;
	    }

	    public void setSale_Price(float sale_Price) {
	        this.sale_Price = sale_Price;
	    }

	    public float getPurchase_Price() {
	        return purchase_Price;
	    }


	    public void setPurchase_Price(float purchase_Price) {
	        this.purchase_Price = purchase_Price;
	    }
	    public boolean isSameAs(Product product) {
	        boolean isEqual = this.getName().equals(product.getName())
	                &&this.ID.equals(product.getID())
	                &&this.category_ID.equals(product.getCategory_ID())
	                && this.getType().equals(product.getType())
	                && this.getDescription().equals(product.getDescription())
	                && this.getSale_Price()==product.getSale_Price()
	                && this.getPurchase_Price()==product.getPurchase_Price();
	        System.out.println("isSameAs result: " + isEqual);
	        return isEqual;
	    }
	}