
package com.tokopedia.session.login.domain.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShopReputation {

    @SerializedName("tooltip")
    @Expose
    private String tooltip;
    @SerializedName("reputation_badge")
    @Expose
    private ReputationBadge reputationBadge;
    @SerializedName("reputation_score")
    @Expose
    private String reputationScore;
    @SerializedName("min_badge_score")
    @Expose
    private int minBadgeScore;
    @SerializedName("score")
    @Expose
    private int score;

    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public ReputationBadge getReputationBadge() {
        return reputationBadge;
    }

    public void setReputationBadge(ReputationBadge reputationBadge) {
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

}
