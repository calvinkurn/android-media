package com.tokopedia.seller.product.draft.data.repository;

import android.content.Context;

import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.product.draft.data.mapper.ProductDraftMapper;
import com.tokopedia.seller.product.draft.data.source.ProductDraftDataSource;
import com.tokopedia.seller.product.draft.domain.model.ProductDraftRepository;
import com.tokopedia.seller.product.edit.data.source.db.model.ProductDraftDataBase;
import com.tokopedia.seller.product.edit.view.model.edit.ProductViewModel;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductDraftRepositoryImpl implements ProductDraftRepository {
    private final ProductDraftDataSource productDraftDataSource;
    private Context context;

    public ProductDraftRepositoryImpl(ProductDraftDataSource productDraftDataSource, Context context) {
        this.productDraftDataSource = productDraftDataSource;
        this.context = context;
    }


    @Override
    public Observable<Long> saveDraft(ProductViewModel domainModel, boolean isUploading) {
        String productDraft = ProductDraftMapper.mapFromDomain(domainModel);
        String shopId = SessionHandler.getShopID(context);
        return productDraftDataSource.saveDraft(productDraft, domainModel.getProductId(), isUploading, shopId);
    }

    @Override
    public Observable<ProductViewModel> getDraft(long productId) {
        return productDraftDataSource.getDraft(productId)
                .map(new ProductDraftMapper(productId));
    }

    @Override
    public Observable<List<ProductViewModel>> getAllDraft() {
        String shopId = SessionHandler.getShopID(context);
        return productDraftDataSource.getAllDraft(shopId)
                .flatMap(new Func1<List<ProductDraftDataBase>, Observable<ProductDraftDataBase>>() {
                    @Override
                    public Observable<ProductDraftDataBase> call(List<ProductDraftDataBase> productDraftDataBases) {
                        return Observable.from(productDraftDataBases);
                    }
                })
                .map(new Func1<ProductDraftDataBase, ProductViewModel>() {
                    @Override
                    public ProductViewModel call(ProductDraftDataBase productDraftDataBase) {
                        long id = productDraftDataBase.getId();
                        return Observable.just(productDraftDataBase).map(new ProductDraftMapper(id)).toBlocking().first();
                    }
                }).toSortedList(new Func2<ProductViewModel, ProductViewModel, Integer>() {
                    @Override
                    public Integer call(ProductViewModel productViewModel, ProductViewModel productViewModel2) {
                        return (int) (productViewModel2.getProductId() - productViewModel.getProductId());
                    }
                });
    }

    @Override
    public Observable<Long> getAllDraftCount() {
        String shopId = SessionHandler.getShopID(context);
        return productDraftDataSource.getAllDraftCount(shopId);
    }

    @Override
    public Observable<Boolean> clearAllDraft() {
        String shopID = SessionHandler.getShopID(context);
        return productDraftDataSource.clearAllDraft(shopID);
    }

    @Override
    public Observable<Boolean> deleteDraft(long productId) {
        return productDraftDataSource.deleteDraft(productId);
    }

    @Override
    public Observable<Long> updateDraftToUpload(long productId, ProductViewModel domainModel,
                                                boolean isUploading) {
        String productDraft = ProductDraftMapper.mapFromDomain(domainModel);
        return productDraftDataSource.updateDraft(productId, productDraft, isUploading);
    }

    @Override
    public Observable<Boolean> updateuploadingStatusDraft(long productId, boolean isUploading) {
        return productDraftDataSource.updateUploadingStatusDraft(productId, isUploading);
    }
}
