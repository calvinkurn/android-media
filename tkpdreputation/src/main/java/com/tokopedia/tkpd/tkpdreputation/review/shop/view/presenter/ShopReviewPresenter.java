package com.tokopedia.tkpd.tkpdreputation.shopreview.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.model.LikeDislikeDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.DeleteReviewResponseDomain;
import com.tokopedia.tkpd.tkpdreputation.productreview.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.tkpd.tkpdreputation.productreview.view.ProductReviewListMapper;
import com.tokopedia.tkpd.tkpdreputation.shopreview.domain.ShopReviewUseCase;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 1/19/18.
 */

public class ShopReviewPresenter extends BaseDaggerPresenter<ShopReviewContract.View> implements ShopReviewContract.Presenter {

    private final ShopReviewUseCase shopReviewUseCase;
    private final LikeDislikeReviewUseCase likeDislikeReviewUseCase;
    private final DeleteReviewResponseUseCase deleteReviewResponseUseCase;
    private final ProductReviewListMapper productReviewListMapper;
    private final SessionHandler sessionHandler;

    @Inject
    public ShopReviewPresenter(ShopReviewUseCase shopReviewUseCase,
                               LikeDislikeReviewUseCase likeDislikeReviewUseCase,
                               DeleteReviewResponseUseCase deleteReviewResponseUseCase,
                               ProductReviewListMapper productReviewListMapper,
                               SessionHandler sessionHandler) {
        this.shopReviewUseCase = shopReviewUseCase;
        this.likeDislikeReviewUseCase = likeDislikeReviewUseCase;
        this.deleteReviewResponseUseCase = deleteReviewResponseUseCase;
        this.productReviewListMapper = productReviewListMapper;
        this.sessionHandler = sessionHandler;
    }

    public void deleteReview(String reviewId, String reputationId, String productId){
        getView().showProgressLoading();
        deleteReviewResponseUseCase.execute(DeleteReviewResponseUseCase.getParam(reviewId, productId, sessionHandler.getShopID(), reputationId),
                getSubscriberDeleteReview(reviewId));
    }

    private Subscriber<DeleteReviewResponseDomain> getSubscriberDeleteReview(final String reviewId) {
        return new Subscriber<DeleteReviewResponseDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().hideProgressLoading();
                    getView().onErrorDeleteReview(e);
                }
            }

            @Override
            public void onNext(DeleteReviewResponseDomain deleteReviewResponseDomain) {
                getView().hideProgressLoading();
                if(deleteReviewResponseDomain.isSuccess()) {
                    getView().onSuccessDeleteReview(deleteReviewResponseDomain, reviewId);
                }else{
                    getView().onErrorDeleteReview(new RuntimeException());
                }
            }
        };
    }

    public void postLikeDislikeReview(String reviewId, int likeStatus, String productId){
        getView().showProgressLoading();
        likeDislikeReviewUseCase.execute(LikeDislikeReviewUseCase.getParam(reviewId, likeStatus, productId, sessionHandler.getShopID()),
                getSubscriberPostLikeDislike(reviewId));
    }

    private Subscriber<LikeDislikeDomain> getSubscriberPostLikeDislike(final String reviewId) {
        return new Subscriber<LikeDislikeDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().hideProgressLoading();
                    getView().onErrorPostLikeDislike(e);
                }
            }

            @Override
            public void onNext(LikeDislikeDomain likeDislikeDomain) {
                getView().hideProgressLoading();
                getView().onSuccessPostLikeDislike(likeDislikeDomain, reviewId);
            }
        };
    }

    public void getShopReview(String shopDomain, String shopId, int page) {
        shopReviewUseCase.execute(shopReviewUseCase.createRequestParams(shopDomain, shopId, String.valueOf(page), sessionHandler.getLoginID()),
                getSubscriberGetShopReview());
    }

    private Subscriber<DataResponseReviewShop> getSubscriberGetShopReview() {
        return new Subscriber<DataResponseReviewShop>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if(isViewAttached()){
                    getView().showGetListError(e);
                }
            }

            @Override
            public void onNext(DataResponseReviewShop dataResponseReviewShop) {
                getView().renderList(productReviewListMapper.map(dataResponseReviewShop, sessionHandler.getLoginID()),
                        !TextUtils.isEmpty(dataResponseReviewShop.getPaging().getUriNext()));
            }
        };
    }
}
