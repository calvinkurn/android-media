
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Notification {

    @SerializedName("unassessed_buyer_reputation")
    @Expose
    private int unassessedBuyerReputation;
    @SerializedName("unassessed_seller_reputation")
    @Expose
    private int unassessedSellerReputation;
    @SerializedName("updated_buyer_reputation")
    @Expose
    private int updatedBuyerReputation;

    public int getUnassessedBuyerReputation() {
        return unassessedBuyerReputation;
    }

    public void setUnassessedBuyerReputation(int unassessedBuyerReputation) {
        this.unassessedBuyerReputation = unassessedBuyerReputation;
    }

    public int getUnassessedSellerReputation() {
        return unassessedSellerReputation;
    }

    public void setUnassessedSellerReputation(int unassessedSellerReputation) {
        this.unassessedSellerReputation = unassessedSellerReputation;
    }

    public int getUpdatedBuyerReputation() {
        return updatedBuyerReputation;
    }

    public void setUpdatedBuyerReputation(int updatedBuyerReputation) {
        this.updatedBuyerReputation = updatedBuyerReputation;
    }

}
