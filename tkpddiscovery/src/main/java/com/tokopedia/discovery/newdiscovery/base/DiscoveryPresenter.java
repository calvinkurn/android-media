package com.tokopedia.discovery.newdiscovery.base;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract.View;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;

/**
 * Created by hangnadi on 9/28/17.
 */

@SuppressWarnings("unchecked")
public class DiscoveryPresenter<T1 extends CustomerView, D2 extends View>
        extends BaseDiscoveryPresenter<T1, D2> {

    private GetProductUseCase getProductUseCase;

    public DiscoveryPresenter(GetProductUseCase getProductUseCase) {
        this.getProductUseCase = getProductUseCase;
    }

    @Override
    public void detachView() {
        super.detachView();

        if(getProductUseCase !=null) getProductUseCase.unsubscribe();
    }

}
