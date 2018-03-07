package com.tokopedia.inbox.attachproduct.data.repository;

import com.tokopedia.inbox.attachproduct.domain.model.AttachProductDomainModel;

import rx.Observable;

/**
 * Created by Hendri on 13/02/18.
 */

public interface AttachProductRepository {
    Observable<AttachProductDomainModel> loadProductFromShop(String query, String shopId, int page);
}