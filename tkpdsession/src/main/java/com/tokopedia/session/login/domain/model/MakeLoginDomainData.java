package com.tokopedia.session.login.domain.model;

/**
 * @author by nisie on 5/26/17.
 */

public class MakeLoginDomainData {


    private final int shopIsGold;
    private final String msisdnIsVerified;
    private final int shopId;
    private final String shopName;
    private final String fullName;
    private final ShopReputationDomain shopReputation;
    private final String isLogin;
    private final UserReputationDomain userReputation;
    private final int shopHasTerms;
    private final int shopIsOfficial;
    private final int isRegisterDevice;
    private final int userId;
    private final int msisdnShowDialog;
    private final String shopAvatar;
    private final String userImage;

    public MakeLoginDomainData(int shopIsGold,
                               String msisdnIsVerified,
                               int shopId, String shopName,
                               String fullName,
                               ShopReputationDomain shopReputation,
                               String isLogin,
                               UserReputationDomain userReputation,
                               int shopHasTerms,
                               int shopIsOfficial,
                               int isRegisterDevice,
                               int userId,
                               int msisdnShowDialog,
                               String shopAvatar,
                               String userImage) {
        this.shopIsGold = shopIsGold;
        this.msisdnIsVerified = msisdnIsVerified;
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
    }
}
