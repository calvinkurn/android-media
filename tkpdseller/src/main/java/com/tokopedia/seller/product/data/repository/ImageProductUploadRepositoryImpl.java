package com.tokopedia.seller.product.data.repository;


import com.tokopedia.seller.product.data.mapper.AddProductInputMapper;
import com.tokopedia.seller.product.data.mapper.AddProductPictureMapper;
import com.tokopedia.seller.product.data.mapper.UploadImageMapper;
import com.tokopedia.seller.product.data.source.ImageProductUploadDataSource;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProcessDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ImageProductUploadRepositoryImpl implements ImageProductUploadRepository {
    private final ImageProductUploadDataSource imageProductUploadDataSource;

    public ImageProductUploadRepositoryImpl(ImageProductUploadDataSource imageProductUploadDataSource) {
        this.imageProductUploadDataSource = imageProductUploadDataSource;
    }

    @Override
    public Observable<ImageProcessDomainModel> uploadImageProduct(String hostUrl, int serverId, String imagePath, int productId) {
        return imageProductUploadDataSource.uploadImage(hostUrl, serverId, imagePath, productId)
                .map(new UploadImageMapper());
    }

    @Override
    public Observable<AddProductPictureDomainModel> addProductPicture(AddProductPictureInputDomainModel domainModel) {
        AddProductPictureInputServiceModel serviceModel = AddProductInputMapper.mapPicture(domainModel);
        return imageProductUploadDataSource.addProductPicture(serviceModel)
                .map(new AddProductPictureMapper());
    }
}
