package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author by nisie on 8/30/17.
 */

public class RevieweeBadgeSellerViewModel implements Parcelable {

    private String tooltip;
    private String reputationScore;
    private int score;
    private int minBadgeScore;
    private String reputationBadgeUrl;
    private ReputationBadgeViewModel reputationBadge;

    public RevieweeBadgeSellerViewModel(String tooltip, String reputationScore, int score,
                                        int minBadgeScore, String reputationBadgeUrl,
                                        ReputationBadgeViewModel reputationBadge) {
        this.tooltip = tooltip;
        this.reputationScore = reputationScore;
        this.score = score;
        this.minBadgeScore = minBadgeScore;
        this.reputationBadgeUrl = reputationBadgeUrl;
        this.reputationBadge = reputationBadge;
    }

    protected RevieweeBadgeSellerViewModel(Parcel in) {
        tooltip = in.readString();
        reputationScore = in.readString();
        score = in.readInt();
        minBadgeScore = in.readInt();
        reputationBadgeUrl = in.readString();
        reputationBadge = in.readParcelable(ReputationBadgeViewModel.class.getClassLoader());
    }

    public static final Creator<RevieweeBadgeSellerViewModel> CREATOR = new Creator<RevieweeBadgeSellerViewModel>() {
        @Override
        public RevieweeBadgeSellerViewModel createFromParcel(Parcel in) {
            return new RevieweeBadgeSellerViewModel(in);
        }

        @Override
        public RevieweeBadgeSellerViewModel[] newArray(int size) {
            return new RevieweeBadgeSellerViewModel[size];
        }
    };

    public String getTooltip() {
        return tooltip;
    }

    public String getReputationScore() {
        return reputationScore;
    }

    public int getScore() {
        return score;
    }

    public int getMinBadgeScore() {
        return minBadgeScore;
    }

    public String getReputationBadgeUrl() {
        return reputationBadgeUrl;
    }

    public ReputationBadgeViewModel getReputationBadge() {
        return reputationBadge;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tooltip);
        dest.writeString(reputationScore);
        dest.writeInt(score);
        dest.writeInt(minBadgeScore);
        dest.writeString(reputationBadgeUrl);
        dest.writeParcelable(reputationBadge, flags);
    }
}
