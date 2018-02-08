package com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.adapter.typefactory.ShopListTypeFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class ShopViewModel implements Parcelable {

    private boolean hasNextPage;
    private String query;
    private List<ShopItem> shopItemList = new ArrayList<>();

    public boolean isHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<ShopItem> getShopItemList() {
        return shopItemList;
    }

    public void setShopItemList(List<ShopItem> shopItemList) {
        this.shopItemList = shopItemList;
    }

    public static class ShopItem implements Parcelable, Visitable<ShopListTypeFactory> {
        private String shopGoldShop;
        private String shopDescription;
        private String shopLucky;
        private String shopId;
        private String reputationScore;
        private String shopDomain;
        private String shopImage;
        private String shopTotalFavorite;
        private String shopTotalTransaction;
        private String shopRateAccuracy;
        private String shopImage300;
        private String shopName;
        private String shopIsImg;
        private String shopLocation;
        private String reputationImageUri;
        private String shopRateService;
        private String shopIsOwner;
        private String shopUrl;
        private String shopRateSpeed;
        private String shopTagLine;
        private String shopStatus;
        private boolean isOfficial;
        private boolean isFavorited;
        private boolean isFavoriteButtonEnabled = true;
        private List<String> productImages = new ArrayList<>();

        public String getShopGoldShop() {
            return shopGoldShop;
        }

        public void setShopGoldShop(String shopGoldShop) {
            this.shopGoldShop = shopGoldShop;
        }

        public String getShopDescription() {
            return shopDescription;
        }

        public void setShopDescription(String shopDescription) {
            this.shopDescription = shopDescription;
        }

        public String getShopLucky() {
            return shopLucky;
        }

        public void setShopLucky(String shopLucky) {
            this.shopLucky = shopLucky;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getReputationScore() {
            return reputationScore;
        }

        public void setReputationScore(String reputationScore) {
            this.reputationScore = reputationScore;
        }

        public String getShopDomain() {
            return shopDomain;
        }

        public void setShopDomain(String shopDomain) {
            this.shopDomain = shopDomain;
        }

        public String getShopImage() {
            return shopImage;
        }

        public void setShopImage(String shopImage) {
            this.shopImage = shopImage;
        }

        public String getShopTotalFavorite() {
            return shopTotalFavorite;
        }

        public void setShopTotalFavorite(String shopTotalFavorite) {
            this.shopTotalFavorite = shopTotalFavorite;
        }

        public String getShopTotalTransaction() {
            return shopTotalTransaction;
        }

        public void setShopTotalTransaction(String shopTotalTransaction) {
            this.shopTotalTransaction = shopTotalTransaction;
        }

        public String getShopRateAccuracy() {
            return shopRateAccuracy;
        }

        public void setShopRateAccuracy(String shopRateAccuracy) {
            this.shopRateAccuracy = shopRateAccuracy;
        }

        public String getShopImage300() {
            return shopImage300;
        }

        public void setShopImage300(String shopImage300) {
            this.shopImage300 = shopImage300;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getShopIsImg() {
            return shopIsImg;
        }

        public void setShopIsImg(String shopIsImg) {
            this.shopIsImg = shopIsImg;
        }

        public String getShopLocation() {
            return shopLocation;
        }

        public void setShopLocation(String shopLocation) {
            this.shopLocation = shopLocation;
        }

        public String getReputationImageUri() {
            return reputationImageUri;
        }

        public void setReputationImageUri(String reputationImageUri) {
            this.reputationImageUri = reputationImageUri;
        }

        public String getShopRateService() {
            return shopRateService;
        }

        public void setShopRateService(String shopRateService) {
            this.shopRateService = shopRateService;
        }

        public String getShopIsOwner() {
            return shopIsOwner;
        }

        public void setShopIsOwner(String shopIsOwner) {
            this.shopIsOwner = shopIsOwner;
        }

        public String getShopUrl() {
            return shopUrl;
        }

        public void setShopUrl(String shopUrl) {
            this.shopUrl = shopUrl;
        }

        public String getShopRateSpeed() {
            return shopRateSpeed;
        }

        public void setShopRateSpeed(String shopRateSpeed) {
            this.shopRateSpeed = shopRateSpeed;
        }

        public String getShopTagLine() {
            return shopTagLine;
        }

        public void setShopTagLine(String shopTagLine) {
            this.shopTagLine = shopTagLine;
        }

        public String getShopStatus() {
            return shopStatus;
        }

        public void setShopStatus(String shopStatus) {
            this.shopStatus = shopStatus;
        }

        public boolean isOfficial() {
            return isOfficial;
        }

        public void setOfficial(boolean official) {
            isOfficial = official;
        }

        public boolean isFavorited() {
            return isFavorited;
        }

        public void setFavorited(boolean favorited) {
            isFavorited = favorited;
        }

        public boolean isFavoriteButtonEnabled() {
            return isFavoriteButtonEnabled;
        }

        public void setFavoriteButtonEnabled(boolean favoriteButtonEnabled) {
            isFavoriteButtonEnabled = favoriteButtonEnabled;
        }

        public List<String> getProductImages() {
            return productImages;
        }

        public void setProductImages(List<String> productImages) {
            this.productImages = productImages;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.shopGoldShop);
            dest.writeString(this.shopDescription);
            dest.writeString(this.shopLucky);
            dest.writeString(this.shopId);
            dest.writeString(this.reputationScore);
            dest.writeString(this.shopDomain);
            dest.writeString(this.shopImage);
            dest.writeString(this.shopTotalFavorite);
            dest.writeString(this.shopTotalTransaction);
            dest.writeString(this.shopRateAccuracy);
            dest.writeString(this.shopImage300);
            dest.writeString(this.shopName);
            dest.writeString(this.shopIsImg);
            dest.writeString(this.shopLocation);
            dest.writeString(this.reputationImageUri);
            dest.writeString(this.shopRateService);
            dest.writeString(this.shopIsOwner);
            dest.writeString(this.shopUrl);
            dest.writeString(this.shopRateSpeed);
            dest.writeString(this.shopTagLine);
            dest.writeString(this.shopStatus);
            dest.writeByte(this.isOfficial ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFavorited ? (byte) 1 : (byte) 0);
            dest.writeByte(this.isFavoriteButtonEnabled ? (byte) 1 : (byte) 0);
            dest.writeStringList(this.productImages);
        }

        public ShopItem() {
        }

        protected ShopItem(Parcel in) {
            this.shopGoldShop = in.readString();
            this.shopDescription = in.readString();
            this.shopLucky = in.readString();
            this.shopId = in.readString();
            this.reputationScore = in.readString();
            this.shopDomain = in.readString();
            this.shopImage = in.readString();
            this.shopTotalFavorite = in.readString();
            this.shopTotalTransaction = in.readString();
            this.shopRateAccuracy = in.readString();
            this.shopImage300 = in.readString();
            this.shopName = in.readString();
            this.shopIsImg = in.readString();
            this.shopLocation = in.readString();
            this.reputationImageUri = in.readString();
            this.shopRateService = in.readString();
            this.shopIsOwner = in.readString();
            this.shopUrl = in.readString();
            this.shopRateSpeed = in.readString();
            this.shopTagLine = in.readString();
            this.shopStatus = in.readString();
            this.isOfficial = in.readByte() != 0;
            this.isFavorited = in.readByte() != 0;
            this.isFavoriteButtonEnabled = in.readByte() != 0;
            this.productImages = in.createStringArrayList();
        }

        public static final Parcelable.Creator<ShopItem> CREATOR = new Parcelable.Creator<ShopItem>() {
            @Override
            public ShopItem createFromParcel(Parcel source) {
                return new ShopItem(source);
            }

            @Override
            public ShopItem[] newArray(int size) {
                return new ShopItem[size];
            }
        };

        @Override
        public int type(ShopListTypeFactory typeFactory) {
            return typeFactory.type(this);
        }
    }

    public ShopViewModel() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.hasNextPage ? (byte) 1 : (byte) 0);
        dest.writeString(this.query);
        dest.writeTypedList(this.shopItemList);
    }

    protected ShopViewModel(Parcel in) {
        this.hasNextPage = in.readByte() != 0;
        this.query = in.readString();
        this.shopItemList = in.createTypedArrayList(ShopItem.CREATOR);
    }

    public static final Creator<ShopViewModel> CREATOR = new Creator<ShopViewModel>() {
        @Override
        public ShopViewModel createFromParcel(Parcel source) {
            return new ShopViewModel(source);
        }

        @Override
        public ShopViewModel[] newArray(int size) {
            return new ShopViewModel[size];
        }
    };
}
