package com.tokopedia.seller.product.manage.view.presenter;

import android.accounts.NetworkErrorException;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.domain.DeleteProductUseCase;
import com.tokopedia.seller.product.manage.domain.EditPriceProductUseCase;
import com.tokopedia.seller.product.manage.domain.MultipleDeleteProductUseCase;
import com.tokopedia.seller.product.manage.domain.model.MultipleDeleteProductModel;
import com.tokopedia.seller.product.manage.view.listener.ProductManageView;
import com.tokopedia.seller.product.manage.view.mapper.GetProductListManageMapperView;
import com.tokopedia.seller.product.manage.view.model.ProductListManageModelView;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.domain.interactor.GetProductListSellingUseCase;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ProductManagePresenterImpl extends BaseDaggerPresenter<ProductManageView> implements ProductManagePresenter {

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetProductListSellingUseCase getProductListSellingUseCase;
    private final EditPriceProductUseCase editPriceProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductListManageMapperView getProductListManageMapperView;
    private final SellerModuleRouter sellerModuleRouter;
    private final MultipleDeleteProductUseCase multipleDeleteProductUseCase;

    public ProductManagePresenterImpl(GetShopInfoUseCase getShopInfoUseCase,
                                      GetProductListSellingUseCase getProductListSellingUseCase,
                                      EditPriceProductUseCase editPriceProductUseCase,
                                      DeleteProductUseCase deleteProductUseCase,
                                      GetProductListManageMapperView getProductListManageMapperView,
                                      SellerModuleRouter sellerModuleRouter,
                                      MultipleDeleteProductUseCase multipleDeleteProductUseCase) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getProductListSellingUseCase = getProductListSellingUseCase;
        this.editPriceProductUseCase = editPriceProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.getProductListManageMapperView = getProductListManageMapperView;
        this.sellerModuleRouter = sellerModuleRouter;
        this.multipleDeleteProductUseCase = multipleDeleteProductUseCase;
    }

    @Override
    public void getGoldMerchantStatus() {
        getShopInfoUseCase.execute(RequestParams.EMPTY, new Subscriber<ShopModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ShopModel shopModel) {
                getView().onSuccessGetShopInfo(shopModel.getInfo().isGoldMerchant(), shopModel.getInfo().isOfficialStore());
            }
        });
    }

    @Override
    public void setCashback(final String productId, final int cashback) {
        getView().showLoadingProgress();
        sellerModuleRouter.setCashBack(productId, cashback).subscribe(new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideLoadingProgress();
                    getView().onErrorSetCashback(e, productId, cashback);
                }
            }

            @Override
            public void onNext(Boolean isSuccess) {
                getView().hideLoadingProgress();
                if (isSuccess) {
                    getView().onSuccessSetCashback(productId, cashback);
                } else {
                    getView().onErrorSetCashback(new NetworkErrorException(), productId, cashback);
                }
            }
        });
    }

    @Override
    public void deleteProduct(final List<String> productIdList) {
        getView().showLoadingProgress();
        multipleDeleteProductUseCase.execute(MultipleDeleteProductUseCase.createRequestParams(productIdList), new Subscriber<MultipleDeleteProductModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideLoadingProgress();
                    getView().onErrorMultipleDeleteProduct(e, new ArrayList<String>(), productIdList);
                }
            }

            @Override
            public void onNext(MultipleDeleteProductModel multipleDeleteProductModel) {
                getView().hideLoadingProgress();
                if (multipleDeleteProductModel.getProductIdFailedToDeleteList().size() > 0) {
                    getView().onErrorMultipleDeleteProduct(new NetworkErrorException(),
                            multipleDeleteProductModel.getProductIdDeletedList(),
                            multipleDeleteProductModel.getProductIdFailedToDeleteList());
                } else {
                    getView().onSuccessMultipleDeleteProduct();
                }
            }
        });
    }

    @Override
    public void getListFeaturedProduct() {
        sellerModuleRouter.getFeaturedProduct().subscribe(getSubscriberGetListFeaturedProduct());
    }

    @Override
    public void editPrice(final String productId, final String price, final String currencyId, final String currencyText) {
        getView().showLoadingProgress();
        editPriceProductUseCase.execute(EditPriceProductUseCase.createRequestParams(price, currencyId, productId), new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable t) {
                if (isViewAttached()) {
                    getView().hideLoadingProgress();
                    getView().onErrorEditPrice(t, productId, price, currencyId, currencyText);
                }
            }

            @Override
            public void onNext(Boolean isSuccessEditPrice) {
                getView().hideLoadingProgress();
                if (isSuccessEditPrice) {
                    getView().onSuccessEditPrice(productId, price, currencyId, currencyText);
                } else {
                    getView().onErrorEditPrice(new NetworkErrorException(), productId, price, currencyId, currencyText);
                }
            }
        });
    }

    @Override
    public void getListProduct(int page, String keywordFilter, @CatalogProductOption String catalogOption,
                               @ConditionProductOption String conditionOption, int etalaseId,
                               @PictureStatusProductOption String pictureOption, @SortProductOption String sortOption, String categoryId) {
        getProductListSellingUseCase.execute(GetProductListSellingUseCase.createRequestParamsManageProduct(page,
                keywordFilter, catalogOption, conditionOption, categoryId, etalaseId,
                pictureOption, sortOption), getSubscriberGetListProduct());
    }

    private Subscriber<ProductListSellerModel> getSubscriberGetListProduct() {
        return new Subscriber<ProductListSellerModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().onLoadSearchError(e);
                }
            }

            @Override
            public void onNext(ProductListSellerModel productListSellerModel) {
                ProductListManageModelView productListManageModelView = getProductListManageMapperView.transform(productListSellerModel);
                getView().onSearchLoaded(productListManageModelView.getProductManageViewModels(),
                        productListManageModelView.getProductManageViewModels().size(),
                        productListManageModelView.isHasNextPage());
            }
        };
    }

    private Subscriber<GMFeaturedProductDomainModel> getSubscriberGetListFeaturedProduct() {
        return new Subscriber<GMFeaturedProductDomainModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(GMFeaturedProductDomainModel gmFeaturedProductDomainModel) {
                getView().onSuccessGetFeaturedProductList(transform(gmFeaturedProductDomainModel.getData()));
            }
        };
    }

    private List<String> transform(List<GMFeaturedProductDomainModel.Datum> datas) {
        List<String> productIds = new ArrayList<>();
        for (GMFeaturedProductDomainModel.Datum data : datas) {
            productIds.add(String.valueOf(data.getProductId()));
        }
        return productIds;
    }

    @Override
    public void detachView() {
        super.detachView();
        getProductListSellingUseCase.unsubscribe();
        editPriceProductUseCase.unsubscribe();
        deleteProductUseCase.unsubscribe();
        multipleDeleteProductUseCase.unsubscribe();
    }
}
