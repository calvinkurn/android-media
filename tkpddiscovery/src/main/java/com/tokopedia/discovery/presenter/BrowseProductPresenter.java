package com.tokopedia.discovery.presenter;

import com.tokopedia.discovery.interactor.DiscoveryInteractorImpl;

/**
 * Created by nakama on 23/03/17.
 */
public interface BrowseProductPresenter {
    void retrieveLastGridConfig(String departmentId,
                                DiscoveryInteractorImpl.GetGridConfigCallback callback);

    void onGridTypeChanged(String departmentId, String gridType);
}
