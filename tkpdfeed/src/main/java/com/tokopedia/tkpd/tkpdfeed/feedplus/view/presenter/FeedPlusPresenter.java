package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.R;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.CheckNewFeedUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FavoriteShopUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.FollowKolPostUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFirstPageFeedsCloudUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.GetFirstPageFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.domain.usecase.LikeKolPostUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.listener.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.FollowKolSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetFeedsSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetFirstPageFeedsSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.LikeKolPostSubscriber;
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
    private final CheckNewFeedUseCase checkNewFeedUseCase;
    private final LikeKolPostUseCase likeKolPostUseCase;
    private final FollowKolPostUseCase followKolPostUseCase;
    private GetFeedsUseCase getFeedsUseCase;
    private GetFirstPageFeedsUseCase getFirstPageFeedsUseCase;
    private FavoriteShopUseCase doFavoriteShopUseCase;
    private GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase;
    private String currentCursor = "";
    private FeedPlus.View viewListener;
    private PagingHandler pagingHandler;

    @Inject
    FeedPlusPresenter(SessionHandler sessionHandler,
                      GetFeedsUseCase getFeedsUseCase,
                      GetFirstPageFeedsUseCase getFirstPageFeedsUseCase,
                      FavoriteShopUseCase favoriteShopUseCase,
                      GetFirstPageFeedsCloudUseCase getFirstPageFeedsCloudUseCase,
                      CheckNewFeedUseCase checkNewFeedUseCase,
                      LikeKolPostUseCase likeKolPostUseCase,
                      FollowKolPostUseCase followKolPostUseCase) {
        this.sessionHandler = sessionHandler;
        this.pagingHandler = new PagingHandler();
        this.getFeedsUseCase = getFeedsUseCase;
        this.getFirstPageFeedsCloudUseCase = getFirstPageFeedsCloudUseCase;
        this.doFavoriteShopUseCase = favoriteShopUseCase;
        this.getFirstPageFeedsUseCase = getFirstPageFeedsUseCase;
        this.checkNewFeedUseCase = checkNewFeedUseCase;
        this.likeKolPostUseCase = likeKolPostUseCase;
        this.followKolPostUseCase = followKolPostUseCase;
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
        getFirstPageFeedsCloudUseCase.unsubscribe();
        likeKolPostUseCase.unsubscribe();
        followKolPostUseCase.unsubscribe();
    }


    @Override
    public void fetchFirstPage() {
        pagingHandler.resetPage();
        viewListener.showRefresh();
        currentCursor = "";
        getFirstPageFeedsUseCase.execute(
                getFirstPageFeedsUseCase.getRefreshParam(sessionHandler),
                new GetFirstPageFeedsSubscriber(viewListener, pagingHandler.getPage()));
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
                new GetFeedsSubscriber(viewListener, pagingHandler.getPage()));
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
                    viewListener.updateFavoriteFromEmpty(promotedShopViewModel.getShop().getId());
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
        getFirstPageFeedsCloudUseCase.execute(
                getFirstPageFeedsCloudUseCase.getRefreshParam(sessionHandler),
                new GetFirstPageFeedsSubscriber(viewListener, pagingHandler.getPage()));
    }

    @Override
    public void checkNewFeed(String firstCursor) {
//        checkNewFeedUseCase.execute(
//                CheckNewFeedUseCase.getParam(sessionHandler, firstCursor),
//                new CheckNewFeedSubscriber(viewListener));
    }

    @Override
    public void followKol(int id) {
        getView().showLoadingProgress();
        followKolPostUseCase.execute(FollowKolPostUseCase.getParam(0, 0), new FollowKolSubscriber
                (getView()));
    }

    @Override
    public void unfollowKol(int id) {
        getView().showLoadingProgress();
        followKolPostUseCase.execute(FollowKolPostUseCase.getParam(0, 0), new FollowKolSubscriber
                (getView()));
    }

    @Override
    public void likeKol(int id) {
        getView().showLoadingProgress();
        likeKolPostUseCase.execute(LikeKolPostUseCase.getParam(id), new LikeKolPostSubscriber(getView()));
    }

    @Override
    public void unlikeKol(int id) {
        getView().showLoadingProgress();
        likeKolPostUseCase.execute(LikeKolPostUseCase.getParam(id), new LikeKolPostSubscriber(getView()));
    }


    public String getUserId() {
        return sessionHandler.getLoginID();
    }
}
