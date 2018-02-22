package com.tokopedia.profile.view.viewmodel;

/**
 * Created by alvinatin on 15/02/18.
 */

public class TopProfileViewModel {

    private String userId;
    private String name;
    private String title;
    private String biodata;
    private String following;
    private String followers;
    private String favoritedShop;

    private boolean isPhoneVerified;
    private boolean isEmailVerified;
    private String phoneNumber;
    private String email;
    private String gender;
    private String birthDate;
    private Integer completion;

    private String summaryScore;
    private String positiveScore;
    private String netralScore;
    private String negativeScore;

    private String shopName;
    private boolean isGoldShop;
    private boolean isOfficialShop;
    private String shopLocation;
    private String shopLogo;
    private String shopBadge;
    private String shopLastOnline;
    private String shopAppLink;

    public TopProfileViewModel() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
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

    public Integer getCompletion(){
        return completion;
    }

    public void setCompletion(Integer completion){
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
}
