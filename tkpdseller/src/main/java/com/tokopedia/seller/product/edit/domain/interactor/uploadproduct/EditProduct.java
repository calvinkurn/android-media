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
    private final ProductVariantRepository productVariantRepository;

    public EditProduct(UploadProductRepository uploadProductRepository,
                       ProductVariantRepository productVariantRepository) {
        this.uploadProductRepository = uploadProductRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public Observable<Boolean> call(final UploadProductInputDomainModel editProductInputServiceModel) {
        return Observable.just(editProductInputServiceModel.getProductDepartmentId())
                    .flatMap(new Func1<Long, Observable<List<ProductVariantByCatModel>>>() {
                        @Override
                        public Observable<List<ProductVariantByCatModel>> call(Long categoryId) {
                            if (categoryId > 0) {
                                return productVariantRepository.fetchProductVariantByCat(categoryId);
                            } else {
                                return null;
                            }
                        }
                    })
                    .flatMap(new Func1<List<ProductVariantByCatModel>, Observable<Boolean>>() {
                        @Override
                        public Observable<Boolean> call(List<ProductVariantByCatModel> productVariantByCatModelList) {
                            return uploadProductRepository.editProduct(editProductInputServiceModel, productVariantByCatModelList);
                        }
                    });
    }
}