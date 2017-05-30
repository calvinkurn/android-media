package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.FavoriteShopUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFirstPageFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetFeedsSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.subscriber.GetFirstPageFeedsSubscriber;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromotedShopViewModel;

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
    private String currentCursor = "";
    private FeedPlus.View viewListener;

    @Inject
    FeedPlusPresenter(SessionHandler sessionHandler,
                      GetFeedsUseCase getFeedsUseCase,
                      GetFirstPageFeedsUseCase getFirstPageFeedsUseCase,
                      FavoriteShopUseCase favoriteShopUseCase) {
        this.sessionHandler = sessionHandler;
        this.getFeedsUseCase = getFeedsUseCase;
        this.getFirstPageFeedsUseCase = getFirstPageFeedsUseCase;
        this.doFavoriteShopUseCase = favoriteShopUseCase;
    }

    @Override
    public void attachView(FeedPlus.View view) {
        super.attachView(view);
        this.viewListener = view;
    }

    @Override
    public void detachView() {
        super.detachView();
    }


    @Override
    public void fetchFirstPage(boolean isLoadFromCache) {
        viewListener.showRefresh();
        getFirstPageFeedsUseCase.execute(getFeedPlusParam(isLoadFromCache),
                new GetFirstPageFeedsSubscriber(viewListener));
    }

    private RequestParams getFeedPlusParam(boolean isLoadFromCache) {
        RequestParams params = getFeedPlusParam();
        params.putBoolean(GetFirstPageFeedsUseCase.PARAM_IS_LOAD_FROM_CACHE, isLoadFromCache);
        currentCursor = "";
        params.putString(GetFeedsUseCase.PARAM_CURSOR, "");
        return params;
    }

    private RequestParams getFeedPlusParam() {
        RequestParams params = RequestParams.create();
        params.putInt(GetFeedsUseCase.PARAM_USER_ID, Integer.parseInt(sessionHandler.getLoginID()));
        params.putString(GetFeedsUseCase.PARAM_CURSOR, currentCursor);
        return params;
    }

    @Override
    public void fetchNextPage() {
        viewListener.showLoading();
        getFeedsUseCase.execute(getFeedPlusParam(), new GetFeedsSubscriber(viewListener));
    }

    public void favoriteShop(PromotedShopViewModel promotedShopViewModel, final int adapterPosition) {
        RequestParams params = RequestParams.create();
        AuthUtil.generateParamsNetwork2(viewListener.getActivity(), params.getParameters());
        params.putString(FavoriteShopUseCase.PARAM_SHOP_ID, promotedShopViewModel.getShopId());
        params.putString(FavoriteShopUseCase.PARAM_SHOP_DOMAIN, promotedShopViewModel.getShopDomain());
        params.putString(FavoriteShopUseCase.PARAM_SRC, promotedShopViewModel.getSrc());
        params.putString(FavoriteShopUseCase.PARAM_AD_KEY, promotedShopViewModel.getAdKey());
        doFavoriteShopUseCase.execute(params, new Subscriber<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                viewListener.showSnackbar(s);
                viewListener.updateFavorite(adapterPosition);
            }
        });
    }

    public void setCursor(String currentCursor) {
        this.currentCursor = currentCursor;
    }
}
