package com.tokopedia.discovery.newdiscovery.domain.usecase;

import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.tome.FavoriteCheckResult;
import com.tokopedia.core.network.apiservices.tome.TomeService;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.data.repository.ShopRepository;
import com.tokopedia.discovery.newdiscovery.search.fragment.shop.viewmodel.ShopViewModel;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by henrypriyono on 10/13/17.
 */

public class GetShopUseCase extends UseCase<ShopViewModel> {
    private final ShopRepository shopRepository;
    private final TomeService tomeService;

    public GetShopUseCase(ThreadExecutor threadExecutor,
                          PostExecutionThread postExecutionThread,
                          ShopRepository shopRepository,
                          TomeService tomeService) {
        super(threadExecutor, postExecutionThread);
        this.shopRepository = shopRepository;
        this.tomeService = tomeService;
    }

    /**
     *
     * @param searchParameter
     * have to setSource();
     * have to setUniqueID();
     * have to setQuery();
     * have to setSortKey();
     * have to setUserID() if exist;
     * else will be template default using static value
     * @return
     * will become requestParams spesifically for search using @See DiscoverySearchView.class
     */
    public static RequestParams createInitializeSearchParam(SearchParameter searchParameter) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.OB, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_SORT);
        requestParams.putString(BrowseApi.ROWS, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS);
        requestParams.putString(BrowseApi.START, Integer.toString(searchParameter.getStartRow()));
        requestParams.putString(BrowseApi.IMAGE_SIZE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(BrowseApi.IMAGE_SQUARE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
        requestParams.putString(BrowseApi.Q, searchParameter.getQueryKey());
        requestParams.putString(BrowseApi.UNIQUE_ID, searchParameter.getUniqueID());
        requestParams.putString(SearchApiConst.OFFICIAL, String.valueOf(searchParameter.getIsOfficial()));

        if (!TextUtils.isEmpty(searchParameter.getUserID())) {
            requestParams.putString(BrowseApi.USER_ID, searchParameter.getUserID());
        }
        return requestParams;
    }

    @Override
    public Observable<ShopViewModel> createObservable(RequestParams requestParams) {
        return shopRepository.getShop(requestParams.getParameters())
                .flatMap(favoriteDataEnricher(requestParams.getString(BrowseApi.USER_ID, "")));
    }

    private Func1<ShopViewModel, Observable<ShopViewModel>> favoriteDataEnricher(final String userId) {
        return new Func1<ShopViewModel, Observable<ShopViewModel>>() {
            @Override
            public Observable<ShopViewModel> call(final ShopViewModel shopViewModel) {
                if (TextUtils.isEmpty(userId) || shopViewModel.getShopItemList().isEmpty()) {
                    return Observable.just(shopViewModel);
                }

                return Observable.zip(Observable.just(shopViewModel),
                        getFavoriteData(shopViewModel, userId),
                        new Func2<ShopViewModel, Response<FavoriteCheckResult>, ShopViewModel>() {
                            @Override
                            public ShopViewModel call(ShopViewModel shopViewModel,
                                                          Response<FavoriteCheckResult> favoriteCheckResultResponse) {
                                enrichWithFavoriteData(shopViewModel, favoriteCheckResultResponse);
                                return shopViewModel;
                            }
                        }).onErrorReturn(new Func1<Throwable, ShopViewModel>() {
                    @Override
                    public ShopViewModel call(Throwable throwable) {
                        return shopViewModel;
                    }
                });
            }
        };
    }

    private Observable<Response<FavoriteCheckResult>> getFavoriteData(ShopViewModel shopViewModel, String userId) {

        List<String> shopIdList = generateShopIdList(shopViewModel.getShopItemList());

        return tomeService.getApi().checkIsShopFavorited(userId, TextUtils.join(",", shopIdList))
                .onErrorReturn(new Func1<Throwable, Response<FavoriteCheckResult>>() {
                    @Override
                    public Response<FavoriteCheckResult> call(Throwable throwable) {
                        return Response.success(new FavoriteCheckResult());
                    }
                });
    }

    private List<String> generateShopIdList(List<ShopViewModel.ShopItem> shopItemList) {
        List<String> shopIdList = new ArrayList<>();
        for (ShopViewModel.ShopItem shopItem : shopItemList) {
            shopIdList.add(shopItem.getShopId());
        }
        return shopIdList;
    }

    private void enrichWithFavoriteData(ShopViewModel shopViewModel,
                                        Response<FavoriteCheckResult> favoriteCheckResultResponse) {

        List<String> favoritedIdList = favoriteCheckResultResponse.body().getShopIds();

        for (ShopViewModel.ShopItem shopItem : shopViewModel.getShopItemList()) {
            shopItem.setFavorited(favoritedIdList.contains(shopItem.getShopId()));
        }
    }
}
