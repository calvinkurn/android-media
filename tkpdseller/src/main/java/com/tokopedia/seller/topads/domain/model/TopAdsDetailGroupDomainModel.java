package com.tokopedia.seller.topads.domain.model;

/**
 * Created by Nathaniel on 2/24/2017.
 */

public class TopAdsDetailGroupDomainModel extends TopAdsDetailProductDomainModel {
    String groupTotal;

    public TopAdsDetailGroupDomainModel(){

    }

    public String getGroupTotal() {
        return groupTotal;
    }

    public void setGroupTotal(String groupTotal) {
        this.groupTotal = groupTotal;
    }

    public TopAdsDetailGroupDomainModel(String adId,
                                        String adTitle,
                                        String groupId,
                                        String shopId,
                                        String status,
                                        String isScheduled,
                                        String startDate,
                                        String startTime,
                                        String endDate,
                                        String endTime,
                                        float priceBid,
                                        String budget,
                                        int priceDaily,
                                        String stickerId,
                                        String groupTotal) {
        super(adId, adTitle, groupId, shopId, status, isScheduled, startDate, startTime, endDate, endTime, priceBid, budget, priceDaily, stickerId);
        this.groupTotal = groupTotal;
    }
}
