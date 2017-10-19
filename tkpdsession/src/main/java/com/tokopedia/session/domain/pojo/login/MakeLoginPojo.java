package com.tokopedia.session.domain.pojo.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginPojo {

    @SerializedName("shop_is_gold")
    @Expose
    private int shopIsGold;
    @SerializedName("msisdn_is_verified")
    @Expose
    private String msisdnIsVerified;
    @SerializedName("shop_id")
    @Expose
    private int shopId;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("full_name")
    @Expose
    private String fullName;
    @SerializedName("shop_reputation")
    @Expose
    private ShopReputationPojo shopReputation;
    @SerializedName("is_login")
    @Expose
    private String isLogin;
    @SerializedName("user_reputation")
    @Expose
    private UserReputationPojo userReputation;
    @SerializedName("shop_has_terms")
    @Expose
    private int shopHasTerms;
    @SerializedName("shop_is_official")
    @Expose
    private int shopIsOfficial;
    @SerializedName("is_register_device")
    @Expose
    private int isRegisterDevice;
    @SerializedName("user_id")
    @Expose
    private int userId;
    @SerializedName("msisdn_show_dialog")
    @Expose
    private int msisdnShowDialog;
    @SerializedName("shop_avatar")
    @Expose
    private String shopAvatar;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("security")
    @Expose
    private SecurityPojo securityPojo;

    public int getShopIsGold() {
        return shopIsGold;
    }

    public void setShopIsGold(int shopIsGold) {
        this.shopIsGold = shopIsGold;
    }

    public String getMsisdnIsVerified() {
        return msisdnIsVerified;
    }

    public void setMsisdnIsVerified(String msisdnIsVerified) {
        this.msisdnIsVerified = msisdnIsVerified;
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

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public ShopReputationPojo getShopReputation() {
        return shopReputation;
    }

    public void setShopReputation(ShopReputationPojo shopReputation) {
        this.shopReputation = shopReputation;
    }

    public String getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(String isLogin) {
        this.isLogin = isLogin;
    }

    public UserReputationPojo getUserReputation() {
        return userReputation;
    }

    public void setUserReputation(UserReputationPojo userReputation) {
        this.userReputation = userReputation;
    }

    public int getShopHasTerms() {
        return shopHasTerms;
    }

    public void setShopHasTerms(int shopHasTerms) {
        this.shopHasTerms = shopHasTerms;
    }

    public int getShopIsOfficial() {
        return shopIsOfficial;
    }

    public void setShopIsOfficial(int shopIsOfficial) {
        this.shopIsOfficial = shopIsOfficial;
    }

    public int getIsRegisterDevice() {
        return isRegisterDevice;
    }

    public void setIsRegisterDevice(int isRegisterDevice) {
        this.isRegisterDevice = isRegisterDevice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getMsisdnShowDialog() {
        return msisdnShowDialog;
    }

    public void setMsisdnShowDialog(int msisdnShowDialog) {
        this.msisdnShowDialog = msisdnShowDialog;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public SecurityPojo getSecurityPojo() {
        return securityPojo;
    }
}
