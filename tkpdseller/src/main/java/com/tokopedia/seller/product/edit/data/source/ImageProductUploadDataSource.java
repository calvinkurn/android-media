package com.tokopedia.seller.product.edit.data.source;

import com.tokopedia.seller.product.edit.data.source.cloud.ImageProductUploadDataSourceCloud;
import com.tokopedia.seller.product.edit.data.source.cloud.model.AddProductPictureInputServiceModel;
import com.tokopedia.seller.product.edit.data.source.cloud.model.ResultUploadImage;
import com.tokopedia.seller.product.edit.data.source.cloud.model.addproductpicture.AddProductPictureServiceModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 3/21/17.
 */

public class ImageProductUploadDataSource {
    private final ImageProductUploadDataSourceCloud imageProductUploadDataSourceCloud;

    @Inject
    public ImageProductUploadDataSource(ImageProductUploadDataSourceCloud imageProductUploadDataSourceCloud) {
        this.imageProductUploadDataSourceCloud = imageProductUploadDataSourceCloud;
    }

    public Observable<ResultUploadImage> uploadImage(final String hostUrl, int serverId, final String imagePath, int productId) {
        return imageProductUploadDataSourceCloud.uploadImage(hostUrl, serverId, imagePath, productId);
    }

    public Observable<AddProductPictureServiceModel> addProductPicture(AddProductPictureInputServiceModel serviceModel) {
        return imageProductUploadDataSourceCloud.addProductPicture(serviceModel);
    }
}
