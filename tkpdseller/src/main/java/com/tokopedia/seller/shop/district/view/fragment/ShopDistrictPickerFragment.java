package com.tokopedia.seller.shop.district.view.fragment;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseSearchListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.shop.district.data.source.database.model.DistrictDB;

/**
 * Created by nathan on 10/23/17.
 */

public class ShopDistrictPickerFragment extends BaseSearchListFragment<BlankPresenter, DistrictDB> {

    public static ShopDistrictPickerFragment createInstance() {
        ShopDistrictPickerFragment fragment = new ShopDistrictPickerFragment();
        return fragment;
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected BaseListAdapter<DistrictDB> getNewAdapter() {
        return null;
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(DistrictDB districtDB) {

    }
}