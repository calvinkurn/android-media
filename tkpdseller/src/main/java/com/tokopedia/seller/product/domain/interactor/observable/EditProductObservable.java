package com.tokopedia.seller.product.domain.interactor.observable;

import com.tokopedia.seller.product.domain.UploadProductRepository;
import com.tokopedia.seller.product.domain.model.EditProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class EditProductObservable implements Func1<UploadProductInputDomainModel, Observable<EditProductDomainModel>> {
    private final UploadProductRepository uploadProductRepository;

    public EditProductObservable(UploadProductRepository uploadProductRepository) {
        this.uploadProductRepository = uploadProductRepository;
    }

    @Override
    public Observable<EditProductDomainModel> call(UploadProductInputDomainModel uploadProductInputDomainModel) {
        return uploadProductRepository.editProduct(uploadProductInputDomainModel);
    }
}
