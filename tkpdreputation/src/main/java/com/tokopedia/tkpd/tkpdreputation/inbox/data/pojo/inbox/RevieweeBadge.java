
package com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RevieweeBadge {

//    SELLER BADGE
    @SerializedName("tooltip")
    @Expose
    private String tooltip;
    @SerializedName("reputation_score")
    @Expose
    private String reputationScore;
    @SerializedName("score")
    @Expose
    private int score;
    @SerializedName("min_badge_score")
    @Expose
    private int minBadgeScore;
    @SerializedName("reputation_badge_url")
    @Expose
    private String reputationBadgeUrl;
    @SerializedName("reputation_badge")
    @Expose
    private ReputationBadge reputationBadge;

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getReputationScore() {
        return reputationScore;
    }

    public void setReputationScore(String reputationScore) {
        this.reputationScore = reputationScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getMinBadgeScore() {
        return minBadgeScore;
    }

    public void setMinBadgeScore(int minBadgeScore) {
        this.minBadgeScore = minBadgeScore;
    }

    public String getReputationBadgeUrl() {
        return reputationBadgeUrl;
    }

    public void setReputationBadgeUrl(String reputationBadgeUrl) {
        this.reputationBadgeUrl = reputationBadgeUrl;
    }

    public ReputationBadge getReputationBadge() {
        return reputationBadge;
    }

    public void setReputationBadge(ReputationBadge reputationBadge) {
        this.reputationBadge = reputationBadge;
    }

    //    CUSTOMER BADGE
    @SerializedName("positive")
    @Expose
    private String positive;
    @SerializedName("neutral")
    @Expose
    private String neutral;
    @SerializedName("negative")
    @Expose
    private String negative;
    @SerializedName("positive_percentage")
    @Expose
    private String positive_percentage;
    @SerializedName("no_reputation")
    @Expose
    private String no_reputation;

    public String getPositive() {
        return positive;
    }

    public String getNeutral() {
        return neutral;
    }

    public String getNegative() {
        return negative;
    }

    public String getPositive_percentage() {
        return positive_percentage;
    }

    public String getNo_reputation() {
        return no_reputation;
    }
}
