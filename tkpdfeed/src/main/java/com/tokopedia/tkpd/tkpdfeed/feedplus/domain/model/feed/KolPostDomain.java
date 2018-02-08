package com.tokopedia.tkpd.tkpdfeed.feedplus.domain.model.feed;

import javax.annotation.Nullable;

/**
 * @author by nisie on 11/2/17.
 */

public class KolPostDomain {
    private final
    @Nullable
    int id;

    private final
    @Nullable
    String imageUrl;

    private final
    @Nullable
    String description;

    private final
    @Nullable
    int commentCount;

    private final
    @Nullable
    int likeCount;

    private final
    @Nullable
    boolean isLiked;

    private final
    @Nullable
    boolean isFollowed;

    private final
    @Nullable
    String createTime;

    private final
    @Nullable
    String productPrice;

    private final
    @Nullable
    String contentLink;

    private final
    @Nullable
    String contentUrl;

    private final
    @Nullable
    String userName;

    private final
    @Nullable
    String userPhoto;

    private final
    @Nullable
    String tagsType;

    private final
    @Nullable
    String caption;

    private final
    @Nullable
    int itemId;

    private final
    @Nullable
    String label;

    private final
    @Nullable
    String headerTitle;

    @Nullable
    private final String userUrl;

    @Nullable
    private final int userId;

    @Nullable
    private final boolean isShowComment;

    @Nullable
    private final String cardType;


    public KolPostDomain(int id, String imageUrl, String description, int commentCount,
                         int likeCount, boolean isLiked, boolean isFollowed, String createTime,
                         String productPrice, String contentLink, String contentUrl, String userName,
                         String userPhoto, String tagsType, String caption, int itemId, String label,
                         String headerTitle, String userUrl, int userId, boolean isShowComment, String cardType) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.description = description;
        this.commentCount = commentCount;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
        this.isFollowed = isFollowed;
        this.createTime = createTime;
        this.productPrice = productPrice;
        this.contentLink = contentLink;
        this.contentUrl = contentUrl;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.tagsType = tagsType;
        this.caption = caption;
        this.itemId = itemId;
        this.label = label;
        this.headerTitle = headerTitle;
        this.userUrl = userUrl;
        this.userId = userId;
        this.isShowComment = isShowComment;
        this.cardType = cardType;
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
    public String getContentLink() {
        return contentLink;
    }

    @Nullable
    public String getContentUrl() {
        return contentUrl;
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

    @Nullable
    public String getCaption() {
        return caption;
    }

    @Nullable
    public int getItemId() {
        return itemId;
    }

    @Nullable
    public String getLabel() {
        return label;
    }

    @Nullable
    public String getHeaderTitle() {
        return headerTitle;
    }

    @Nullable
    public String getUserUrl() {
        return userUrl;
    }

    @Nullable
    public int getUserId() {
        return userId;
    }

    @Nullable
    public boolean isShowComment() {
        return isShowComment;
    }

    @Nullable
    public String getCardType() {
        return cardType;
    }
}
