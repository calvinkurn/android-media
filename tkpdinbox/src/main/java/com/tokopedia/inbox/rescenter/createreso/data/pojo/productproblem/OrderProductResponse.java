package com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by yoasfs on 14/08/17.
 */

public class OrderProductResponse {
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("thumb")
    @Expose
    private String thumb;
    @SerializedName("variant")
    @Expose
    private String variant;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("amount")
    @Expose
    private AmountResponse amount;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public AmountResponse getAmount() {
        return amount;
    }

    public void setAmount(AmountResponse amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderProductResponse{" +
                "name='" + name + '\'' +
                ", thumb='" + thumb + '\'' +
                ", variant='" + variant + '\'' +
                ", variant=" + variant +
                ", amount=" + amount.toString() +
                '}';
    }
}
