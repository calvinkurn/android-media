package com.tokopedia.seller.topads.keyword.view.fragment;

import android.app.Activity;
import android.content.Intent;

import com.tokopedia.seller.topads.keyword.view.presenter.TopAdsKeywordListPresenter;
import com.tokopedia.seller.topads.view.fragment.TopAdsAdListFragment;
import com.tokopedia.seller.topads.view.presenter.TopAdsAdListPresenter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author normansyahputa on 5/17/17.
 */
public abstract class TopAdsBaseKeywordListFragment<T extends TopAdsAdListPresenter> extends TopAdsAdListFragment<T> {

    public static final int REQUEST_CODE_FILTER_KEYWORD = 21;
    protected static final int REQUEST_CODE_CREATE_KEYWORD = 20;
    protected String keyword;
    protected Map<String, String> filters = new HashMap<>();

    public void onSearchChanged(String query) {
        keyword = query;
        searchAd(START_PAGE);
        updateEmptyViewNoResult();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == REQUEST_CODE_FILTER_KEYWORD && intent != null) {
            filters.putAll(parseFilter(resultCode, intent));
            searchAd(START_PAGE);
            updateEmptyViewNoResult();
        } else if (requestCode == REQUEST_CODE_CREATE_KEYWORD){
            if (resultCode == Activity.RESULT_OK) {
                onSearchChanged(null);
            }
        }
    }

    public Map<String, String> parseFilter(int resultCode, Intent intent) {
        return new HashMap<>();
    }

    public abstract void onFilterChanged(Object someObject);

    public abstract void onCreateKeyword();
}
