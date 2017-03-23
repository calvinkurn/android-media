package com.tokopedia.discovery.presenter;

import com.tokopedia.discovery.interactor.DiscoveryInteractor;
import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;

/**
 * Created by nakama on 23/03/17.
 */

public class BrowseProductPresenterImpl implements BrowseProductPresenter {

    DiscoveryInteractor discoveryInteractor;

    public BrowseProductPresenterImpl() {
        discoveryInteractor = new DiscoveryInteractorImpl();
    }

    @Override
    public void retrieveLastGridConfig(String departmentId,
                                       DiscoveryInteractorImpl.GetGridConfigCallback callback) {

        discoveryInteractor.getLastGridConfig(departmentId, callback);
    }

    @Override
    public void onGridTypeChanged(String departmentId, String gridType) {
        discoveryInteractor.saveLastGridConfig(departmentId, gridType);
    }
}
