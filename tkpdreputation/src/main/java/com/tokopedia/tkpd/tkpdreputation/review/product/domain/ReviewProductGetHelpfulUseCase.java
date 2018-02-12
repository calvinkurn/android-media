package com.tokopedia.tkpd.tkpdreputation.review.product.domain;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeListDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.Review;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by zulfikarrahman on 1/17/18.
 */

public class ReviewProductGetHelpfulUseCase extends UseCase<DataResponseReviewHelpful> {
    public static final String PRODUCT_ID = "product_id";
    public static final String USER_ID = "user_id";
    private ReputationRepository reputationRepository;
    private GetLikeDislikeReviewUseCase getLikeDislikeReviewUseCase;
    private UserSession userSession;

    @Inject
    public ReviewProductGetHelpfulUseCase(ReputationRepository reputationRepository,
                                          GetLikeDislikeReviewUseCase getLikeDislikeReviewUseCase,
                                          UserSession userSession) {
        this.reputationRepository = reputationRepository;
        this.getLikeDislikeReviewUseCase = getLikeDislikeReviewUseCase;
        this.userSession = userSession;
    }

    @Override
    public Observable<DataResponseReviewHelpful> createObservable(final RequestParams requestParams) {
        return reputationRepository.getReviewHelpful(userSession.getShopId(), requestParams.getString(PRODUCT_ID, ""))
                .flatMap(new Func1<DataResponseReviewHelpful, Observable<DataResponseReviewHelpful>>() {
                    @Override
                    public Observable<DataResponseReviewHelpful> call(DataResponseReviewHelpful dataResponseReviewHelpful) {
                        if(dataResponseReviewHelpful.getList()!= null && dataResponseReviewHelpful.getList().size() > 0) {
                            return getLikeDislikeReviewUseCase.createObservable(
                                    GetLikeDislikeReviewUseCase.getParam(createReviewIds(dataResponseReviewHelpful), requestParams.getString(USER_ID, "")))
                                    .onErrorReturn(new Func1<Throwable, GetLikeDislikeReviewDomain>() {
                                        @Override
                                        public GetLikeDislikeReviewDomain call(Throwable throwable) {
                                            return null;
                                        }
                                    })
                                    .zipWith(Observable.just(dataResponseReviewHelpful), new Func2<GetLikeDislikeReviewDomain, DataResponseReviewHelpful, DataResponseReviewHelpful>() {
                                        @Override
                                        public DataResponseReviewHelpful call(GetLikeDislikeReviewDomain getLikeDislikeReviewDomain, DataResponseReviewHelpful dataResponseReviewHelpful) {
                                            if (getLikeDislikeReviewDomain != null) {
                                                return mapLikeModelToReviewModel(getLikeDislikeReviewDomain, dataResponseReviewHelpful);
                                            } else {
                                                return dataResponseReviewHelpful;
                                            }
                                        }
                                    });
                        }else{
                            return Observable.just(dataResponseReviewHelpful);
                        }
                    }
                });
    }

    private DataResponseReviewHelpful mapLikeModelToReviewModel(GetLikeDislikeReviewDomain getLikeDislikeReviewDomain,
                                                                DataResponseReviewHelpful dataResponseReviewHelpful) {
        for(Review review : dataResponseReviewHelpful.getList()) {
            for (LikeDislikeListDomain likeDislikeListDomain : getLikeDislikeReviewDomain.getList()) {
                if(likeDislikeListDomain.getReviewId() == review.getReviewId()){
                    review.setTotalLike(likeDislikeListDomain.getTotalLike());
                    review.setLikeStatus(likeDislikeListDomain.getLikeStatus());
                    break;
                }
            }
        }
        return dataResponseReviewHelpful;
    }

    private String createReviewIds(DataResponseReviewHelpful dataResponseReviewHelpful) {
        List<String> listIds = new ArrayList<>();
        for(Review review :dataResponseReviewHelpful.getList()){
            listIds.add(String.valueOf(review.getReviewId()));
        }
        return StringUtils.convertListToStringDelimiter(listIds,"~");
    }

    public RequestParams createRequestParams(String productId, String userId) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PRODUCT_ID, productId);
        requestParams.putString(USER_ID, userId);
        return requestParams;
    }
}
