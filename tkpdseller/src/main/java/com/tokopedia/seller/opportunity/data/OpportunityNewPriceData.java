package com.tokopedia.seller.opportunity.data;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by normansyahputa on 1/11/18.
 */

public class OpportunityNewPriceData {
    @SerializedName("replacement_id")
    @Expose
    private long replacementId;
    @SerializedName("old_item_price")
    @Expose
    private long oldItemPrice;
    @SerializedName("old_item_price_idr")
    @Expose
    private String oldItemPriceIdr;
    @SerializedName("old_shipping_price")
    @Expose
    private long oldShippingPrice;
    @SerializedName("old_shipping_price_idr")
    @Expose
    private String oldShippingPriceIdr;
    @SerializedName("new_item_price")
    @Expose
    private long newItemPrice;
    @SerializedName("new_item_price_idr")
    @Expose
    private String newItemPriceIdr;
    @SerializedName("new_shipping_price")
    @Expose
    private long newShippingPrice;
    @SerializedName("new_shipping_price_idr")
    @Expose
    private String newShippingPriceIdr;

    @SerializedName("opportunity_title")
    @Expose
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getReplacementId() {
        return replacementId;
    }

    public void setReplacementId(long replacementId) {
        this.replacementId = replacementId;
    }

    public long getOldItemPrice() {
        return oldItemPrice;
    }

    public void setOldItemPrice(long oldItemPrice) {
        this.oldItemPrice = oldItemPrice;
    }

    public String getOldItemPriceIdr() {
        return oldItemPriceIdr;
    }

    public void setOldItemPriceIdr(String oldItemPriceIdr) {
        this.oldItemPriceIdr = oldItemPriceIdr;
    }

    public long getOldShippingPrice() {
        return oldShippingPrice;
    }

    public void setOldShippingPrice(long oldShippingPrice) {
        this.oldShippingPrice = oldShippingPrice;
    }

    public String getOldShippingPriceIdr() {
        return oldShippingPriceIdr;
    }

    public void setOldShippingPriceIdr(String oldShippingPriceIdr) {
        this.oldShippingPriceIdr = oldShippingPriceIdr;
    }

    public long getNewItemPrice() {
        return newItemPrice;
    }

    public void setNewItemPrice(long newItemPrice) {
        this.newItemPrice = newItemPrice;
    }

    public String getNewItemPriceIdr() {
        return newItemPriceIdr;
    }

    public void setNewItemPriceIdr(String newItemPriceIdr) {
        this.newItemPriceIdr = newItemPriceIdr;
    }

    public long getNewShippingPrice() {
        return newShippingPrice;
    }

    public void setNewShippingPrice(long newShippingPrice) {
        this.newShippingPrice = newShippingPrice;
    }

    public String getNewShippingPriceIdr() {
        return newShippingPriceIdr;
    }

    public void setNewShippingPriceIdr(String newShippingPriceIdr) {
        this.newShippingPriceIdr = newShippingPriceIdr;
    }

}
