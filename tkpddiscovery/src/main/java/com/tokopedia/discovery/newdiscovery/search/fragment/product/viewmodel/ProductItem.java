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
    private int position;
    private String originalPrice;
    private int discountPercentage;
    private boolean isOfficial;
    private String topLabel;
    private String bottomLabel;

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

    public int getPageNumber() {
        return (position - 1) / Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS) + 1;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
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

    public Object getProductAsObjectDataLayerForImageSearch(String userId) {
        return DataLayer.mapOf(
                "name", getProductName(),
                "id", getProductID(),
                "price", Integer.toString(CurrencyFormatHelper.convertRupiahToInt(getPrice())),
                "category", "",
                "list", String.format(SearchTracking.imageClick, getPosition()),
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
        dest.writeString(productID);
        dest.writeString(productName);
        dest.writeString(imageUrl);
        dest.writeString(imageUrl700);
        dest.writeInt(rating);
        dest.writeInt(countReview);
        dest.writeInt(countCourier);
        dest.writeString(price);
        dest.writeString(priceRange);
        dest.writeString(shopID);
        dest.writeString(shopName);
        dest.writeString(shopCity);
        dest.writeByte((byte) (isGoldMerchant ? 0x01 : 0x00));
        dest.writeByte((byte) (isWishlisted ? 0x01 : 0x00));
        dest.writeByte((byte) (isWishlistButtonEnabled ? 0x01 : 0x00));
        if (badgesList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(badgesList);
        }
        if (labelList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(labelList);
        }
        dest.writeInt(position);
        dest.writeString(originalPrice);
        dest.writeInt(discountPercentage);
        dest.writeByte((byte) (isOfficial ? 0x01 : 0x00));
        dest.writeString(topLabel);
        dest.writeString(bottomLabel);
    }

    protected ProductItem(Parcel in) {
        productID = in.readString();
        productName = in.readString();
        imageUrl = in.readString();
        imageUrl700 = in.readString();
        rating = in.readInt();
        countReview = in.readInt();
        countCourier = in.readInt();
        price = in.readString();
        priceRange = in.readString();
        shopID = in.readString();
        shopName = in.readString();
        shopCity = in.readString();
        isGoldMerchant = in.readByte() != 0x00;
        isWishlisted = in.readByte() != 0x00;
        isWishlistButtonEnabled = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            badgesList = new ArrayList<BadgeItem>();
            in.readList(badgesList, BadgeItem.class.getClassLoader());
        } else {
            badgesList = null;
        }
        if (in.readByte() == 0x01) {
            labelList = new ArrayList<LabelItem>();
            in.readList(labelList, LabelItem.class.getClassLoader());
        } else {
            labelList = null;
        }
        position = in.readInt();
        originalPrice = in.readString();
        discountPercentage = in.readInt();
        isOfficial = in.readByte() != 0x00;
        topLabel = in.readString();
        bottomLabel = in.readString();

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
