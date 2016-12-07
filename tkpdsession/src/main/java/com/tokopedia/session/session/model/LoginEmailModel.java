package com.tokopedia.session.session.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

/**
 * Created by m.normansyah on 17/11/2015.
 */
@Parcel
public class LoginEmailModel {
    @SerializedName("is_login")
    boolean isLogin;

    @SerializedName("is_register_device")
    int isRegisterDevice;
    public static int INVALID_DEVICE_ID = 0;
    public static int VALID_DEVICE_ID = 1;

    @SerializedName("shop_is_gold")
    int shopIsGold;//shop_is_gold

    @SerializedName("shop_id")
    int shopId;// shop_id

    @SerializedName("shop_name")
    String  shopName;//shop_name

    @SerializedName("full_name")
    String fullName;

    @SerializedName("user_id")
    int userID;// dapat berarti google id atau facebook id

    @SerializedName("shop_has_terms")
    int shopHasTerms;//shop_has_terms

    @SerializedName("msisdn_show_dialog")
    int msisdnShowDialog;// msisdn_show_dialog

    @SerializedName("msisdn_is_verified")
    String msisdnIsVerified;// msisdn is verified

    @SerializedName("shop_avatar")
    String shopAvatar;// shop_avatar

    @SerializedName("user_image")
    String userImage;// user_image

    ShopRepModel shopRepModel;

    UserRepModel userRepModel;

    public ShopRepModel getShopRepModel() {
        return shopRepModel;
    }

    public void setShopRepModel(ShopRepModel shopRepModel) {
        this.shopRepModel = shopRepModel;
    }

    public UserRepModel getUserRepModel() {
        return userRepModel;
    }

    public void setUserRepModel(UserRepModel userRepModel) {
        this.userRepModel = userRepModel;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public void setIsLogin(boolean isLogin) {
        this.isLogin = isLogin;
    }

    public int getIsRegisterDevice() {
        return isRegisterDevice;
    }

    public void setIsRegisterDevice(int isRegisterDevice) {
        this.isRegisterDevice = isRegisterDevice;
    }

    public boolean getMsisdnIsVerified() {
        return msisdnIsVerified.equals("1");
    }

    public int getShopIsGold() {
        return shopIsGold;
    }

    public void setShopIsGold(int shopIsGold) {
        this.shopIsGold = shopIsGold;
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

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
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

    public int getShopHasTerms() {
        return shopHasTerms;
    }

    public void setShopHasTerms(int shopHasTerms) {
        this.shopHasTerms = shopHasTerms;
    }
}
