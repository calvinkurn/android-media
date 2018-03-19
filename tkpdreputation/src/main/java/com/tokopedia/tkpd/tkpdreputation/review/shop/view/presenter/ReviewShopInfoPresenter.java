package com.tokopedia.tkpd.tkpdreputation.review.shop.view.presenter;

import android.text.TextUtils;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.DeleteReviewResponseUseCase;
import com.tokopedia.tkpd.tkpdreputation.domain.interactor.LikeDislikeReviewUseCase;
import com.tokopedia.tkpd.tkpdreputation.review.product.data.model.reviewlist.DataResponseReviewShop;
import com.tokopedia.tkpd.tkpdreputation.review.product.view.ReviewProductListMapper;
import com.tokopedia.tkpd.tkpdreputation.review.shop.domain.ReviewShopUseCase;

import rx.Subscriber;

/**
 * Created by normansyahputa on 2/14/18.
 */

public class ReviewShopInfoPresenter extends ReviewShopPresenter {
    public ReviewShopInfoPresenter(ReviewShopUseCase shopReviewUseCase, LikeDislikeReviewUseCase likeDislikeReviewUseCase, DeleteReviewResponseUseCase deleteReviewResponseUseCase, ReviewProductListMapper productReviewListMapper, UserSession userSession) {
        super(shopReviewUseCase, likeDislikeReviewUseCase, deleteReviewResponseUseCase, productReviewListMapper, userSession);
    }

    @Override
    protected Subscriber<DataResponseReviewShop> getSubscriberGetShopReview() {
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
                getView().renderList(productReviewListMapper.map(dataResponseReviewShop, userSession.getUserId()),
                        !TextUtils.isEmpty(dataResponseReviewShop.getPaging().getUriNext()));
            }
        };
    }
}
