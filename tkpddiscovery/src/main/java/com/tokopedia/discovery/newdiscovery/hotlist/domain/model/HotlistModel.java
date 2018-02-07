package com.tokopedia.discovery.newdiscovery.hotlist.domain.model;

import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;

import java.util.List;

/**
 * Created by hangnadi on 10/6/17.
 */

public class HotlistModel {

    private List<ProductModel> productList;
    private HotlistBannerModel banner;
    private HotlistAttributeModel attribute;
    private int totalData;
    private String shareURL;

    public void setProductList(List<ProductModel> productList) {
        this.productList = productList;
    }

    public List<ProductModel> getProductList() {
        return productList;
    }

    public void setBanner(HotlistBannerModel banner) {
        this.banner = banner;
    }

    public HotlistBannerModel getBanner() {
        return banner;
    }

    public void setAttribute(HotlistAttributeModel attribute) {
        this.attribute = attribute;
    }

    public HotlistAttributeModel getAttribute() {
        return attribute;
    }

    public void setTotalData(int totalData) {
        this.totalData = totalData;
    }

    public int getTotalData() {
        return totalData;
    }

    public void setShareURL(String shareURL) {
        this.shareURL = shareURL;
    }

    public String getShareURL() {
        return shareURL;
    }
}
