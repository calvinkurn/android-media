package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductPictureInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface AddProductRepository {
    Observable<AddProductValidationDomainModel> addProductValidation(UploadProductInputDomainModel uploadProductInputDomainModel);

    Observable<AddProductPictureDomainModel> addProductPicture(AddProductPictureInputDomainModel addProductValidationDomainModel);

    Observable<AddProductDomainModel> addProductSubmit(AddProductSubmitInputDomainModel addProductPictureDomainModel);
}
