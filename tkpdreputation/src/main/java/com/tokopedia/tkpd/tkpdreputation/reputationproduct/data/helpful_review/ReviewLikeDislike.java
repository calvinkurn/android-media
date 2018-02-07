package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.helpful_review;

/**
 * Created by stevenfredian on 3/1/16.
 */
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReviewLikeDislike implements Parcelable{

    @SerializedName("total_like")
    @Expose
    private int totalLike;
    @SerializedName("total_dislike")
    @Expose
    private int totalDislike;

    protected ReviewLikeDislike(Parcel in) {
        totalLike = in.readInt();
        totalDislike = in.readInt();
    }

    public static final Creator<ReviewLikeDislike> CREATOR = new Creator<ReviewLikeDislike>() {
        @Override
        public ReviewLikeDislike createFromParcel(Parcel in) {
            return new ReviewLikeDislike(in);
        }

        @Override
        public ReviewLikeDislike[] newArray(int size) {
            return new ReviewLikeDislike[size];
        }
    };

    /**
     * @return The totalLike
     */
    public int getTotalLike() {
        return totalLike;
    }

    /**
     * @param totalLike The total_like
     */
    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    /**
     * @return The totalDislike
     */
    public int getTotalDislike() {
        return totalDislike;
    }

    /**
     * @param totalDislike The total_dislike
     */
    public void setTotalDislike(int totalDislike) {
        this.totalDislike = totalDislike;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(totalLike);
        dest.writeInt(totalDislike);
    }
}