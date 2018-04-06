
package com.tokopedia.shop.common.data.source.cloud.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShopInfoDetail {

    @SerializedName("badges")
    @Expose
    private List<Object> badges = null;
    @SerializedName("date_shop_created")
    @Expose
    private String dateShopCreated;
    @SerializedName("epoch_shop_created")
    @Expose
    private long epochShopCreated;
    @SerializedName("shop_already_favorited")
    @Expose
    private String shopAlreadyFavorited;
    @SerializedName("shop_avatar")
    @Expose
    private String shopAvatar;
    @SerializedName("shop_cover")
    @Expose
    private String shopCover;
    @SerializedName("shop_description")
    @Expose
    private String shopDescription;
    @SerializedName("shop_domain")
    @Expose
    private String shopDomain;
    @SerializedName("shop_gold_expired_time")
    @Expose
    private long shopGoldExpiredTime;
    @SerializedName("shop_has_terms")
    @Expose
    private long shopHasTerms;
    @SerializedName("shop_id")
    @Expose
    private String shopId;
    @SerializedName("shop_is_allow_manage")
    @Expose
    private long shopIsAllowManage;
    @SerializedName("shop_is_closed_note")
    @Expose
    private long shopIsClosedNote;
    @SerializedName("shop_is_closed_reason")
    @Expose
    private long shopIsClosedReason;
    @SerializedName("shop_is_closed_until")
    @Expose
    private long shopIsClosedUntil;
    @SerializedName("shop_is_free_returns")
    @Expose
    private String shopIsFreeReturns;
    @SerializedName("shop_is_gold")
    @Expose
    private String shopIsGold;
    @SerializedName("shop_is_gold_badge")
    @Expose
    private boolean shopIsGoldBadge;
    @SerializedName("shop_is_official")
    @Expose
    private String shopIsOfficial;
    @SerializedName("shop_is_owner")
    @Expose
    private long shopIsOwner;
    @SerializedName("shop_location")
    @Expose
    private String shopLocation;
    @SerializedName("shop_lucky")
    @Expose
    private String shopLucky;
    @SerializedName("shop_min_badge_score")
    @Expose
    private long shopMinBadgeScore;
    @SerializedName("shop_name")
    @Expose
    private String shopName;
    @SerializedName("shop_official_bot")
    @Expose
    private String shopOfficialBot;
    @SerializedName("shop_official_top")
    @Expose
    private String shopOfficialTop;
    @SerializedName("shop_open_since")
    @Expose
    private String shopOpenSince;
    @SerializedName("shop_owner_id")
    @Expose
    private long shopOwnerId;
    @SerializedName("shop_owner_last_login")
    @Expose
    private String shopOwnerLastLogin;
    @SerializedName("shop_reputation")
    @Expose
    private String shopReputation;
    @SerializedName("shop_reputation_badge")
    @Expose
    private String shopReputationBadge;
    @SerializedName("shop_score")
    @Expose
    private long shopScore;
    @SerializedName("shop_status")
    @Expose
    private int shopStatus;
    @SerializedName("shop_status_message")
    @Expose
    private String shopStatusMessage;
    @SerializedName("shop_status_title")
    @Expose
    private String shopStatusTitle;
    @SerializedName("shop_tagline")
    @Expose
    private String shopTagline;
    @SerializedName("shop_total_favorit")
    @Expose
    private long shopTotalFavorit;
    @SerializedName("shop_url")
    @Expose
    private String shopUrl;
    @SerializedName("total_active_product")
    @Expose
    private long totalActiveProduct;

    public List<Object> getBadges() {
        return badges;
    }

    public void setBadges(List<Object> badges) {
        this.badges = badges;
    }

    public String getDateShopCreated() {
        return dateShopCreated;
    }

    public void setDateShopCreated(String dateShopCreated) {
        this.dateShopCreated = dateShopCreated;
    }

    public long getEpochShopCreated() {
        return epochShopCreated;
    }

    public void setEpochShopCreated(long epochShopCreated) {
        this.epochShopCreated = epochShopCreated;
    }

    public String getShopAlreadyFavorited() {
        return shopAlreadyFavorited;
    }

    public void setShopAlreadyFavorited(String shopAlreadyFavorited) {
        this.shopAlreadyFavorited = shopAlreadyFavorited;
    }

    public String getShopAvatar() {
        return shopAvatar;
    }

    public void setShopAvatar(String shopAvatar) {
        this.shopAvatar = shopAvatar;
    }

    public String getShopCover() {
        return shopCover;
    }

    public void setShopCover(String shopCover) {
        this.shopCover = shopCover;
    }

    public String getShopDescription() {
        return shopDescription;
    }

    public void setShopDescription(String shopDescription) {
        this.shopDescription = shopDescription;
    }

    public String getShopDomain() {
        return shopDomain;
    }

    public void setShopDomain(String shopDomain) {
        this.shopDomain = shopDomain;
    }

    public long getShopGoldExpiredTime() {
        return shopGoldExpiredTime;
    }

    public void setShopGoldExpiredTime(long shopGoldExpiredTime) {
        this.shopGoldExpiredTime = shopGoldExpiredTime;
    }

    public long getShopHasTerms() {
        return shopHasTerms;
    }

    public void setShopHasTerms(long shopHasTerms) {
        this.shopHasTerms = shopHasTerms;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public long getShopIsAllowManage() {
        return shopIsAllowManage;
    }

    public void setShopIsAllowManage(long shopIsAllowManage) {
        this.shopIsAllowManage = shopIsAllowManage;
    }

    public long getShopIsClosedNote() {
        return shopIsClosedNote;
    }

    public void setShopIsClosedNote(long shopIsClosedNote) {
        this.shopIsClosedNote = shopIsClosedNote;
    }

    public long getShopIsClosedReason() {
        return shopIsClosedReason;
    }

    public void setShopIsClosedReason(long shopIsClosedReason) {
        this.shopIsClosedReason = shopIsClosedReason;
    }

    public long getShopIsClosedUntil() {
        return shopIsClosedUntil;
    }

    public void setShopIsClosedUntil(long shopIsClosedUntil) {
        this.shopIsClosedUntil = shopIsClosedUntil;
    }

    public String getShopIsFreeReturns() {
        return shopIsFreeReturns;
    }

    public void setShopIsFreeReturns(String shopIsFreeReturns) {
        this.shopIsFreeReturns = shopIsFreeReturns;
    }

    public String getShopIsGold() {
        return shopIsGold;
    }

    public void setShopIsGold(String shopIsGold) {
        this.shopIsGold = shopIsGold;
    }

    public boolean isShopIsGoldBadge() {
        return shopIsGoldBadge;
    }

    public void setShopIsGoldBadge(boolean shopIsGoldBadge) {
        this.shopIsGoldBadge = shopIsGoldBadge;
    }

    public String getShopIsOfficial() {
        return shopIsOfficial;
    }

    public void setShopIsOfficial(String shopIsOfficial) {
        this.shopIsOfficial = shopIsOfficial;
    }

    public long getShopIsOwner() {
        return shopIsOwner;
    }

    public void setShopIsOwner(long shopIsOwner) {
        this.shopIsOwner = shopIsOwner;
    }

    public String getShopLocation() {
        return shopLocation;
    }

    public void setShopLocation(String shopLocation) {
        this.shopLocation = shopLocation;
    }

    public String getShopLucky() {
        return shopLucky;
    }

    public void setShopLucky(String shopLucky) {
        this.shopLucky = shopLucky;
    }

    public long getShopMinBadgeScore() {
        return shopMinBadgeScore;
    }

    public void setShopMinBadgeScore(long shopMinBadgeScore) {
        this.shopMinBadgeScore = shopMinBadgeScore;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getShopOfficialBot() {
        return shopOfficialBot;
    }

    public void setShopOfficialBot(String shopOfficialBot) {
        this.shopOfficialBot = shopOfficialBot;
    }

    public String getShopOfficialTop() {
        return shopOfficialTop;
    }

    public void setShopOfficialTop(String shopOfficialTop) {
        this.shopOfficialTop = shopOfficialTop;
    }

    public String getShopOpenSince() {
        return shopOpenSince;
    }

    public void setShopOpenSince(String shopOpenSince) {
        this.shopOpenSince = shopOpenSince;
    }

    public long getShopOwnerId() {
        return shopOwnerId;
    }

    public void setShopOwnerId(long shopOwnerId) {
        this.shopOwnerId = shopOwnerId;
    }

    public String getShopOwnerLastLogin() {
        return shopOwnerLastLogin;
    }

    public void setShopOwnerLastLogin(String shopOwnerLastLogin) {
        this.shopOwnerLastLogin = shopOwnerLastLogin;
    }

    public String getShopReputation() {
        return shopReputation;
    }

    public void setShopReputation(String shopReputation) {
        this.shopReputation = shopReputation;
    }

    public String getShopReputationBadge() {
        return shopReputationBadge;
    }

    public void setShopReputationBadge(String shopReputationBadge) {
        this.shopReputationBadge = shopReputationBadge;
    }

    public long getShopScore() {
        return shopScore;
    }

    public void setShopScore(long shopScore) {
        this.shopScore = shopScore;
    }

    public int getShopStatus() {
        return shopStatus;
    }

    public void setShopStatus(int shopStatus) {
        this.shopStatus = shopStatus;
    }

    public String getShopStatusMessage() {
        return shopStatusMessage;
    }

    public void setShopStatusMessage(String shopStatusMessage) {
        this.shopStatusMessage = shopStatusMessage;
    }

    public String getShopStatusTitle() {
        return shopStatusTitle;
    }

    public void setShopStatusTitle(String shopStatusTitle) {
        this.shopStatusTitle = shopStatusTitle;
    }

    public String getShopTagline() {
        return shopTagline;
    }

    public void setShopTagline(String shopTagline) {
        this.shopTagline = shopTagline;
    }

    public long getShopTotalFavorit() {
        return shopTotalFavorit;
    }

    public void setShopTotalFavorit(long shopTotalFavorit) {
        this.shopTotalFavorit = shopTotalFavorit;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public long getTotalActiveProduct() {
        return totalActiveProduct;
    }

    public void setTotalActiveProduct(long totalActiveProduct) {
        this.totalActiveProduct = totalActiveProduct;
    }

}
