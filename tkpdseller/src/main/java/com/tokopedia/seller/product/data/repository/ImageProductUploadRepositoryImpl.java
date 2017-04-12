package com.tokopedia.seller.product.data.repository;


import com.tokopedia.seller.product.data.source.cloud.model.ResultUploadImage;
import com.tokopedia.seller.product.data.source.ImageProductUploadDataSource;
import com.tokopedia.seller.product.domain.ImageProductUploadRepository;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ImageProductUploadRepositoryImpl implements ImageProductUploadRepository {
    ImageProductUploadDataSource imageProductUploadDataSource;

    public ImageProductUploadRepositoryImpl(ImageProductUploadDataSource imageProductUploadDataSource) {
        this.imageProductUploadDataSource = imageProductUploadDataSource;
    }

    @Override
    public Observable<ResultUploadImage> uploadImageProduct(String imagePath) {
        return imageProductUploadDataSource.uploadImage(imagePath);
    }
}
