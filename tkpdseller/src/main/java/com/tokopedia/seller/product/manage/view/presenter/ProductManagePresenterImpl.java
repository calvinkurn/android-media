package com.tokopedia.seller.product.manage.view.presenter;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.common.featuredproduct.GMFeaturedProductDomainModel;
import com.tokopedia.seller.product.manage.constant.CatalogProductOption;
import com.tokopedia.seller.product.manage.constant.ConditionProductOption;
import com.tokopedia.seller.product.manage.constant.EtalaseProductOption;
import com.tokopedia.seller.product.manage.constant.PictureStatusProductOption;
import com.tokopedia.seller.product.manage.constant.SortProductOption;
import com.tokopedia.seller.product.manage.domain.DeleteProductUseCase;
import com.tokopedia.seller.product.manage.domain.EditPriceProductUseCase;
import com.tokopedia.seller.product.manage.view.listener.ProductManageView;
import com.tokopedia.seller.product.manage.view.mapper.GetProductListManageMapperView;
import com.tokopedia.seller.product.manage.view.model.ProductListManageModelView;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.domain.interactor.GetProductListSellingUseCase;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by zulfikarrahman on 9/22/17.
 */

public class ProductManagePresenterImpl extends BaseDaggerPresenter<ProductManageView> implements ProductManagePresenter {

    private final GetProductListSellingUseCase getProductListSellingUseCase;
    private final EditPriceProductUseCase editPriceProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final GetProductListManageMapperView getProductListManageMapperView;
    private final SellerModuleRouter sellerModuleRouter;

    public ProductManagePresenterImpl(GetProductListSellingUseCase getProductListSellingUseCase,
                                      EditPriceProductUseCase editPriceProductUseCase,
                                      DeleteProductUseCase deleteProductUseCase,
                                      GetProductListManageMapperView getProductListManageMapperView,
                                      SellerModuleRouter sellerModuleRouter) {
        this.getProductListSellingUseCase = getProductListSellingUseCase;
        this.editPriceProductUseCase = editPriceProductUseCase;
        this.deleteProductUseCase = deleteProductUseCase;
        this.getProductListManageMapperView = getProductListManageMapperView;
        this.sellerModuleRouter = sellerModuleRouter;
    }

    @Override
    public void getListFeaturedProduct() {
        sellerModuleRouter.getFeaturedProduct().subscribe(getSubscriberGetListFeaturedProduct());
    }

    @Override
    public void editPrice(String productId, String price, String priceCurrency) {
        getView().showLoadingProgress();
        editPriceProductUseCase.execute(EditPriceProductUseCase.createRequestParams(price, priceCurrency, productId), getSubscriberEditPrice());
    }

    @Override
    public void deleteProduct(String productId) {
        getView().showLoadingProgress();
        deleteProductUseCase.execute(DeleteProductUseCase.createRequestParams(productId), getSubscriberDeleteProduct());
    }

    @Override
    public void getListProduct(int page, String keywordFilter) {
        getProductListSellingUseCase.execute(GetProductListSellingUseCase.createRequestParamsManageProduct(page,
                keywordFilter, CatalogProductOption.NOT_USED, ConditionProductOption.NOT_USED, "", EtalaseProductOption.ALL_SHOWCASE,
                PictureStatusProductOption.NOT_USED, SortProductOption.POSITION), getSubscriberGetListProduct());
    }

    private Subscriber<Boolean> getSubscriberDeleteProduct() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideLoadingProgress();
                    getView().onErrorDeleteProduct();
                }
            }

            @Override
            public void onNext(Boolean isSuccessDeleteProduct) {
                if (isSuccessDeleteProduct) {
                    getView().onSuccessDeleteProduct();
                } else {
                    getView().onErrorDeleteProduct();
                }
                getView().hideLoadingProgress();
            }
        };
    }

    private Subscriber<Boolean> getSubscriberEditPrice() {
        return new Subscriber<Boolean>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                if (isViewAttached()) {
                    getView().hideLoadingProgress();
                    getView().onErrorEditPrice();
                }
            }

            @Override
            public void onNext(Boolean isSuccessEditPrice) {
                if (isSuccessEditPrice) {
                    getView().onSuccessEditPrice();
                } else {
                    getView().onErrorEditPrice();
                }
                getView().hideLoadingProgress();
            }
        };
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
                if (isViewAttached()) {
                    getView().onErrorGetFeaturedProductList();
                }
            }

            @Override
            public void onNext(GMFeaturedProductDomainModel gmFeaturedProductDomainModel) {
                getView().onGetFeaturedProductList(transform(gmFeaturedProductDomainModel.getData()));
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
    }
}
