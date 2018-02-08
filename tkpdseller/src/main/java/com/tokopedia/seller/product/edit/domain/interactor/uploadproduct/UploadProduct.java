package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.data.exception.UploadProductException;
import com.tokopedia.seller.product.edit.domain.GenerateHostRepository;
import com.tokopedia.seller.product.edit.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.listener.AddProductNotificationListener;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class UploadProduct implements Func1<UploadProductInputDomainModel, Observable<AddProductDomainModel>> {
    private final long productId;
    private final AddProductNotificationListener listener;
    private final GenerateHostRepository generateHostRepository;
    private final UploadProductRepository uploadProductRepository;
    private final ImageProductUploadRepository imageProductUploadRepository;
    private final UploadProductUseCase.ProductDraftUpdate draftUpdate;

    public UploadProduct(long productId, AddProductNotificationListener listener,
                         GenerateHostRepository generateHostRepository,
                         UploadProductRepository uploadProductRepository,
                         ImageProductUploadRepository imageProductUploadRepository,
                         UploadProductUseCase.ProductDraftUpdate draftUpdate) {
        this.productId = productId;
        this.listener = listener;
        this.generateHostRepository = generateHostRepository;
        this.uploadProductRepository = uploadProductRepository;
        this.imageProductUploadRepository = imageProductUploadRepository;
        this.draftUpdate = draftUpdate;
    }

    @Override
    public Observable<AddProductDomainModel> call(final UploadProductInputDomainModel domainModel) {
        NotificationManager notificationManager = new NotificationManager(listener, productId, domainModel.getProductName());
        return Observable.just(domainModel)
                .flatMap(new GetGeneratedHost(generateHostRepository))
                .doOnNext(notificationManager.getUpdateNotification())
                .map(new PrepareUploadImage(domainModel))
                .flatMap(new ProceedUploadProduct(notificationManager, uploadProductRepository, imageProductUploadRepository, draftUpdate))
                .onErrorResumeNext(new AddProductStatusToError(domainModel.getProductStatus()));
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
