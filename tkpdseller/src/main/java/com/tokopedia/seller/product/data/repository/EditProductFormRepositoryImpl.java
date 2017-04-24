package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.mapper.EditProductFormMapper;
import com.tokopedia.seller.product.data.source.EditProductFormDataSource;
import com.tokopedia.seller.product.domain.EditProductFormRepository;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class EditProductFormRepositoryImpl implements EditProductFormRepository {
    private final EditProductFormDataSource editProductFormDataSource;
    private final EditProductFormMapper editProductFormMapper;

    public EditProductFormRepositoryImpl(EditProductFormDataSource editProductFormDataSource, EditProductFormMapper editProductFormMapper) {
        this.editProductFormDataSource = editProductFormDataSource;
        this.editProductFormMapper = editProductFormMapper;
    }

    @Override
    public Observable<UploadProductInputDomainModel> fetchEditProduct(String productId) {
        return editProductFormDataSource.fetchEditProductForm(productId).map(editProductFormMapper);
    }
}
