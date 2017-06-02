package com.tokopedia.discovery.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.network.apiservices.ace.DiscoveryService;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.hades.HadesService;
import com.tokopedia.core.network.apiservices.hades.apis.HadesApi;
import com.tokopedia.core.network.apiservices.mojito.MojitoSimpleService;
import com.tokopedia.core.network.apiservices.search.HotListService;
import com.tokopedia.core.network.apiservices.search.SearchSuggestionService;
import com.tokopedia.core.network.apiservices.topads.TopAdsService;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.core.network.entity.intermediary.CategoryHadesModel;
import com.tokopedia.core.network.entity.intermediary.Data;
import com.tokopedia.core.network.entity.discovery.BrowseCatalogModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.entity.discovery.BrowseShopModel;
import com.tokopedia.core.network.entity.wishlist.WishlistCheckResult;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.var.ProductItem;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.discovery.dynamicfilter.DynamicFilterFactory;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.ErrorContainer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


/**
 * Created by noiz354 on 3/17/16.
 */
public class DiscoveryInteractorImpl implements DiscoveryInteractor {

    private static final String TAG = DiscoveryInteractorImpl.class.getSimpleName();
    DiscoveryService discoveryService;
    DiscoveryListener discoveryListener;
    HotListService hotListService;
    TopAdsService topAdsService;
    HadesService hadesService;
    SearchSuggestionService searchSuggestionService;
    CompositeSubscription compositeSubscription;
    Gson gson = new GsonBuilder().create();
    GlobalCacheManager cacheManager;
    private MojitoSimpleService mojitoSimpleService;

    public CompositeSubscription getCompositeSubscription() {
        return RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    public void setCompositeSubscription(CompositeSubscription compositeSubscription) {
        this.compositeSubscription = compositeSubscription;
    }

    public DiscoveryInteractorImpl() {
        discoveryService = new DiscoveryService();
        hotListService = new HotListService();
        topAdsService = new TopAdsService();
        hadesService = new HadesService();
        searchSuggestionService = new SearchSuggestionService();
        cacheManager = new GlobalCacheManager();
        mojitoSimpleService = new MojitoSimpleService();
    }

    public DiscoveryListener getDiscoveryListener() {
        return discoveryListener;
    }

    public void setDiscoveryListener(DiscoveryListener discoveryListener) {
        this.discoveryListener = discoveryListener;
    }

    @Override
    public void getHotListBanner(HashMap<String, String> data) {
        getCompositeSubscription().add(hotListService.getApi().getHotListBanner(MapNulRemover.removeNull(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TkpdResponse>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(DiscoveryService.TAG, DiscoveryService.TAG + " -> " + e.getMessage());
                                Pair<String, ErrorContainer> pair = new Pair<>(
                                        DiscoveryListener.ERRORCONTAINER,
                                        new ErrorContainer(e)
                                );
                                discoveryListener.onFailed(DiscoveryListener.HOTLIST_BANNER, pair);
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {

                                HotListBannerModel hotListBannerModel = new GsonBuilder().create().fromJson(tkpdResponseResponse.body().getStringData(), HotListBannerModel.class);
                                Pair<String, HotListBannerModel.HotListBannerContainer> pair =
                                        new Pair<>(
                                                DiscoveryListener.HOTLISTBANNER,
                                                new HotListBannerModel.HotListBannerContainer(
                                                        hotListBannerModel
                                                )
                                        );
                                discoveryListener.onSuccess(DiscoveryListener.HOTLIST_BANNER, pair);
                            }
                        }
                ));
    }

    @Override
    public void storeCacheCategoryHeader(int level, Data categoriesHadesModel) {
        new GlobalCacheManager()
                .setKey(TkpdCache.Key.CATEOGRY_HEADER_LEVEL + level)
                .setValue(gson.toJson(categoriesHadesModel))
                .store();
    }

    @Override
    public Data getCategoryHeaderCache(int level) {
        return cacheManager.getConvertObjData(TkpdCache.Key.CATEOGRY_HEADER_LEVEL + level, Data.class);
    }

    @Override
    public Observable<Map<String, Boolean>> checkProductsInWishlist(String userId,
                                                                    List<ProductItem> productItemList) {

        StringBuilder productIds = new StringBuilder();

        for (ProductItem item : productItemList) {
            productIds.append(item.getId());
            productIds.append(",");
        }

        productIds.deleteCharAt(productIds.length() - 1);

        return mojitoSimpleService.getApi().checkWishlist(userId, productIds.toString())
                .map(new Func1<Response<WishlistCheckResult>, Map<String, Boolean>>() {
                    @Override
                    public Map<String, Boolean> call(Response<WishlistCheckResult> wishlistCheckResultResponse) {
                        Map<String, Boolean> resultMap = new HashMap<>();
                        List<String> idList = wishlistCheckResultResponse.body().getCheckResultIds().getIds();
                        for (String id : idList) {
                            resultMap.put(id, true);
                        }
                        return resultMap;
                    }
                });
    }

    @Override
    public void getProductWithCategory(HashMap<String, String> data, String categoryId, final int level) {
        Log.d(TAG, "getProduct2 data " + data.toString());
        if (discoveryListener == null)
            throw new RuntimeException("please supply Discovery Listener !!!");

        getCompositeSubscription().add(getProductObservable(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .doOnNext(new Action1<BrowseProductModel>() {
                    @Override
                    public void call(BrowseProductModel productModel) {
                        storeCacheCategoryHeader(level, productModel.getCategoryData());
                    }
                })
                .subscribe(new Subscriber<BrowseProductModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Pair<String, ErrorContainer> pair = new Pair<String, ErrorContainer>(
                                DiscoveryListener.ERRORCONTAINER,
                                new ErrorContainer(e)
                        );
                        discoveryListener.onFailed(DiscoveryListener.BROWSE_PRODUCT, pair);
                    }

                    @Override
                    public void onNext(BrowseProductModel productModel) {
                        Pair<String, BrowseProductModel.BrowseProductContainer>
                                pair = new Pair<String, BrowseProductModel.BrowseProductContainer>(DiscoveryListener.BROWSEPRODUCT, new BrowseProductModel.BrowseProductContainer(productModel)
                        );
                        discoveryListener.onSuccess(DiscoveryListener.BROWSE_PRODUCT, pair);
                    }
                })
        );
    }

