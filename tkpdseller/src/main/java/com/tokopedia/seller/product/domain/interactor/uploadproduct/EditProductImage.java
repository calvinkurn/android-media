package com.tokopedia.seller.product.domain.interactor.uploadproduct;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.product.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.domain.UploadProductRepository;
import com.tokopedia.seller.product.domain.model.EditImageProductDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProcessDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class EditProductImage implements Func1<UploadProductInputDomainModel, Observable<List<ImageProductInputDomainModel>>> {
    private String hostUrl;
    private String productId;
    private int serverId;
    private final UploadProductRepository uploadProductRepository;
    private final ImageProductUploadRepository imageProductUploadRepository;

    public EditProductImage(UploadProductRepository uploadProductRepository, ImageProductUploadRepository imageProductUploadRepository) {
        this.uploadProductRepository = uploadProductRepository;
        this.imageProductUploadRepository = imageProductUploadRepository;
    }

    @Override
    public Observable<List<ImageProductInputDomainModel>> call(UploadProductInputDomainModel domainModel) {
        hostUrl = domainModel.getHostUrl();
        serverId = domainModel.getServerId();
        productId = domainModel.getProductId();
        return Observable.from(domainModel.getProductPhotos().getPhotos())
                .flatMap(new EditProductSigleImage())
                .filter(new Func1<ImageProductInputDomainModel, Boolean>() {
                    @Override
                    public Boolean call(ImageProductInputDomainModel imageProductInputDomainModel) {
                        return imageProductInputDomainModel != null && StringUtils.isNotBlank(imageProductInputDomainModel.getPicId());
                    }
                })
                .toList();
    }

    private class EditProductSigleImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {
        @Override
        public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel domainModel) {
            if (StringUtils.isNotBlank(domainModel.getImagePath())){
                return Observable
                        .just(domainModel)
                        .flatMap(new UploadNewImage());
            } else if (StringUtils.isBlank(domainModel.getUrl())){
                return Observable
                        .just(domainModel)
                        .flatMap(new DeleteImage());
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
                    return this.domainModel;
                }
            }
        }

        private class DeleteImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {
            @Override
            public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel domainModel) {
                return uploadProductRepository.deleteProductPicture(domainModel.getPicId(), productId);
            }
        }
    }
}