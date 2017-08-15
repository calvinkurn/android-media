package com.tokopedia.tkpd.tkpdreputation.inbox.domain.model;

/**
 * @author by nisie on 8/15/17.
 */

public class NotificationDomain {

    private int unassessedBuyerReputation;
    private int unassessedSellerReputation;
    private int updatedBuyerReputation;

    public NotificationDomain(int unassessedBuyerReputation,
                              int unassessedSellerReputation,
                              int updatedBuyerReputation) {
        this.unassessedBuyerReputation = unassessedBuyerReputation;
        this.unassessedSellerReputation = unassessedSellerReputation;
        this.updatedBuyerReputation = updatedBuyerReputation;
    }
}
