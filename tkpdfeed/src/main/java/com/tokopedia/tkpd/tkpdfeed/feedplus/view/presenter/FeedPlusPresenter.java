package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.RefreshFeedUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FavoriteShopUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFirstPageFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.CheckNewFeedSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetFeedsSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetFirstPageFeedsSubscriber;
import com.tokopedia.topads.sdk.domain.model.Data;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusPresenter
        extends BaseDaggerPresenter<FeedPlus.View>
        implements FeedPlus.Presenter {

    private final SessionHandler sessionHandler;
    private GetFeedsUseCase getFeedsUseCase;
    private GetFirstPageFeedsUseCase getFirstPageFeedsUseCase;
    private FavoriteShopUseCase doFavoriteShopUseCase;
    private RefreshFeedUseCase refreshFeedUseCase;
    private String currentCursor = "";
    private FeedPlus.View viewListener;
    private PagingHandler pagingHandler;

    @Inject
    FeedPlusPresenter(SessionHandler sessionHandler,
                      GetFeedsUseCase getFeedsUseCase,
                      GetFirstPageFeedsUseCase getFirstPageFeedsUseCase,
                      FavoriteShopUseCase favoriteShopUseCase,
                      RefreshFeedUseCase refreshFeedUseCase) {
        this.sessionHandler = sessionHandler;
        this.getFeedsUseCase = getFeedsUseCase;
        this.getFirstPageFeedsUseCase = getFirstPageFeedsUseCase;
        this.doFavoriteShopUseCase = favoriteShopUseCase;
        this.refreshFeedUseCase = refreshFeedUseCase;
        this.pagingHandler = new PagingHandler();
    }

    @Override
    public void attachView(FeedPlus.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
        getFeedsUseCase.unsubscribe();
        getFirstPageFeedsUseCase.unsubscribe();
        doFavoriteShopUseCase.unsubscribe();
        refreshFeedUseCase.unsubscribe();
    }


    @Override
    public void fetchFirstPage() {
        pagingHandler.resetPage();
        viewListener.showRefresh();
        currentCursor = "";
        getFirstPageFeedsUseCase.execute(
                getFirstPageFeedsUseCase.getFeedPlusParam(
                        pagingHandler.getPage(),
                        sessionHandler,
                        currentCursor),
                new GetFirstPageFeedsSubscriber(viewListener));
    }

    @Override
    public void fetchNextPage() {
        pagingHandler.nextPage();

        if (currentCursor == null)
            return;
        getFeedsUseCase.execute(
                getFeedsUseCase.getFeedPlusParam(
                        pagingHandler.getPage(),
                        sessionHandler,
                        currentCursor),
                new GetFeedsSubscriber(viewListener));
    }

    public void favoriteShop(final Data promotedShopViewModel, final int adapterPosition) {
        RequestParams params = RequestParams.create();
        AuthUtil.generateParamsNetwork2(viewListener.getActivity(), params.getParameters());
        params.putString(FavoriteShopUseCase.PARAM_SHOP_ID, promotedShopViewModel.getShop().getId());
        params.putString(FavoriteShopUseCase.PARAM_SHOP_DOMAIN, promotedShopViewModel.getShop().getDomain());
        params.putString(FavoriteShopUseCase.PARAM_SRC, FavoriteShopUseCase.DEFAULT_VALUE_SRC);
        params.putString(FavoriteShopUseCase.PARAM_AD_KEY, promotedShopViewModel.getAdRefKey());
        doFavoriteShopUseCase.execute(params, new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Boolean isSuccess) {
                StringBuilder stringBuilder = new StringBuilder();

                if (isSuccess) {
                    stringBuilder.append(
                            MethodChecker.fromHtml(promotedShopViewModel.getShop()
                                    .getName()));
                    if (promotedShopViewModel.isFavorit()) {
                        stringBuilder.append(" dihapus dari toko favorit");
                    } else {
                        stringBuilder.append(" berhasil difavoritkan");
                    }
                } else {
                    stringBuilder.append(viewListener.getString(R.string.msg_network_error));
                }
                viewListener.showSnackbar(stringBuilder.toString());

                if (viewListener.hasFeed())
                    viewListener.updateFavorite(adapterPosition);
                else
                    viewListener.updateFavoriteFromEmpty();
            }
        });
    }

    public void setCursor(String currentCursor) {
        this.currentCursor = currentCursor;
    }

    @Override
    public void refreshPage() {
        pagingHandler.resetPage();
        viewListener.showRefresh();
        currentCursor = "";
        refreshFeedUseCase.execute(
                refreshFeedUseCase.getFeedPlusParam(
                        pagingHandler.getPage(),
                        sessionHandler,
                        currentCursor),
                new GetFirstPageFeedsSubscriber(viewListener));
    }

    @Override
    public void checkNewFeed(String firstCursor) {
        refreshFeedUseCase.execute(
                refreshFeedUseCase.getFeedPlusParam(
                        1,
                        sessionHandler,
                        ""),
                new CheckNewFeedSubscriber(firstCursor, viewListener));
    }


    public String getUserId() {
        return sessionHandler.getLoginID();
    }
}
