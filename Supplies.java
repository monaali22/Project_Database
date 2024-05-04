package application;

public class Supplies {
	String sup_ID,pro_ID;
    String booking_date;
    String received_date;
    int num_of_supplied_products;
    public Supplies(String sup_ID, String pro_ID, String booking_date,String received_date, int num_of_supplied_products)
    {
        this.sup_ID=sup_ID;
        this.pro_ID=pro_ID;
        this.booking_date = booking_date;
        this.received_date=received_date;
        this.num_of_supplied_products=num_of_supplied_products;
    }

    public String getSup_ID() {
        return sup_ID;
    }

    public void setSup_ID(String sup_ID) {
        this.sup_ID = sup_ID;
    }

    public String getPro_ID() {
        return pro_ID;
    }

    public void setPro_ID(String pro_ID) {
        this.pro_ID = pro_ID;
    }

    public String getBooking_date() {
        return booking_date;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }
    public String getReceived_date()
    {
        return received_date;
    }

    public void setReceived_date(String received_date) {
        this.received_date = received_date;
    }

    public int getNum_of_supplied_products() {
        return num_of_supplied_products;
    }

    public void setNum_of_supplied_products(int num_of_supplied_products) {
        this.num_of_supplied_products = num_of_supplied_products;
    }
}