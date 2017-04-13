package com.tokopedia.seller.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.seller.product.domain.interactor.AddProductUseCase;
import com.tokopedia.seller.product.domain.interactor.SaveDraftProductUseCase;
import com.tokopedia.seller.product.domain.model.AddProductDomainModel;
import com.tokopedia.seller.product.domain.model.UploadProductInputDomainModel;
import com.tokopedia.seller.product.view.mapper.UploadProductMapper;
import com.tokopedia.seller.product.view.model.upload.UploadProductInputViewModel;

import rx.Subscriber;

/**
 * @author sebastianuskh on 4/13/17.
 */

public class ProductAddPresenterImpl extends ProductAddPresenter {
    private final SaveDraftProductUseCase saveDraftProductUseCase;
    private final AddProductUseCase addProductUseCase;

    public ProductAddPresenterImpl(SaveDraftProductUseCase saveDraftProductUseCase, AddProductUseCase addProductUseCase) {
        this.saveDraftProductUseCase = saveDraftProductUseCase;
        this.addProductUseCase = addProductUseCase;
    }

    @Override
    public void saveDraft(UploadProductInputViewModel viewModel) {
        UploadProductInputDomainModel domainModel = UploadProductMapper.map(viewModel);
        RequestParams requestParam = SaveDraftProductUseCase.generateUploadProductParam(domainModel);
        saveDraftProductUseCase.execute(requestParam, new SaveDraftSubscriber());
    }

    private class SaveDraftSubscriber extends Subscriber<Long> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(Long productId) {
            RequestParams requestParam = AddProductUseCase.generateUploadProductParam(productId);
            addProductUseCase.execute(requestParam, new AddProductSubscriber());
        }
    }

    private class AddProductSubscriber extends Subscriber<AddProductDomainModel> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(AddProductDomainModel addProductDomainModel) {

        }
    }
}
