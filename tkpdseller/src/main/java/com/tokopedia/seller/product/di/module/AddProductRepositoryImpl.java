package com.tokopedia.seller.product.di.module;

import com.tokopedia.seller.product.domain.AddProductRepository;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/11/17.
 */

public class AddProductRepositoryImpl implements AddProductRepository{
    @Override
    public Observable<AddProductValidationDomainModel> addProductValidation(UploadProductInputDomainModel uploadProductInputDomainModel) {
        return null;
    }

    @Override
    public Observable<AddProductPictureDomainModel> addProductPicture(AddProductPictureInputDomainModel addProductValidationDomainModel) {
        return null;
    }

    @Override
    public Observable<AddProductDomainModel> addProductSubmit(AddProductSubmitInputDomainModel addProductPictureDomainModel) {
        return null;
    }
}
