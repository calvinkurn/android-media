package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef;
import com.tokopedia.seller.product.edit.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.repository.ProductVariantRepository;

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
    public ProceedUploadProduct(NotificationManager notificationManager, UploadProductRepository uploadProductRepository,
                                ImageProductUploadRepository imageProductUploadRepository,
                                UploadProductUseCase.ProductDraftUpdate draftUpdate) {
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
                    .flatMap(new DeleteImageEditProduct(uploadProductRepository, draftUpdate))
                    .map(new ClearImageDeleted())
                    .doOnNext(notificationManager.getUpdateNotification())
                    .flatMap(new UploadImageEditProduct(uploadProductRepository, imageProductUploadRepository, draftUpdate))
                    .doOnNext(notificationManager.getUpdateNotification())
                    .map(new PrepareEditProduct(domainModel))
                    .flatMap(new EditProduct(uploadProductRepository))
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

    private class ClearImageDeleted implements Func1<UploadProductInputDomainModel, UploadProductInputDomainModel> {
        @Override
        public UploadProductInputDomainModel call(UploadProductInputDomainModel domainModel) {
            for (int i = domainModel.getProductPhotos().getPhotos().size() - 1; i >= 0; i --) {
                ImageProductInputDomainModel imageDomainModel = domainModel.getProductPhotos().getPhotos().get(i);
                if (imageDomainModel.getStatus() == ImageStatusTypeDef.ALREADY_DELETED) {
                    domainModel.getProductPhotos().getPhotos().remove(i);
                }
            }
            return domainModel;
        }
    }
}
