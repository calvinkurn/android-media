package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.repository.ProductVariantRepository;

import java.util.List;

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
    public Observable<Boolean> call(final UploadProductInputDomainModel editProductInputServiceModel) {
        return uploadProductRepository.editProduct(editProductInputServiceModel);
    }
}