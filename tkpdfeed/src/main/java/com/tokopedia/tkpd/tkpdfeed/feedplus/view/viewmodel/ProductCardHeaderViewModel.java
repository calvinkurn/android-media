package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.FeedPlusDetailTypeFactory;

/**
 * @author by nisie on 5/19/17.
 */

public class ProductCardHeaderViewModel implements Visitable<FeedPlusDetailTypeFactory>,
        Parcelable {

    private String shopName;
    private String shopAvatar;
    private String actionText;
    private boolean isGoldMerchant;
    private String postTime;

    public ProductCardHeaderViewModel() {
        this.actionText = "ubah 1 produk";
        this.shopAvatar = "https://imagerouter.tokopedia.com/img/100-square/shops-1/2016/8/5/1205649/1205649_620e3ec4-9a94-4210-bac4-f31ab1d1b9f5.jpg";
        this.isGoldMerchant = true;
        this.postTime = "2017-05-17T15:10:53+07:00";
    }

    @Override
    public int type(FeedPlusDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    protected ProductCardHeaderViewModel(Parcel in) {
        shopName = in.readString();
        shopAvatar = in.readString();
        actionText = in.readString();
        isGoldMerchant = in.readByte() != 0;
        postTime = in.readString();
    }

    public static final Creator<ProductCardHeaderViewModel> CREATOR = new Creator<ProductCardHeaderViewModel>() {
        @Override
        public ProductCardHeaderViewModel createFromParcel(Parcel in) {
            return new ProductCardHeaderViewModel(in);
        }

        @Override
        public ProductCardHeaderViewModel[] newArray(int size) {
            return new ProductCardHeaderViewModel[size];
        }
    };

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

    public String getActionText() {
        return actionText;
    }

    public void setActionText(String actionText) {
        this.actionText = actionText;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean goldMerchant) {
        isGoldMerchant = goldMerchant;
    }

    public String getPostTime() {
        return postTime;
    }

    public void setPostTime(String postTime) {
        this.postTime = postTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(shopName);
        dest.writeString(shopAvatar);
        dest.writeString(actionText);
        dest.writeByte((byte) (isGoldMerchant ? 1 : 0));
        dest.writeString(postTime);
    }
}
