package com.tokopedia.seller.product.edit.data.repository;


import com.tokopedia.seller.product.edit.data.mapper.AddProductPictureMapper;
import com.tokopedia.seller.product.edit.data.mapper.UploadImageMapper;
import com.tokopedia.seller.product.edit.data.mapper.UploadProductPictureInputMapper;
import com.tokopedia.seller.product.edit.data.source.ImageProductUploadDataSource;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.edit.domain.ImageProductUploadRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProcessDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */
@Deprecated
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
        uploadProductPictureInputMapper.mapProductPhoto(serviceModel, domainModel);
        return imageProductUploadDataSource.addProductPicture(serviceModel)
                .map(new AddProductPictureMapper());
    }


}
