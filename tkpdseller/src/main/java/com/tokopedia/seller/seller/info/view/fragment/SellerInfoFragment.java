package com.tokopedia.seller.seller.info.view.fragment;

import android.os.Bundle;
import android.view.View;

import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.seller.info.view.adapter.SellerInfoAdapter;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;
import com.tokopedia.seller.seller.info.view.util.SellerInfoDateUtil;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoFragment extends BaseListFragment<BlankPresenter, SellerInfoModel> {

    private String[] monthNamesAbrev;
    private SellerInfoDateUtil sellerInfoDateUtil;

    public static SellerInfoFragment newInstance(){
        return new SellerInfoFragment();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        monthNamesAbrev = getResources().getStringArray(R.array.lib_date_picker_month_entries);
        sellerInfoDateUtil = new SellerInfoDateUtil(monthNamesAbrev);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected BaseListAdapter<SellerInfoModel> getNewAdapter() {
        return new SellerInfoAdapter(sellerInfoDateUtil);
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(SellerInfoModel sellerInfoModel) {

    }
}
