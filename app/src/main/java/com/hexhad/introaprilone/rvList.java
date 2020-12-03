package com.hexhad.introaprilone;

public class rvList {

    private String img;
    private String name;
    private String desc;
    private String price;

    public rvList(String img, String name, String desc, String price) {
        this.img = img;
        this.name = name;
        this.desc = desc;
        this.price = price;
    }

    public rvList() {
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
