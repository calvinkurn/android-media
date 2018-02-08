package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.repository.ProductVariantRepository;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */

public class AddProductValidation implements Func1<UploadProductInputDomainModel, Observable<AddProductValidationDomainModel>> {
    private final UploadProductRepository uploadProductRepository;

    public AddProductValidation(UploadProductRepository uploadProductRepository) {
        this.uploadProductRepository = uploadProductRepository;
    }

    @Override
    public Observable<AddProductValidationDomainModel> call(final UploadProductInputDomainModel uploadProductInputDomainModel) {
        return uploadProductRepository.addProductValidation(uploadProductInputDomainModel);
    }

}