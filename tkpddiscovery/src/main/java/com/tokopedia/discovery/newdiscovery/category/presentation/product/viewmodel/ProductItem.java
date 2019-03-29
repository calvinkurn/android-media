package com.tokopedia.discovery.newdiscovery.category.presentation.product.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.tagmanager.DataLayer;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.category.presentation.product.adapter.typefactory.CategoryProductListTypeFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by henrypriyono on 10/9/17.
 */

public class ProductItem implements Parcelable, Visitable<CategoryProductListTypeFactory> {
    private String productID;
    private String productName;
    private String imageUrl;
    private String imageUrl700;
    private int rating;
    private int countReview;
    private int countCourier;
    private String price;
    private String priceRange;
    private String shopID;
    private String shopName;
    private String shopCity;
    private boolean isGoldMerchant;
    private boolean isWishlisted;
    private boolean isWishlistButtonEnabled = true;
    private List<BadgeItem> badgesList;
    private List<LabelItem> labelList;
    private String trackerName;
    private String trackerPosition;
    private String homeAttribution;
    private String originalPrice;
    private int discountPercentage;
    private boolean isOfficial;
    private String topLabel;
    private String bottomLabel;
    private String categoryBreadcrumb;

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

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPrice() {
        return price;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
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

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getCountReview() {
        return countReview;
    }

    public void setCountReview(int countReview) {
        this.countReview = countReview;
    }

    public int getCountCourier() {
        return countCourier;
    }

    public void setCountCourier(int countCourier) {
        this.countCourier = countCourier;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public int getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(int discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public String getCategoryBreadcrumb() {
        return categoryBreadcrumb;
    }

    public void setCategoryBreadcrumb(String categoryBreadcrumb) {
        this.categoryBreadcrumb = categoryBreadcrumb;
    }

    public ProductItem() {
    }

    @Override
    public int type(CategoryProductListTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setTrackerName(String trackerName) {
        this.trackerName = trackerName;
    }

    public String getTrackerName() {
        return trackerName;
    }

    public void setTrackerPosition(String trackerPosition) {
        this.trackerPosition = trackerPosition;
    }

    public String getTrackerPosition() {
        return trackerPosition;
    }

    public void setHomeAttribution(String homeAttribution) {
        this.homeAttribution = homeAttribution;
    }

    public String getHomeAttribution() {
        if (homeAttribution == null || homeAttribution.isEmpty()) return "none / other";
        else return homeAttribution;
    }

    public boolean isOfficial() {
        return isOfficial;
    }

    public void setOfficial(boolean official) {
        isOfficial = official;
    }

    public String getTopLabel() {
        return topLabel;
    }

    public void setTopLabel(String topLabel) {
        this.topLabel = topLabel;
    }

    public String getBottomLabel() {
        return bottomLabel;
    }

    public void setBottomLabel(String bottomLabel) {
        this.bottomLabel = bottomLabel;
    }

    public Map<String, Object> generateImpressionDataLayer() {
        return DataLayer.mapOf(
                "name", getProductName(),
                "id", getProductID(),
                "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(
                        getPrice()
                )),
                "brand", "none / other",
                "category", getCategoryBreadcrumb(),
                "variant", "none / other",
                "list", getTrackerName(),
                "position", getTrackerPosition(),
                "dimension38", getHomeAttribution()
        );
    }

    public Map<String, Object> generateClickDataLayer() {
        return DataLayer.mapOf(
                "name", getProductName(),
                "id", getProductID(),
                "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(getPrice())),
                "brand", "none / other",
                "category", getCategoryBreadcrumb(),
                "variant", "none / other",
                "position", getTrackerPosition(),
                "dimension38", getHomeAttribution()
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
        dest.writeInt(this.rating);
        dest.writeInt(this.countReview);
        dest.writeInt(this.countCourier);
        dest.writeString(this.price);
        dest.writeString(this.priceRange);
        dest.writeString(this.shopID);
        dest.writeString(this.shopName);
        dest.writeString(this.shopCity);
        dest.writeByte(this.isGoldMerchant ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWishlisted ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWishlistButtonEnabled ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.badgesList);
        dest.writeTypedList(this.labelList);
        dest.writeString(this.trackerName);
        dest.writeString(this.trackerPosition);
        dest.writeString(this.homeAttribution);
        dest.writeString(this.originalPrice);
        dest.writeInt(this.discountPercentage);
        dest.writeByte(this.isOfficial ? (byte) 1 : (byte) 0);
        dest.writeString(this.topLabel);
        dest.writeString(this.bottomLabel);
        dest.writeString(this.categoryBreadcrumb);
    }

    protected ProductItem(Parcel in) {
        this.productID = in.readString();
        this.productName = in.readString();
        this.imageUrl = in.readString();
        this.imageUrl700 = in.readString();
        this.rating = in.readInt();
        this.countReview = in.readInt();
        this.countCourier = in.readInt();
        this.price = in.readString();
        this.priceRange = in.readString();
        this.shopID = in.readString();
        this.shopName = in.readString();
        this.shopCity = in.readString();
        this.isGoldMerchant = in.readByte() != 0;
        this.isWishlisted = in.readByte() != 0;
        this.isWishlistButtonEnabled = in.readByte() != 0;
        this.badgesList = in.createTypedArrayList(BadgeItem.CREATOR);
        this.labelList = in.createTypedArrayList(LabelItem.CREATOR);
        this.trackerName = in.readString();
        this.trackerPosition = in.readString();
        this.homeAttribution = in.readString();
        this.originalPrice = in.readString();
        this.discountPercentage = in.readInt();
        this.isOfficial = in.readByte() != 0;
        this.topLabel = in.readString();
        this.bottomLabel = in.readString();
        this.categoryBreadcrumb = in.readString();
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
