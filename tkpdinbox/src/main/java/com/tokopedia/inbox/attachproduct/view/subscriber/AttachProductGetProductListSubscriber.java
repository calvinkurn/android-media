package com.tokopedia.inbox.attachproduct.view.subscriber;

import com.tokopedia.core.util.getproducturlutil.model.GetProductPass;
import com.tokopedia.inbox.attachproduct.view.presenter.AttachProductContract;
import com.tokopedia.inbox.attachproduct.view.viewmodel.AttachProductItemViewModel;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Hendri on 08/03/18.
 */

public class AttachProductGetProductListSubscriber extends Subscriber<List<AttachProductItemViewModel>> {

    private final AttachProductContract.View view;

    public AttachProductGetProductListSubscriber(AttachProductContract.View view){
        this.view = view;
    }

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onNext(List<AttachProductItemViewModel> attachProductItemViewModels) {
        view.hideAllLoadingIndicator();
        view.addProductToList(attachProductItemViewModels,(attachProductItemViewModels.size() >= Integer.parseInt(GetProductPass.DEFAULT_ROWS)));
    }
}
