package com.tokopedia.inbox.rescenter.product.view.presenter;

import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.inbox.rescenter.product.domain.interactor.GetProductDetailUseCase;
import com.tokopedia.inbox.rescenter.product.view.subscriber.GetProductDetailSubscriber;

/**
 * Created by hangnadi on 3/28/17.
 */

public class ProductDetailFragmentImpl implements ProductDetailFragmentContract.Presenter {

    private ProductDetailFragmentContract.ViewListener viewListener;
    private GetProductDetailUseCase getProductDetailUseCase;

    public ProductDetailFragmentImpl(ProductDetailFragmentContract.ViewListener viewListener, GetProductDetailUseCase getProductDetailUseCase) {
        this.viewListener = viewListener;
        this.getProductDetailUseCase = getProductDetailUseCase;
    }

    @Override
    public void onFirstTimeLaunched() {
        viewListener.setLoadingView(true);
        viewListener.setMainView(false);
        getProductDetailUseCase.execute(getProductDetailParams(),
                new GetProductDetailSubscriber(viewListener));
    }

    private RequestParams getProductDetailParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetProductDetailUseCase.PARAM_RESOLUTION_ID, viewListener.getResolutionID());
        requestParams.putString(GetProductDetailUseCase.PARAM_TROUBLE_ID, viewListener.getTroubleID());
        return requestParams;
    }

    @Override
    public void setOnDestroyView() {
        unSubscibeObservable();
    }

    private void unSubscibeObservable() {
        getProductDetailUseCase.unsubscribe();
    }
}
