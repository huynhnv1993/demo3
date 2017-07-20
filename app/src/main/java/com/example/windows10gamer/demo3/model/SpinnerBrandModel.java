package com.example.windows10gamer.demo3.model;

import org.json.JSONArray;

/**
 * Created by Windows 10 Gamer on 04/07/2017.
 */

public class SpinnerBrandModel {
    private String Name;
    private String Image;
    private Integer Id;
    private Double Discount;
    private JSONArray product;
    private String IdNumber;

    public String getIdNumber() {
        return IdNumber;
    }

    public void setIdNumber(String idNumber) {
        IdNumber = idNumber;
    }

    public SpinnerBrandModel(String name, String image, Integer id, Double discount, JSONArray product, String idNumber) {
        Name = name;
        Image = image;
        Id = id;
        Discount = discount;
        this.product = product;
        IdNumber = idNumber;
    }


    public JSONArray getProduct() {
        return product;
    }

    public void setProduct(JSONArray product) {
        this.product = product;
    }

    public Double getDiscount() {
        return Discount;
    }

    public void setDiscount(Double discount) {
        Discount = discount;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
