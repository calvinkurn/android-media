package com.tokopedia.discovery.newdiscovery.base;

import android.content.Context;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.discovery.imagesearch.data.subscriber.DefaultImageSearchSubscriber;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract.View;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;

/**
 * Created by hangnadi on 9/28/17.
 */

@SuppressWarnings("unchecked")
public class DiscoveryPresenter<T1 extends CustomerView, D2 extends View>
        extends BaseDiscoveryPresenter<T1, D2> {

    private GetProductUseCase getProductUseCase;
    private GetImageSearchUseCase getImageSearchUseCase;

    public DiscoveryPresenter(Context context, GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        this.getProductUseCase = getProductUseCase;
        this.getImageSearchUseCase = getImageSearchUseCase;
    }

    @Override
    public void requestImageSearch(String imagePath) {
        super.requestImageSearch(imagePath);
        getImageSearchUseCase.setImagePath(imagePath);
        getImageSearchUseCase.execute(
                RequestParams.EMPTY,
                new DefaultImageSearchSubscriber(getBaseDiscoveryView())
        );
    }

    @Override
    public void detachView() {
        super.detachView();

        if(getProductUseCase !=null) getProductUseCase.unsubscribe();
        if(getImageSearchUseCase != null) getImageSearchUseCase.unsubscribe();
    }

}
