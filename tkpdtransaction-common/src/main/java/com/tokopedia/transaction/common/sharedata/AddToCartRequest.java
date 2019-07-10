package com.tokopedia.transaction.common.sharedata;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author anggaprasetiyo on 13/02/18.
 */

public class AddToCartRequest {

    public static final String ATC_FROM_WISHLIST = "wishlist_list";
    public static final String ATC_FROM_RECENT_VIEW = "last_seen_list";
    public static final String ATC_FROM_RECOMMENDATION = "recommendation_list";

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
    @SerializedName("is_trade_in")
    private int isTradeIn;
    @SerializedName("atc_from_external_source")
    @Expose
    private String atcFromExternalSource;


    private AddToCartRequest(Builder builder) {
        setProductId(builder.productId);
        setQuantity(builder.quantity);
        setNotes(builder.notes);
        setShopId(builder.shopId);
        setTrackerAttribution(builder.trackerAttribution);
        setTrackerListName(builder.trackerListName);
        setWarehouseId(builder.warehouseId);
        setIsTradeIn(builder.istradein);
        setAtcFromExternalSource(builder.atcFromExternalSource);
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

    public int getIsTradeIn() {
        return isTradeIn;
    }

    public void setIsTradeIn(int isTradeIn) {
        this.isTradeIn = isTradeIn;
    }

    public int getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(int warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getAtcFromExternalSource() {
        return atcFromExternalSource;
    }

    public void setAtcFromExternalSource(String atcFromExternalSource) {
        this.atcFromExternalSource = atcFromExternalSource;
    }

    public static final class Builder {
        private int productId;
        private int quantity;
        private String notes;
        private int shopId;
        private String trackerAttribution;
        private String trackerListName;
        private int istradein;
        private int warehouseId;
        private String atcFromExternalSource;

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

        public Builder isTradein(int val) {
            istradein = val;
            return this;
        }

        public Builder atcFromExternalSource(String val) {
            atcFromExternalSource = val;
            return this;
        }


        public AddToCartRequest build() {
            return new AddToCartRequest(this);
        }
    }
}
