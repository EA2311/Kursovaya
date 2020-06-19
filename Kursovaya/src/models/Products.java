package models;

public class Products {

    protected String code;
    protected String name;
    protected int price;
    protected String size;
    protected String deskr;
    protected int amount;


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
    @Override
    public String toString(){
        return "Code:"+ code + "Name:" +name + "Price:" + price+ "Size:" + size+ "Description:" + deskr+ "Amount:" + amount;
    }
}
