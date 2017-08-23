package com.tokopedia.seller.base.view.fragment;

import android.view.View;

import com.tokopedia.design.text.SearchInputView;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.ItemType;

/**
 * @author normansyahputa on 5/17/17.
 *         another type of {@link com.tokopedia.seller.topads.dashboard.view.fragment.TopAdsAdListFragment}
 */

public abstract class BaseSearchListFragment<P, T extends ItemType> extends BaseListFragment<P, T> implements SearchInputView.Listener {

    protected SearchInputView searchInputView;

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_base_search_list;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        searchInputView = (SearchInputView) view.findViewById(R.id.search_input_view);
        searchInputView.setListener(this);
    }
}