package com.tokopedia.seller.product.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.constant.ProductNetworkConstant;
import com.tokopedia.seller.product.domain.UploadProductRepository;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/15/17.
 */

class DeleteImageEditProduct implements Func1<Boolean, Observable<Boolean>> {
    private final UploadProductInputDomainModel domainModel;
    private final UploadProductRepository uploadProductRepository;

    public DeleteImageEditProduct(UploadProductInputDomainModel domainModel, UploadProductRepository uploadProductRepository) {
        this.domainModel = domainModel;
        this.uploadProductRepository = uploadProductRepository;
    }

    @Override
    public Observable<Boolean> call(Boolean aBoolean) {
        return Observable.from(domainModel.getProductPhotos().getPhotos())
                .flatMap(new EditProductSigleImage(uploadProductRepository, domainModel.getProductId()))
                .toList()
                .map(new Func1<List<ImageProductInputDomainModel>, Boolean>() {
                    @Override
                    public Boolean call(List<ImageProductInputDomainModel> objects) {
                        return true;
                    }
                });
    }

    private static class EditProductSigleImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {
        private final UploadProductRepository uploadProductRepository;
        private final String productId;

        private EditProductSigleImage(UploadProductRepository uploadProductRepository, String productId) {
            this.uploadProductRepository = uploadProductRepository;
            this.productId = productId;
        }

        @Override
        public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel imageProductInputDomainModel) {
            if (imageProductInputDomainModel.getUrl() != null &&
                    imageProductInputDomainModel.getUrl().equals(ProductNetworkConstant.IMAGE_STATUS_DELETED)) {
                return Observable
                        .just(imageProductInputDomainModel)
                        .flatMap(new DeleteImage(uploadProductRepository, productId));
            }
            return Observable.just(imageProductInputDomainModel);
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
}
