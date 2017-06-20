package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

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
    public Observable<List<UploadProductInputDomainModel>> getAllDraft() {
        return productDraftDataSource.getAllDraft()
                .flatMap(new Func1<List<ProductDraftDataBase>, Observable<ProductDraftDataBase>>() {
                    @Override
                    public Observable<ProductDraftDataBase> call(List<ProductDraftDataBase> productDraftDataBases) {
                        return Observable.from(productDraftDataBases);
                    }
                })
                .map(new Func1<ProductDraftDataBase, UploadProductInputDomainModel>() {
                    @Override
                    public UploadProductInputDomainModel call(ProductDraftDataBase productDraftDataBase) {
                        long id = productDraftDataBase.getId();
                        String data = productDraftDataBase.getData();
                        return Observable.just(data).map(new ProductDraftMapper(id)).toBlocking().first();
                    }
                }).toList();
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
