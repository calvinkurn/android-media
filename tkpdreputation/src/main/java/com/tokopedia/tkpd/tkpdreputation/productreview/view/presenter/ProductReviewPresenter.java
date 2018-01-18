package com.tokopedia.tkpd.tkpdreputation.productreview.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewHelpful;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewProduct;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewstarcount.DataResponseReviewStarCount;
import com.tokopedia.tkpd.tkpdreputation.productreview.domain.ProductReviewGetHelpfulUseCase;
import com.tokopedia.tkpd.tkpdreputation.productreview.domain.ProductReviewGetListUseCase;
import com.tokopedia.tkpd.tkpdreputation.productreview.domain.ProductReviewGetRatingUseCase;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.ProductReviewListMapper;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 1/16/18.
 */

public class ProductReviewPresenter extends BaseDaggerPresenter<ProductReviewContract.View> implements ProductReviewContract.Presenter {

    private final SessionHandler sessionHandler;
    private final ProductReviewGetListUseCase productReviewGetListUseCase;
    private final ProductReviewGetHelpfulUseCase productReviewGetHelpfulUseCase;
    private final ProductReviewGetRatingUseCase productReviewGetRatingUseCase;
    private final LikeDislikeReviewUseCase likeDislikeReviewUseCase;
    private final DeleteReviewResponseUseCase deleteReviewResponseUseCase;
    private final ProductReviewListMapper productReviewListMapper;

    public ProductReviewPresenter(ProductReviewGetListUseCase productReviewGetListUseCase,
                                  ProductReviewGetHelpfulUseCase productReviewGetHelpfulUseCase,
                                  ProductReviewGetRatingUseCase productReviewGetRatingUseCase,
                                  LikeDislikeReviewUseCase likeDislikeReviewUseCase,
                                  DeleteReviewResponseUseCase deleteReviewResponseUseCase,
                                  ProductReviewListMapper productReviewListMapper,
                                  SessionHandler sessionHandler) {
        this.productReviewGetListUseCase = productReviewGetListUseCase;
        this.productReviewGetHelpfulUseCase = productReviewGetHelpfulUseCase;
        this.productReviewGetRatingUseCase = productReviewGetRatingUseCase;
        this.likeDislikeReviewUseCase = likeDislikeReviewUseCase;
        this.deleteReviewResponseUseCase = deleteReviewResponseUseCase;
        this.productReviewListMapper = productReviewListMapper;
        this.sessionHandler = sessionHandler;
    }

    public void deleteReview(String reviewId, String reputationId, String productId){
        deleteReviewResponseUseCase.execute(DeleteReviewResponseUseCase.getParam(reviewId, productId, sessionHandler.getShopID(), reputationId),
                getSubscriberDeleteReview());
    }

    private Subscriber<DeleteReviewResponseDomain> getSubscriberDeleteReview() {
        return new Subscriber<DeleteReviewResponseDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onErrorDeleteReview(e);
                }
            }

            @Override
            public void onNext(DeleteReviewResponseDomain deleteReviewResponseDomain) {
                getView().onSuccessDeleteReview(deleteReviewResponseDomain);
            }
        };
    }

    public void postLikeDislikeReview(String reviewId, int likeStatus, String productId){
        likeDislikeReviewUseCase.execute(LikeDislikeReviewUseCase.getParam(reviewId, likeStatus, productId, sessionHandler.getShopID()),
                getSubscriberPostLikeDislike());
    }

    private Subscriber<LikeDislikeDomain> getSubscriberPostLikeDislike() {
        return new Subscriber<LikeDislikeDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onErrorPostLikeDislike(e);
                }
            }

            @Override
            public void onNext(LikeDislikeDomain likeDislikeDomain) {
                getView().onSuccessPostLikeDislike(likeDislikeDomain);
            }
        };
    }

    public void getRatingReview(String productId) {
        productReviewGetRatingUseCase.execute(productReviewGetRatingUseCase.createRequestParams(productId),
                getSubscriberGetRating());
    }

    private Subscriber<DataResponseReviewStarCount> getSubscriberGetRating() {
        return new Subscriber<DataResponseReviewStarCount>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onErrorGetRatingView(e);
                }
            }

            @Override
            public void onNext(DataResponseReviewStarCount dataResponseReviewStarCount) {
                getView().onGetRatingReview(dataResponseReviewStarCount);
            }
        };
    }

    public void getHelpfulReview(String productId) {
        productReviewGetHelpfulUseCase.execute(productReviewGetHelpfulUseCase.createRequestParams(productId, sessionHandler.getLoginID()),
                getSubscriberGetHelpfulReview());
    }

    private Subscriber<DataResponseReviewHelpful> getSubscriberGetHelpfulReview() {
        return new Subscriber<DataResponseReviewHelpful>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().onErrorGetListReviewHelpful(e);
                }
            }

            @Override
            public void onNext(DataResponseReviewHelpful dataResponseReviewHelpful) {
                getView().onGetListReviewHelpful(productReviewListMapper.map(dataResponseReviewHelpful, sessionHandler.getLoginID()));
            }
        };
    }

    public void getProductReview(String productId, int page, int rating) {
        productReviewGetListUseCase.execute(productReviewGetListUseCase.createRequestParams(productId,
                String.valueOf(page), String.valueOf(rating), sessionHandler.getLoginID()), getSubscriberGetProductReview());
    }

    private Subscriber<DataResponseReviewProduct> getSubscriberGetProductReview() {
        return new Subscriber<DataResponseReviewProduct>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()) {
                    getView().onErrorGetListReviewProduct(e);
                }
            }

            @Override
            public void onNext(DataResponseReviewProduct dataResponseReviewProduct) {
                getView().onGetListReviewProduct(productReviewListMapper.map(dataResponseReviewProduct, sessionHandler.getLoginID()),
                        !TextUtils.isEmpty(dataResponseReviewProduct.getPaging().getUriNext()));
            }
        };
    }
}
