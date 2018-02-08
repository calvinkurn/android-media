package com.tokopedia.seller.product.edit.domain;

import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductValidationDomainModel;
import com.tokopedia.seller.product.edit.domain.model.EditImageProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;

import java.util.List;

import rx.Observable;

/**
 * @author sebastianuskh on 4/10/17.
 */

public interface UploadProductRepository {
    Observable<AddProductValidationDomainModel> addProductValidation(UploadProductInputDomainModel uploadProductInputDomainModel);

    Observable<AddProductDomainModel> addProductSubmit(AddProductSubmitInputDomainModel addProductPictureDomainModel);

    Observable<Boolean> editProduct(UploadProductInputDomainModel uploadProductInputDomainModel);

    Observable<EditImageProductDomainModel> editImageProduct(String picObj);

    Observable<ImageProductInputDomainModel> deleteProductPicture(String picId, String productId);
}
