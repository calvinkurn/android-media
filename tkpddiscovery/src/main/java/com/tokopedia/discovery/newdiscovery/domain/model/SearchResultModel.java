package com.tokopedia.discovery.newdiscovery.domain.model;

import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;

import java.util.List;

/**
 * Created by hangnadi on 10/3/17.
 */

public class SearchResultModel {

    private String redirectUrl;
    private boolean hasCatalog;
    private OfficialStoreBannerModel officialStoreBannerModel;
    private List<ProductModel> productList;
    private int totalData;
    private String totalDataText;
    private String query;
    private String source;
    private String shareUrl;
    private String suggestionText;
    private String suggestedQuery;
    private String departmentId="";
    private String additionalParams;

    public OfficialStoreBannerModel getOfficialStoreBannerModel() {
        return officialStoreBannerModel;
    }

    public void setOfficialStoreBannerModel(OfficialStoreBannerModel officialStoreBannerModel) {
        this.officialStoreBannerModel = officialStoreBannerModel;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public void setHasCatalog(boolean hasCatalog) {
        this.hasCatalog = hasCatalog;
    }

    public boolean isHasCatalog() {
        return hasCatalog;
    }

    public void setProductList(List<ProductModel> productList) {
        this.productList = productList;
    }

    public List<ProductModel> getProductList() {
        return productList;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public int getTotalData() {
        return totalData;
    }

    public String getTotalDataText() {
        return totalDataText;
    }

    public void setTotalDataText(String totalDataText) {
        this.totalDataText = totalDataText;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getShareUrl() {
        return shareUrl;
    }

    public void setShareUrl(String shareUrl) {
        this.shareUrl = shareUrl;
    }

    public String getSuggestionText() {
        return suggestionText;
    }

    public void setSuggestionText(String suggestionText) {
        this.suggestionText = suggestionText;
    }

    public String getSuggestedQuery() {
        return suggestedQuery;
    }

    public void setSuggestedQuery(String suggestedQuery) {
        this.suggestedQuery = suggestedQuery;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getAdditionalParams() {
        return additionalParams;
    }

    public void setAdditionalParams(String additionalParams) {
        this.additionalParams = additionalParams;
    }
}
