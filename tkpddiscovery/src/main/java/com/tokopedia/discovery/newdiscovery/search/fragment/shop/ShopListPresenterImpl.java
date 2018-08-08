package com.tokopedia.discovery.newdiscovery.search.fragment.shop;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.discovery.newdiscovery.di.component.DaggerSearchComponent;
import com.tokopedia.discovery.newdiscovery.di.component.SearchComponent;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetDynamicFilterUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetShopUseCase;
import com.tokopedia.discovery.newdiscovery.domain.usecase.ToggleFavoriteActionUseCase;
import com.tokopedia.discovery.newdiscovery.search.fragment.GetDynamicFilterSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.SearchSectionFragmentPresenterImpl;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.listener.FavoriteActionListener;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.subscriber.ToggleFavoriteActionSubscriber;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class ShopListPresenterImpl extends SearchSectionFragmentPresenterImpl<ShopListFragmentView> implements ShopListPresenter {

    @Inject
    GetShopUseCase getShopUseCase;
    @Inject
    GetDynamicFilterUseCase getDynamicFilterUseCase;
    @Inject
    ToggleFavoriteActionUseCase toggleFavoriteActionUseCase;
    private ShopListFragmentView viewListener;
    private FavoriteActionListener favoriteActionListener;
    private Context context;

    public ShopListPresenterImpl(Context context) {
        this.context = context;
        SearchComponent component = DaggerSearchComponent.builder()
                .appComponent(getComponent(context))
                .build();
        component.inject(this);
    }

    @Override
    public void attachView(ShopListFragmentView viewListener,
                           FavoriteActionListener favoriteActionListener) {
        attachView(viewListener);
        this.viewListener = viewListener;
        this.favoriteActionListener = favoriteActionListener;
    }

    @Override
    public void handleFavoriteButtonClicked(ShopViewModel.ShopItem shopItem,
                                            int adapterPosition) {
        if (viewListener.isUserHasLogin()) {
            viewListener.disableFavoriteButton(adapterPosition);
            Log.d(this.toString(),
                    "Toggle favorite " + shopItem.getShopId() + " " + Boolean.toString(!shopItem.isFavorited()));
            toggleFavoriteActionUseCase.execute(
                    ToggleFavoriteActionUseCase
                            .generateParam(shopItem.getShopId(), shopItem.getShopDomain()),
                    new ToggleFavoriteActionSubscriber(favoriteActionListener,
                            adapterPosition, !shopItem.isFavorited())
            );
        } else {
            launchLoginActivity(shopItem.getShopId());
        }
    }

    private void launchLoginActivity(String shopId) {
        Bundle extras = new Bundle();
        extras.putString("shop_id", shopId);
        viewListener.launchLoginActivity(extras);
    }

    @Override
    protected void getFilterFromNetwork(RequestParams requestParams) {
        getDynamicFilterUseCase.execute(requestParams, new GetDynamicFilterSubscriber(getView()));
    }

    @Override
    protected RequestParams getDynamicFilterParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putAll(AuthUtil.generateParamsNetwork2(context, requestParams.getParameters()));
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_SHOP);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.Q, getView().getQueryKey());
        requestParams.putString(BrowseApi.SC, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_SC);
        return requestParams;
    }

    @Override
    public void loadShop(SearchParameter searchParameter,
                         ShopListPresenterImpl.LoadMoreListener loadMoreListener) {

        RequestParams requestParams
                = enrichWithFilterAndSortParams(GetShopUseCase.createInitializeSearchParam(searchParameter));
        removeDefaultCategoryParam(requestParams);

        getShopUseCase.execute(requestParams, createSubscriber(loadMoreListener));
    }

    private Subscriber<ShopViewModel> createSubscriber(final LoadMoreListener loadMoreListener) {
        return new Subscriber<ShopViewModel>() {

            @Override
            public void onCompleted() {
                viewListener.getDynamicFilter();
            }

            @Override
            public void onError(Throwable e) {
                loadMoreListener.onFailed();
            }

            @Override
            public void onNext(ShopViewModel shopViewModel) {
                loadMoreListener.onSuccess(shopViewModel.getShopItemList(), shopViewModel.isHasNextPage());
            }
        };
    }

    public interface LoadMoreListener {
        void onSuccess(List<ShopViewModel.ShopItem> shopItemList, boolean isHasNextPage);
        void onFailed();
    }

    @Override
    public void detachView() {
        super.detachView();
        getShopUseCase.unsubscribe();
        getDynamicFilterUseCase.unsubscribe();
        toggleFavoriteActionUseCase.unsubscribe();
    }

}
