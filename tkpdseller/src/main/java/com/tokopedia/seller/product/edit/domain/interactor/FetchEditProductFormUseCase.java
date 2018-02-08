package com.tokopedia.seller.product.edit.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.edit.domain.EditProductFormRepository;
import com.tokopedia.seller.product.edit.domain.model.UploadProductInputDomainModel;

import javax.inject.Inject;

import rx.Observable;

/**
 * @author sebastianuskh on 4/21/17.
 */

public class FetchEditProductFormUseCase extends UseCase<UploadProductInputDomainModel> {
    private static final String UNSELECTED_PRODUCT_ID = "-1";
    private static final String PRODUCT_ID = "PRODUCT_ID";
    private final EditProductFormRepository editProductFormRepository;

    @Inject
    public FetchEditProductFormUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, EditProductFormRepository editProductFormRepository) {
        super(threadExecutor, postExecutionThread);
        this.editProductFormRepository = editProductFormRepository;
    }

    @Override
    public Observable<UploadProductInputDomainModel> createObservable(RequestParams requestParams) {
        String productId = requestParams.getString(PRODUCT_ID, UNSELECTED_PRODUCT_ID);
        if (productId.equals(UNSELECTED_PRODUCT_ID)) {
            throw new RuntimeException("Product id is not selected");
        }
        return editProductFormRepository.fetchEditProduct(productId);
    }

    public static RequestParams createParams(String productId){
        RequestParams params = RequestParams.create();
        params.putString(PRODUCT_ID, productId);
        return params;
    }
}
