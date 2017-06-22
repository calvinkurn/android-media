package com.tokopedia.seller.product.data.repository;

import com.tokopedia.seller.product.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.domain.ProductDraftRepository;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftRepositoryImpl implements ProductDraftRepository {
    private final ProductDraftDataSource productDraftDataSource;

    public ProductDraftRepositoryImpl(ProductDraftDataSource productDraftDataSource) {
        this.productDraftDataSource = productDraftDataSource;
    }


    @Override
    public Observable<Long> saveDraft(UploadProductInputDomainModel domainModel, boolean isUploading) {
        String productDraft = ProductDraftMapper.mapFromDomain(domainModel);
        return productDraftDataSource.saveDraft(productDraft, domainModel.getId(), isUploading);
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
                }).toSortedList(new Func2<UploadProductInputDomainModel, UploadProductInputDomainModel, Integer>() {
                    @Override
                    public Integer call(UploadProductInputDomainModel uploadProductInputDomainModel, UploadProductInputDomainModel uploadProductInputDomainModel2) {
                        return (int) (uploadProductInputDomainModel2.getId() - uploadProductInputDomainModel.getId());
                    }
                });
    }

    @Override
    public Observable<Long> getAllDraftCount() {
        return productDraftDataSource.getAllDraftCount();
    }

    @Override
    public Observable<Boolean> clearAllDraft() {
        return productDraftDataSource.clearAllDraft();
    }

    @Override
    public Observable<Boolean> deleteDraft(long productId) {
        return productDraftDataSource.deleteDraft(productId);
    }

    @Override
    public Observable<Boolean> updateDraft(long productId, UploadProductInputDomainModel domainModel) {
        String productDraft = ProductDraftMapper.mapFromDomain(domainModel);
        return productDraftDataSource.updateDraft(productId, productDraft);
    }

    @Override
    public Observable<Boolean> updateuploadingStatusDraft(long productId, boolean isUploading) {
        return productDraftDataSource.updateUploadingStatusDraft(productId, isUploading);
    }
}
