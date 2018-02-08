package com.tokopedia.discovery.presenter;

import android.content.Context;

import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.entity.intermediary.Data;
import com.tokopedia.core.network.entity.intermediary.SimpleCategory;
import com.tokopedia.core.network.entity.discovery.BannerOfficialStoreModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.network.entity.discovery.BrowseProductModel;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.discovery.model.NetworkParam;

import java.util.Map;

/**
 * Created by noiz354 on 3/24/16.
 */
public interface BrowseView {
    BrowseProductActivityModel getBrowseProductActivityModel();

    boolean isFragmentCreated(String TAG);

    BrowseProductModel getDataForBrowseProduct();
    NetworkParam.Product getProductParam();
    Context getContext();
    boolean checkHasFilterAttrIsNull(int activeTab);
    void openFilter(DataValue filterAttribute,
                    String source,
                    String parentDepartment,
                    String departmentId,
                    Map<String, String> filters);
    void openCategoryNavigation(String departementId);
    void openSort(DataValue filterAttribute, String source);

    void initDiscoverySearchView(String lastQuery);

    void initToolbar(String title, boolean isClickable);

    void setFilterAttribute(DataValue filterAttribute, int activeTab);
    void showFailedFetchAttribute();
    void showLoading(boolean isLoading);
    void showEmptyState(NetworkErrorHelper.RetryClickedListener retryClickedListener);
    void removeEmptyState();
    void setupShopItemsBottomBar(String source);

    void setupAllItemsBottomBar(String source);

    void setFocusOnBottomBarFirstItem();
    void close();
    void showBrowseParentFragment(BrowseProductActivityModel browseModel);
    void sendQueryBroadcast(String query);

    void sendChangeGridBroadcast(BrowseProductRouter.GridType gridType);
    void renderUpperCategoryLevel(SimpleCategory simpleCategory);

    int getCurrentSuggestionTab();
    void changeBottomBarGridIcon(int gridIconResId, int gridTitleResId);

    void showSearchPage();

    void startShareActivity(ShareData shareData);

    String getShareUrl();
    String getSource();

    void setDefaultGridTypeFromNetwork(Integer viewType);

    void launchOfficialStorePage();
}
