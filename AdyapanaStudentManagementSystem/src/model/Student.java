package model;

public class Student {

    private String sno;
    private String name;
    private String address;
    private String dob;
    private String mobile;

    public void setSno(String sno) {
        this.sno = sno;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getSno() {
        return this.sno;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getDob() {
        return this.dob;
    }

    public String getMobile() {
        return this.mobile;
    }

}
