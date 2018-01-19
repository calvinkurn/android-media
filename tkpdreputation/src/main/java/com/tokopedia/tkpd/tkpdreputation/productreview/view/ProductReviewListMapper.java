package com.tokopedia.tkpd.tkpdreputation.productreview.view;

import android.text.TextUtils;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.Owner;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.Review;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.ReviewImageAttachment;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewModel;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewModelContent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by zulfikarrahman on 1/17/18.
 */

public class ProductReviewListMapper {

    public static final int LIKE_STATUS_ACTIVE = 1;

    @Inject
    public ProductReviewListMapper() {
    }

    public List<ProductReviewModel> map(DataResponseReviewProduct dataResponseReviewProduct, String userId) {
        List<ProductReviewModel> reviewModelContents = new ArrayList<>();
        for(Review review : dataResponseReviewProduct.getList()){
            ProductReviewModelContent productReviewModelContent = new ProductReviewModelContent();
            productReviewModelContent.setResponseCreateTime(review.getReviewResponse().getResponseTime().getDateTimeFmt1());
            productReviewModelContent.setResponseMessage(review.getReviewResponse().getResponseMessage());
            productReviewModelContent.setReviewCanReported(isReviewCanReported(userId, review));
            productReviewModelContent.setReputationId(String.valueOf(review.getReputationId()));
            productReviewModelContent.setProductId(String.valueOf(dataResponseReviewProduct.getProduct().getProductId()));
            productReviewModelContent.setReviewerId(String.valueOf(review.getUser().getUserId()));
            productReviewModelContent.setReviewAttachment(generateImageAttachmentModel(review));
            productReviewModelContent.setReviewIsAnonymous(review.getReviewAnonymous() == 1);
            productReviewModelContent.setReviewStar(review.getProductRating());
            productReviewModelContent.setSellerName(dataResponseReviewProduct.getOwner().getUser().getUserFullName());
            productReviewModelContent.setReviewTime(getReviewCreateTime(review));
            productReviewModelContent.setReviewMessage(review.getReviewMessage());
            productReviewModelContent.setReviewerName(review.getUser().getFullName());
            productReviewModelContent.setReviewHasReplied(!TextUtils.isEmpty(review.getReviewResponse().getResponseMessage()));
            productReviewModelContent.setSellerRepliedOwner(isUserOwnedReplied(userId, dataResponseReviewProduct.getOwner()));
            productReviewModelContent.setShopId(String.valueOf(dataResponseReviewProduct.getOwner().getShop().getShopId()));
            productReviewModelContent.setLikeStatus(review.getLikeStatus() == LIKE_STATUS_ACTIVE);
            productReviewModelContent.setTotalLike(review.getTotalLike());
            productReviewModelContent.setReviewId(String.valueOf(review.getReviewId()));
            reviewModelContents.add(productReviewModelContent);
        }
        return reviewModelContents;
    }

    public List<ProductReviewModel> map(DataResponseReviewHelpful dataResponseReviewHelpful, String userId){
        List<ProductReviewModel> reviewModelContents = new ArrayList<>();
        for(Review review : dataResponseReviewHelpful.getList()){
            ProductReviewModelContent productReviewModelContent = new ProductReviewModelContent();
            productReviewModelContent.setResponseCreateTime(review.getReviewResponse().getResponseTime().getDateTimeFmt1());
            productReviewModelContent.setResponseMessage(review.getReviewMessage());
            productReviewModelContent.setReviewCanReported(isReviewCanReported(userId, review));
            productReviewModelContent.setReputationId(String.valueOf(review.getReputationId()));
            productReviewModelContent.setReviewerId(String.valueOf(review.getUser().getUserId()));
            productReviewModelContent.setReviewAttachment(generateImageAttachmentModel(review));
            productReviewModelContent.setReviewIsAnonymous(review.getReviewAnonymous() == 1);
            productReviewModelContent.setReviewStar(review.getProductRating());
            productReviewModelContent.setSellerName(dataResponseReviewHelpful.getOwner().getUser().getUserFullName());
            productReviewModelContent.setReviewTime(getReviewCreateTime(review));
            productReviewModelContent.setReviewerName(review.getUser().getFullName());
            productReviewModelContent.setReviewHasReplied(!TextUtils.isEmpty(review.getReviewResponse().getResponseMessage()));
            productReviewModelContent.setSellerRepliedOwner(isUserOwnedReplied(userId, dataResponseReviewHelpful.getOwner()));
            productReviewModelContent.setShopId(String.valueOf(dataResponseReviewHelpful.getOwner().getShop().getShopId()));
            productReviewModelContent.setLikeStatus(review.getLikeStatus() == LIKE_STATUS_ACTIVE);
            productReviewModelContent.setTotalLike(review.getTotalLike());
            productReviewModelContent.setReviewId(String.valueOf(review.getReviewId()));
            reviewModelContents.add(productReviewModelContent);
        }
        return reviewModelContents;
    }

    private boolean isUserOwnedReplied(String userId, Owner owner) {
        return userId.equals(String.valueOf(owner.getUser().getUserId()));
    }

    private List<ImageAttachmentViewModel> generateImageAttachmentModel(Review review) {
        List<ImageAttachmentViewModel> imageAttachmentViewModels = new ArrayList<>();
        for(ReviewImageAttachment reviewImageAttachment :review.getReviewImageAttachment()){
            ImageAttachmentViewModel imageAttachmentViewModel = new ImageAttachmentViewModel(reviewImageAttachment.getAttachmentId(),
                    reviewImageAttachment.getDescription(), reviewImageAttachment.getUriThumbnail(), reviewImageAttachment.getUriLarge());
            imageAttachmentViewModels.add(imageAttachmentViewModel);
        }
        return imageAttachmentViewModels;
    }

    private boolean isReviewCanReported(String userId, Review review) {
        return !TextUtils.isEmpty(userId) && !userId.equals(String.valueOf(review.getUser().getUserId()));
    }

    private String getReviewCreateTime(Review review) {
        if(!TextUtils.isEmpty(review.getReviewUpdateTime().getDateTimeFmt1())){
            return review.getReviewUpdateTime().getDateTimeFmt1();
        }else {
            return review.getReviewCreateTime().getDateTimeFmt1();
        }
    }
}
