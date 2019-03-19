package com.tokopedia.transaction.common.sharedata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 13/02/18.
 */

public class AddToCartRequest {

    @SerializedName("product_id")
    @Expose
    private int productId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("notes")
    @Expose
    private String notes;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("tracker_attribution")
    @Expose
    private String trackerAttribution;
    @SerializedName("tracker_list_name")
    @Expose
    private String trackerListName;
    @SerializedName("warehouse_id")
    @Expose
    private int warehouseId;


    private AddToCartRequest(Builder builder) {
        setProductId(builder.productId);
        setQuantity(builder.quantity);
        setNotes(builder.notes);
        setShopId(builder.shopId);
        setTrackerAttribution(builder.trackerAttribution);
        setTrackerListName(builder.trackerListName);
        setWarehouseId(builder.warehouseId);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getTrackerAttribution() {
        return trackerAttribution;
    }

    public void setTrackerAttribution(String trackerAttribution) {
        this.trackerAttribution = trackerAttribution;
    }

    public String getTrackerListName() {
        return trackerListName;
    }

    public void setTrackerListName(String trackerListName) {
        this.trackerListName = trackerListName;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public static final class Builder {
        private int productId;
        private int quantity;
        private String notes;
        private int shopId;
        private String trackerAttribution;
        private String trackerListName;
        private int warehouseId;

        public Builder() {
        }

        public Builder productId(int val) {
            productId = val;
            return this;
        }

        public Builder quantity(int val) {
            quantity = val;
            return this;
        }

        public Builder notes(String val) {
            notes = val;
            return this;
        }

        public Builder shopId(int val) {
            shopId = val;
            return this;
        }

        public Builder trackerAttribution(String val) {
            trackerAttribution = val;
            return this;
        }

        public Builder trackerListName(String val) {
            trackerListName = val;
            return this;
        }

        public Builder warehouseId(int val) {
            warehouseId = val;
            return this;
        }

        public AddToCartRequest build() {
            return new AddToCartRequest(this);
        }
    }
}
