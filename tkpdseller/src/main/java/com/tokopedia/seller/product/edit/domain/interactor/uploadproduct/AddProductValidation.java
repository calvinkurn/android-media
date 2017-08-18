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
    private final ProductVariantRepository productVariantRepository;

    public AddProductValidation(UploadProductRepository uploadProductRepository,
                                ProductVariantRepository productVariantRepository) {
        this.uploadProductRepository = uploadProductRepository;
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public Observable<AddProductValidationDomainModel> call(final UploadProductInputDomainModel uploadProductInputDomainModel) {
        return Observable.just(uploadProductInputDomainModel.getProductDepartmentId())
                    .flatMap(new Func1<Long, Observable<List<ProductVariantByCatModel>>>() {
                        @Override
                        public Observable<List<ProductVariantByCatModel>> call(Long categoryId) {
                            if (categoryId > 0) {
                                return productVariantRepository.fetchProductVariantByCat(categoryId);
                            }
                            else {
                                return null;
                            }
                        }
                    })
                    .flatMap(new Func1<List<ProductVariantByCatModel>, Observable<AddProductValidationDomainModel>>() {
                        @Override
                        public Observable<AddProductValidationDomainModel> call(List<ProductVariantByCatModel> productVariantByCatModelList) {
                            return uploadProductRepository
                                    .addProductValidation(uploadProductInputDomainModel, productVariantByCatModelList);
                        }
                    });
    }

}