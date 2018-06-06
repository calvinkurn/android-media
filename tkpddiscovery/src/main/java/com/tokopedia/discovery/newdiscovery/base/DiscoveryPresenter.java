package com.tokopedia.discovery.newdiscovery.base;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.discovery.imagesearch.data.subscriber.DefaultImageSearchSubscriber;
import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.base.BaseDiscoveryContract.View;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

/**
 * Created by hangnadi on 9/28/17.
 */

@SuppressWarnings("unchecked")
public class DiscoveryPresenter<T1 extends CustomerView, D2 extends View>
        extends BaseDiscoveryPresenter<T1, D2> {

    private GetProductUseCase getProductUseCase;
    private GetImageSearchUseCase getImageSearchUseCase;

    public DiscoveryPresenter(GetProductUseCase getProductUseCase) {
        this.getProductUseCase = getProductUseCase;
    }

    public DiscoveryPresenter(GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        this.getProductUseCase = getProductUseCase;
        this.getImageSearchUseCase = getImageSearchUseCase;
    }

    @Override
    public void requestProduct(SearchParameter searchParameter, boolean forceSearch, boolean requestOfficialStore) {
        super.requestProduct(searchParameter, forceSearch, requestOfficialStore);
        getProductUseCase.execute(
                GetProductUseCase.createInitializeSearchParam(searchParameter, forceSearch, requestOfficialStore),
                new DefaultSearchSubscriber(searchParameter, forceSearch, getBaseDiscoveryView(), false)
        );
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
        getProductUseCase.unsubscribe();
    }

}
