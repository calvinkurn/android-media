package com.tokopedia.session.data.viewmodel.login;

import android.support.annotation.Nullable;

import com.tokopedia.session.data.viewmodel.SecurityDomain;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginDomain {

    private final int shopIsGold;
    private final boolean isMsisdnVerified;
    private final int shopId;
    private final String shopName;
    private final String fullName;
    private final ShopReputationDomain shopReputation;
    private final boolean isLogin;
    private final UserReputationDomain userReputation;
    private final int shopHasTerms;
    private final int shopIsOfficial;
    private final int isRegisterDevice;
    private final int userId;
    private final int msisdnShowDialog;
    private final String shopAvatar;
    private final String userImage;
    private final SecurityDomain securityDomain;

    public MakeLoginDomain(int shopIsGold,
                           boolean isMsisdnVerified,
                           int shopId, String shopName,
                           String fullName,
                           @Nullable ShopReputationDomain shopReputation,
                           boolean isLogin,
                           @Nullable UserReputationDomain userReputation,
                           int shopHasTerms,
                           int shopIsOfficial,
                           int isRegisterDevice,
                           int userId,
                           int msisdnShowDialog,
                           String shopAvatar,
                           String userImage,
                           @Nullable SecurityDomain securityDomain) {
        this.shopIsGold = shopIsGold;
        this.isMsisdnVerified = isMsisdnVerified;
        this.shopId = shopId;
        this.shopName = shopName;
        this.fullName = fullName;
        this.shopReputation = shopReputation;
        this.isLogin = isLogin;
        this.userReputation = userReputation;
        this.shopHasTerms = shopHasTerms;
        this.shopIsOfficial = shopIsOfficial;
        this.isRegisterDevice = isRegisterDevice;
        this.userId = userId;
        this.msisdnShowDialog = msisdnShowDialog;
        this.shopAvatar = shopAvatar;
        this.userImage = userImage;
        this.securityDomain = securityDomain;
    }

    public int getShopIsGold() {
        return shopIsGold;
    }

    public int getShopId() {
        return shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public String getFullName() {
        return fullName;
    }

    public ShopReputationDomain getShopReputation() {
        return shopReputation;
    }

    public UserReputationDomain getUserReputation() {
        return userReputation;
    }

    public int getShopHasTerms() {
        return shopHasTerms;
    }

    public int getShopIsOfficial() {
        return shopIsOfficial;
    }

    public int getIsRegisterDevice() {
        return isRegisterDevice;
    }

    public int getUserId() {
        return userId;
    }

    public int getMsisdnShowDialog() {
        return msisdnShowDialog;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public String getUserImage() {
        return userImage;
    }

    public boolean isMsisdnVerified() {
        return isMsisdnVerified;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public SecurityDomain getSecurityDomain() {
        return securityDomain;
    }
}
