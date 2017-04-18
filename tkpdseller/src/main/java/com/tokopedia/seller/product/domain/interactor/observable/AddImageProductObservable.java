package com.tokopedia.seller.product.domain.interactor.observable;

import android.support.annotation.NonNull;

import com.tokopedia.seller.product.data.source.cloud.model.ResultUploadImage;
import com.tokopedia.seller.product.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.domain.model.ImageProcessDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class AddImageProductObservable implements Func1<UploadProductInputDomainModel,
        Observable<UploadProductInputDomainModel>> {
    private final ImageProductUploadRepository imageProductUploadRepository;

    @Inject
    public AddImageProductObservable(ImageProductUploadRepository imageProductUploadRepository) {
        this.imageProductUploadRepository = imageProductUploadRepository;
    }

    @Override
    public Observable<UploadProductInputDomainModel> call(
            UploadProductInputDomainModel uploadProductInputDomainModel
    ) {
        Observable<List<ImageProcessDomainModel>> imageResult = Observable
                .from(uploadProductInputDomainModel.getProductPhotos().getPhotos())
                .flatMap(new CheckImageHasUrl())
                .toList();
        return storeImagesToModel(uploadProductInputDomainModel, imageResult);
    }

    private class CheckImageHasUrl
            implements Func1<ImageProductInputDomainModel,
            Observable<ImageProcessDomainModel>> {
        @Override
        public Observable<ImageProcessDomainModel> call(
                ImageProductInputDomainModel imageDomainModel
        ) {
            return imageProductUploadRepository.uploadImageProduct(imageDomainModel.getImagePath(), "");
        }
    }

    @NonNull
    private Observable<UploadProductInputDomainModel> storeImagesToModel(
            UploadProductInputDomainModel uploadProductInputDomainModel,
            Observable<List<ImageProcessDomainModel>> imageResult
    ) {
        return Observable
                .zip(
                        Observable.just(uploadProductInputDomainModel),
                        imageResult,
                        new Func2<UploadProductInputDomainModel,
                                List<ImageProcessDomainModel>,
                                UploadProductInputDomainModel>() {
                            @Override
                            public UploadProductInputDomainModel call(
                                    UploadProductInputDomainModel uploadProductInputDomainModel,
                                    List<ImageProcessDomainModel> imageProductInputDomainModels
                            ) {
                                if (!imageProductInputDomainModels.isEmpty()) {
                                    uploadProductInputDomainModel.setServerId(imageProductInputDomainModels.get(0).getServerId());
                                    ProductPhotoListDomainModel productPhotos = uploadProductInputDomainModel.getProductPhotos();
                                    productPhotos.setPhotos(mapImageProcess(imageProductInputDomainModels));
                                    uploadProductInputDomainModel.setProductPhotos(productPhotos);
                                }
                                return uploadProductInputDomainModel;
                            }
                        });
    }

    private List<ImageProductInputDomainModel> mapImageProcess(List<ImageProcessDomainModel> imageProductInputDomainModels) {
        List<ImageProductInputDomainModel> inputDomainModels = new ArrayList<>();
        for (ImageProcessDomainModel domainModel : imageProductInputDomainModels) {
            ImageProductInputDomainModel inputDomainModel = new ImageProductInputDomainModel();
            inputDomainModel.setUrl(domainModel.getUrl());
            inputDomainModels.add(inputDomainModel);
        }
        return inputDomainModels;
    }

}

