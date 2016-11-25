package com.tokopedia.discovery.presenter;

import com.drew.lang.annotations.Nullable;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.discovery.model.Breadcrumb;
import com.tokopedia.discovery.model.BrowseProductActivityModel;
import com.tokopedia.discovery.model.BrowseProductModel;
import com.tokopedia.discovery.model.NetworkParam;

import java.util.List;

/**
 * Created by noiz354 on 3/24/16.
 */
public interface DiscoveryActivityPresenter {
    BrowseProductActivityModel getBrowseProductActivityModel();
    boolean isFragmentCreated(String TAG);
    void fetchIntent();
    boolean checkHasFilterAttrIsNull(int activeTab);

    /**
     * get firsttime data from activity for browse products
     * @param firstTimeOnly true means first time, false means not first time
     * @return data that already first fetch
     */
    BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly);

    @Nullable
    NetworkParam.Product getProductParam();

    @Nullable
    List<Breadcrumb> getProductBreadCrumb();

    String TAG = "DiscoveryActivity";
    String SEARCH_ACTION_INTENT = BuildConfig.APPLICATION_ID+".SEARCH";
    String CHANGE_GRID_ACTION_INTENT = BuildConfig.APPLICATION_ID+".LAYOUT";
    //    String EXTRAS_SEARCH_TERM = "EXTRAS_SEARCH_TERM";
    String GRID_TYPE_EXTRA = "GRID_TYPE_EXTRA";
    int REQUEST_SORT = 121;

    //    String DEPARTMENT_ID = "DEPARTMENT_ID";
//    String VALUES_HISTORY_FRAGMENT_ID = "VALUES_HISTORY_FRAGMENT_ID";
    int INVALID_FRAGMENT_ID = -1;
//    String AD_SRC = "AD_SRC";
//    String EXTRAS_DISCOVERY_ALIAS = "EXTRAS_DISCOVERY_ALIAS";

    abstract class DiscoveryActivityPresenterImpl implements DiscoveryActivityPresenter{
        @Override
        public boolean isFragmentCreated(String TAG) {
            return false;
        }

        @Override
        public void fetchIntent() {

        }

        @Override
        public BrowseProductModel getDataForBrowseProduct(boolean firstTimeOnly) {
            return null;
        }

        @Override
        public NetworkParam.Product getProductParam() {
            return null;
        }
    }
}
