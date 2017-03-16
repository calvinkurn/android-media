package com.tokopedia.discovery.presenter;

import com.drew.lang.annotations.Nullable;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.discovery.model.Breadcrumb;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
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
    String GRID_TYPE_EXTRA = "GRID_TYPE_EXTRA";
    int REQUEST_SORT = 121;

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
