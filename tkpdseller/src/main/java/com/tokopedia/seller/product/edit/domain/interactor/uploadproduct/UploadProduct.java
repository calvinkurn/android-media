package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.product.edit.data.exception.UploadProductException;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.listener.AddProductNotificationListener;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class UploadProduct implements Func1<ProductViewModel, Observable<AddProductDomainModel>> {
    private final long productId;
    private final AddProductNotificationListener listener;
    private final UploadProductRepository uploadProductRepository;
    private UploadImageUseCase<UploadImageModel> uploadImageUseCase;

    public UploadProduct(long productId, AddProductNotificationListener listener,
                         UploadProductRepository uploadProductRepository, UploadImageUseCase<UploadImageModel> uploadImageUseCase) {
        this.productId = productId;
        this.listener = listener;
        this.uploadProductRepository = uploadProductRepository;
        this.uploadImageUseCase = uploadImageUseCase;
    }

    @Override
    public Observable<AddProductDomainModel> call(final ProductViewModel productViewModel) {
        NotificationManager notificationManager = new NotificationManager(listener, productId, productViewModel.getProductName());
        return Observable.just(productViewModel)
                .doOnNext(notificationManager.getUpdateNotification())
                .flatMap(new ProceedUploadProduct(notificationManager, uploadProductRepository, uploadImageUseCase))
                .onErrorResumeNext(new AddProductStatusToError(productViewModel.getProductStatus()));
    }

    private class AddProductStatusToError implements Func1<Throwable, Observable<? extends AddProductDomainModel>> {
        @ProductStatus
        private int productStatus;

        public AddProductStatusToError(@ProductStatus int productStatus) {
            this.productStatus = productStatus;
        }

        @Override
        public Observable<AddProductDomainModel> call(Throwable throwable) {
            throw new UploadProductException(String.valueOf(productId), productStatus, throwable);
        }
    }
}
