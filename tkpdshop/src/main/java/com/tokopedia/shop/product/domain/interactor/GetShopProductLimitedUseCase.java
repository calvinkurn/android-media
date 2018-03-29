package com.tokopedia.shop.product.domain.interactor;

import com.tokopedia.abstraction.common.data.model.response.PagingList;
import com.tokopedia.shop.common.util.TextApiUtils;
import com.tokopedia.shop.product.data.source.cloud.model.ShopProductCampaign;
import com.tokopedia.shop.product.domain.model.ShopProductRequestModel;
import com.tokopedia.shop.product.view.model.ShopProductBaseViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedFeaturedViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedProductViewModel;
import com.tokopedia.shop.product.view.model.ShopProductLimitedSearchViewModel;
import com.tokopedia.shop.product.view.model.ShopProductViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by normansyahputa on 2/23/18.
 */

public class GetShopProductLimitedUseCase extends UseCase<List<ShopProductBaseViewModel>> {

    private static final String SHOP_ID = "SHOP_ID";
    private static final String GOLD_MERCHANT_STORE = "GOLD_MERCHANT";
    private static final String OFFICIAL_STORE = "OFFICIAL_STORE";
    private static final String PAGE = "PAGE";

    private final GetShopProductFeaturedUseCase getShopProductFeaturedUseCase;
    private final GetShopProductListWithAttributeUseCase getShopProductListWithAttributeUseCase;

    @Inject
    public GetShopProductLimitedUseCase(GetShopProductFeaturedUseCase getShopProductFeaturedUseCase,
                                        GetShopProductListWithAttributeUseCase getShopProductListWithAttributeUseCase) {
        this.getShopProductFeaturedUseCase = getShopProductFeaturedUseCase;
        this.getShopProductListWithAttributeUseCase = getShopProductListWithAttributeUseCase;
    }

    @Override
    public Observable<List<ShopProductBaseViewModel>> createObservable(RequestParams requestParams) {
        final String shopId = requestParams.getString(SHOP_ID, "");
        final boolean goldMerchantStore = requestParams.getBoolean(GOLD_MERCHANT_STORE, false);
        final boolean officialStore = requestParams.getBoolean(OFFICIAL_STORE, false);
        final int page = requestParams.getInt(PAGE, 0);
        final ShopProductRequestModel shopProductRequestModel = new ShopProductRequestModel();
        shopProductRequestModel.setShopId(shopId);
        shopProductRequestModel.setOfficialStore(officialStore);
        shopProductRequestModel.setPage(page);
        List<ShopProductViewModel> defaultFeaturedProductList = new ArrayList<>();
        Observable<List<ShopProductViewModel>> featuredProductObservable = Observable.just(defaultFeaturedProductList);
        if (goldMerchantStore || officialStore) {
            featuredProductObservable = getShopProductFeaturedUseCase.createObservable(GetShopProductFeaturedUseCase.createRequestParam(shopId, officialStore));
        }
        Observable<PagingList<ShopProductViewModel>> shopProductObservable = getShopProductListWithAttributeUseCase.createObservable(GetShopProductListUseCase.createRequestParam(shopProductRequestModel));

        return Observable.zip(
                featuredProductObservable.subscribeOn(Schedulers.io()), shopProductObservable.subscribeOn(Schedulers.io()),
                new Func2<List<ShopProductViewModel>, PagingList<ShopProductViewModel>, List<ShopProductBaseViewModel>>() {
                    @Override
                    public List<ShopProductBaseViewModel> call(List<ShopProductViewModel> shopProductViewModelList, PagingList<ShopProductViewModel> shopProductList) {
                        List<ShopProductBaseViewModel> shopProductBaseViewModelList = new ArrayList<>();
                        if(page==1){
                            if (shopProductViewModelList.size() > 0) {
                                ShopProductLimitedFeaturedViewModel shopProductLimitedFeaturedViewModel = new ShopProductLimitedFeaturedViewModel();
                                shopProductLimitedFeaturedViewModel.setShopProductViewModelList(shopProductViewModelList);
                                shopProductBaseViewModelList.add(shopProductLimitedFeaturedViewModel);
                            }
                        }
                        if (shopProductList.getList().size() > 0) {
                            ShopProductLimitedProductViewModel shopProductLimitedProductViewModel = new ShopProductLimitedProductViewModel();
                            shopProductLimitedProductViewModel.setShopProductViewModelList(shopProductList);
                            shopProductBaseViewModelList.add(shopProductLimitedProductViewModel);
                        }
                        return shopProductBaseViewModelList;
                    }
                }
        );
    }

    public static RequestParams createRequestParam(String shopId, boolean goldMerchantStore, boolean officialStore, int page) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(SHOP_ID, shopId);
        requestParams.putBoolean(GOLD_MERCHANT_STORE, goldMerchantStore);
        requestParams.putBoolean(OFFICIAL_STORE, officialStore);
        requestParams.putInt(PAGE, page);
        return requestParams;
    }
}
