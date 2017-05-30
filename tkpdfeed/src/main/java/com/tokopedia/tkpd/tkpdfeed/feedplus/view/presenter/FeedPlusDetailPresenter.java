package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.AddWishlistUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.RemoveWishlistUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.AddWishlistSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.FeedDetailSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.RemoveWishlistSubscriber;

import javax.inject.Inject;

/**
 * @author by nisie on 5/18/17.
 */

public class FeedPlusDetailPresenter extends BaseDaggerPresenter<FeedPlusDetail.View>
        implements FeedPlusDetail.Presenter {

    private final GetFeedsDetailUseCase getFeedsDetailUseCase;
    private final AddWishlistUseCase addWishlistUseCase;
    private final RemoveWishlistUseCase removeWishlistUseCase;
    private final SessionHandler sessionHandler;
    private FeedPlusDetail.View viewListener;

    @Inject
    FeedPlusDetailPresenter(GetFeedsDetailUseCase getFeedsDetailUseCase,
                            AddWishlistUseCase addWishlistUseCase,
                            RemoveWishlistUseCase removeWishlistUseCase,
                            SessionHandler sessionHandler) {
        this.getFeedsDetailUseCase = getFeedsDetailUseCase;
        this.addWishlistUseCase = addWishlistUseCase;
        this.removeWishlistUseCase = removeWishlistUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void attachView(FeedPlusDetail.View view) {
        this.viewListener = view;
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
        getFeedsDetailUseCase.unsubscribe();
        addWishlistUseCase.unsubscribe();
        removeWishlistUseCase.unsubscribe();
    }

    public void getFeedDetail(String detailId, int page) {
        viewListener.showLoading();
        getFeedsDetailUseCase.execute(
                getFeedsDetailUseCase.getFeedDetailParam(detailId, page),
                new FeedDetailSubscriber(viewListener));
    }

    public void addToWishlist(String productId) {
        viewListener.showLoadingProgress();
        addWishlistUseCase.execute(
                AddWishlistUseCase.generateParam(productId, sessionHandler),
                new AddWishlistSubscriber(viewListener));
    }


    @Override
    public void removeFromWishlist(String productId) {
        viewListener.showLoadingProgress();
        removeWishlistUseCase.execute(
                RemoveWishlistUseCase.generateParam(productId, sessionHandler),
                new RemoveWishlistSubscriber(viewListener));
    }
}
