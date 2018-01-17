package com.tokopedia.tkpd.tkpdreputation.productreview.view;

import android.text.TextUtils;

import com.tokopedia.tkpd.tkpdreputation.inbox.view.viewmodel.inboxdetail.ImageAttachmentViewModel;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.Owner;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.Review;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.ReviewImageAttachment;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.adapter.ProductReviewModelContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 1/17/18.
 */

public class ProductReviewListMapper {
    public List<ProductReviewModelContent> map(DataResponseReviewProduct dataResponseReviewProduct, String userId) {
        List<ProductReviewModelContent> reviewModelContents = new ArrayList<>();
        for(Review review : dataResponseReviewProduct.getList()){
            ProductReviewModelContent productReviewModelContent = new ProductReviewModelContent();
            productReviewModelContent.setResponseCreateTime(review.getReviewResponse().getResponseTime().getDateTimeFmt1());
            productReviewModelContent.setResponseMessage(review.getReviewMessage());
            productReviewModelContent.setReviewCanReported(isReviewCanReported(userId, review));
            productReviewModelContent.setReputationId(String.valueOf(review.getReputationId()));
            productReviewModelContent.setProductId(String.valueOf(dataResponseReviewProduct.getProduct().getProductId()));
            productReviewModelContent.setReviewerId(String.valueOf(review.getReviewId()));
            productReviewModelContent.setReviewAttachment(generateImageAttachmentModel(review));
            productReviewModelContent.setReviewIsAnonymous(review.getReviewAnonymous() == 1);
            productReviewModelContent.setReviewStar(review.getProductRating());
            productReviewModelContent.setSellerName(dataResponseReviewProduct.getOwner().getUser().getUserFullName());
            productReviewModelContent.setReviewTime(getReviewCreateTime(review));
            productReviewModelContent.setReviewerName(review.getUser().getFullName());
            productReviewModelContent.setReviewHasReplied(!TextUtils.isEmpty(review.getReviewResponse().getResponseMessage()));
            productReviewModelContent.setSellerRepliedOwner(isUserOwnedReplied(userId, dataResponseReviewProduct.getOwner()));
            productReviewModelContent.setShopId(String.valueOf(dataResponseReviewProduct.getOwner().getShop().getShopId()));
            reviewModelContents.add(productReviewModelContent);
        }
        return reviewModelContents;
    }

    public List<ProductReviewModelContent> map(DataResponseReviewHelpful dataResponseReviewHelpful, String userId){
        List<ProductReviewModelContent> reviewModelContents = new ArrayList<>();
        for(Review review : dataResponseReviewHelpful.getList()){
            ProductReviewModelContent productReviewModelContent = new ProductReviewModelContent();
            productReviewModelContent.setResponseCreateTime(review.getReviewResponse().getResponseTime().getDateTimeFmt1());
            productReviewModelContent.setResponseMessage(review.getReviewMessage());
            productReviewModelContent.setReviewCanReported(isReviewCanReported(userId, review));
            productReviewModelContent.setReputationId(String.valueOf(review.getReputationId()));
            productReviewModelContent.setReviewerId(String.valueOf(review.getReviewId()));
            productReviewModelContent.setReviewAttachment(generateImageAttachmentModel(review));
            productReviewModelContent.setReviewIsAnonymous(review.getReviewAnonymous() == 1);
            productReviewModelContent.setReviewStar(review.getProductRating());
            productReviewModelContent.setSellerName(dataResponseReviewHelpful.getOwner().getUser().getUserFullName());
            productReviewModelContent.setReviewTime(getReviewCreateTime(review));
            productReviewModelContent.setReviewerName(review.getUser().getFullName());
            productReviewModelContent.setReviewHasReplied(!TextUtils.isEmpty(review.getReviewResponse().getResponseMessage()));
            productReviewModelContent.setSellerRepliedOwner(isUserOwnedReplied(userId, dataResponseReviewHelpful.getOwner()));
            productReviewModelContent.setShopId(String.valueOf(dataResponseReviewHelpful.getOwner().getShop().getShopId()));
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
