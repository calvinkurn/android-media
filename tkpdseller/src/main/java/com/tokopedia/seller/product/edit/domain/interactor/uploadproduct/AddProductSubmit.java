package com.tokopedia.seller.product.edit.domain.interactor.uploadproduct;

import com.tokopedia.seller.product.edit.domain.UploadProductRepository;
import com.tokopedia.seller.product.edit.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.edit.domain.model.AddProductSubmitInputDomainModel;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;
import com.tokopedia.seller.product.edit.view.model.upload.intdef.ProductStatus;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 5/8/17.
 */


public class AddProductSubmit implements Func1<ProductViewModel, Observable<AddProductDomainModel>> {

    private final UploadProductRepository uploadProductRepository;

    public AddProductSubmit(UploadProductRepository uploadProductRepository) {
        this.uploadProductRepository = uploadProductRepository;
    }

    @Override
    public Observable<AddProductDomainModel> call(ProductViewModel productViewModel) {
        if(productViewModel.getProductStatus() == ProductStatus.ADD) {
            return uploadProductRepository
                    .addProductSubmit(productViewModel);
        }else{
            return uploadProductRepository.editProduct(productViewModel);
        }
    }

}