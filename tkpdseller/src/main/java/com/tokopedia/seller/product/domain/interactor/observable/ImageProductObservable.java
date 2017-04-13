package com.tokopedia.seller.product.domain.interactor.observable;

import android.support.annotation.NonNull;

import com.tokopedia.seller.product.domain.ImageProductRepository;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class ImageProductObservable implements Func1<UploadProductInputDomainModel,
        Observable<UploadProductInputDomainModel>> {
    private final ImageProductRepository imageProductRepository;

    public ImageProductObservable(ImageProductRepository imageProductRepository) {
        this.imageProductRepository = imageProductRepository;
    }

    @Override
    public Observable<UploadProductInputDomainModel> call(
            UploadProductInputDomainModel uploadProductInputDomainModel
    ) {
        Observable<List<ImageProductInputDomainModel>> imageResult = Observable
                .from(uploadProductInputDomainModel.getProductPhotos().getPhotos())
                .flatMap(new CheckImageHasUrl())
                .toList();
        return storeImagesToModel(uploadProductInputDomainModel, imageResult);
    }

    private class CheckImageHasUrl
            implements Func1<ImageProductInputDomainModel,
            Observable<ImageProductInputDomainModel>> {
        @Override
        public Observable<ImageProductInputDomainModel> call(
                ImageProductInputDomainModel imageDomainModel
        ) {
            if (imageDomainModel.getUrl() != null && !imageDomainModel.getUrl().isEmpty()) {
                return Observable.just(imageDomainModel);
            } else {
                return imageProductRepository.uploadImageProduct(imageDomainModel.getUri());
            }

        }
    }

    @NonNull
    private Observable<UploadProductInputDomainModel> storeImagesToModel(
            UploadProductInputDomainModel uploadProductInputDomainModel,
            Observable<List<ImageProductInputDomainModel>> imageResult
    ) {
        return Observable
                .zip(
                        Observable.just(uploadProductInputDomainModel),
                        imageResult,
                        new Func2<UploadProductInputDomainModel,
                                List<ImageProductInputDomainModel>,
                                UploadProductInputDomainModel>()
                        {
                            @Override
                            public UploadProductInputDomainModel call(
                                    UploadProductInputDomainModel uploadProductInputDomainModel,
                                    List<ImageProductInputDomainModel> imageProductInputDomainModels
                            ) {
                                ProductPhotoListDomainModel productPhotos = new ProductPhotoListDomainModel();
                                productPhotos.setPhotos(imageProductInputDomainModels);
                                uploadProductInputDomainModel.setProductPhotos(productPhotos);
                                return uploadProductInputDomainModel;
                            }
                        });
    }

}

