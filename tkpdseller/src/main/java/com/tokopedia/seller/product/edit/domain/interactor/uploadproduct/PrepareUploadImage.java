package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.model.GenerateHostDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */


public class PrepareUploadImage implements Func1<GenerateHostDomainModel, UploadProductInputDomainModel> {

    private final UploadProductInputDomainModel domainModel;

    public PrepareUploadImage(UploadProductInputDomainModel domainModel) {
        this.domainModel = domainModel;
    }

    @Override
    public UploadProductInputDomainModel call(GenerateHostDomainModel generateHost) {
        domainModel.setServerId(generateHost.getServerId());
        domainModel.setHostUrl(generateHost.getUrl());
        return domainModel;
    }
}
