package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.abstraction.common.data.model.session.UserSession;
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
    private UserSession userSession;

    public UploadProduct(long productId, AddProductNotificationListener listener,
                         UploadProductRepository uploadProductRepository, UploadImageUseCase<UploadImageModel> uploadImageUseCase,
                         UserSession userSession) {
        this.productId = productId;
        this.listener = listener;
        this.uploadProductRepository = uploadProductRepository;
        this.uploadImageUseCase = uploadImageUseCase;
        this.userSession = userSession;
    }

    @Override
    public Observable<AddProductDomainModel> call(final ProductViewModel productViewModel) {
        NotificationManager notificationManager = new NotificationManager(listener, productId, productViewModel.getProductName());
        return Observable.just(productViewModel)
                .doOnNext(notificationManager.getUpdateNotification())
                .flatMap(new ProceedUploadProduct(notificationManager, uploadProductRepository, uploadImageUseCase, userSession))
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends AddProductDomainModel>>() {
                    @Override
                    public Observable<? extends AddProductDomainModel> call(Throwable throwable) {
                        throw new UploadProductException(String.valueOf(productId), throwable);
                    }
                });
    }

}
