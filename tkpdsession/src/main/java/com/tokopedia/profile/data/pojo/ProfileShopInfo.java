package com.tokopedia.profile.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * @author by alvinatin on 28/02/18.
 */

public class ProfileShopInfo {

    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {
        @SerializedName("shop_id")
        @Expose
        private int shopId;
        @SerializedName("shop_url")
        @Expose
        private String shopUrl;
        @SerializedName("user_id")
        @Expose
        private int userId;
        @SerializedName("admin_id")
        @Expose
        private List<Integer> adminId = null;
        @SerializedName("shop_name")
        @Expose
        private String shopName;
        @SerializedName("domain")
        @Expose
        private String domain;
        @SerializedName("is_gold")
        @Expose
        private int isGold;
        @SerializedName("is_gold_badge")
        @Expose
        private boolean isGoldBadge;
        @SerializedName("is_official")
        @Expose
        private int isOfficial;
        @SerializedName("is_free_returns")
        @Expose
        private int isFreeReturns;
        @SerializedName("shop_status_title")
        @Expose
        private String shopStatusTitle;
        @SerializedName("shop_status_message")
        @Expose
        private String shopStatusMessage;
        @SerializedName("location")
        @Expose
        private String location;
        @SerializedName("logo")
        @Expose
        private String logo;
        @SerializedName("reputation_badge")
        @Expose
        private String reputationBadge;
        @SerializedName("s_reputation")
        @Expose
        private ShopReputation shopReputation;
        @SerializedName("s_badges")
        @Expose
        private List<Object> sBadges = null;
        @SerializedName("use_ace")
        @Expose
        private int useAce;
        @SerializedName("last_online")
        @Expose
        private String lastOnline;
        @SerializedName("applink")
        @Expose
        private String applink;

        public int getShopId() {
            return shopId;
        }

        public void setShopId(int shopId) {
            this.shopId = shopId;
        }

        public String getShopUrl() {
            return shopUrl;
        }

        public void setShopUrl(String shopUrl) {
            this.shopUrl = shopUrl;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public List<Integer> getAdminId() {
            return adminId;
        }

        public void setAdminId(List<Integer> adminId) {
            this.adminId = adminId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getDomain() {
            return domain;
        }

        public void setDomain(String domain) {
            this.domain = domain;
        }

        public int getIsGold() {
            return isGold;
        }

        public void setIsGold(int isGold) {
            this.isGold = isGold;
        }

        public boolean isGoldBadge() {
            return isGoldBadge;
        }

        public void setGoldBadge(boolean goldBadge) {
            isGoldBadge = goldBadge;
        }

        public int getIsOfficial() {
            return isOfficial;
        }

        public void setIsOfficial(int isOfficial) {
            this.isOfficial = isOfficial;
        }

        public int getIsFreeReturns() {
            return isFreeReturns;
        }

        public void setIsFreeReturns(int isFreeReturns) {
            this.isFreeReturns = isFreeReturns;
        }

        public String getShopStatusTitle() {
            return shopStatusTitle;
        }

        public void setShopStatusTitle(String shopStatusTitle) {
            this.shopStatusTitle = shopStatusTitle;
        }

        public String getShopStatusMessage() {
            return shopStatusMessage;
        }

        public void setShopStatusMessage(String shopStatusMessage) {
            this.shopStatusMessage = shopStatusMessage;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getReputationBadge() {
            return reputationBadge;
        }

        public void setReputationBadge(String reputationBadge) {
            this.reputationBadge = reputationBadge;
        }

        public ShopReputation getShopReputation() {
            return shopReputation;
        }

        public void setShopReputation(ShopReputation shopReputation) {
            this.shopReputation = shopReputation;
        }

        public List<Object> getsBadges() {
            return sBadges;
        }

        public void setsBadges(List<Object> sBadges) {
            this.sBadges = sBadges;
        }

        public int getUseAce() {
            return useAce;
        }

        public void setUseAce(int useAce) {
            this.useAce = useAce;
        }

        public String getLastOnline() {
            return lastOnline;
        }

        public void setLastOnline(String lastOnline) {
            this.lastOnline = lastOnline;
        }

        public String getApplink() {
            return applink;
        }

        public void setApplink(String applink) {
            this.applink = applink;
        }

        public static class ShopReputation {
            @SerializedName("tooltip")
            @Expose
            public String tooltip;
            @SerializedName("reputation_badge")
            @Expose
            public String reputationBadge;
            @SerializedName("ReputationScore")
            @Expose
            public String reputationScore;
            @SerializedName("min_badge_score")
            @Expose
            public int minBadgeScore;
            @SerializedName("score")
            @Expose
            public int score;
            @SerializedName("badge_level")
            @Expose
            public int badgeLevel;

            public String getTooltip() {
                return tooltip;
            }

            public void setTooltip(String tooltip) {
                this.tooltip = tooltip;
            }

            public String getReputationBadge() {
                return reputationBadge;
            }

            public void setReputationBadge(String reputationBadge) {
                this.reputationBadge = reputationBadge;
            }

            public String getReputationScore() {
                return reputationScore;
            }

            public void setReputationScore(String reputationScore) {
                this.reputationScore = reputationScore;
            }

            public int getMinBadgeScore() {
                return minBadgeScore;
            }

            public void setMinBadgeScore(int minBadgeScore) {
                this.minBadgeScore = minBadgeScore;
            }

            public int getScore() {
                return score;
            }

            public void setScore(int score) {
                this.score = score;
            }

            public int getBadgeLevel() {
                return badgeLevel;
            }

            public void setBadgeLevel(int badgeLevel) {
                this.badgeLevel = badgeLevel;
            }
        }
    }


}
