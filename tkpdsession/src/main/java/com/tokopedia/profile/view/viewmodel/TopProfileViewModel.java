package com.tokopedia.profile.view.viewmodel;

/**
 * Created by alvinatin on 15/02/18.
 */

public class TopProfileViewModel {

    private String name;
    private String following;
    private String favoritedShop;

    private boolean isVerified;
    private String phoneNumber;
    private String email;
    private String gender;
    private String birthDate;

    private String summaryScore;
    private String positiveScore;
    private String netralScore;
    private String negativeScore;

    public TopProfileViewModel() {

    }

    public boolean getVerified(){
        return this.isVerified;
    }

    public void setVerified(boolean isVerified){
        this.isVerified = isVerified;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFollowing() {
        return this.following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFavoritedShop() {
        return this.favoritedShop;
    }

    public void setFavoritedShop(String favoritedShop) {
        this.favoritedShop = favoritedShop;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthDate() {
        return this.birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getSummaryScore() {
        return this.summaryScore;
    }

    public void setSummaryScore(String summaryScore) {
        this.summaryScore = summaryScore;
    }

    public String getPositiveScore() {
        return this.positiveScore;
    }

    public void setPositiveScore(String positiveScore) {
        this.positiveScore = positiveScore;
    }

    public String getNegativeScore() {
        return this.negativeScore;
    }

    public void setNEgativeScore(String negativeScore) {
        this.negativeScore = negativeScore;
    }

    public String getNetralScore() {
        return this.netralScore;
    }

    public void setNetralScore(String netralScore) {
        this.netralScore = netralScore;
    }
}
