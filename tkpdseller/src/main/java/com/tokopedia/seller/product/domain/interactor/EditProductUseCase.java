package com.tokopedia.seller.product.domain.interactor;

import com.tokopedia.core.base.domain.executor.PostExecutionThread;
import com.tokopedia.core.base.domain.executor.ThreadExecutor;
import com.tokopedia.seller.product.domain.interactor.observable.EditProductObservable;
import com.tokopedia.seller.product.domain.interactor.observable.ImageProductObservable;
import com.tokopedia.seller.product.domain.model.EditProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author sebastianuskh on 4/10/17.
 */

public class EditProductUseCase extends UploadProductUseCase<EditProductDomainModel>{
    private final ImageProductObservable imageProductObservable;
    private final EditProductObservable editProductObservable;

    public EditProductUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, ImageProductObservable imageProductObservable, EditProductObservable editProductObservable) {
        super(threadExecutor, postExecutionThread);
        this.imageProductObservable = imageProductObservable;
        this.editProductObservable = editProductObservable;
    }

    @Override
    protected Func1<UploadProductInputDomainModel, Observable<EditProductDomainModel>> getUploadProductObservable() {
        return editProductObservable;
    }

    @Override
    protected Func1<UploadProductInputDomainModel, Observable<UploadProductInputDomainModel>> getImageProductObservable() {
        return imageProductObservable;
    }
}
