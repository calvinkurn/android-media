package com.tokopedia.seller.product.data.repository;


import com.tokopedia.seller.product.data.mapper.AddProductPictureMapper;
import com.tokopedia.seller.product.data.mapper.UploadImageMapper;
import com.tokopedia.seller.product.data.mapper.UploadProductPictureInputMapper;
import com.tokopedia.seller.product.data.source.ImageProductUploadDataSource;
import com.tokopedia.seller.product.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProcessDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ImageProductUploadRepositoryImpl implements ImageProductUploadRepository {
    private final ImageProductUploadDataSource imageProductUploadDataSource;
    private final UploadProductPictureInputMapper uploadProductPictureInputMapper;

    public ImageProductUploadRepositoryImpl(ImageProductUploadDataSource imageProductUploadDataSource,
                                            UploadProductPictureInputMapper uploadProductPictureInputMapper) {
        this.imageProductUploadDataSource = imageProductUploadDataSource;
        this.uploadProductPictureInputMapper = uploadProductPictureInputMapper;
    }

    @Override
    public Observable<ImageProcessDomainModel> uploadImageProduct(String hostUrl, int serverId, String imagePath, int productId) {
        return imageProductUploadDataSource.uploadImage(hostUrl, serverId, imagePath, productId)
                .map(new UploadImageMapper());
    }

    @Override
    public Observable<AddProductPictureDomainModel> addProductPicture(UploadProductInputDomainModel domainModel) {
        AddProductPictureInputServiceModel serviceModel = new AddProductPictureInputServiceModel();
        uploadProductPictureInputMapper.map(serviceModel, domainModel);
        return imageProductUploadDataSource.addProductPicture(serviceModel)
                .map(new AddProductPictureMapper());
    }


}
