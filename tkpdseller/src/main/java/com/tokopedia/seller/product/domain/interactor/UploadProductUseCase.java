package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.domain.UseCase;
import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public abstract class UploadProductUseCase<ReturnType> extends UseCase<ReturnType>{
    public static final String UPLOAD_PRODUCT_INPUT_MODEL = "UPLOAD_PRODUCT_INPUT_MODEL";

    public UploadProductUseCase(ThreadExecutor threadExecutor,
                                PostExecutionThread postExecutionThread) {
        super(threadExecutor, postExecutionThread);
    }

    @Override
    public Observable<ReturnType> createObservable(RequestParams requestParams) {
        UploadProductInputDomainModel inputModel;
        if (isInputProductNotNull(requestParams) &&
                isUploadProductDomainModel(requestParams)){
            inputModel = (UploadProductInputDomainModel)
                    requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL);
        } else {
            throw new RuntimeException("Input model is missing");
        }
        return Observable.just(inputModel)
                .flatMap(getImageProductObservable())
                .flatMap(getUploadProductObservable());
    }

    private boolean isInputProductNotNull(RequestParams requestParams) {
        return requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL) != null;
    }

    private boolean isUploadProductDomainModel(RequestParams requestParams) {
        return requestParams.getObject(UPLOAD_PRODUCT_INPUT_MODEL)
                instanceof UploadProductInputDomainModel;
    }


    public static RequestParams generateUploadProductParam(UploadProductInputDomainModel domainModel){
        RequestParams params = RequestParams.create();
        params.putObject(UPLOAD_PRODUCT_INPUT_MODEL, domainModel);
        return params;
    }

    protected abstract
    Func1<UploadProductInputDomainModel, Observable<ReturnType>>
    getUploadProductObservable();

    protected abstract
    Func1<UploadProductInputDomainModel, Observable<UploadProductInputDomainModel>>
    getImageProductObservable();

}
