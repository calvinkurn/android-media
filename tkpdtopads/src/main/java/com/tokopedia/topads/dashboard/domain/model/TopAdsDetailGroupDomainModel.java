package com.tokopedia.topads.dashboard.domain.model;

/**
 * Created by Nathaniel on 2/24/2017.
 */

public class TopAdsDetailGroupDomainModel extends TopAdsDetailProductDomainModel {
    String groupTotal;
    String keywordTotal;
    private long suggestionBidValue;
    private String suggestionBidButton;

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

    public long getSuggestionBidValue() {
        return suggestionBidValue;
    }

    public void setSuggestionBidValue(long suggestionBidValue) {
        this.suggestionBidValue = suggestionBidValue;
    }

    public String getSuggestionBidButton() {
        return suggestionBidButton;
    }

    public void setSuggestionBidButton(String suggestionBidButton) {
        this.suggestionBidButton = suggestionBidButton;
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
        detailGroupDomainModel.setSuggestionBidValue(getSuggestionBidValue());
        detailGroupDomainModel.setSuggestionBidButton(getSuggestionBidButton());
        return detailGroupDomainModel;
    }

}
