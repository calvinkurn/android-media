package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.data.source.cloud.model.UploadImageModel;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;

import java.net.URI;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ImageProductUploadRepository {
    Observable<UploadImageModel.Result> uploadImageProduct(String uri);
}
