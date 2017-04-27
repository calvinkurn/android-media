package com.tokopedia.seller.product.domain;

import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.domain.model.EditImageProductDomainModel;
import com.tokopedia.seller.product.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface UploadProductRepository {
    Observable<AddProductValidationDomainModel> addProductValidation(UploadProductInputDomainModel uploadProductInputDomainModel);

    Observable<AddProductDomainModel> addProductSubmit(AddProductSubmitInputDomainModel addProductPictureDomainModel);

    Observable<AddProductDomainModel> editProduct(UploadProductInputDomainModel uploadProductInputDomainModel);

    Observable<EditImageProductDomainModel> editImageProduct(String picObj);

    Observable<ImageProductInputDomainModel> deleteProductPicture(String picId, String productId);
}
