package com.tokopedia.profile.view.viewmodel;

/**
 * Created by alvinatin on 15/02/18.
 */

public class TopProfileViewModel {

    private int userId;
    private String name;
    private String avatar;
    private String title;
    private String biodata;
    private String following;
    private String followers;
    private boolean isFollowed;
    private String favoritedShop;
    private String userPhoto;
    private boolean isKol;
    private boolean isFollowed;

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
    private int shopBadgeLevel;
    private String shopLastOnline;
    private String shopAppLink;

    private boolean isUser;

    public TopProfileViewModel() {

    }

    public Boolean getIsUser() {
        return this.isUser;
    }

    public void setIsUser(boolean isUser) {
        this.isUser = isUser;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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

    public boolean isFollowed() {
        return isFollowed;
    }

    public void setFollowed(boolean followed) {
        isFollowed = followed;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public boolean isKol() {
        return isKol;
    }

    public void setKol(boolean kol) {
        isKol = kol;
    }

    public boolean isGoldBadge() {
        return isGoldBadge;
    }

    public void setGoldBadge(boolean goldBadge) {
        isGoldBadge = goldBadge;
    }

    public int getShopBadgeLevel() {
        return shopBadgeLevel;
    }

    public void setShopBadgeLevel(int shopBadgeLevel) {
        this.shopBadgeLevel = shopBadgeLevel;
    }

    public boolean isUser() {
        return isUser;
    }

    public void setUser(boolean user) {
        isUser = user;
    }
}
