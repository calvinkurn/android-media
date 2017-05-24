package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactory;

/**
 * @author by nisie on 5/24/17.
 */

public class FeedDetailHeaderViewModel implements Visitable<FeedPlusDetailTypeFactory> {

    private String shopName;
    private String shopAvatar;
    private boolean isGoldMerchant;
    private String shopSlogan;
    private boolean isOfficialStore;

    @Override
    public int type(FeedPlusDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public FeedDetailHeaderViewModel(String shopName,
                                     String shopAvatar,
                                     boolean isGoldMerchant,
                                     String shopSlogan,
                                     boolean isOfficialStore) {
        this.shopName = shopName;
        this.shopAvatar = shopAvatar;
        this.isGoldMerchant = isGoldMerchant;
        this.shopSlogan = shopSlogan;
        this.isOfficialStore = isOfficialStore;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public String getShopSlogan() {
        return shopSlogan;
    }

    public void setShopSlogan(String shopSlogan) {
        this.shopSlogan = shopSlogan;
    }

    public boolean isOfficialStore() {
        return isOfficialStore;
    }

    public void setOfficialStore(boolean officialStore) {
        isOfficialStore = officialStore;
    }
}
