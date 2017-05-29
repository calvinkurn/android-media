package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactory;

/**
 * @author by nisie on 5/19/17.
 */

public class ProductCardHeaderViewModel{

    private String shopName;
    private String shopAvatar;
    private boolean isGoldMerchant;
    private String time;
    private boolean isOfficialStore;

    public ProductCardHeaderViewModel() {
        this.shopName = "Nisie shop";
        this.shopAvatar = "https://imagerouter.tokopedia.com/img/100-square/shops-1/2016/8/5/1205649/1205649_620e3ec4-9a94-4210-bac4-f31ab1d1b9f5.jpg";
        this.isGoldMerchant = true;
        this.isOfficialStore = false;
        this.time = "2017-05-17T15:10:53+07:00";
    }

    public ProductCardHeaderViewModel(String shopName,
                                      String shopAvatar,
                                      boolean isGoldMerchant,
                                      String postTime,
                                      boolean isOfficialStore) {
        this.shopName = shopName;
        this.shopAvatar = shopAvatar;
        this.isGoldMerchant = isGoldMerchant;
        this.time = postTime;
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isOfficialStore() {
        return isOfficialStore;
    }

    public void setOfficialStore(boolean isOfficialStore) {
        this.isOfficialStore = isOfficialStore;
    }
}
