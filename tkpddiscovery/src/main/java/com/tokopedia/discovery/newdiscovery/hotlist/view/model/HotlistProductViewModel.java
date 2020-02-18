package com.tokopedia.discovery.newdiscovery.hotlist.view.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.analytic_constant.DataLayer;
import com.tkpd.library.utils.CurrencyFormatHelper;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.hotlist.view.adapter.factory.HotlistAdapterTypeFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by hangnadi on 10/8/17.
 */

@SuppressWarnings("unused")
public class HotlistProductViewModel implements Visitable<HotlistAdapterTypeFactory>,Parcelable {

    private String productID;
    private String productName;
    private String productUrl;
    private String imageUrl;
    private String imageUrl700;
    private String rating;
    private String countReview;
    private String countCourier;
    private String price;
    private String shopID;
    private String shopName;
    private String shopCity;
    private boolean isGoldMerchant;
    private List<BadgeModel> badgesList;
    private List<LabelModel> labelList;
    private boolean wishlist;
    private boolean isWishlistButtonEnabled;
    private boolean featured;
    private String trackerName;
    private String trackerPosition;
    private String homeAttribution;
    private String originalPrice;
    private int discountPercentage;
    private boolean isOfficial;
    private String priceRange;
    private String topLabel;
    private String bottomLabel;
    private String categoryBreadcrumb;

    public HotlistProductViewModel() {
        isWishlistButtonEnabled = true;
    }

    public boolean isWishlistButtonEnabled() {
        return isWishlistButtonEnabled;
    }

    public void setWishlistButtonEnabled(boolean wishlistButtonEnabled) {
        isWishlistButtonEnabled = wishlistButtonEnabled;
    }

    public String getProductUrl() {
        return productUrl;
    }

    public void setProductUrl(String productUrl) {
        this.productUrl = productUrl;
    }

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

    public void setBadgesList(List<BadgeModel> badgesList) {
        this.badgesList = badgesList;
    }

    public List<BadgeModel> getBadgesList() {
        return badgesList;
    }

    public void setLabelList(List<LabelModel> labelList) {
        this.labelList = labelList;
    }

    public List<LabelModel> getLabelList() {
        return labelList;
    }

    public boolean isWishlist() {
        return wishlist;
    }

    public void setWishlist(boolean wishlist) {
        this.wishlist = wishlist;
    }

    public void setFeatured(boolean featured) {
        this.featured = featured;
    }

    public boolean isFeatured() {
        return featured;
    }

    public String getCountCourier() {
        return countCourier;
    }

