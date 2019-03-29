package com.tokopedia.transaction.orders.orderdetails.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Items {
    @SerializedName("categoryID")
    @Expose
    private int categoryID;

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("tapActions")
    @Expose
    private List<ActionButton> tapActions;

    @SerializedName("price")
    @Expose
    private String price;

    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("quantity")
    @Expose
    private int quantity;

    @SerializedName("promotionAmount")
    @Expose
    private String promotionAmount;

    @SerializedName("actionButtons")
    @Expose
    private List<ActionButton> actionButtons;

    @SerializedName("metaData")
    @Expose
    private String metaData;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("productUrl")
    @Expose
    private String productUrl;

    @SerializedName("totalPrice")
    @Expose
    private String totalPrice;

    @SerializedName("weight")
    @Expose
    private String weight;

    private boolean isTapActionsLoaded;

    private boolean isActionButtonLoaded;

    public int getCategoryID() {
        return categoryID;
    }
    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public List<ActionButton> getTapActions() {
        return tapActions;
    }

    public void setTapActions(List<ActionButton> tapActions) {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
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

    public boolean isActionButtonLoaded() {
        return isActionButtonLoaded;
    }

    public void setActionButtonLoaded(boolean actionButtonLoaded) {
        isActionButtonLoaded = actionButtonLoaded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ClassPojo [title = " + title + ", tapActions = " + tapActions + ", price = " + price + ", imageUrl = " + imageUrl + ", quantity = " + quantity + ", promotionAmount = " + promotionAmount + ", actionButtons = " + actionButtons + ", metaData = " + metaData + "]";
    }
}

