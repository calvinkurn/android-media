package com.tokopedia.discovery.newdiscovery.search;

import com.tokopedia.discovery.newdiscovery.base.DiscoveryPresenter;
import com.tokopedia.discovery.newdiscovery.domain.usecase.GetProductUseCase;

/**
 * Created by henrypriyono on 10/10/17.
 */

public class SearchPresenter extends DiscoveryPresenter<SearchContract.View, SearchActivity>
        implements SearchContract.Presenter {

    public SearchPresenter(GetProductUseCase getProductUseCase) {
        super(getProductUseCase);
    }
}
