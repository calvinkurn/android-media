package com.tokopedia.inbox.attachproduct.domain.usecase;

import com.tokopedia.inbox.attachproduct.data.repository.AttachProductRepository;
import com.tokopedia.inbox.attachproduct.domain.model.mapper.DataModelToDomainModelMapper;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Hendri on 14/02/18.
 */

public class AttachProductUseCase extends UseCase<List<AttachProductItemViewModel>> {
    private static final String KEYWORD_KEY = "keyword";
    private static final String SHOP_ID_KEY = "shop_id";
    private static final String PAGE_KEY = "page";
    private final AttachProductRepository repository;
    private final DataModelToDomainModelMapper mapper;

    @Inject
    public AttachProductUseCase(AttachProductRepository repository, DataModelToDomainModelMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Observable<List<AttachProductItemViewModel>> createObservable(RequestParams requestParams) {
        return repository.loadProductFromShop(requestParams.getParamsAllValueInString()).map(mapper);
    }

    public static RequestParams createRequestParams(String query, String shopId, int page) {
        if(page == 0) page = 1;
        RequestParams param = RequestParams.create();
        param.putString(KEYWORD_KEY,query);
        param.putString(SHOP_ID_KEY,shopId);
        param.putString(PAGE_KEY,String.valueOf(page));
        return param;
    }
}
