package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.core.base.utils.StringUtils;
import com.tokopedia.seller.product.edit.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.edit.domain.model.ImageProcessDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class AddProductImage implements Func1<UploadProductInputDomainModel, Observable<List<ImageProductInputDomainModel>>> {

    private final ImageProductUploadRepository imageProductUploadRepository;

    public AddProductImage(ImageProductUploadRepository imageProductUploadRepository) {
        this.imageProductUploadRepository = imageProductUploadRepository;
    }

    @Override
    public Observable<List<ImageProductInputDomainModel>> call(UploadProductInputDomainModel domainModel) {
        return Observable.from(domainModel.getProductPhotos().getPhotos())
                .flatMap(new UploadSingleImage(domainModel.getServerId(), domainModel.getHostUrl()))
                .toList();
    }

    private class UploadSingleImage implements Func1<ImageProductInputDomainModel, Observable<ImageProductInputDomainModel>> {

        private final int serverId;

        private final String hostUrl;

        public UploadSingleImage(int serverId, String hostUrl) {
            this.serverId = serverId;
            this.hostUrl = hostUrl;
        }

        @Override
        public Observable<ImageProductInputDomainModel> call(ImageProductInputDomainModel imageProductInputDomainModel) {
            if (StringUtils.isNotBlank(imageProductInputDomainModel.getImagePath())) {
                return imageProductUploadRepository.uploadImageProduct(
                        hostUrl,
                        serverId,
                        imageProductInputDomainModel.getImagePath(),
                        0)
                        .map(new MapImageModelToProductInput(imageProductInputDomainModel));
            } else {
                return Observable.just(imageProductInputDomainModel);
            }
        }

        private class MapImageModelToProductInput implements Func1<ImageProcessDomainModel, ImageProductInputDomainModel> {

            private final ImageProductInputDomainModel inputDomainModel;

            public MapImageModelToProductInput(ImageProductInputDomainModel inputDomainModel) {
                this.inputDomainModel = inputDomainModel;
            }

            @Override
            public ImageProductInputDomainModel call(ImageProcessDomainModel uploadImageDomainModel) {
                inputDomainModel.setUrl(uploadImageDomainModel.getUrl());
                return inputDomainModel;
            }

        }
    }

}
