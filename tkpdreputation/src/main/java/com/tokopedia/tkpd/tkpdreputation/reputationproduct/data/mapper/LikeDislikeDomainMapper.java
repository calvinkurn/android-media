package com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.LikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.domain.model.TotalLikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo.LikeDislike;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo.LikeDislikeReview;
import com.tokopedia.tkpd.tkpdreputation.reputationproduct.data.pojo.TotalLikeDislike;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 18/07/17.
 */

public class LikeDislikeDomainMapper implements Func1<Response<TkpdResponse>, LikeDislikeDomain> {
    @Override
    public LikeDislikeDomain call(Response<TkpdResponse> response) {
        LikeDislikeDomain likeDislikeDomain = new LikeDislikeDomain();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                LikeDislike likeDislike = response.body().convertDataObj(LikeDislike.class);
                likeDislikeDomain.setLikeDislikeReviewDomain(mappingLikeDislikeDomain(likeDislike.getLikeDislikeReview()));
            } else {
                likeDislikeDomain.setSuccess(false);
                likeDislikeDomain.setErrMessage(generateMessageError(response));
            }
        } else {
            likeDislikeDomain.setSuccess(false);
            likeDislikeDomain.setErrCode(response.code());
        }
        return likeDislikeDomain;
    }

    private String generateMessageError(retrofit2.Response<TkpdResponse> response) {
        return response.body().getErrorMessageJoined();
    }

    private List<LikeDislikeReviewDomain> mappingLikeDislikeDomain(List<LikeDislikeReview> likeDislikeReviewList) {
        if (likeDislikeReviewList == null) {
            return null;
        }
        List<LikeDislikeReviewDomain> likeDislikeReviewDomains = new ArrayList<>();
        for (LikeDislikeReview likeDislikeReview : likeDislikeReviewList) {
            LikeDislikeReviewDomain likeDislikeReviewDomain = new LikeDislikeReviewDomain();
            likeDislikeReviewDomain.setLikeStatus(likeDislikeReview.getLikeStatus());
            likeDislikeReviewDomain.setReviewId(likeDislikeReview.getReviewId());
            likeDislikeReviewDomain.setTotalLikeDislikeDomain(mappingTotalLikeDislikeDomain(likeDislikeReview.getTotalLikeDislike()));
            likeDislikeReviewDomains.add(likeDislikeReviewDomain);
        }

        return likeDislikeReviewDomains;

    }

    private TotalLikeDislikeDomain mappingTotalLikeDislikeDomain(TotalLikeDislike totalLikeDislike) {
        if (totalLikeDislike == null) {
            return null;
        }
        TotalLikeDislikeDomain totalLikeDislikeDomain = new TotalLikeDislikeDomain();
        totalLikeDislikeDomain.setTotalDislike(totalLikeDislike.getTotalDislike());
        totalLikeDislikeDomain.setTotalLike(totalLikeDislike.getTotalLike());
        return totalLikeDislikeDomain;
    }
}
