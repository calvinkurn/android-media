package com.tokopedia.seller.product.edit.domain;

import com.tokopedia.seller.product.edit.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProcessDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ImageProductUploadRepository {
    Observable<ImageProcessDomainModel> uploadImageProduct(String hostUrl, int serverId, String imagePath, int productId);

    Observable<AddProductPictureDomainModel> addProductPicture(UploadProductInputDomainModel domainModel);
}
