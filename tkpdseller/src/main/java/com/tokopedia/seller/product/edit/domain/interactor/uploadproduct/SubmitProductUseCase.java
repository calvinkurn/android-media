package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import android.text.TextUtils;

import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.product.edit.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.seller.product.edit.domain.listener.ProductSubmitNotificationListener;
import com.tokopedia.seller.product.edit.domain.mapper.ProductUploadMapper;
import com.tokopedia.seller.product.edit.view.model.edit.BasePictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class SubmitProductUseCase extends UseCase<Boolean> {

    private static final String NOTIFICATION_COUNT_LISTENER = "NOTIFICATION_COUNT_LISTENER";
    private static final String PRODUCT_VIEW_MODEL = "PRODUCT_VIEW_MODEL";

    private final GetShopInfoUseCase getShopInfoUseCase;
    private final GetProductDetailUseCase getProductDetailUseCase;
    private final UploadProductImageUseCase uploadProductImageUseCase;
    private final SubmitRawProductUseCase submitRawProductUseCase;
    private final ProductUploadMapper productUploadMapper;

    private ProductSubmitNotificationListener notificationCountListener;

    @Inject
    public SubmitProductUseCase(
            GetShopInfoUseCase getShopInfoUseCase,
            GetProductDetailUseCase getProductDetailUseCase,
            UploadProductImageUseCase uploadProductImageUseCase,
            SubmitRawProductUseCase submitRawProductUseCase,
            ProductUploadMapper productUploadMapper) {
        this.getShopInfoUseCase = getShopInfoUseCase;
        this.getProductDetailUseCase = getProductDetailUseCase;
        this.uploadProductImageUseCase = uploadProductImageUseCase;
        this.submitRawProductUseCase = submitRawProductUseCase;
        this.productUploadMapper = productUploadMapper;
    }

    @Override
    public Observable<Boolean> createObservable(RequestParams requestParams) {
        ProductViewModel productViewModel = (ProductViewModel) requestParams.getObject(PRODUCT_VIEW_MODEL);
        notificationCountListener = (ProductSubmitNotificationListener) requestParams.getObject(NOTIFICATION_COUNT_LISTENER);
        return Observable.just(productViewModel)
                .flatMap(new Func1<ProductViewModel, Observable<ProductViewModel>>() {
                    @Override
                    public Observable<ProductViewModel> call(ProductViewModel productFromDraft) {
                        if (TextUtils.isEmpty(productFromDraft.getProductId())) {
                            return Observable.just(productFromDraft);
                        } else {
                            return getProductDetailUseCase.createObservable(GetProductDetailUseCase.createParams(productFromDraft.getProductId()))
                                    .map(removeUnusedParam(productFromDraft));
                        }
                    }
                })
                .doOnNext(new Action1<ProductViewModel>() {
                    @Override
                    public void call(ProductViewModel productViewModel) {
                        notificationCountListener.addProgress();
                    }
                })
                .flatMap(new Func1<ProductViewModel, Observable<ProductViewModel>>() {
                    @Override
                    public Observable<ProductViewModel> call(final ProductViewModel productViewModel) {
                        // Replace product id to shop id if new, recommendation from power ranger team to avoid null on image path
                        if (!TextUtils.isEmpty(productViewModel.getProductId())) {
                            return uploadProductPhoto(productViewModel, productViewModel.getProductId());
                        } else {
                            return getShopInfoUseCase.createObservable().flatMap(new Func1<ShopModel, Observable<ProductViewModel>>() {
                                @Override
                                public Observable<ProductViewModel> call(ShopModel shopModel) {
                                    return uploadProductPhoto(productViewModel, shopModel.getInfo().getShopId());
                                }
                            });
                        }
                    }
                })
                .doOnNext(new Action1<ProductViewModel>() {
                    @Override
                    public void call(ProductViewModel productViewModel) {
                        notificationCountListener.addProgress();
                    }
                })
                .flatMap(new Func1<ProductViewModel, Observable<Boolean>>() {
                    @Override
                    public Observable<Boolean> call(final ProductViewModel productViewModel) {
                        return submitRawProductUseCase.createObservable(SubmitRawProductUseCase.createParams(productViewModel));
                    }
                })
                .doOnNext(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        notificationCountListener.addProgress();
                    }
                });
    }

    private Observable<ProductViewModel> uploadProductPhoto(final ProductViewModel productViewModel, String productId) {
        return uploadProductImageUseCase.createObservable(UploadProductImageUseCase.createParams(productViewModel, productId, notificationCountListener)).map(new Func1<List<BasePictureViewModel>, ProductViewModel>() {
            @Override
            public ProductViewModel call(List<BasePictureViewModel> basePictureViewModels) {
                return productViewModel;
            }
        });
    }

    private Func1<ProductViewModel, ProductViewModel> removeUnusedParam(final ProductViewModel productFromDraft) {
        return new Func1<ProductViewModel, ProductViewModel>() {
            @Override
            public ProductViewModel call(ProductViewModel productFromServer) {
                return productUploadMapper.convertUnusedParamToNull(productFromServer, productFromDraft);
            }
        };
    }

    public static RequestParams createParams(ProductViewModel productViewModel, ProductSubmitNotificationListener notificationCountListener) {
        RequestParams params = RequestParams.create();
        params.putObject(PRODUCT_VIEW_MODEL, productViewModel);
        params.putObject(NOTIFICATION_COUNT_LISTENER, notificationCountListener);
        return params;
    }
}