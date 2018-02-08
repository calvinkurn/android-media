package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductSubmitInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */


public class AddProductSubmit implements Func1<AddProductSubmitInputDomainModel, Observable<AddProductDomainModel>> {

    private final UploadProductRepository uploadProductRepository;

    public AddProductSubmit(UploadProductRepository uploadProductRepository) {
        this.uploadProductRepository = uploadProductRepository;
    }

    @Override
    public Observable<AddProductDomainModel> call(AddProductSubmitInputDomainModel addProductSubmitInputDomainModel) {
        return uploadProductRepository
                .addProductSubmit(addProductSubmitInputDomainModel);
    }

}