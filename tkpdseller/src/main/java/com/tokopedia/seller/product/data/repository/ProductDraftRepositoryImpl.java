package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftRepositoryImpl implements ProductDraftRepository {
    private final ProductDraftDataSource productDraftDataSource;

    public ProductDraftRepositoryImpl(ProductDraftDataSource productDraftDataSource) {
        this.productDraftDataSource = productDraftDataSource;
    }


    @Override
    public Observable<Long> saveDraft(UploadProductInputDomainModel domainModel) {
        String productDraft = ProductDraftMapper.mapFromDomain(domainModel);
        return productDraftDataSource.saveDraft(productDraft, domainModel.getId());
    }

    @Override
    public Observable<UploadProductInputDomainModel> getDraft(long productId) {
        return productDraftDataSource.getDraft(productId)
                .map(new ProductDraftMapper(productId));
    }

    @Override
    public Observable<Boolean> clearAllDraft() {
        return productDraftDataSource.clearAllDraft();
    }

    @Override
    public void deleteDraft(long productId) {
        productDraftDataSource.deleteDraft(productId);
    }

    @Override
    public void updateDraft(long productId, UploadProductInputDomainModel domainModel) {
        String productDraft = ProductDraftMapper.mapFromDomain(domainModel);
        productDraftDataSource.updateDraft(productId, productDraft);
    }
}
