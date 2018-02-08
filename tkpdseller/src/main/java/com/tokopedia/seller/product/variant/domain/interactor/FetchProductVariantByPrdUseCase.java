package com.tokopedia.seller.product.variant.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.variant.data.model.variantbycat.ProductVariantByCatModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;
import com.tokopedia.seller.product.variant.repository.ProductVariantRepository;

import java.util.List;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/28/17.
 */

public class FetchProductVariantByPrdUseCase extends UseCase<ProductVariantByPrdModel> {
    private static final int UNSELECTED = -2;
    private static final String PRODUCT_ID = "p_id";

    private final ProductVariantRepository productVariantRepository;

    @Inject
    public FetchProductVariantByPrdUseCase(ThreadExecutor threadExecutor,
                                           PostExecutionThread postExecutionThread,
                                           ProductVariantRepository productVariantRepository) {
        super(threadExecutor, postExecutionThread);
        this.productVariantRepository = productVariantRepository;
    }

    @Override
    public final Observable<ProductVariantByPrdModel> createObservable(RequestParams requestParams) {
        return productVariantRepository.fetchProductVariantByPrd(requestParams.getLong(PRODUCT_ID, UNSELECTED));
    }

    public static RequestParams generateParam(long productId) {
        RequestParams requestParam = RequestParams.create();
        requestParam.putLong(PRODUCT_ID, productId);
        return requestParam;
    }
}
