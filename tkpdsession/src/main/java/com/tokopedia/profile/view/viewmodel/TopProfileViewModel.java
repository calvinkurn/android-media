package com.tokopedia.profile.view.viewmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by alvinatin on 15/02/18.
 */

public class TopProfileViewModel implements Parcelable {

    private int userId;
    private String name;
    private String title;
    private String biodata;
    private String following;
    private String followers;
    private boolean isFollowed;
    private String favoritedShop;
    private String userPhoto;
    private boolean isKol;

    private boolean isPhoneVerified;
    private boolean isEmailVerified;
    private String phoneNumber;
    private String email;
    private String gender;
    private String birthDate;
    private int completion;

    private String summaryScore;
    private String positiveScore;
    private String netralScore;
    private String negativeScore;

    private int shopId;
    private String shopName;
    private boolean isGoldShop;
    private boolean isGoldBadge;
    private boolean isOfficialShop;
    private String shopLocation;
    private String shopLogo;
    private String shopBadge;
    private String shopTooltip;
    private int shopScore;
    private int shopBadgeLevel;
    private String shopLastOnline;
    private String shopAppLink;
    private boolean isFavorite;

    private boolean isUser;

    public TopProfileViewModel() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBiodata() {
        return biodata;
    }

    public void setBiodata(String biodata) {
        this.biodata = biodata;
    }

    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getFavoritedShop() {
        return favoritedShop;
    }

    public void setFavoritedShop(String favoritedShop) {
        this.favoritedShop = favoritedShop;
    }

    public boolean isKol() {
        return isKol;
    }

    public void setKol(boolean kol) {
        isKol = kol;
    }

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public boolean isPhoneVerified() {
        return isPhoneVerified;
    }

    public void setPhoneVerified(boolean phoneVerified) {
        isPhoneVerified = phoneVerified;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getCompletion() {
        return completion;
    }

    public void setCompletion(int completion) {
        this.completion = completion;
    }

    public String getSummaryScore() {
        return summaryScore;
    }

    public void setSummaryScore(String summaryScore) {
        this.summaryScore = summaryScore;
    }

    public String getPositiveScore() {
        return positiveScore;
    }

    public void setPositiveScore(String positiveScore) {
        this.positiveScore = positiveScore;
    }

    public String getNetralScore() {
        return netralScore;
    }

    public void setNetralScore(String netralScore) {
        this.netralScore = netralScore;
    }

    public String getNegativeScore() {
        return negativeScore;
    }

    public void setNegativeScore(String negativeScore) {
        this.negativeScore = negativeScore;
    }

    public int getShopId() {
        return shopId;
    }

    public void setShopId(int shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public boolean isGoldShop() {
        return isGoldShop;
    }

    public void setGoldShop(boolean goldShop) {
        isGoldShop = goldShop;
    }

    public boolean isOfficialShop() {
        return isOfficialShop;
    }

    public void setOfficialShop(boolean officialShop) {
        isOfficialShop = officialShop;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getShopLogo() {
        return shopLogo;
    }

    public void setShopLogo(String shopLogo) {
        this.shopLogo = shopLogo;
    }

    public String getShopBadge() {
        return shopBadge;
    }

    public void setShopBadge(String shopBadge) {
        this.shopBadge = shopBadge;
    }

    public String getShopLastOnline() {
        return shopLastOnline;
    }

    public void setShopLastOnline(String shopLastOnline) {
        this.shopLastOnline = shopLastOnline;
    }

    public String getShopAppLink() {
        return shopAppLink;
    }

    public void setShopAppLink(String shopAppLink) {
        this.shopAppLink = shopAppLink;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public boolean isGoldBadge() {
        return isGoldBadge;
    }

    public void setGoldBadge(boolean goldBadge) {
        isGoldBadge = goldBadge;
    }

    public String getShopTooltip() {
        return shopTooltip;
    }

    public void setShopTooltip(String shopTooltip) {
        this.shopTooltip = shopTooltip;
    }

    public int getShopScore() {
        return shopScore;
    }

    public void setShopScore(int shopScore) {
        this.shopScore = shopScore;
    }

    public int getShopBadgeLevel() {
        return shopBadgeLevel;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    public void setShopBadgeLevel(int shopBadgeLevel) {
        this.shopBadgeLevel = shopBadgeLevel;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setIsUser(boolean user) {
        isUser = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.name);
        dest.writeString(this.title);
        dest.writeString(this.biodata);
        dest.writeString(this.following);
        dest.writeString(this.followers);
        dest.writeByte(this.isFollowed ? (byte) 1 : (byte) 0);
        dest.writeString(this.favoritedShop);
        dest.writeString(this.userPhoto);
        dest.writeByte(this.isKol ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPhoneVerified ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isEmailVerified ? (byte) 1 : (byte) 0);
        dest.writeString(this.phoneNumber);
        dest.writeString(this.email);
        dest.writeString(this.gender);
        dest.writeString(this.birthDate);
        dest.writeInt(this.completion);
        dest.writeString(this.summaryScore);
        dest.writeString(this.positiveScore);
        dest.writeString(this.netralScore);
        dest.writeString(this.negativeScore);
        dest.writeInt(this.shopId);
        dest.writeString(this.shopName);
        dest.writeByte(this.isGoldShop ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isGoldBadge ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isOfficialShop ? (byte) 1 : (byte) 0);
        dest.writeString(this.shopLocation);
        dest.writeString(this.shopLogo);
        dest.writeString(this.shopBadge);
        dest.writeString(this.shopTooltip);
        dest.writeInt(this.shopScore);
        dest.writeInt(this.shopBadgeLevel);
        dest.writeString(this.shopLastOnline);
        dest.writeString(this.shopAppLink);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isUser ? (byte) 1 : (byte) 0);
    }

    protected TopProfileViewModel(Parcel in) {
        this.userId = in.readInt();
        this.name = in.readString();
        this.title = in.readString();
        this.biodata = in.readString();
        this.following = in.readString();
        this.followers = in.readString();
        this.isFollowed = in.readByte() != 0;
        this.favoritedShop = in.readString();
        this.userPhoto = in.readString();
        this.isKol = in.readByte() != 0;
        this.isPhoneVerified = in.readByte() != 0;
        this.isEmailVerified = in.readByte() != 0;
        this.phoneNumber = in.readString();
        this.email = in.readString();
        this.gender = in.readString();
        this.birthDate = in.readString();
        this.completion = in.readInt();
        this.summaryScore = in.readString();
        this.positiveScore = in.readString();
        this.netralScore = in.readString();
        this.negativeScore = in.readString();
        this.shopId = in.readInt();
        this.shopName = in.readString();
        this.isGoldShop = in.readByte() != 0;
        this.isGoldBadge = in.readByte() != 0;
        this.isOfficialShop = in.readByte() != 0;
        this.shopLocation = in.readString();
        this.shopLogo = in.readString();
        this.shopBadge = in.readString();
        this.shopTooltip = in.readString();
        this.shopScore = in.readInt();
        this.shopBadgeLevel = in.readInt();
        this.shopLastOnline = in.readString();
        this.shopAppLink = in.readString();
        this.isFavorite = in.readByte() != 0;
        this.isUser = in.readByte() != 0;
    }

    public static final Parcelable.Creator<TopProfileViewModel> CREATOR = new Parcelable.Creator<TopProfileViewModel>() {
        @Override
        public TopProfileViewModel createFromParcel(Parcel source) {
            return new TopProfileViewModel(source);
        }

        @Override
        public TopProfileViewModel[] newArray(int size) {
            return new TopProfileViewModel[size];
        }
    };
}
