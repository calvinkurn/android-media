package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.officialstore;

/**
 * @author by nisie on 7/26/17.
 */

public class OfficialStoreViewModel {
    private int shopId;

    private String shopUrl;

    private String shopName;

    private String logoUrl;

    private boolean isNew;

    public int getShopId() {
        return shopId;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public String getShopName() {
        return shopName;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public boolean getIsNew() {
        return isNew;
    }

    public OfficialStoreViewModel(int shopId, String shopUrl, String shopName,
                                  String logoUrl, boolean isNew) {
        this.shopId = shopId;
        this.shopUrl = shopUrl;
        this.shopName = shopName;
        this.logoUrl = logoUrl;
        this.isNew = isNew;
    }
}
