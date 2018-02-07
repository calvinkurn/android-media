package com.tokopedia.discovery.newdiscovery.hotlist.domain.usecase;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.core.network.apiservices.ace.apis.BrowseApi;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.discovery.newdiscovery.data.repository.AttributeRepository;
import com.tokopedia.discovery.newdiscovery.data.repository.BannerRepository;
import com.tokopedia.discovery.newdiscovery.domain.model.SearchResultModel;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistAttributeModel;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistBannerModel;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistModel;
import com.tokopedia.discovery.newdiscovery.hotlist.domain.model.HotlistQueryModel;
import com.tokopedia.discovery.newdiscovery.util.HotlistParameter;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * Created by hangnadi on 10/6/17.
 */

public class GetHotlistInitializeUseCase extends UseCase<HotlistModel> {

    private final BannerRepository bannerRepository;
    private final AttributeRepository attributeRepository;
    private final GetProductUseCase getProductUseCase;
    private HotlistParameter hotlistParameter;

    public GetHotlistInitializeUseCase(ThreadExecutor threadExecutor,
                                       PostExecutionThread postExecutionThread,
                                       GetProductUseCase getProductUseCase,
                                       BannerRepository bannerRepository,
                                       AttributeRepository attributeRepository) {
        super(threadExecutor, postExecutionThread);
        this.getProductUseCase = getProductUseCase;
        this.bannerRepository = bannerRepository;
        this.attributeRepository = attributeRepository;
    }

    @SuppressWarnings("WeakerAccess")
    public static RequestParams createGetHotlistBannerParam(HotlistParameter hotlistParameter) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString("key", hotlistParameter.getHotlistAlias());
        return requestParams;
    }

    @SuppressWarnings("WeakerAccess")
    public static RequestParams createGetHotlistProductParam(HotlistParameter hotlistParameter, HotlistQueryModel queryModel) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(BrowseApi.SOURCE, BrowseApi.DEFAULT_VALUE_SOURCE_HOTLIST);
        requestParams.putString(BrowseApi.DEVICE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_DEVICE);
        requestParams.putString(BrowseApi.IMAGE_SIZE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SIZE);
        requestParams.putString(BrowseApi.IMAGE_SQUARE, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_IMAGE_SQUARE);
        requestParams.putString(BrowseApi.ROWS, BrowseApi.DEFAULT_VALUE_OF_PARAMETER_ROWS);
        if(queryModel!=null) {
            requestParams.putString(BrowseApi.OB, queryModel.getOrderBy());
            requestParams.putString(BrowseApi.Q, queryModel.getQueryKey());
            requestParams.putString(BrowseApi.H, queryModel.getHotlistID());
            requestParams.putString(BrowseApi.SHOP_ID, queryModel.getShopID());
            requestParams.putString(BrowseApi.FSHOP, queryModel.getFilterGoldMerchant());
            requestParams.putString(BrowseApi.PMAX, queryModel.getPriceMax());
            requestParams.putString(BrowseApi.PMIN, queryModel.getPriceMin());
            requestParams.putString(BrowseApi.DEFAULT_SC, queryModel.getCategoryID());
            requestParams.putString(BrowseApi.NEGATIVE, queryModel.getNegativeKeyword());
            requestParams.putString(BrowseApi.HOT_ID, queryModel.getHotlistID());
        }
        requestParams.putString(BrowseApi.UNIQUE_ID, hotlistParameter.getUniqueID());
        if (hotlistParameter.getUserID() != null && !hotlistParameter.getUserID().isEmpty()) {
            requestParams.putString(BrowseApi.USER_ID, hotlistParameter.getUserID());
        }

        return requestParams;
    }

    @SuppressWarnings("WeakerAccess")
    public static RequestParams createGetHotlistAttributeParam(HotlistParameter hotlistParameter, HotlistQueryModel queryModel) {
        return createGetHotlistProductParam(hotlistParameter, queryModel);
    }

    public void setHotlistParameter(HotlistParameter hotlistParameter) {
        this.hotlistParameter = hotlistParameter;
    }

    @Override
    public Observable<HotlistModel> createObservable(RequestParams requestParams) {
        return Observable.just(new HotlistModel())
                .flatMap(new GetHotlistBanner(createGetHotlistBannerParam(hotlistParameter)))
                .flatMap(new GetHotlistProduct(hotlistParameter))
                .flatMap(new GetHotlistAttribute(hotlistParameter));
    }

    private class GetHotlistProduct implements Func1<HotlistModel, Observable<HotlistModel>> {

        private final HotlistParameter hotlistParameter;

        GetHotlistProduct(HotlistParameter hotlistParameter) {
            this.hotlistParameter = hotlistParameter;
        }

        @Override
        public Observable<HotlistModel> call(HotlistModel hotlistModel) {
            RequestParams requestParams = createGetHotlistProductParam(hotlistParameter, hotlistModel.getBanner().getHotlistQueryModel());
            return Observable.zip(
                    Observable.just(hotlistModel),
                    getProductUseCase.createObservable(requestParams),
                    new Func2<HotlistModel, SearchResultModel, HotlistModel>() {
                        @Override
                        public HotlistModel call(HotlistModel hotlistModel, SearchResultModel searchResultModel) {
                            hotlistModel.setTotalData(searchResultModel.getTotalData());
                            hotlistModel.setProductList(searchResultModel.getProductList());
                            hotlistModel.setShareURL(searchResultModel.getShareUrl());
                            return hotlistModel;
                        }
                    }
            );
        }

    }

    private class GetHotlistBanner implements Func1<HotlistModel, Observable<HotlistModel>> {
        private final TKPDMapParam<String, Object> parameters;

        GetHotlistBanner(RequestParams requestParams) {
            this.parameters = requestParams.getParameters();
        }

        @Override
        public Observable<HotlistModel> call(HotlistModel hotlistModel) {
            return Observable.zip(
                    Observable.just(hotlistModel),
                    bannerRepository.getHotlistBanner(parameters),
                    new Func2<HotlistModel, HotlistBannerModel, HotlistModel>() {
                        @Override
                        public HotlistModel call(HotlistModel hotlistModel, HotlistBannerModel hotlistBannerModel) {
                            hotlistModel.setBanner(hotlistBannerModel);
                            return hotlistModel;
                        }
                    }
            );
        }
    }

    private class GetHotlistAttribute implements Func1<HotlistModel, Observable<HotlistModel>> {

        private final HotlistParameter hotlistParameter;

        GetHotlistAttribute(HotlistParameter hotlistParameter) {
            this.hotlistParameter = hotlistParameter;
        }

        @Override
        public Observable<HotlistModel> call(HotlistModel hotlistModel) {
            RequestParams requestParams = createGetHotlistAttributeParam(hotlistParameter, hotlistModel.getBanner().getHotlistQueryModel());
            return Observable.zip(
                    Observable.just(hotlistModel),
                    attributeRepository.getHotlistAttribute(requestParams.getParameters()),
                    new Func2<HotlistModel, HotlistAttributeModel, HotlistModel>() {
                        @Override
                        public HotlistModel call(HotlistModel hotlistModel, HotlistAttributeModel hotlistAttributeModel) {
                            hotlistModel.setAttribute(hotlistAttributeModel);
                            return hotlistModel;
                        }
                    }
            );
        }
    }
}
