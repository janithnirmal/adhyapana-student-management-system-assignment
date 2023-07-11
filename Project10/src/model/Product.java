package model;

public class Product {

    private Integer id;
    private String brand;
    private String model;
    private Double price;
    private Integer quantity;

    public void setId(Integer id) {
        this.id = id;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getId() {
        return this.id;
    }

    public String getBrand() {
        return this.brand;
    }

    public String getModel() {
        return this.model;
    }

    public Double getPrice() {
        return this.price;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

}
