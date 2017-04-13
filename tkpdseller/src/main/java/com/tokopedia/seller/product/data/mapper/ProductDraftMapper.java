package com.tokopedia.seller.product.data.mapper;

import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftMapper implements Func1<String, UploadProductInputDomainModel> {
    @Override
    public UploadProductInputDomainModel call(String json) {
        return CacheUtil.convertStringToModel(
                json,
                UploadProductInputDomainModel.class
        );
    }

    public static String mapFromDomain(UploadProductInputDomainModel domainModel) {
        return CacheUtil.convertModelToString(domainModel, UploadProductInputDomainModel.class);
    }
}
