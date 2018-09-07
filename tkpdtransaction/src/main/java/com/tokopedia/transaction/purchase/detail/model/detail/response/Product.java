
package com.tokopedia.transaction.purchase.detail.model.detail.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Product {

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("order_detail_id")
    @Expose
    private String orderDetailId;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("weight")
    @Expose
    private String weight;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("note")
    @Expose
    private String note;
    @SerializedName("currency_type")
    @Expose
    private int currencyType;
    @SerializedName("currency_rate")
    @Expose
    private int currencyRate;
    @SerializedName("price_unfmt")
    @Expose
    private String priceUnformatted;
    @SerializedName("weight_unfmt")
    @Expose
    private String weightUnformatted;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderDetailId() {
        return orderDetailId;
    }

    public void setOrderDetailId(String orderDetailId) {
        this.orderDetailId = orderDetailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPrice() {
        return price;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(int currencyType) {
        this.currencyType = currencyType;
    }

    public int getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(int currencyRate) {
        this.currencyRate = currencyRate;
    }

    public String getPriceUnformatted() {
        return priceUnformatted;
    }

    public void setPriceUnformatted(String priceUnformatted) {
        this.priceUnformatted = priceUnformatted;
    }

    public String getWeightUnformatted() {
        return weightUnformatted;
    }

    public void setWeightUnformatted(String weightUnformatted) {
        this.weightUnformatted = weightUnformatted;
    }
}
