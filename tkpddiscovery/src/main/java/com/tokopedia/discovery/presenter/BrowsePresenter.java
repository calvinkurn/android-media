package com.tokopedia.discovery.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.tokopedia.core.discovery.model.DataValue;
import com.tokopedia.core.network.entity.discovery.BrowseProductActivityModel;
import com.tokopedia.core.router.discovery.BrowseProductRouter;

/**
 * Created by HenryPri on 29/03/17.
 */

public interface BrowsePresenter {
    void initPresenterData(Bundle savedInstanceState, Intent intent);
    void disposePresenterData();

    void onSaveInstanceState(Bundle outState);
    void onRestoreInstanceState(Bundle savedInstanceState);

    void handleResultData(int requestCode, Intent data);

    void onBottomBarChanged(String source);
    boolean onBottomBarTabSelected(String source, int position, int activeTab, boolean isShopFragment);

    void fetchHotListHeader(String alias);

    boolean sendQuery(String query, String depId);
    void sendHotlist(String selected, String keyword);

    void resetBrowseProductActivityModel();
    BrowseProductActivityModel getBrowseProductActivityModel();
    boolean isFromCategory();

    void setFilterAttribute(DataValue filterAttribute, int activeTab);
    boolean checkHasFilterAttributeIsNull(int activeTab);

    void onSetFragment(int fragmentId);

    void onBackPressed();

    void onRenderLowerCategoryLevel(String departmentId, String name, String title);

    String getSearchQuery();

    BrowseProductRouter.GridType getGridType();

    void onRenderUpperCategoryLevel(String departmentId);

    void retrieveLastGridConfig(final String departmentId);

    void setDefaultGridTypeFromNetwork(Integer viewType);
}
