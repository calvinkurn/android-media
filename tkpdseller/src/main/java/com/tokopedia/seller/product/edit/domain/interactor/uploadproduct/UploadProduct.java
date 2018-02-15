package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.product.edit.data.exception.UploadProductException;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.edit.domain.ProductRepository;
import com.tokopedia.seller.product.edit.domain.listener.AddProductNotificationListener;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class UploadProduct implements Func1<ProductViewModel, Observable<AddProductDomainModel>> {
    private final long productId;
    private final AddProductNotificationListener listener;
    private final ProductRepository productRepository;
    private UploadImageUseCase<UploadImageModel> uploadImageUseCase;

    public UploadProduct(long productId, AddProductNotificationListener listener,
                         ProductRepository productRepository, UploadImageUseCase<UploadImageModel> uploadImageUseCase) {
        this.productId = productId;
        this.listener = listener;
        this.productRepository = productRepository;
        this.uploadImageUseCase = uploadImageUseCase;
    }

    @Override
    public Observable<AddProductDomainModel> call(final ProductViewModel productViewModel) {
        NotificationManager notificationManager = new NotificationManager(listener, productId, productViewModel.getProductName());
        return Observable.just(productViewModel)
                .doOnNext(notificationManager.getUpdateNotification())
                .flatMap(new ProceedUploadProduct(notificationManager, productRepository, uploadImageUseCase))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends AddProductDomainModel>>() {
                    @Override
                    public Observable<? extends AddProductDomainModel> call(Throwable throwable) {
                        throw new UploadProductException(String.valueOf(productId), throwable);
                    }
                });
    }

}
