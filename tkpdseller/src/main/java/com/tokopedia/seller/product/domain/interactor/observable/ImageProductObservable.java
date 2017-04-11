package com.tokopedia.seller.product.domain.interactor.observable;

import android.support.annotation.NonNull;

import com.tokopedia.seller.product.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
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
    private final ImageProductUploadRepository imageProductUploadRepository;

    public ImageProductObservable(ImageProductUploadRepository imageProductUploadRepository) {
        this.imageProductUploadRepository = imageProductUploadRepository;
    }

    @Override
    public Observable<UploadProductInputDomainModel> call(
            UploadProductInputDomainModel uploadProductInputDomainModel
    ) {
        Observable<List<ImageProductInputDomainModel>> imageResult = Observable
                .from(uploadProductInputDomainModel.getImages())
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
                return imageProductUploadRepository.uploadImageProduct(imageDomainModel.getImagePath())
                        .flatMap(new Func1<UploadImageModel.Result, Observable<ImageProductInputDomainModel>>() {
                            @Override
                            public Observable<ImageProductInputDomainModel> call(UploadImageModel.Result data) {
                                return null;
                            }
                        });
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
                                uploadProductInputDomainModel.setImages(imageProductInputDomainModels);
                                return uploadProductInputDomainModel;
                            }
                        });
    }

}

