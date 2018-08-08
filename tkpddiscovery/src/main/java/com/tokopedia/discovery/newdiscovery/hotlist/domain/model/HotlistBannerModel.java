package com.tokopedia.discovery.newdiscovery.hotlist.domain.model;

/**
 * Created by hangnadi on 10/6/17.
 */

public class HotlistBannerModel {

    private HotlistQueryModel hotlistQueryModel;
    private String bannerImage;
    private String bannerDesc;
    private boolean disableTopads;
    private HotlistPromoInfo hotlistPromoInfo;
    private String hotlistTitle;

    public HotlistQueryModel getHotlistQueryModel() {
        return hotlistQueryModel;
    }

    public void setHotlistQueryModel(HotlistQueryModel hotlistQueryModel) {
        this.hotlistQueryModel = hotlistQueryModel;
    }

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setBannerDesc(String bannerDesc) {
        this.bannerDesc = bannerDesc;
    }

    public String getBannerDesc() {
        return bannerDesc;
    }

    public void setDisableTopads(boolean disableTopads) {
        this.disableTopads = disableTopads;
    }

    public boolean isDisableTopads() {
        return disableTopads;
    }

    public HotlistPromoInfo getHotlistPromoInfo() {
        return hotlistPromoInfo;
    }

    public void setHotlistPromoInfo(HotlistPromoInfo hotlistPromoInfo) {
        this.hotlistPromoInfo = hotlistPromoInfo;
    }

    public String getHotlistTitle() {
        return hotlistTitle;
    }

    public void setHotlistTitle(String hotlistTitle) {
        this.hotlistTitle = hotlistTitle;
    }
}
