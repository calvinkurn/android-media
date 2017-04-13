package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.data.source.cloud.model.ResultUploadImage;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ImageProductUploadRepository {
    Observable<ImageProductInputDomainModel> uploadImageProduct(String uri);
}
