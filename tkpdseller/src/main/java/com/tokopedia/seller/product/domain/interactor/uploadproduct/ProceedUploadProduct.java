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
    private final String productId;
    private final NotificationManager notificationManager;
    private final UploadProductRepository uploadProductRepository;
    private final ImageProductUploadRepository imageProductUploadRepository;

    public ProceedUploadProduct(String productId, NotificationManager notificationManager, UploadProductRepository uploadProductRepository, ImageProductUploadRepository imageProductUploadRepository) {
        this.productId = productId;
        this.notificationManager = notificationManager;
        this.uploadProductRepository = uploadProductRepository;
        this.imageProductUploadRepository = imageProductUploadRepository;
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
                    .flatMap(new UploadImageEditProduct(uploadProductRepository, imageProductUploadRepository))
                    .doOnNext(notificationManager.getUpdateNotification())
                    .map(new PrepareEditProduct(domainModel))
                    .flatMap(new EditProduct(uploadProductRepository))
                    .flatMap(new DeleteImageEditProduct(domainModel, uploadProductRepository))
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
