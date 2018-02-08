package com.tokopedia.seller.product.edit.data.repository;

import com.tokopedia.seller.product.edit.data.mapper.EditProductFormMapper;
import com.tokopedia.seller.product.edit.data.source.EditProductFormDataSource;
import com.tokopedia.seller.product.edit.data.source.FetchVideoEditProductDataSource;
import com.tokopedia.seller.product.edit.domain.EditProductFormRepository;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class EditProductFormRepositoryImpl implements EditProductFormRepository {
    private final EditProductFormDataSource editProductFormDataSource;
    private final EditProductFormMapper editProductFormMapper;
    private final FetchVideoEditProductDataSource fetchVideoEditProductDataSource;

    public EditProductFormRepositoryImpl(EditProductFormDataSource editProductFormDataSource, EditProductFormMapper editProductFormMapper, FetchVideoEditProductDataSource fetchVideoEditProductDataSource) {
        this.editProductFormDataSource = editProductFormDataSource;
        this.editProductFormMapper = editProductFormMapper;
        this.fetchVideoEditProductDataSource = fetchVideoEditProductDataSource;
    }

    @Override
    public Observable<UploadProductInputDomainModel> fetchEditProduct(final String productId) {
        fetchVideoEditProductDataSource.setProductId(productId);

        return editProductFormDataSource.fetchEditProductForm(productId).map(editProductFormMapper)
                .flatMap(fetchVideoEditProductDataSource);
    }
}
