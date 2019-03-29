package com.tokopedia.discovery.imagesearch.search;

import android.content.Context;

import com.tokopedia.discovery.imagesearch.domain.usecase.GetImageSearchUseCase;
import com.tokopedia.discovery.newdiscovery.base.DiscoveryPresenter;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;

/**
 * Created by sachinbansal on 4/12/18.
 */

public class ImageSearchPresenter extends DiscoveryPresenter<ImageSearchContract.View, ImageSearchActivity>
        implements ImageSearchContract.Presenter {

    public ImageSearchPresenter(Context context, GetProductUseCase getProductUseCase, GetImageSearchUseCase getImageSearchUseCase) {
        super(context, getProductUseCase, getImageSearchUseCase);
    }
}
