package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.constant.ImageStatusTypeDef;
import com.tokopedia.seller.product.edit.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.model.EditImageProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProcessDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class UploadImageEditProduct implements Func1<UploadProductInputDomainModel, Observable<List<ImageProductInputDomainModel>>> {
    private String hostUrl;
    private String productId;
    private int serverId;
    private final UploadProductRepository uploadProductRepository;
    private final ImageProductUploadRepository imageProductUploadRepository;
    private final UploadProductUseCase.ProductDraftUpdate draftUpdate;

    public UploadImageEditProduct(UploadProductRepository uploadProductRepository, ImageProductUploadRepository imageProductUploadRepository, UploadProductUseCase.ProductDraftUpdate draftUpdate) {
        this.uploadProductRepository = uploadProductRepository;
        this.imageProductUploadRepository = imageProductUploadRepository;
        this.draftUpdate = draftUpdate;
    }

    @Override
    public Observable<List<ImageProductInputDomainModel>> call(UploadProductInputDomainModel domainModel) {
        hostUrl = domainModel.getHostUrl();
        serverId = domainModel.getServerId();
        productId = domainModel.getProductId();
        return Observable.from(domainModel.getProductPhotos().getPhotos())
                .flatMap(new EditProductSigleImage())
                .doOnNext(new UpdateDraft(domainModel, draftUpdate))
                .toList();
    }

    private class EditProductSigleImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {
        @Override
        public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel domainModel) {
            if (domainModel.getStatus() == ImageStatusTypeDef.WILL_BE_UPLOADED){
                return Observable
                        .just(domainModel)
                        .flatMap(new UploadNewImage());
            }
            return Observable.just(domainModel);
        }

        private class UploadNewImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {

            @Override
            public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel domainModel) {
                return imageProductUploadRepository.uploadImageProduct(
                        hostUrl,
                        serverId,
                        domainModel.getImagePath(),
                        Integer.parseInt(productId))
                        .flatMap(new EditProductPicture())
                        .map(new CreateImageModel(domainModel));

            }

            private class EditProductPicture implements Func1<ImageProcessDomainModel, Observable<EditImageProductDomainModel>> {
                @Override
                public Observable<EditImageProductDomainModel> call(ImageProcessDomainModel imageProcessDomainModel) {
                    return uploadProductRepository.editImageProduct(imageProcessDomainModel.getPicObj());
                }
            }

            private class CreateImageModel implements Func1<EditImageProductDomainModel, ImageProductInputDomainModel> {
                private final ImageProductInputDomainModel domainModel;

                public CreateImageModel(ImageProductInputDomainModel domainModel) {
                    this.domainModel = domainModel;
                }

                @Override
                public ImageProductInputDomainModel call(EditImageProductDomainModel domainModel) {
                    this.domainModel.setPicId(domainModel.getPicId());
                    this.domainModel.setStatus(ImageStatusTypeDef.ALREADY_UPLOADED);
                    return this.domainModel;
                }
            }
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
}