package com.tokopedia.transaction.checkout.data.entity.response.shippingaddressform;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * @author anggaprasetiyo on 22/02/18.
 */
public class GroupShop {

    @SerializedName("errors")
    @Expose
    private List<String> errors = new ArrayList<>();
    @SerializedName("messages")
    @Expose
    private List<String> messages = new ArrayList<>();
    @SerializedName("shop")
    @Expose
    private Shop shop;
    @SerializedName("shop_shipments")
    @Expose
    private List<ShopShipment> shopShipments = new ArrayList<>();
    @SerializedName("products")
    @Expose
    private List<Product> products = new ArrayList<>();

    public List<String> getErrors() {
        return errors;
    }

    public Shop getShop() {
        return shop;
    }

    public List<ShopShipment> getShopShipments() {
        return shopShipments;
    }

    public List<Product> getProducts() {
        return products;
    }

    public List<String> getMessages() {
        return messages;
    }
}