    public void setCountCourier(String countCourier) {
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

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
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

    public String getCategoryBreadcrumb() {
        return categoryBreadcrumb;
    }

    public void setCategoryBreadcrumb(String categoryBreadcrumb) {
        this.categoryBreadcrumb = categoryBreadcrumb;
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

    public String getTrackerName() {
        return trackerName;
    }

    public void setTrackerName(String trackerName) {
        this.trackerName = trackerName;
    }

    public String getTrackerPosition() {
        return trackerPosition;
    }

    public void setTrackerPosition(String trackerPosition) {
        this.trackerPosition = trackerPosition;
    }

    public void setHomeAttribution(String homeAttribution) {
        this.homeAttribution = homeAttribution;
    }

    public String getHomeAttribution() {
        if (homeAttribution == null || homeAttribution.isEmpty()) return "none / other";
        else return homeAttribution;
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

    @SuppressWarnings("WeakerAccess")
    public static class BadgeModel implements Parcelable {
        private String imageUrl;
        private String title;
        private boolean isShown;

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        public boolean isShown() {
            return isShown;
        }

        public void setShown(boolean shown) {
            isShown = shown;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.imageUrl);
            dest.writeString(this.title);
            dest.writeByte(this.isShown ? (byte) 1 : (byte) 0);
        }

        public BadgeModel() {
        }

        protected BadgeModel(Parcel in) {
            this.imageUrl = in.readString();
            this.title = in.readString();
            this.isShown = in.readByte() != 0;
        }

        public static final Creator<BadgeModel> CREATOR = new Creator<BadgeModel>() {
            @Override
            public BadgeModel createFromParcel(Parcel source) {
                return new BadgeModel(source);
            }

            @Override
            public BadgeModel[] newArray(int size) {
                return new BadgeModel[size];
            }
        };
    }

    @SuppressWarnings("WeakerAccess")
    public static class LabelModel implements Parcelable {
        private String color;
        private String title;

        public void setColor(String color) {
            this.color = color;
        }

        public String getColor() {
            return color;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.color);
            dest.writeString(this.title);
        }

        public LabelModel() {
        }

        protected LabelModel(Parcel in) {
            this.color = in.readString();
            this.title = in.readString();
        }

        public static final Creator<LabelModel> CREATOR = new Creator<LabelModel>() {
            @Override
            public LabelModel createFromParcel(Parcel source) {
                return new LabelModel(source);
            }

            @Override
            public LabelModel[] newArray(int size) {
                return new LabelModel[size];
            }
        };
    }

    @Override
    public int type(HotlistAdapterTypeFactory typeFactory) {
        return typeFactory.type(this);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.productID);
        dest.writeString(this.productName);
        dest.writeString(this.productUrl);
        dest.writeString(this.imageUrl);
        dest.writeString(this.imageUrl700);
        dest.writeString(this.rating);
        dest.writeString(this.countReview);
        dest.writeString(this.countCourier);
        dest.writeString(this.price);
        dest.writeString(this.shopID);
        dest.writeString(this.shopName);
        dest.writeString(this.shopCity);
        dest.writeByte(this.isGoldMerchant ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.badgesList);
        dest.writeTypedList(this.labelList);
        dest.writeByte(this.wishlist ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isWishlistButtonEnabled ? (byte) 1 : (byte) 0);
        dest.writeByte(this.featured ? (byte) 1 : (byte) 0);
        dest.writeString(this.trackerName);
        dest.writeString(this.trackerPosition);
        dest.writeString(this.homeAttribution);
        dest.writeString(this.originalPrice);
        dest.writeInt(this.discountPercentage);
        dest.writeByte(this.isOfficial ? (byte) 1 : (byte) 0);
        dest.writeString(this.priceRange);
        dest.writeString(this.topLabel);
        dest.writeString(this.bottomLabel);
        dest.writeString(this.categoryBreadcrumb);
    }

    protected HotlistProductViewModel(Parcel in) {
        this.productID = in.readString();
        this.productName = in.readString();
        this.productUrl = in.readString();
        this.imageUrl = in.readString();
        this.imageUrl700 = in.readString();
        this.rating = in.readString();
        this.countReview = in.readString();
        this.countCourier = in.readString();
        this.price = in.readString();
        this.shopID = in.readString();
        this.shopName = in.readString();
        this.shopCity = in.readString();
        this.isGoldMerchant = in.readByte() != 0;
        this.badgesList = in.createTypedArrayList(BadgeModel.CREATOR);
        this.labelList = in.createTypedArrayList(LabelModel.CREATOR);
        this.wishlist = in.readByte() != 0;
        this.isWishlistButtonEnabled = in.readByte() != 0;
        this.featured = in.readByte() != 0;
        this.trackerName = in.readString();
        this.trackerPosition = in.readString();
        this.homeAttribution = in.readString();
        this.originalPrice = in.readString();
        this.discountPercentage = in.readInt();
        this.isOfficial = in.readByte() != 0;
        this.priceRange = in.readString();
        this.topLabel = in.readString();
        this.bottomLabel = in.readString();
        this.categoryBreadcrumb = in.readString();
    }

    public static final Creator<HotlistProductViewModel> CREATOR = new Creator<HotlistProductViewModel>() {
        @Override
        public HotlistProductViewModel createFromParcel(Parcel source) {
            return new HotlistProductViewModel(source);
        }

        @Override
        public HotlistProductViewModel[] newArray(int size) {
            return new HotlistProductViewModel[size];
        }
    };
}
