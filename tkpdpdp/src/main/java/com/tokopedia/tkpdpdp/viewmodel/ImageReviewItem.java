package com.tokopedia.tkpdpdp.viewmodel;

public class ImageReviewItem {
    private String formattedDate;
    private String reviewerName;
    private String imageUrlThumbnail;
    private String imageUrlLarge;
    private int rating;

    public String getFormattedDate() {
        return formattedDate;
    }

    public void setFormattedDate(String formattedDate) {
        this.formattedDate = formattedDate;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public void setReviewerName(String reviewerName) {
        this.reviewerName = reviewerName;
    }

    public String getImageUrlThumbnail() {
        return imageUrlThumbnail;
    }

    public void setImageUrlThumbnail(String imageUrlThumbnail) {
        this.imageUrlThumbnail = imageUrlThumbnail;
    }

    public String getImageUrlLarge() {
        return imageUrlLarge;
    }

    public void setImageUrlLarge(String imageUrlLarge) {
        this.imageUrlLarge = imageUrlLarge;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
