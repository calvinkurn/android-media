package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class EditProduct implements Func1<UploadProductInputDomainModel, Observable<Boolean>> {
    private final UploadProductRepository uploadProductRepository;

    public EditProduct(UploadProductRepository uploadProductRepository) {
        this.uploadProductRepository = uploadProductRepository;
    }

    @Override
    public Observable<Boolean> call(UploadProductInputDomainModel editProductInputServiceModel) {
        return uploadProductRepository.editProduct(editProductInputServiceModel);
    }
}