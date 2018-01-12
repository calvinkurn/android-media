package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

/**
 * @author by nisie on 10/27/17.
 */

public class KolViewModel implements Visitable<FeedPlusTypeFactory> {
    private final String tagsType;
    private final String contentLink;
    private final int userId;
    private final String cardType;
    private String title;
    private String name;
    private String avatar;
    private String label;
    private String kolProfileUrl;
    private boolean followed;
    private String productTooltip;
    private String review;
    private boolean liked;
    private int totalLike;
    private int totalComment;
    private int page;
    private boolean temporarilyFollowed;
    private String kolImage;
    private int contentId;
    private int kolId;
    private boolean reviewExpanded;
    private String time;
    private String contentName;
    private String productPrice;
    private boolean wishlisted;
    private boolean isShowComment;

    public KolViewModel(String title, String name, String avatar, String label,
                        boolean followed, String kolImage, String productTooltip,
                        String review, boolean liked, int totalLike, int totalComment,
                        int page, String kolProfileUrl, int contentId, int id, String time,
                        String contentName, String productPrice, boolean wishlisted, String tagsType,
                        String contentLink, int userId, boolean isShowComment, String cardType) {
        this.title = title;
        this.name = name;
        this.avatar = avatar;
        this.label = label;
        this.followed = followed;
        this.kolImage = kolImage;
        this.productTooltip = productTooltip;
        this.review = review;
        this.liked = liked;
        this.totalLike = totalLike;
        this.totalComment = totalComment;
        this.page = page;
        this.kolProfileUrl = kolProfileUrl;
        this.contentId = contentId;
        this.kolId = id;
        this.time = time;
        this.contentName = contentName;
        this.productPrice = productPrice;
        this.wishlisted = wishlisted;
        this.tagsType = tagsType;
        this.contentLink = contentLink;
        this.userId = userId;
        this.isShowComment = isShowComment;
        this.cardType = cardType;
    }

    public String getTitle() {
        return title;
    }

    public String getName() {
        return name;
    }

    public String getAvatar() {
        return avatar;
    }

    public String getLabel() {
        return label;
    }

    public boolean isFollowed() {
        return followed;
    }

    public String getKolImage() {
        return kolImage;
    }

    public String getProductTooltip() {
        return productTooltip;
    }

    public String getReview() {
        return review;
    }

    public boolean isLiked() {
        return liked;
    }

    public int getTotalLike() {
        return totalLike;
    }

    public int getTotalComment() {
        return totalComment;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getKolProfileUrl() {
        return kolProfileUrl;
    }

    public boolean isTemporarilyFollowed() {
        return temporarilyFollowed;
    }

    public int getContentId() {
        return contentId;
    }

    public void setProductId(int contentId) {
        this.contentId = contentId;
    }


    public int getId() {
        return kolId;
    }

    public void setId(int id) {
        this.kolId = id;
    }

    @Override
    public int type(FeedPlusTypeFactory typeFactory) {
        return typeFactory.type(this);
    }

    public void setFollowed(boolean followed) {
        this.followed = followed;
    }

    public void setTemporarilyFollowed(boolean temporarilyFollowed) {
        this.temporarilyFollowed = temporarilyFollowed;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isReviewExpanded() {
        return reviewExpanded;
    }

    public void setReviewExpanded(boolean reviewExpanded) {
        this.reviewExpanded = reviewExpanded;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContentName() {
        return contentName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }


    public boolean isWishlisted() {
        return wishlisted;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }

    public String getContentLink() {
        return contentLink;
    }

    public int getUserId() {
        return userId;
    }

    public String getTagsType() {
        return tagsType;
    }

    public boolean isShowComment() {
        return isShowComment;
    }

    public void setShowComment(boolean showComment) {
        isShowComment = showComment;
    }

    public String getCardType() {
        return cardType;
    }
}

