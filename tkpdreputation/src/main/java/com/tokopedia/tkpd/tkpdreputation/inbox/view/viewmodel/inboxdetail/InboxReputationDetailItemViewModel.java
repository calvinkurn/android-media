package com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail;

import android.support.annotation.Nullable;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.tkpd.tkpdreputation.inbox.view.adapter.typefactory.inboxdetail.InboxReputationDetailTypeFactory;

import java.util.ArrayList;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailItemViewModel implements
        Visitable<InboxReputationDetailTypeFactory> {

    private final String reviewId;
    private final boolean reviewHasReviewed;
    private final boolean reviewIsEditable;
    private final boolean reviewIsSkippable;
    private final int shopId;
    private final ReviewResponseViewModel reviewResponseViewModel;
    String productId;
    String productName;
    String productAvatar;
    String reviewerName;
    String reviewTime;
    ArrayList<ImageAttachmentViewModel> reviewAttachment;
    String review;
    int reviewStar;
    boolean reviewIsSkipped;
    int tab;

    public InboxReputationDetailItemViewModel(String productId, String productName,
                                              String productAvatar, String reviewId,
                                              String reviewerName, String reviewTime,
                                              ArrayList<ImageAttachmentViewModel> reviewAttachment,
                                              String review, int reviewStar,
                                              boolean reviewHasReviewed,
                                              boolean reviewIsEditable,
                                              boolean reviewIsSkippable,
                                              boolean reviewIsSkipped,
                                              int shopId,
                                              int tab,
                                              @Nullable ReviewResponseViewModel
                                                      reviewResponseViewModel) {
        this.productId = productId;
        this.productName = productName;
        this.productAvatar = productAvatar;
        this.reviewId = reviewId;
        this.reviewerName = reviewerName;
        this.reviewTime = reviewTime;
        this.reviewAttachment = reviewAttachment;
        this.review = review;
        this.reviewStar = reviewStar;
        this.reviewIsSkipped = reviewIsSkipped;
        this.reviewHasReviewed = reviewHasReviewed;
        this.reviewIsEditable = reviewIsEditable;
        this.reviewIsSkippable = reviewIsSkippable;
        this.shopId = shopId;
        this.tab = tab;
        this.reviewResponseViewModel = reviewResponseViewModel;
    }

    public String getReviewId() {
        return reviewId;
    }

    public String getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductAvatar() {
        return productAvatar;
    }

    public String getReviewerName() {
        return reviewerName;
    }

    public String getReviewTime() {
        return reviewTime;
    }

    public ArrayList<ImageAttachmentViewModel> getReviewAttachment() {
        return reviewAttachment;
    }

    public String getReview() {
        return review;
    }

    public int getReviewStar() {
        return reviewStar;
    }

    public boolean isReviewSkipped() {
        return reviewIsSkipped;
    }

    public boolean isReviewHasReviewed() {
        return reviewHasReviewed;
    }

    public boolean isReviewIsEditable() {
        return reviewIsEditable;
    }

    public boolean isReviewIsSkippable() {
        return reviewIsSkippable;
    }

    public int getShopId() {
        return shopId;
    }

    public int getTab() {
        return tab;
    }

    public ReviewResponseViewModel getReviewResponseViewModel() {
        return reviewResponseViewModel;
    }

    @Override
    public int type(InboxReputationDetailTypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
