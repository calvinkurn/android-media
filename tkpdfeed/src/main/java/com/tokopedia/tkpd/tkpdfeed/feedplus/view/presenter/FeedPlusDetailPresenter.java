package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.AddWishlistUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.RemoveWishlistUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlusDetail;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsDetailUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.WishlistListener;
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
    private WishlistListener wishlistListener;

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

    public void attachView(FeedPlusDetail.View view, WishlistListener wishlistListener) {
        this.viewListener = view;
        this.wishlistListener = wishlistListener;
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
                getFeedsDetailUseCase.getFeedDetailParam(
                        sessionHandler.getLoginID(),
                        detailId,
                        page),
                new FeedDetailSubscriber(viewListener));
    }

    public void addToWishlist(int adapterPosition, String productId) {
        viewListener.showLoadingProgress();
        addWishlistUseCase.execute(
                AddWishlistUseCase.generateParam(productId, sessionHandler),
                new AddWishlistSubscriber(wishlistListener, adapterPosition));
    }


    @Override
    public void removeFromWishlist(int adapterPosition, String productId) {
        viewListener.showLoadingProgress();
        removeWishlistUseCase.execute(
                RemoveWishlistUseCase.generateParam(productId, sessionHandler),
                new RemoveWishlistSubscriber(wishlistListener, adapterPosition));
    }
}
