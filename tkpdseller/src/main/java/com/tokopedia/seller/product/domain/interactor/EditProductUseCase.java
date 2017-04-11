package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.model.EditProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class EditProductUseCase extends UploadProductUseCase<EditProductDomainModel>{
    public EditProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    public Observable<EditProductDomainModel> createObservable(RequestParams requestParams) {
        return null;
    }

    @Override
    protected Func1<UploadProductInputDomainModel, Observable<EditProductDomainModel>> getUploadProductObservable() {
        return null;
    }

    @Override
    protected Func1<UploadProductInputDomainModel, Observable<UploadProductInputDomainModel>> getImageProductObservable() {
        return null;
    }
}
