package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.base.domain.interactor.UploadImageUseCase;
import com.tokopedia.seller.product.edit.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.edit.domain.ProductRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class ProceedUploadProduct implements Func1<ProductViewModel, Observable<AddProductDomainModel>> {
    private final NotificationManager notificationManager;
    private final ProductRepository productRepository;
    private UploadImageUseCase<UploadImageModel> uploadImageUseCase;

    public ProceedUploadProduct(NotificationManager notificationManager, ProductRepository productRepository,
                                UploadImageUseCase<UploadImageModel> uploadImageUseCase) {
        this.notificationManager = notificationManager;
        this.productRepository = productRepository;
        this.uploadImageUseCase = uploadImageUseCase;
    }

    @Override
    public Observable<AddProductDomainModel> call(ProductViewModel productViewModel) {
        return Observable.just(productViewModel)
                .flatMap(new AddProductImage(uploadImageUseCase))
                .doOnNext(notificationManager.getUpdateNotification())
                .map(new MergeProductModelWithImage(productViewModel))
                .doOnNext(notificationManager.getUpdateNotification())
                .flatMap(new AddProductSubmit(productRepository))
                .doOnNext(notificationManager.getUpdateNotification());
    }
}
