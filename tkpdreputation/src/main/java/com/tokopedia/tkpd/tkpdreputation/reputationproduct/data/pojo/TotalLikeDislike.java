
package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TotalLikeDislike {

    @SerializedName("total_like")
    @Expose
    private int totalLike;
    @SerializedName("total_dislike")
    @Expose
    private int totalDislike;

    /**
     * 
     * @return
     *     The totalLike
     */
    public int getTotalLike() {
        return totalLike;
    }

    /**
     * 
     * @param totalLike
     *     The total_like
     */
    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    /**
     * 
     * @return
     *     The totalDislike
     */
    public int getTotalDislike() {
        return totalDislike;
    }

    /**
     * 
     * @param totalDislike
     *     The total_dislike
     */
    public void setTotalDislike(int totalDislike) {
        this.totalDislike = totalDislike;
    }

}
