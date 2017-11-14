package com.tokopedia.topads.dashboard.domain.model;

/**
 * Created by Nathaniel on 2/24/2017.
 */

public class TopAdsDetailGroupDomainModel extends TopAdsDetailProductDomainModel {
    String groupTotal;
    String keywordTotal;

    public String getKeywordTotal() {
        return keywordTotal;
    }

    public void setKeywordTotal(String keywordTotal) {
        this.keywordTotal = keywordTotal;
    }

    public String getGroupTotal() {
        return groupTotal;
    }

    public void setGroupTotal(String groupTotal) {
        this.groupTotal = groupTotal;
    }
    
    public TopAdsDetailGroupDomainModel copy(){
        TopAdsDetailGroupDomainModel detailGroupDomainModel = new TopAdsDetailGroupDomainModel();
        detailGroupDomainModel.setGroupTotal(getGroupTotal());
        detailGroupDomainModel.setAdBudget(getAdBudget());
        detailGroupDomainModel.setAdEndDate(getAdEndDate());
        detailGroupDomainModel.setAdEndTime(getAdEndTime());
        detailGroupDomainModel.setAdImage(getAdImage());
        detailGroupDomainModel.setAdSchedule(getAdSchedule());
        detailGroupDomainModel.setAdStartDate(getAdStartDate());
        detailGroupDomainModel.setAdStartTime(getAdStartTime());
        detailGroupDomainModel.setAdTitle(getAdTitle());
        detailGroupDomainModel.setGroupId(getGroupId());
        detailGroupDomainModel.setPriceBid(getPriceBid());
        detailGroupDomainModel.setPriceDaily(getPriceDaily());
        detailGroupDomainModel.setShopId(getShopId());
        detailGroupDomainModel.setStatus(getStatus());
        detailGroupDomainModel.setStickerId(getStickerId());
        detailGroupDomainModel.setAdId(getAdId());
        detailGroupDomainModel.setItemId(getItemId());
        detailGroupDomainModel.setAdType(getAdType());
        detailGroupDomainModel.setKeywordTotal(getKeywordTotal());
        return detailGroupDomainModel;
    }

}
