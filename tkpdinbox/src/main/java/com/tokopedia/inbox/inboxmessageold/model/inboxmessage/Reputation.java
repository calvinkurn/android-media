
package com.tokopedia.inbox.inboxmessageold.model.inboxmessage;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Reputation implements Parcelable {

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
    private String score;

    protected Reputation(Parcel in) {
        tooltip = in.readString();
        reputationBadge = in.readParcelable(ReputationBadge.class.getClassLoader());
        reputationScore = in.readString();
        minBadgeScore = in.readInt();
        score = in.readString();
    }

    public static final Creator<Reputation> CREATOR = new Creator<Reputation>() {
        @Override
        public Reputation createFromParcel(Parcel in) {
            return new Reputation(in);
        }

        @Override
        public Reputation[] newArray(int size) {
            return new Reputation[size];
        }
    };

    /**
     * 
     * @return
     *     The tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * 
     * @param tooltip
     *     The tooltip
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * 
     * @return
     *     The reputationBadge
     */
    public ReputationBadge getReputationBadge() {
        return reputationBadge;
    }

    /**
     * 
     * @param reputationBadge
     *     The reputation_badge
     */
    public void setReputationBadge(ReputationBadge reputationBadge) {
        this.reputationBadge = reputationBadge;
    }

    /**
     * 
     * @return
     *     The reputationScore
     */
    public String getReputationScore() {
        return reputationScore;
    }

    /**
     * 
     * @param reputationScore
     *     The reputation_score
     */
    public void setReputationScore(String reputationScore) {
        this.reputationScore = reputationScore;
    }

    /**
     * 
     * @return
     *     The minBadgeScore
     */
    public int getMinBadgeScore() {
        return minBadgeScore;
    }

    /**
     * 
     * @param minBadgeScore
     *     The min_badge_score
     */
    public void setMinBadgeScore(int minBadgeScore) {
        this.minBadgeScore = minBadgeScore;
    }

    /**
     * 
     * @return
     *     The score
     */
    public String getScore() {
        return score;
    }

    /**
     * 
     * @param score
     *     The score
     */
    public void setScore(String score) {
        this.score = score;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tooltip);
        dest.writeParcelable(reputationBadge, flags);
        dest.writeString(reputationScore);
        dest.writeInt(minBadgeScore);
        dest.writeString(score);
    }
}
