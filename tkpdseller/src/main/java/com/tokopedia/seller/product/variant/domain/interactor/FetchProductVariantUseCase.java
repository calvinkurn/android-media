package com.tokopedia.seller.product.variant.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.repository.ProductVariantRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/28/17.
 */

public class FetchProductVariantUseCase extends UseCase<List<ProductVariantByCatModel>> {
    private static final int UNSELECTED = -2;
    private static final String CATEGORY_ID = "cat_id";

    private final ProductVariantRepository productVariantRepository;

    @Inject
    public FetchProductVariantUseCase(ThreadExecutor threadExecutor,
                                      PostExecutionThread postExecutionThread,
                                      ProductVariantRepository productVariantRepository) {
        super(threadExecutor, postExecutionThread);
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public final Observable<List<ProductVariantByCatModel>> createObservable(RequestParams requestParams) {
        return productVariantRepository.fetchProductVariant(requestParams.getLong(CATEGORY_ID, UNSELECTED));
    }

    public static RequestParams generateParam(long categoryId) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putLong(CATEGORY_ID, categoryId);
        return requestParam;
    }
}
