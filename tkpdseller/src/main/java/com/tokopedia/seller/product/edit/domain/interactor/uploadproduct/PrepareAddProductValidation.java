package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.model.ImageProductInputDomainModel;
import com.tokopedia.seller.product.edit.domain.model.ProductPhotoListDomainModel;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductPictureViewModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.List;

import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */


public class PrepareAddProductValidation implements Func1<List<ProductPictureViewModel>, ProductViewModel> {

    private final ProductViewModel productViewModel;

    public PrepareAddProductValidation(ProductViewModel productViewModel) {
        this.productViewModel = productViewModel;
    }
    @Override
    public ProductViewModel call(List<ProductPictureViewModel> productPictureViewModels) {
        productViewModel.setProductPicture(productPictureViewModels);
        return productViewModel;
    }

}