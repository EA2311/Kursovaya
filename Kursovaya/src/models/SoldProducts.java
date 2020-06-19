package models;

public class SoldProducts extends Products  {

    private String seller;
    private String date;

    public void SoldProducts(){}

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString(){
        return "Code:"+ code + "Name:" +name + "Price:" + price+ "Size:" + size+ "Description:" + deskr+ "Amount:" + amount + "Seller:" + seller+ ":Date:" + date;
    }
}
