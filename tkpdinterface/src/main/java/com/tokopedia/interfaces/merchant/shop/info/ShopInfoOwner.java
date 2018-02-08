
package com.tokopedia.interfaces.merchant.shop.info;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopInfoOwner {

    @SerializedName("is_gold_merchant")
    @Expose
    private boolean isGoldMerchant;
    @SerializedName("is_seller")
    @Expose
    private boolean isSeller;
    @SerializedName("owner_email")
    @Expose
    private long ownerEmail;
    @SerializedName("owner_id")
    @Expose
    private long ownerId;
    @SerializedName("owner_image")
    @Expose
    private String ownerImage;
    @SerializedName("owner_messenger")
    @Expose
    private long ownerMessenger;
    @SerializedName("owner_name")
    @Expose
    private String ownerName;
    @SerializedName("owner_phone")
    @Expose
    private long ownerPhone;

    public boolean isIsGoldMerchant() {
        return isGoldMerchant;
    }

    public void setIsGoldMerchant(boolean isGoldMerchant) {
        this.isGoldMerchant = isGoldMerchant;
    }

    public boolean isIsSeller() {
        return isSeller;
    }

    public void setIsSeller(boolean isSeller) {
        this.isSeller = isSeller;
    }

    public long getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(long ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerImage() {
        return ownerImage;
    }

    public void setOwnerImage(String ownerImage) {
        this.ownerImage = ownerImage;
    }

    public long getOwnerMessenger() {
        return ownerMessenger;
    }

    public void setOwnerMessenger(long ownerMessenger) {
        this.ownerMessenger = ownerMessenger;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public long getOwnerPhone() {
        return ownerPhone;
    }

    public void setOwnerPhone(long ownerPhone) {
        this.ownerPhone = ownerPhone;
    }

}
