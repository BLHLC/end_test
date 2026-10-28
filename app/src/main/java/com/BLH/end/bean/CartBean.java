package com.BLH.end.bean;
import java.io.Serializable;

public class CartBean implements Serializable {
    private int id; // 数据库自增主键
    private int shopId;
    private int foodId;
    private String foodName;
    private double price;
    private int count;
    private String image;
    private String shopName; // 新增

    public String getShopName() { return shopName; }
    public void setShopName(String shopName) { this.shopName = shopName; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getShopId() { return shopId; }
    public void setShopId(int shopId) { this.shopId = shopId; }
    public int getFoodId() { return foodId; }
    public void setFoodId(int foodId) { this.foodId = foodId; }
    public String getFoodName() { return foodName; }
    public void setFoodName(String foodName) { this.foodName = foodName; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getCount() { return count; }
    public void setCount(int count) { this.count = count; }
    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
}