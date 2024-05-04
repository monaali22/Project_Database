package application;

public class Supplier {
	 String ID;
	    String name;
	    String phone_number,email_address;
	    float balance;
	    public Supplier(String ID, String name, String phone_number, String email_address, float balance)
	    {
	        this.ID=ID;
	        this.name=name;
	        this.phone_number=phone_number;
	        this.email_address=email_address;
	        this.balance=balance;
	    }

	    public String getName() {
	        return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }
	    public String getID() {
	        return ID;
	    }
	    public void setID(String ID) {
	        this.ID = ID;
	    }
	    public String getPhone_number() {
	        return phone_number;
	    }
	    public void setPhone_number(String phone_number) {
	        this.phone_number = phone_number;
	    }
	    public String getEmail_address() {
	        return email_address;
	    }
	    public void setEmail_address(String email_address) {
	        this.email_address = email_address;
	    }
	    public float getBalance() {
	        return balance;
	    }
	    public void setBalance(float balance) {
	        this.balance = balance;
	    }

	    public boolean isSameAs(Supplier other) {
	        boolean isEqual = this.getBalance() == other.getBalance()
	                && this.getEmail_address().equals(other.getEmail_address())
	                && this.getPhone_number().equals(other.getPhone_number())
	                && this.getName().equals(other.getName());
	        System.out.println("isSameAs result: " + isEqual);
	        return isEqual;
	    }
	}