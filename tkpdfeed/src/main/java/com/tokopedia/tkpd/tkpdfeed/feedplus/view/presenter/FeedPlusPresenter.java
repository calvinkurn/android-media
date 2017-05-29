package com.tokopedia.tkpd.tkpdfeed.feedplus.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.FeedPlus;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.cloud.CloudFeedDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.data.source.cloud.FavoriteShopDataSource;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.model.FeedResult;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.FavoriteShopUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.domain.usecase.GetFirstPageFeedsUseCase;
import com.tokopedia.tkpd.tkpdfeed.feedplus.view.viewmodel.PromotedShopViewModel;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * @author by nisie on 5/15/17.
 */

public class FeedPlusPresenter
        extends BaseDaggerPresenter<FeedPlus.View>
        implements FeedPlus.Presenter {

    private GetFeedsUseCase getFeedsUseCase;
    private GetFirstPageFeedsUseCase getFirstPageFeedsUseCase;
    private FavoriteShopUseCase doFavoriteShopUseCase;
    private String currentCursor = "";
    private FeedPlus.View view;

    @Inject
    FeedPlusPresenter(GetFeedsUseCase getFeedsUseCase,
                      GetFirstPageFeedsUseCase getFirstPageFeedsUseCase,
                      FavoriteShopUseCase favoriteShopUseCase) {

        this.getFeedsUseCase = getFeedsUseCase;
        this.getFirstPageFeedsUseCase = getFirstPageFeedsUseCase;
        this.doFavoriteShopUseCase = favoriteShopUseCase;

    }

    @Override
    public void attachView(FeedPlus.View view) {
        super.attachView(view);
        this.view = view;
        fetchFirstPage();

    }

    @Override
    public void detachView() {
        super.detachView();
    }


    @Override
    public void fetchFirstPage() {
        getFirstPageFeedsUseCase.execute(RequestParams.EMPTY, getFirstPageFeedsSubscriber());
    }

    @Override
    public void fetchNextPage() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(CloudFeedDataSource.KEY_CURSOR, currentCursor);
        getFeedsUseCase.execute(requestParams, getFeedsSubscriber());
    }

    private Subscriber<FeedResult> getFirstPageFeedsSubscriber() {
        return new Subscriber<FeedResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FeedResult feedResult) {
                if (feedResult.getDataSource() == FeedResult.SOURCE_LOCAL) {
                    //load from cache, display the feed from cache
                } else {
                    //load from network, update the view or display the feed if empty
                }

                currentCursor = getCurrentCursor(feedResult);
            }
        };
    }

    private String getCurrentCursor(FeedResult feedResult) {
        int lastIndex = feedResult.getDataFeedDomainList().size() - 1;
        return feedResult.getDataFeedDomainList().get(lastIndex).getCursor();
    }

    private  Subscriber<FeedResult> getFeedsSubscriber() {
        return new Subscriber<FeedResult>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FeedResult feedResult) {

                currentCursor = getCurrentCursor(feedResult);
            }
        };
    }

    public void favoriteShop(PromotedShopViewModel promotedShopViewModel, final int adapterPosition) {
        RequestParams params = RequestParams.create();
        AuthUtil.generateParamsNetwork2(view.getActivity(), params.getParameters());
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
                view.showSnackbar(s);
                view.updateFavorite(adapterPosition);
            }
        });
    }
}
