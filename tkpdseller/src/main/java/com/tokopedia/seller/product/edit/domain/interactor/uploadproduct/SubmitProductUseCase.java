package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import android.text.TextUtils;

import com.tokopedia.seller.product.edit.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.seller.product.edit.domain.listener.ProductSubmitNotificationListener;
import com.tokopedia.seller.product.edit.domain.mapper.ProductUploadMapper;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
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

    private final GetProductDetailUseCase getProductDetailUseCase;
    private final UploadProductImageUseCase uploadProductImageUseCase;
    private final SubmitRawProductUseCase submitProductUseCase;
    private final ProductUploadMapper productUploadMapper;

    private ProductSubmitNotificationListener notificationCountListener;

    @Inject
    public SubmitProductUseCase(
            GetProductDetailUseCase getProductDetailUseCase,
            UploadProductImageUseCase uploadProductImageUseCase,
            SubmitRawProductUseCase submitProductUseCase,
            ProductUploadMapper productUploadMapper) {
        this.getProductDetailUseCase = getProductDetailUseCase;
        this.uploadProductImageUseCase = uploadProductImageUseCase;
        this.submitProductUseCase = submitProductUseCase;
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
                        return uploadProductImageUseCase.createObservable(UploadProductImageUseCase.createParams(productViewModel, notificationCountListener))
                                .map(new Func1<List<ProductPictureViewModel>, ProductViewModel>() {
                                    @Override
                                    public ProductViewModel call(List<ProductPictureViewModel> productPictureViewModelList) {
                                        productViewModel.setProductPictureViewModelList(productPictureViewModelList);
                                        return productViewModel;
                                    }
                                });
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
                        return submitProductUseCase.createObservable(SubmitRawProductUseCase.createParams(productViewModel));
                    }
                })
                .doOnNext(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        notificationCountListener.addProgress();
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