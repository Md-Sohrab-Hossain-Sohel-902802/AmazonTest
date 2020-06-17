package com.example.amazontest.DataModuler;

public class Products {

    private  String pid;
    private  String date;
    private  String time;
    private  String  description;
    private  String image;
    private  String category;
    private  String price;
    private  String pname;


    public Products(){

    }


    public Products(String pid, String date, String time, String description, String image, String category, String price, String pname) {
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.description = description;
        this.image = image;
        this.category = category;
        this.price = price;
        this.pname = pname;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }
}
