package com.example.windows10gamer.demo3.model;

/**
 * Created by Windows 10 Gamer on 04/07/2017.
 */

public class GridCardModel {
    public int id;
    public String name;
    public double discount;
    public int sale_price,price;

    public GridCardModel(int id, String name, double discount, int sale_price, int price) {
        this.id = id;
        this.name = name;
        this.discount = discount;
        this.sale_price = sale_price;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    public int getSale_price() {
        return sale_price;
    }

    public void setSale_price(int sale_price) {
        this.sale_price = sale_price;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
