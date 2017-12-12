package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo.ActResult;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ActResultDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ProductOwnerDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.ReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.pojo.ProductOwner;
import com.tokopedia.tkpd.tkpdreputation.shopreputation.domain.pojo.ReviewResponse;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 20/07/17.
 */

public class ActResultMapper implements Func1<Response<TkpdResponse>, ActResultDomain> {
    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";
    @Override
    public ActResultDomain call(Response<TkpdResponse> response) {
        ActResultDomain actResultDomain = new ActResultDomain();
        if (response.isSuccessful()) {
            ActResult actResult = response.body().convertDataObj(ActResult.class);
            if (!response.body().isError()) {
                if (actResult.getIsSuccess() == 0) {
                    throw new ErrorMessageException(DEFAULT_ERROR);
                } else {
                    actResultDomain = mappingActResultDomain(actResult);
                    actResultDomain.setSuccess(true);
                }
            } else {
                actResultDomain.setSuccess(false);
                actResultDomain.setErrMessage(generateMessageError(response));
                throw new ErrorMessageException(actResultDomain.getErrMessage());
            }
        } else {
            actResultDomain.setSuccess(false);
            actResultDomain.setErrCode(response.code());
        }
        return actResultDomain;
    }
    private String generateMessageError(retrofit2.Response<TkpdResponse> response) {
        return response.body().getErrorMessageJoined();
    }


    private ActResultDomain mappingActResultDomain(ActResult actResult) {
        ActResultDomain actResultDomain = new ActResultDomain();
        actResultDomain.setAttachmentId(actResult.getAttachmentId());
        actResultDomain.setFeedbackId(actResult.getFeedbackId());
        actResultDomain.setIsOwner(actResult.getIsOwner());
        actResultDomain.setPostKey(actResult.getPostKey());
        if (actResult.getProductOwner() != null) {
            actResultDomain.setProductOwner(mappingProductOwnerDomain(actResult.getProductOwner()));
        }
        actResultDomain.setReputationReviewCounter(actResult.getReputationReviewCounter());
        if (actResult.getReviewResponse() != null) {
            actResultDomain.setReviewResponse(mappingReviewResponseDomain(actResult.getReviewResponse()));
        }
        actResultDomain.setShowBookmark(actResult.getShowBookmark());
        return actResultDomain;
    }

    private ProductOwnerDomain mappingProductOwnerDomain(ProductOwner productOwner) {
        ProductOwnerDomain productOwnerDomain = new ProductOwnerDomain();
        productOwnerDomain.setFullName(productOwner.getFullName());
        productOwnerDomain.setShopId(productOwner.getShopId());
        productOwnerDomain.setShopImg(productOwner.getShopImg());
        productOwnerDomain.setShopName(productOwner.getShopName());
        productOwnerDomain.setShopReputationBadge(productOwner.getShopReputationBadge());
        productOwnerDomain.setShopReputationScore(productOwner.getShopReputationScore());
        productOwnerDomain.setShopUrl(productOwner.getShopUrl());
        productOwnerDomain.setUserId(productOwner.getUserId());
        productOwnerDomain.setUserImg(productOwner.getUserImg());
        productOwnerDomain.setUserLabel(productOwner.getUserLabel());
        productOwnerDomain.setUserLabelId(productOwner.getUserLabelId());
        productOwnerDomain.setUserUrl(productOwner.getUserUrl());
        return productOwnerDomain;
    }

    private ReviewResponseDomain mappingReviewResponseDomain(ReviewResponse reviewResponse) {
        ReviewResponseDomain reviewResponseDomain = new ReviewResponseDomain();
        reviewResponseDomain.setResponseMsg(reviewResponse.getResponseMsg());
        reviewResponseDomain.setResponseTimeAgo(reviewResponse.getResponseTimeAgo());
        reviewResponseDomain.setResponseTimeFmt(reviewResponse.getResponseTimeFmt());
        return reviewResponseDomain;
    }
}
