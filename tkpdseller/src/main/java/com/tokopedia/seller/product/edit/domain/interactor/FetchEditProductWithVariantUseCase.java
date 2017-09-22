package com.tokopedia.seller.product.edit.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.edit.domain.EditProductFormRepository;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.variant.data.model.variantbyprd.ProductVariantByPrdModel;
import com.tokopedia.seller.product.variant.data.model.variantsubmit.ProductVariantDataSubmit;
import com.tokopedia.seller.product.variant.domain.interactor.FetchProductVariantByPrdUseCase;
import com.tokopedia.seller.product.variant.util.ProductVariantUtils;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func2;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class FetchEditProductWithVariantUseCase extends UseCase<UploadProductInputDomainModel> {
    private static final String UNSELECTED_PRODUCT_ID = "-1";
    private static final String PRODUCT_ID = "PRODUCT_ID";

    private final FetchEditProductFormUseCase fetchEditProductFormUseCase;
    private final FetchProductVariantByPrdUseCase fetchProductVariantByPrdUseCase;

    @Inject
    public FetchEditProductWithVariantUseCase(ThreadExecutor threadExecutor,
                                              PostExecutionThread postExecutionThread,
                                              FetchEditProductFormUseCase fetchEditProductFormUseCase,
                                              FetchProductVariantByPrdUseCase fetchProductVariantByPrdUseCase) {
        super(threadExecutor, postExecutionThread);
        this.fetchEditProductFormUseCase = fetchEditProductFormUseCase;
        this.fetchProductVariantByPrdUseCase = fetchProductVariantByPrdUseCase;
    }

    @Override
    public Observable<UploadProductInputDomainModel> createObservable(RequestParams requestParams) {
        String productId = requestParams.getString(PRODUCT_ID, UNSELECTED_PRODUCT_ID);
        return Observable.zip(
                fetchEditProductFormUseCase.createObservable(FetchEditProductFormUseCase.createParams(productId)),
                fetchProductVariantByPrdUseCase.createObservable(FetchProductVariantByPrdUseCase.generateParam(Long.parseLong(productId))),
                new Func2<UploadProductInputDomainModel, ProductVariantByPrdModel, UploadProductInputDomainModel>() {
                    @Override
                    public UploadProductInputDomainModel call(UploadProductInputDomainModel uploadProductInputDomainModel,
                                                              ProductVariantByPrdModel productVariantByPrdModel) {
                        ProductVariantDataSubmit productVariantDataSubmit =
                                ProductVariantUtils.generateProductVariantSubmit(productVariantByPrdModel);
                        uploadProductInputDomainModel.setProductVariantDataSubmit(productVariantDataSubmit);
                        return uploadProductInputDomainModel;
                    }
                }
        ).take(1);
    }

    public static RequestParams createParams(String productId){
        RequestParams params = RequestParams.create();
        params.putString(PRODUCT_ID, productId);
        return params;
    }
}
