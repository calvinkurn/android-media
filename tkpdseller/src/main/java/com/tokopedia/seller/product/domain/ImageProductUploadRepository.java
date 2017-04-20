package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProcessDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ImageProductUploadRepository {
    Observable<ImageProcessDomainModel> uploadImageProduct(String hostUrl, int serverId, String imagePath, int productId);

    Observable<AddProductPictureDomainModel> addProductPicture(AddProductPictureInputDomainModel addProductPictureInputDomainModel);
}
