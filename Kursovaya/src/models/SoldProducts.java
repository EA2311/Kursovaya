package models;

import java.util.Date;

public class SoldProducts  {
    private String code;
    private String name;
    private int price;
    private String size;
    private String deskr;
    private int amount;
    private String seller;
    private String date;

    public void Products(){}

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDeskr() {
        return deskr;
    }

    public void setDeskr(String deskr) {
        this.deskr = deskr;
    }

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
