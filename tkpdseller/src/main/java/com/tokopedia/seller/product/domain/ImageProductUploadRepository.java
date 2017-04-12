package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.data.source.cloud.model.ResultUploadImage;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ImageProductUploadRepository {
    Observable<ResultUploadImage> uploadImageProduct(String uri);
}
