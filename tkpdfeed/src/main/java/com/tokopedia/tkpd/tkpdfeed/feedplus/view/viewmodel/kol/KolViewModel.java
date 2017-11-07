package com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.kol;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.adapter.typefactory.feed.FeedPlusTypeFactory;

/**
 * @author by nisie on 10/27/17.
 */

public class KolViewModel implements Visitable<FeedPlusTypeFactory> {
    private final String tagsType;
    private String title;
    private String name;
    private String avatar;
    private String label;
    private boolean followed;
    private String productImage;
    private String productTooltip;
    private String review;
    private boolean liked;
    private int totalLike;
    private int totalComment;
    private int page;
    private String kolProfileUrl;
    private boolean temporarilyFollowed;
    private String productId;
    private int id;
    private boolean reviewExpanded;
    private String time;
    private String productName;
    private String productPrice;
    private boolean wishlisted;

    public KolViewModel(String title, String name, String avatar, String label,
                        boolean followed, String productImage, String productTooltip,
                        String review, boolean liked, int totalLike, int totalComment,
                        int page, String kolProfileUrl, String productId, int id, String time,
                        String productName, String productPrice, boolean wishlisted, String tagsType) {
        this.title = title;
        this.name = name;
        this.avatar = avatar;
        this.label = label;
        this.followed = followed;
        this.productImage = productImage;
        this.productTooltip = productTooltip;
        this.review = review;
        this.liked = liked;
        this.totalLike = totalLike;
        this.totalComment = totalComment;
        this.page = page;
        this.kolProfileUrl = kolProfileUrl;
        this.productId = productId;
        this.id = id;
        this.time = time;
        this.productName = productName;
        this.productPrice = productPrice;
        this.wishlisted = wishlisted;
        this.tagsType = tagsType;
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

    public String getProductImage() {
        return productImage;
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

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
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

    public void setWishlisted(boolean wishlisted) {
        this.wishlisted = wishlisted;
    }

    public String getTagsType() {
        return tagsType;
    }

    public void setTotalLike(int totalLike) {
        this.totalLike = totalLike;
    }

    public void setTotalComment(int totalComment) {
        this.totalComment = totalComment;
    }
}

