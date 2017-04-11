package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.interactor.observable.AddProductObservable;
import com.tokopedia.seller.product.domain.interactor.observable.ImageProductObservable;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class AddProductUseCase extends UploadProductUseCase<AddProductDomainModel>{


    private final ImageProductObservable imageProductObservable;
    private final AddProductObservable addProductObservable;

    public AddProductUseCase(
            ThreadExecutor threadExecutor,
            PostExecutionThread postExecutionThread,
            ImageProductObservable imageProductObservable,
            AddProductObservable addProductObservable) {
        super(threadExecutor, postExecutionThread);
        this.imageProductObservable = imageProductObservable;
        this.addProductObservable = addProductObservable;
    }


    @Override
    protected Func1<UploadProductInputDomainModel, Observable<AddProductDomainModel>> getUploadProductObservable() {
        return addProductObservable;
    }

    @Override
    protected Func1<UploadProductInputDomainModel, Observable<UploadProductInputDomainModel>> getImageProductObservable() {
        return imageProductObservable;
    }
}
