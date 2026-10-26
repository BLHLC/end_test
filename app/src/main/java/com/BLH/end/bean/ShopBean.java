package com.BLH.end.bean;

import java.io.Serializable;
import java.util.List;

public class ShopBean implements Serializable {
    private int shopId;
    private String shopName;
    private String logo;
    private String announcement;
    private double score;
    private int monthSale;
    private double deliveryFee;
    private double minPrice;
    private String deliveryTime;
    private String distance;
    private List<FoodBean> foods;

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(String announcement) {
        this.announcement = announcement;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getMonthSale() {
        return monthSale;
    }

    public void setMonthSale(int monthSale) {
        this.monthSale = monthSale;
    }

    public double getDeliveryFee() {
        return deliveryFee;
    }

    public void setDeliveryFee(double deliveryFee) {
        this.deliveryFee = deliveryFee;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public List<FoodBean> getFoods() {
        return foods;
    }

    public void setFoods(List<FoodBean> foods) {
        this.foods = foods;
    }

}