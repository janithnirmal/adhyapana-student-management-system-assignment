package model;

public class Teacher {

    private String tno;
    private String name;
    private String address;
    private String mobile;

    public void setTno(String tno) {
        this.tno = tno;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getTno() {
        return this.tno;
    }

    public String getName() {
        return this.name;
    }

    public String getAddress() {
        return this.address;
    }

    public String getMobile() {
        return this.mobile;
    }

}
