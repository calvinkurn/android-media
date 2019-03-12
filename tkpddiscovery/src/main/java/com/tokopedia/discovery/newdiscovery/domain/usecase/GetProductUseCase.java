package com.tokopedia.discovery.newdiscovery.domain.usecase;

import android.content.Context;
import android.text.TextUtils;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.apiservices.mojito.apis.MojitoApi;
import com.tokopedia.core.network.entity.wishlist.WishlistCheckResult;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.discovery.newdiscovery.constant.SearchApiConst;
import com.tokopedia.discovery.newdiscovery.data.repository.BannerRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.ProductRepository;
import com.tokopedia.discovery.newdiscovery.domain.model.ProductModel;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.search.model.OfficialStoreBannerModel;
import com.tokopedia.discovery.newdiscovery.search.model.SearchParameter;
import com.tokopedia.remoteconfig.RemoteConfig;
import com.tokopedia.topads.sdk.domain.TopAdsParams;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by hangnadi on 10/3/17.
 */

public class GetProductUseCase extends UseCase<SearchResultModel> {

    public static final String PARAMETER_ROWS = "8";
    private final ProductRepository productRepository;
    private final BannerRepository bannerRepository;
    private final MojitoApi service;
    private static final String REQUEST_OS_BANNER = "REQUEST_OS_BANNER";
    private static boolean mRequestOfficialStoreBanner = false;
    private final Context context;
    private static boolean changeParamRow;
    private final RemoteConfig firebaseRemoteConfig;

    public GetProductUseCase(Context context, ThreadExecutor threadExecutor,
                             PostExecutionThread postExecutionThread,
                             ProductRepository productRepository,
                             BannerRepository bannerRepository,
                             MojitoApi service,
                             RemoteConfig remoteConfig) {
        super(threadExecutor, postExecutionThread);
        this.context = context;
        this.productRepository = productRepository;
        this.bannerRepository = bannerRepository;
        this.service = service;
        this.firebaseRemoteConfig = remoteConfig;
        this.changeParamRow = firebaseRemoteConfig.getBoolean(TkpdCache.RemoteConfigKey.APP_CHANGE_PARAMETER_ROW, false);
    }

    public static RequestParams createInitializeSearchParam(SearchParameter searchParameter) {
        return createInitializeSearchParam(searchParameter, false);
    }

    /**
     * @param searchParameter have to setSource();
     *                        have to setUniqueID();
     *                        have to setQuery();
     *                        have to setSortKey();
     *                        have to setUserID() if exist;
     *                        else will be template default using static value
     * @return will become requestParams spesifically for search using @See DiscoverySearchView.class
     */
    public static RequestParams createInitializeSearchParam(SearchParameter searchParameter, boolean forceSearch) {
        return createInitializeSearchParam(searchParameter, forceSearch, false);
    }

    public static RequestParams createInitializeSearchParam(SearchParameter searchParameter, boolean forceSearch, boolean requestOfficialStoreBanner) {
        mRequestOfficialStoreBanner = requestOfficialStoreBanner;

        RequestParams requestParams = RequestParams.create();

        requestParams.putAll(searchParameter.getSearchParameterHashMap());
        putRequestParamsOtherParameters(requestParams, searchParameter, forceSearch);

        return requestParams;
    }

    private static void putRequestParamsOtherParameters(RequestParams requestParams, SearchParameter searchParameter, boolean forceSearch) {
        putRequestParamsSearchParameters(requestParams, searchParameter, forceSearch);

        putRequestParamsTopAdsParameters(requestParams, searchParameter);

        putRequestParamsDepartmentIdIfNotEmpty(requestParams, searchParameter);
    }

