package com.tokopedia.discovery.model;

import com.google.gson.Gson;
import com.tokopedia.core.network.apiservices.topads.api.TopAdsApi;
import com.tokopedia.discovery.activity.BrowseProductActivity;
import com.tokopedia.discovery.dynamicfilter.presenter.DynamicFilterPresenter;
import com.tokopedia.discovery.fragment.browseparent.ProductFragment;

import org.parceler.Parcel;

import java.util.Map;

/**
 * Created by noiz354 on 7/15/16.
 */
@Parcel
public class BrowseProductActivityModel {
    protected String parentDepartement = "0";
    protected String departmentId = "0";
    protected int fragmentId = BrowseProductActivity.INVALID_FRAGMENT_ID;
    protected String adSrc = TopAdsApi.SRC_BROWSE_PRODUCT;
    HotListBannerModel hotListBannerModel;
    public String alias;
    public String q = "";
    public String ob; //set to default sort
    public String obCatalog; //set to default sort
    public boolean isSearchDeeplink = false;
    public String source = DynamicFilterPresenter.VALUES_DYNAMIC_FILTER_SEARCH_PRODUCT;
    public int activeTab;
    public String unique_id;
    public Map<String, String> filterOptions;

    public HotListBannerModel getHotListBannerModel() {
        return hotListBannerModel;
    }

    public void setHotListBannerModel(HotListBannerModel hotListBannerModel) {
        this.hotListBannerModel = hotListBannerModel;

        fragmentId = ProductFragment.VALUES_PRODUCT_FRAGMENT_ID;
        adSrc = TopAdsApi.SRC_HOTLIST;
    }

    public void removeBannerModel() {
        this.hotListBannerModel = null;
        fragmentId = ProductFragment.VALUES_PRODUCT_FRAGMENT_ID;
    }

    public String getParentDepartement() {
        return parentDepartement;
    }

    public void setParentDepartement(String parentDepartement) {
        this.parentDepartement = parentDepartement;
    }

    public boolean isSearchDeeplink() {
        return isSearchDeeplink;
    }

    public void setSearchDeeplink(boolean searchDeeplink) {
        isSearchDeeplink = searchDeeplink;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getUnique_id() {
        return unique_id;
    }

    public void setUnique_id(String unique_id) {
        this.unique_id = unique_id;
    }

    public int getActiveTab() {
        return activeTab;
    }

    public void setActiveTab(int tab) {
        this.activeTab = tab;
    }

    public String getAdSrc() {
        return adSrc;
    }

    public void setAdSrc(String adSrc) {
        this.adSrc = adSrc;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public int getFragmentId() {
        return fragmentId;
    }

    public void setFragmentId(int fragmentId) {
        this.fragmentId = fragmentId;
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getOb() {
        return ob;
    }

    public void setOb(String ob) {
        this.ob = ob;
    }

    public String getObCatalog() {
        return obCatalog;
    }

    public void setObCatalog(String obPosition) {
        this.obCatalog = obPosition;
    }

    public Map<String, String> getFilterOptions() {
        return filterOptions;
    }

    public void setFilterOptions(Map<String, String> filterOptions) {
        this.filterOptions = filterOptions;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
