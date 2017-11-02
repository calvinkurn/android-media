package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

import javax.annotation.Nullable;

/**
 * @author by nisie on 11/2/17.
 */

public class KolPostDomain {
    private final @Nullable
    int id;

    private final @Nullable String imageUrl;

    private final @Nullable String description;

    private final @Nullable int commentCount;

    private final @Nullable int likeCount;

    private final @Nullable boolean isLiked;

    private final @Nullable boolean isFollowed;

    private final @Nullable String createTime;

    private final @Nullable String productPrice;

    private final @Nullable String productLink;

    private final @Nullable String productUrl;

    private final @Nullable String shopUrl;

    private final @Nullable String shopLink;

    private final @Nullable String userName;

    private final @Nullable String userPhoto;

    private final @Nullable String tagsType;

    public KolPostDomain(int id, String imageUrl, String description, int commentCount,
                         int likeCount, boolean isLiked, boolean isFollowed, String createTime,
                         String productPrice, String productLink, String productUrl, String shopUrl,
                         String shopLink, String userName, String userPhoto, String tagsType) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.description = description;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.isFollowed = isFollowed;
        this.createTime = createTime;
        this.productPrice = productPrice;
        this.productLink = productLink;
        this.productUrl = productUrl;
        this.shopUrl = shopUrl;
        this.shopLink = shopLink;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.tagsType = tagsType;
    }

    @Nullable
    public int getId() {
        return id;
    }

    @Nullable
    public String getImageUrl() {
        return imageUrl;
    }

    @Nullable
    public String getDescription() {
        return description;
    }

    @Nullable
    public int getCommentCount() {
        return commentCount;
    }

    @Nullable
    public int getLikeCount() {
        return likeCount;
    }

    @Nullable
    public boolean isLiked() {
        return isLiked;
    }

    @Nullable
    public boolean isFollowed() {
        return isFollowed;
    }

    @Nullable
    public String getCreateTime() {
        return createTime;
    }

    @Nullable
    public String getProductPrice() {
        return productPrice;
    }

    @Nullable
    public String getProductLink() {
        return productLink;
    }

    @Nullable
    public String getProductUrl() {
        return productUrl;
    }

    @Nullable
    public String getShopUrl() {
        return shopUrl;
    }

    @Nullable
    public String getShopLink() {
        return shopLink;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    @Nullable
    public String getUserPhoto() {
        return userPhoto;
    }

    @Nullable
    public String getTagsType() {
        return tagsType;
    }
}
