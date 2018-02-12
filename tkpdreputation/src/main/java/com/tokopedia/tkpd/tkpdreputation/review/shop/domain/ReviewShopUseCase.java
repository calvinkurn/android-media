package com.tokopedia.tkpd.tkpdreputation.review.shop.domain;

import com.tokopedia.design.utils.StringUtils;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.GetLikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.model.GetLikeDislikeReviewDomain;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeListDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.repository.ReputationRepository;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.Review;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ReviewShopUseCase extends UseCase<DataResponseReviewShop> {

    public static final String SHOP_DOMAIN = "shop_domain";
    public static final String SHOP_ID = "shop_id";
    public static final String PAGE = "page";
    public static final String PER_PAGE = "per_page";
    public static final String DEFAULT_PER_PAGE = "10";
    private static final String USER_ID = "user_id";
    private ReputationRepository reputationRepository;
    private GetLikeDislikeReviewUseCase getLikeDislikeReviewUseCase;

    @Inject
    public ReviewShopUseCase(ReputationRepository reputationRepository, GetLikeDislikeReviewUseCase getLikeDislikeReviewUseCase) {
        this.reputationRepository = reputationRepository;
        this.getLikeDislikeReviewUseCase = getLikeDislikeReviewUseCase;
    }

    @Override
    public Observable<DataResponseReviewShop> createObservable(final RequestParams requestParams) {
        return reputationRepository.getReviewShopList(requestParams.getParamsAllValueInString())
                .flatMap(new Func1<DataResponseReviewShop, Observable<DataResponseReviewShop>>() {
                    @Override
                    public Observable<DataResponseReviewShop> call(final DataResponseReviewShop dataResponseReviewShop) {
                        if(dataResponseReviewShop.getList()!= null && dataResponseReviewShop.getList().size() > 0) {
                            return getLikeDislikeReviewUseCase.createObservable(
                                    GetLikeDislikeReviewUseCase.getParam(createReviewIds(dataResponseReviewShop), requestParams.getString(USER_ID, "")))
                                    .map(new Func1<GetLikeDislikeReviewDomain, DataResponseReviewShop>() {
                                        @Override
                                        public DataResponseReviewShop call(GetLikeDislikeReviewDomain getLikeDislikeReviewDomain) {
                                            return mapLikeModelToReviewModel(getLikeDislikeReviewDomain, dataResponseReviewShop);
                                        }
                                    });
                        }else{
                            return Observable.just(dataResponseReviewShop);
                        }
                    }
                });
    }

    private DataResponseReviewShop mapLikeModelToReviewModel(GetLikeDislikeReviewDomain getLikeDislikeReviewDomain,
                                                                DataResponseReviewShop dataResponseReviewShop) {
        for(Review review : dataResponseReviewShop.getList()) {
            for (LikeDislikeListDomain likeDislikeListDomain : getLikeDislikeReviewDomain.getList()) {
                if(likeDislikeListDomain.getReviewId() == review.getReviewId()){
                    review.setTotalLike(likeDislikeListDomain.getTotalLike());
                    review.setLikeStatus(likeDislikeListDomain.getLikeStatus());
                    break;
                }
            }
        }
        return dataResponseReviewShop;
    }

    private String createReviewIds(DataResponseReviewShop dataResponseReviewShop) {
        List<String> listIds = new ArrayList<>();
        for(Review review :dataResponseReviewShop.getList()){
            listIds.add(String.valueOf(review.getReviewId()));
        }
        return StringUtils.convertListToStringDelimiter(listIds,"~");
    }

    public RequestParams createRequestParams(String shopDomain, String shopId, String page, String userId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_DOMAIN, shopDomain);
        requestParams.putString(SHOP_ID, shopId);
        requestParams.putString(PAGE, page);
        requestParams.putString(PER_PAGE, DEFAULT_PER_PAGE);
        requestParams.putString(USER_ID, userId);
        return requestParams;
    }
}