    private static void putRequestParamsSearchParameters(RequestParams requestParams, SearchParameter searchParameter, boolean forceSearch) {
        requestParams.putString(SearchApiConst.SOURCE, getSearchSource(searchParameter));
        requestParams.putString(SearchApiConst.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(SearchApiConst.ROWS, getSearchRows());
        requestParams.putString(SearchApiConst.OB, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_SORT);
        requestParams.putString(SearchApiConst.START, getSearchStart(searchParameter));
        requestParams.putString(SearchApiConst.IMAGE_SIZE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(SearchApiConst.IMAGE_SQUARE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
        requestParams.putString(SearchApiConst.Q, omitNewline(searchParameter.getSearchQuery()));
        requestParams.putString(SearchApiConst.UNIQUE_ID, searchParameter.get(SearchApiConst.UNIQUE_ID));
        requestParams.putBoolean(SearchApiConst.REFINED, forceSearch);
    }

    private static void putRequestParamsTopAdsParameters(RequestParams requestParams, SearchParameter searchParameter) {
        requestParams.putInt(TopAdsParams.KEY_ITEM, 2);
        requestParams.putString(TopAdsParams.KEY_EP, TopAdsParams.DEFAULT_KEY_EP);
        requestParams.putString(TopAdsParams.KEY_SRC, getSearchSource(searchParameter));
        requestParams.putInt(TopAdsParams.KEY_PAGE, getTopAdsKeyPage(searchParameter));
    }

    private static void putRequestParamsDepartmentIdIfNotEmpty(RequestParams requestParams, SearchParameter searchParameter) {
        String departmentId = searchParameter.get(SearchApiConst.SC);

        if (!TextUtils.isEmpty(departmentId)) {
            requestParams.putString(SearchApiConst.SC, departmentId);
            requestParams.putString(TopAdsParams.KEY_DEPARTEMENT_ID, departmentId);
        }
    }

    private static String getSearchSource(SearchParameter searchParameter) {
        String sourceFromParamModel = searchParameter.get(SearchApiConst.SOURCE);

        return !TextUtils.isEmpty(sourceFromParamModel) ?
                sourceFromParamModel :
                BrowseApi.DEFAULT_VALUE_SOURCE_SEARCH;
    }

    private static String getSearchRows() {
        return (changeParamRow) ? PARAMETER_ROWS : BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS;
    }

    private static String getSearchStart(SearchParameter searchParameter) {
        return String.valueOf(searchParameter.getInteger(SearchApiConst.START));
    }

    private static String omitNewline(String text) {
        return text.replace("\n", "");
    }

    private static int getTopAdsKeyPage(SearchParameter searchParameter) {
        return (searchParameter.getInteger(SearchApiConst.START) /
                Integer.parseInt(BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS) + 1);
    }

    @Override
    public Observable<SearchResultModel> createObservable(RequestParams requestParams) {
        if (mRequestOfficialStoreBanner) {
            return Observable.zip(productRepository.getProduct(requestParams.getParameters()),
                    bannerRepository.getOfficialStoreBanner(requestParams.getString(BrowseApi.Q, "")),
                    new Func2<SearchResultModel, OfficialStoreBannerModel, SearchResultModel>() {
                        @Override
                        public SearchResultModel call(SearchResultModel searchResultModel, OfficialStoreBannerModel officialStoreBannerModel) {
                            searchResultModel.setOfficialStoreBannerModel(officialStoreBannerModel);
                            return searchResultModel;
                        }
                    }).flatMap(wishlistDataEnricher(requestParams.getString(BrowseApi.USER_ID, "")));
        } else {
            return productRepository.getProduct(requestParams.getParameters())
                    .flatMap(wishlistDataEnricher(requestParams.getString(BrowseApi.USER_ID, "")));
        }
    }

    private Func1<SearchResultModel, Observable<SearchResultModel>> wishlistDataEnricher(final String userId) {
        return new Func1<SearchResultModel, Observable<SearchResultModel>>() {
            @Override
            public Observable<SearchResultModel> call(final SearchResultModel searchResultModel) {
                if (TextUtils.isEmpty(userId) || searchResultModel.getProductList().isEmpty()) {
                    return Observable.just(searchResultModel);
                }

                return Observable.zip(Observable.just(searchResultModel),
                        getWishlistData(searchResultModel, userId),
                        new Func2<SearchResultModel, Response<WishlistCheckResult>, SearchResultModel>() {
                            @Override
                            public SearchResultModel call(SearchResultModel searchResultModel,
                                                          Response<WishlistCheckResult> wishlistCheckResultResponse) {
                                enrichWithWishlistData(searchResultModel, wishlistCheckResultResponse);
                                return searchResultModel;
                            }
                        }).onErrorReturn(new Func1<Throwable, SearchResultModel>() {
                    @Override
                    public SearchResultModel call(Throwable throwable) {
                        return searchResultModel;
                    }
                });
            }
        };
    }

    private Observable<Response<WishlistCheckResult>> getWishlistData(SearchResultModel searchResultModel, String userId) {

        List<String> productIdList = generateProductIdList(searchResultModel.getProductList());

        return service.checkWishlist(userId, TextUtils.join(",", productIdList))
                .onErrorReturn(new Func1<Throwable, Response<WishlistCheckResult>>() {
                    @Override
                    public Response<WishlistCheckResult> call(Throwable throwable) {
                        WishlistCheckResult wishlistCheckResult = new WishlistCheckResult();
                        WishlistCheckResult.CheckResultIds ids = new WishlistCheckResult.CheckResultIds();
                        wishlistCheckResult.setCheckResultIds(ids);
                        return Response.success(wishlistCheckResult);
                    }
                });
    }

    private List<String> generateProductIdList(List<ProductModel> productModelList) {
        List<String> productIdList = new ArrayList<>();
        for (ProductModel productModel : productModelList) {
            productIdList.add(productModel.getProductID());
        }
        return productIdList;
    }

    private void enrichWithWishlistData(SearchResultModel searchResultModel,
                                        Response<WishlistCheckResult> wishlistCheckResultResponse) {

        List<String> wishlistedIdList = wishlistCheckResultResponse.body().getCheckResultIds().getIds();

        for (ProductModel productModel : searchResultModel.getProductList()) {
            productModel.setWishlisted(wishlistedIdList.contains(productModel.getProductID()));
        }
    }
}
