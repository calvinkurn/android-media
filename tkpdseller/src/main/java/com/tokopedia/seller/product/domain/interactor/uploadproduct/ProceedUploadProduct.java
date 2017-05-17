package com.tokopedia.seller.product.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.domain.UploadProductRepository;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.model.upload.intdef.ProductStatus;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class ProceedUploadProduct implements Func1<UploadProductInputDomainModel, Observable<AddProductDomainModel>> {
    private final NotificationManager notificationManager;
    private final UploadProductRepository uploadProductRepository;
    private final ImageProductUploadRepository imageProductUploadRepository;
    private final UploadProductUseCase.ProductDraftUpdate draftUpdate;
    public ProceedUploadProduct(NotificationManager notificationManager, UploadProductRepository uploadProductRepository, ImageProductUploadRepository imageProductUploadRepository, UploadProductUseCase.ProductDraftUpdate draftUpdate) {
        this.notificationManager = notificationManager;
        this.uploadProductRepository = uploadProductRepository;
        this.imageProductUploadRepository = imageProductUploadRepository;
        this.draftUpdate = draftUpdate;
    }

    @Override
    public Observable<AddProductDomainModel> call(UploadProductInputDomainModel domainModel) {
        if (domainModel.getProductStatus() == ProductStatus.ADD){
            return Observable.just(domainModel)
                    .flatMap(new AddProductImage(imageProductUploadRepository))
                    .doOnNext(notificationManager.getUpdateNotification())
                    .map(new PrepareAddProductValidation(domainModel))
                    .flatMap(new AddProductValidation(uploadProductRepository))
                    .doOnNext(notificationManager.getUpdateNotification())
                    .flatMap(new ProcessAddProductValidation(domainModel, imageProductUploadRepository, uploadProductRepository))
                    .doOnNext(notificationManager.getUpdateNotification());
        } else if (domainModel.getProductStatus() == ProductStatus.EDIT){
            return Observable.just(domainModel)
                    .flatMap(new UploadImageEditProduct(uploadProductRepository, imageProductUploadRepository, draftUpdate))
                    .doOnNext(notificationManager.getUpdateNotification())
                    .map(new PrepareEditProduct(domainModel))
                    .flatMap(new EditProduct(uploadProductRepository))
                    .doOnNext(notificationManager.getUpdateNotification())
                    .flatMap(new DeleteImageEditProduct(domainModel, uploadProductRepository, draftUpdate))
                    .doOnNext(notificationManager.getUpdateNotification())
                    .map(new ToUploadProductModel(domainModel));
        } else {
            throw new RuntimeException("No product status available");
        }
    }

    private class ToUploadProductModel implements Func1<Boolean, AddProductDomainModel> {
        private final UploadProductInputDomainModel domainModel;

        public ToUploadProductModel(UploadProductInputDomainModel domainModel) {
            this.domainModel = domainModel;
        }

        @Override
        public AddProductDomainModel call(Boolean aBoolean) {
            AddProductDomainModel uploadProductDomainModel = new AddProductDomainModel();
            uploadProductDomainModel.setProductName(domainModel.getProductName());
            uploadProductDomainModel.setProductDesc(domainModel.getProductDescription());
            return uploadProductDomainModel;
        }
    }
}
