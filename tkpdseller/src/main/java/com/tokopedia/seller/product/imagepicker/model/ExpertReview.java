
package com.tokopedia.seller.product.imagepicker.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ExpertReview {

    @SerializedName("source")
    @Expose
    private String source;
    @SerializedName("review")
    @Expose
    private String review;
    @SerializedName("good")
    @Expose
    private List<Object> good = null;
    @SerializedName("bad")
    @Expose
    private List<Object> bad = null;
    @SerializedName("rating")
    @Expose
    private String rating;
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("max_score")
    @Expose
    private int maxScore;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public List<Object> getGood() {
        return good;
    }

    public void setGood(List<Object> good) {
        this.good = good;
    }

    public List<Object> getBad() {
        return bad;
    }

    public void setBad(List<Object> bad) {
        this.bad = bad;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

}
