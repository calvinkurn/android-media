package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef;
import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/15/17.
 */

class DeleteImageEditProduct implements Func1<UploadProductInputDomainModel, Observable<UploadProductInputDomainModel>> {
    private final UploadProductRepository uploadProductRepository;
    private final UploadProductUseCase.ProductDraftUpdate draftUpdate;

    public DeleteImageEditProduct(UploadProductRepository uploadProductRepository, UploadProductUseCase.ProductDraftUpdate draftUpdate) {
        this.uploadProductRepository = uploadProductRepository;
        this.draftUpdate = draftUpdate;
    }

    @Override
    public Observable<UploadProductInputDomainModel> call(UploadProductInputDomainModel domainModel) {
        return Observable.from(domainModel.getProductPhotos().getPhotos())
                .flatMap(new DeleteProductSingleImage(uploadProductRepository, domainModel.getProductId()))
                .doOnNext(new UpdateDraft(domainModel, draftUpdate))
                .toList()
                .map(new InsertToModel(domainModel));
    }

    private static class DeleteProductSingleImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {
        private final UploadProductRepository uploadProductRepository;
        private final String productId;

        private DeleteProductSingleImage(UploadProductRepository uploadProductRepository, String productId) {
            this.uploadProductRepository = uploadProductRepository;
            this.productId = productId;
        }

        @Override
        public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel imageProductInputDomainModel) {
            if (imageProductInputDomainModel.getStatus() == ImageStatusTypeDef.WILL_BE_DELETED) {
                return Observable
                        .just(imageProductInputDomainModel)
                        .flatMap(new DeleteImage(uploadProductRepository, productId))
                        .map(new UpdateModel(imageProductInputDomainModel));
            }
            return Observable.just(imageProductInputDomainModel);
        }

        private static class UpdateModel implements Func1<ImageProductInputDomainModel, ImageProductInputDomainModel> {
            private final ImageProductInputDomainModel domainModel;

            public UpdateModel(ImageProductInputDomainModel domainModel) {
                this.domainModel = domainModel;
            }

            @Override
            public ImageProductInputDomainModel call(ImageProductInputDomainModel imageProductInputDomainModel) {
                domainModel.setStatus(ImageStatusTypeDef.ALREADY_DELETED);
                return domainModel;
            }
        }
    }

    private static class DeleteImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {
        private final UploadProductRepository uploadProductRepository;

        private final String productId;

        private DeleteImage(UploadProductRepository uploadProductRepository, String productId) {
            this.uploadProductRepository = uploadProductRepository;
            this.productId = productId;
        }

        @Override
        public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel domainModel) {
            return uploadProductRepository.deleteProductPicture(domainModel.getPicId(), productId);
        }

    }

    private static class UpdateDraft implements Action1<ImageProductInputDomainModel> {
        private final UploadProductInputDomainModel domainModel;
        private final UploadProductUseCase.ProductDraftUpdate draftUpdate;

        public UpdateDraft(UploadProductInputDomainModel domainModel, UploadProductUseCase.ProductDraftUpdate draftUpdate) {
            this.domainModel = domainModel;
            this.draftUpdate = draftUpdate;
        }

        @Override
        public void call(ImageProductInputDomainModel imageProductInputDomainModel) {
            draftUpdate.updateDraft(domainModel);
        }
    }

    private class InsertToModel implements Func1<List<ImageProductInputDomainModel>, UploadProductInputDomainModel> {
        private final UploadProductInputDomainModel domainModel;

        public InsertToModel(UploadProductInputDomainModel domainModel) {
            this.domainModel = domainModel;
        }

        @Override
        public UploadProductInputDomainModel call(List<ImageProductInputDomainModel> imageProductInputDomainModels) {
            ProductPhotoListDomainModel productPhotos = domainModel.getProductPhotos();
            productPhotos.setPhotos(imageProductInputDomainModels);
            return domainModel;
        }
    }
}
