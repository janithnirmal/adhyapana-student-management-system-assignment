package model;

public class Subject {

    private String subno;
    private String name;
    private String description;
    private Double price;

    public void setSubno(String subno) {
        this.subno = subno;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPriec(Double price) {
        this.price = price;
    }

    public String getSubno() {
        return this.subno;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Double getPrice() {
        return this.price;
    }

}
