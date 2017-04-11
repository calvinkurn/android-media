package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;

import java.net.URI;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface ImageProductRepository {
    Observable<ImageProductInputDomainModel> uploadImageProduct(URI uri);
}
