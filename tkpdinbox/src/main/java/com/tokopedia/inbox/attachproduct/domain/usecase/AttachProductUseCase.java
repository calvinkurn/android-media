package com.tokopedia.inbox.attachproduct.domain.usecase;

import com.tokopedia.inbox.attachproduct.data.repository.AttachProductRepository;
import com.tokopedia.inbox.attachproduct.domain.model.mapper.DataModelToDomainModelMapper;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Hendri on 14/02/18.
 */

public class AttachProductUseCase {
    private final AttachProductRepository repository;
    private final DataModelToDomainModelMapper mapper;

    @Inject
    public AttachProductUseCase(AttachProductRepository repository, DataModelToDomainModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Observable<List<AttachProductItemViewModel>> getProductList(String query, String shopId, int page){
        return repository.loadProductFromShop(query,shopId,page).map(mapper);
    }
}