    public Observable<BrowseProductModel> getProductObservable(HashMap<String, String> data) {
        Map<String, String> param = MapNulRemover.removeNull(data);
        return Observable.zip(hadesService.getApi().getCategories(data.get(BrowseApi.SC)),
                discoveryService.getApi().browseProducts(param), new Func2<Response<CategoryHadesModel>, Response<BrowseProductModel>, BrowseProductModel>() {
                    @Override
                    public BrowseProductModel call(Response<CategoryHadesModel> categoryHadesModelResponse,
                                                   Response<BrowseProductModel> browseProductModelResponse) {
                        BrowseProductModel productModel = browseProductModelResponse.body();
                        productModel.setCategoryData(categoryHadesModelResponse.body().getData());
                        return productModel;
                    }
                });
    }

    @Override
    public void getProducts(HashMap<String, String> data) {
        Log.d(TAG, "getProduct2 data " + data.toString());
        if (discoveryListener == null)
            throw new RuntimeException("please supply Discovery Listener !!!");

        getCompositeSubscription().add(discoveryService.getApi().browseProducts(MapNulRemover.removeNull(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<BrowseProductModel>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "getProduct2 onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(DiscoveryService.TAG, DiscoveryService.TAG + " -> " + e.getMessage());

                        Pair<String, ErrorContainer> pair = new Pair<String, ErrorContainer>(
                                DiscoveryListener.ERRORCONTAINER,
                                new ErrorContainer(e)
                        );
                        discoveryListener.onFailed(DiscoveryListener.BROWSE_PRODUCT, pair);
                    }

                    @Override
                    public void onNext(Response<BrowseProductModel> browseProductModelResponse) {
                        Log.d(TAG, "getProduct2 onNext");
                        Pair<String, BrowseProductModel.BrowseProductContainer>
                                pair = new Pair<String, BrowseProductModel.BrowseProductContainer>(DiscoveryListener.BROWSEPRODUCT, new BrowseProductModel.BrowseProductContainer(browseProductModelResponse.body())
                        );

                        discoveryListener.onSuccess(DiscoveryListener.BROWSE_PRODUCT, pair);
                    }
                }));

    }

    @Override
    public void getCatalogs(HashMap<String, String> data) {
        Log.d(TAG, "getCatalog2");
        getCompositeSubscription().add(discoveryService.getApi().browseCatalogs(MapNulRemover.removeNull(data)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<BrowseCatalogModel>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(DiscoveryService.TAG, DiscoveryService.TAG + " -> " + e);

                        Pair<String, ErrorContainer> pair = new Pair<String, ErrorContainer>(
                                DiscoveryListener.ERRORCONTAINER,
                                new ErrorContainer(e)
                        );
                        discoveryListener.onFailed(DiscoveryListener.BROWSE_CATALOG, pair);
                    }

                    @Override
                    public void onNext(Response<BrowseCatalogModel> browseCatalogModelResponse) {
                        Log.d(DiscoveryService.TAG, DiscoveryService.TAG + " -> " + browseCatalogModelResponse);

                        Pair<String, BrowseCatalogModel.BrowseCatalogContainer> pair = new Pair(DiscoveryListener.BROWSECATALOG, new BrowseCatalogModel.BrowseCatalogContainer(browseCatalogModelResponse.body()));
                        discoveryListener.onSuccess(DiscoveryListener.BROWSE_CATALOG, pair);
                    }
                }));
    }

    @Override
    public void getShops(HashMap<String, String> data) {
        Log.d(TAG, "getShops2 data " + data.toString());
        getCompositeSubscription().add(discoveryService.getApi().browseShops(MapNulRemover.removeNull(data)).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<BrowseShopModel>>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(DiscoveryService.TAG,
                                        DiscoveryService.TAG + " -> " + e);

                                Pair<String, ErrorContainer> pair = new Pair<String, ErrorContainer>(
                                        DiscoveryListener.ERRORCONTAINER,
                                        new ErrorContainer(e)
                                );
                                discoveryListener.onFailed(DiscoveryListener.BROWSE_SHOP, pair);
                            }

                            @Override
                            public void onNext(Response<BrowseShopModel> browseShopModelResponse) {
                                Log.d(DiscoveryService.TAG,
                                        DiscoveryService.TAG + " -> " + browseShopModelResponse);

                                Pair<String, BrowseShopModel.BrowseShopContainer> pair =
                                        new Pair(
                                                DiscoveryListener.BROWSESHOP,
                                                new BrowseShopModel.BrowseShopContainer(browseShopModelResponse.body())
                                        );
                                discoveryListener.onSuccess(DiscoveryListener.BROWSE_SHOP, pair);
                            }
                        }
                ));
    }

    @Override
    public void getDynamicAttribute(Context context, String source, String depId) {
        Log.d(TAG, "getDynamicAttribute source " + source + " depId " + depId);
        getCompositeSubscription().add(DynamicFilterFactory.createDynamicFilterObservable(context, source, "android", depId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io()).subscribe(new Subscriber<Response<DynamicFilterModel>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "getDynamicAttribute onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "getDynamicAttribute onError " + e.getLocalizedMessage());
                        Pair<String, ErrorContainer> pair = new Pair<String, ErrorContainer>(
                                DiscoveryListener.ERRORCONTAINER,
                                new ErrorContainer(e)
                        );
                        discoveryListener.onFailed(DiscoveryListener.DYNAMIC_ATTRIBUTE, pair);
                    }

                    @Override
                    public void onNext(Response<DynamicFilterModel> dynamicFilterModelResponse) {
                        DynamicFilterModel body = dynamicFilterModelResponse.body();
                        Pair<String, DynamicFilterModel.DynamicFilterContainer> pair = new Pair<String, DynamicFilterModel.DynamicFilterContainer>(DiscoveryListener.DYNAMICATTRIBUTE, new DynamicFilterModel.DynamicFilterContainer(body));
                        discoveryListener.onSuccess(DiscoveryListener.DYNAMIC_ATTRIBUTE, pair);
                    }
                }));
    }
}
