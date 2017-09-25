package com.tokopedia.seller.product.picker.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.picker.common.ProductListPickerConstant;
import com.tokopedia.seller.product.picker.data.model.ProductListSellerModel;
import com.tokopedia.seller.product.picker.domain.GetProductListSellingRepository;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by zulfikarrahman on 9/4/17.
 */

public class GetProductListSellingUseCase extends UseCase<ProductListSellerModel> {
    private final GetProductListSellingRepository getProductListSellingRepository;

    @Inject
    public GetProductListSellingUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread,
                                        GetProductListSellingRepository getProductListSellingRepository) {
        super(threadExecutor, postExecutionThread);
        this.getProductListSellingRepository = getProductListSellingRepository;
    }

    @Override
    public Observable<ProductListSellerModel> createObservable(RequestParams requestParams) {
        return getProductListSellingRepository.getProductListSeller(requestParams.getParamsAllValueInString());
    }

    public static RequestParams createRequestParams(int page, String keywordFilter) {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(ProductListPickerConstant.QUERY_PER_PAGE, ProductListPickerConstant.REQUEST_PER_PAGE);
        requestParams.putString(ProductListPickerConstant.QUERY_PAGE, String.valueOf(page));
        requestParams.putString(ProductListPickerConstant.QUERY_SORT, ProductListPickerConstant.DEFAULT_SORT);
        requestParams.putString(ProductListPickerConstant.QUERY_KEYWORD, keywordFilter);
        return requestParams;
    }
}
