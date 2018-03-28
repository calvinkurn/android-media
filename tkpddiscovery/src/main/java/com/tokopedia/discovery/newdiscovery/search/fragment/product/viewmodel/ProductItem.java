package com.tokopedia.discovery.newdiscovery.search.fragment.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.analytics.SearchTracking;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.discovery.newdiscovery.search.fragment.product.adapter.typefactory.ProductListTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class ProductItem implements Parcelable, Visitable<ProductListTypeFactory> {
    private String productID;
    private String productName;
    private String imageUrl;
    private String imageUrl700;
    private String rating;
    private String countReview;
    private String price;
    private String shopID;
    private String shopName;
    private String shopCity;
    private boolean isGoldMerchant;
    private boolean isWishlisted;
    private boolean isWishlistButtonEnabled = true;
    private List<BadgeItem> badgesList;
    private List<LabelItem> labelList;
    private int position;

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl700(String imageUrl700) {
        this.imageUrl700 = imageUrl700;
    }

    public String getImageUrl700() {
        return imageUrl700;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public void setCountReview(String countReview) {
        this.countReview = countReview;
    }

    public String getCountReview() {
        return countReview;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public void setShopID(String shopID) {
        this.shopID = shopID;
    }

    public String getShopID() {
        return shopID;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopCity(String shopCity) {
        this.shopCity = shopCity;
    }

    public String getShopCity() {
        return shopCity;
    }

    public boolean isGoldMerchant() {
        return isGoldMerchant;
    }

    public void setGoldMerchant(boolean isGoldMerchant) {
        this.isGoldMerchant = isGoldMerchant;
    }

    public boolean isWishlisted() {
        return isWishlisted;
    }

    public void setWishlisted(boolean wishlisted) {
        isWishlisted = wishlisted;
    }

    public boolean isWishlistButtonEnabled() {
        return isWishlistButtonEnabled;
    }

    public void setWishlistButtonEnabled(boolean wishlistButtonEnabled) {
        isWishlistButtonEnabled = wishlistButtonEnabled;
    }

    public void setBadgesList(List<BadgeItem> badgesList) {
        this.badgesList = badgesList;
    }

    public List<BadgeItem> getBadgesList() {
        return badgesList;
    }

    public void setLabelList(List<LabelItem> labelList) {
        this.labelList = labelList;
    }

    public List<LabelItem> getLabelList() {
        return labelList;
    }

    public int getPageNumber() {
        return (position - 1) / Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ProductItem() {
    }

    @Override
    public int type(ProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public Object getProductAsObjectDataLayer(String userId) {
        return DataLayer.mapOf(
                "name", getProductName(),
                "id", getProductID(),
                "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(getPrice())),
                "brand", "none / other",
                "category", "none / other",
                "variant", "none / other",
                "list", SearchTracking.getActionFieldString(getPageNumber()),
                "position", Integer.toString(getPosition()),
                "userId", userId
        );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productID);
        dest.writeString(this.productName);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageUrl700);
        dest.writeString(this.rating);
        dest.writeString(this.countReview);
        dest.writeString(this.price);
        dest.writeString(this.shopID);
        dest.writeString(this.shopName);
        dest.writeString(this.shopCity);
        dest.writeByte(this.isGoldMerchant ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWishlisted ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWishlistButtonEnabled ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.badgesList);
        dest.writeTypedList(this.labelList);
        dest.writeInt(this.position);
    }

    protected ProductItem(Parcel in) {
        this.productID = in.readString();
        this.productName = in.readString();
        this.imageUrl = in.readString();
        this.imageUrl700 = in.readString();
        this.rating = in.readString();
        this.countReview = in.readString();
        this.price = in.readString();
        this.shopID = in.readString();
        this.shopName = in.readString();
        this.shopCity = in.readString();
        this.isGoldMerchant = in.readByte() != 0;
        this.isWishlisted = in.readByte() != 0;
        this.isWishlistButtonEnabled = in.readByte() != 0;
        this.badgesList = in.createTypedArrayList(BadgeItem.CREATOR);
        this.labelList = in.createTypedArrayList(LabelItem.CREATOR);
        this.position = in.readInt();
    }

    public static final Creator<ProductItem> CREATOR = new Creator<ProductItem>() {
        @Override
        public ProductItem createFromParcel(Parcel source) {
            return new ProductItem(source);
        }

        @Override
        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };
}
