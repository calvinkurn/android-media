package com.tokopedia.discovery.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.GsonBuilder;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.discovery.model.DynamicFilterModel;
import com.tokopedia.core.discovery.model.HotListBannerModel;
import com.tokopedia.core.discovery.model.ObjContainer;
import com.tokopedia.core.discovery.model.searchSuggestion.SearchDataModel;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.apiservices.ace.DiscoveryService;
import com.tokopedia.core.network.apiservices.hades.HadesService;
import com.tokopedia.core.network.apiservices.search.HotListService;
import com.tokopedia.core.network.apiservices.search.SearchSuggestionService;
import com.tokopedia.core.network.apiservices.topads.TopAdsService;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.core.network.entity.discovery.BrowseCatalogModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.network.entity.discovery.BrowseShopModel;
import com.tokopedia.core.network.entity.topads.TopAdsResponse;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.MapNulRemover;
import com.tokopedia.core.util.Pair;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.discovery.dynamicfilter.DynamicFilterFactory;
import com.tokopedia.discovery.interfaces.DiscoveryListener;
import com.tokopedia.discovery.model.ErrorContainer;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
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

    public CompositeSubscription getCompositeSubscription() {
        return compositeSubscription;
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
    public void getCategoryHeader(String categoryId) {
        getCompositeSubscription().add(hadesService.getApi().getCategories(categoryId)
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
                                Log.d(TAG, "onError: ");
                               /* Log.e(DiscoveryService.TAG, DiscoveryService.TAG + " -> " + e.getMessage());
                                Pair<String, ErrorContainer> pair = new Pair<>(
                                        DiscoveryListener.ERRORCONTAINER,
                                        new ErrorContainer(e)
                                );
                                discoveryListener.onFailed(DiscoveryListener.HOTLIST_BANNER, pair);*/
                            }

                            @Override
                            public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                                Log.d(TAG, "onNext: ");
                                /*HotListBannerModel hotListBannerModel = new GsonBuilder().create().fromJson(tkpdResponseResponse.body().getStringData(), HotListBannerModel.class);
                                Pair<String, HotListBannerModel.HotListBannerContainer> pair =
                                        new Pair<>(
                                                DiscoveryListener.HOTLISTBANNER,
                                                new HotListBannerModel.HotListBannerContainer(
                                                        hotListBannerModel
                                                )
                                        );
                                discoveryListener.onSuccess(DiscoveryListener.HOTLIST_BANNER, pair);*/
                            }
                        }
                ));
    }

    @Override
    public void getTopAds(HashMap<String, String> data) {
        final String page = data.get(TopAdsApi.PAGE);
        Log.d(TAG, "getTopAds params "+data.toString());
        String xDevice = "android";
        String userId = SessionHandler.getLoginID(MainApplication.getAppContext());
        String sessionId = GCMHandler.getRegistrationId(MainApplication.getAppContext());
        getCompositeSubscription().add(topAdsService.getApi().getTopAds(xDevice, userId, sessionId, MapNulRemover.removeNull(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(
                        new Subscriber<Response<TopAdsResponse>>() {
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
                                discoveryListener.onFailed(DiscoveryListener.TOPADS, pair);
                            }

                            @Override
                            public void onNext(Response<TopAdsResponse> topAdsResponseResponse) {
                                TopAdsResponse.TopAdsContainer topAdsContainer = new TopAdsResponse.TopAdsContainer(
                                        topAdsResponseResponse.body()
                                );
                                topAdsContainer.page = Integer.parseInt(page);
                                Pair<String, TopAdsResponse.TopAdsContainer> pair =
                                        new Pair<>(
                                                DiscoveryListener.TOPADS_STRING,
                                                topAdsContainer
                                        );
                                discoveryListener.onSuccess(DiscoveryListener.TOPADS,
                                        pair);
                            }
                        }
                ));
    }

    @Override
    public void loadSearchSuggestion(final String querySearch, String unique_id, int count) {
        Log.d(TAG, "loadSearchSuggestion query " + querySearch + " unique_id " + unique_id);
        getCompositeSubscription().add(searchSuggestionService.getApi().searchSuggestion(
                querySearch, unique_id, String.valueOf(count))
                .debounce(150, TimeUnit.MICROSECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<SearchDataModel>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError -> " + e.getMessage());
                        Pair<String, ErrorContainer> pair = new Pair<String, ErrorContainer>(
                                DiscoveryListener.ERRORCONTAINER,
                                new ErrorContainer(e)
                        );
                        discoveryListener.onFailed(DiscoveryListener.SEARCH_SUGGESTION, pair);
                    }

                    @Override
                    public void onNext(Response<SearchDataModel> searchDataModelResponse) {
                        Log.d(TAG, "onNext -> " + searchDataModelResponse.body().toString());
                        Pair<String, SearchDataModel.SearchSuggestionContainer> pair = new Pair<String, SearchDataModel.SearchSuggestionContainer>(querySearch, new SearchDataModel.SearchSuggestionContainer(searchDataModelResponse.body()));
                        discoveryListener.onSuccess(DiscoveryListener.SEARCH_SUGGESTION, pair);
                    }
                })

        );
    }

    @Override
    public void deleteSearchHistory(final String unique_id, final String keyword, boolean clear_all) {
        getCompositeSubscription().add(searchSuggestionService.getApi().deleteHistorySearch(
                keyword, unique_id, String.valueOf(clear_all))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Response<Void>>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError -> " + e.getMessage());
                        Pair<String, ErrorContainer> pair = new Pair<String, ErrorContainer>(
                                DiscoveryListener.ERRORCONTAINER,
                                new ErrorContainer(e)
                        );
                        discoveryListener.onFailed(DiscoveryListener.SEARCH_SUGGESTION, pair);
                    }

                    @Override
                    public void onNext(Response<Void> voidResponse) {
                        Pair<String, ObjContainer<HashMap<String, String>>> pair = new Pair<String, ObjContainer<HashMap<String, String>>>(keyword, new ObjContainer<HashMap<String, String>>() {
                            @Override
                            public HashMap<String, String> body() {
                                HashMap<String, String> hashMap = new HashMap<String, String>();
                                hashMap.put("unique_id", unique_id);
                                hashMap.put("keyword", keyword);
                                return hashMap;
                            }
                        });
                        discoveryListener.onSuccess(DiscoveryListener.DELETE_SUGGESTION, pair);
                    }
                })
        );
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
