package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Items {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("tapActions")
    @Expose
    private List<TapActions> tapActions;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("quantity")
    @Expose
    private String quantity;

    @SerializedName("promotionAmount")
    @Expose
    private String promotionAmount;

    @SerializedName("actionButtons")
    @Expose
    private List<ActionButton> actionButtons;

    @SerializedName("metaData")
    @Expose
    private String metaData;

    private boolean isTapActionsLoaded;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<TapActions> getTapActions() {
        return tapActions;
    }

    public void setTapActions(List<TapActions> tapActions) {
        this.tapActions = tapActions;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(String promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public List<ActionButton> getActionButtons() {
        return actionButtons;
    }

    public void setActionButtons(List<ActionButton> actionButtons) {
        this.actionButtons = actionButtons;
    }

    public String getMetaData() {
        return metaData;
    }

    public void setMetaData(String metaData) {
        this.metaData = metaData;
    }

    public boolean isTapActionsLoaded() {
        return isTapActionsLoaded;
    }

    public void setTapActionsLoaded(boolean tapActionsLoaded) {
        isTapActionsLoaded = tapActionsLoaded;
    }

    @Override
    public String toString() {
        return "ClassPojo [title = " + title + ", tapActions = " + tapActions + ", price = " + price + ", imageUrl = " + imageUrl + ", quantity = " + quantity + ", promotionAmount = " + promotionAmount + ", actionButtons = " + actionButtons + ", metaData = " + metaData + "]";
    }
}

