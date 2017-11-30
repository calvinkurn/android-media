package com.tokopedia.seller.seller.info.view.fragment;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.base.view.fragment.BaseListFragment;
import com.tokopedia.seller.base.view.presenter.BlankPresenter;
import com.tokopedia.seller.seller.info.view.model.SellerInfoModel;

/**
 * Created by normansyahputa on 11/30/17.
 */

public class SellerInfoFragment extends BaseListFragment<BlankPresenter, SellerInfoModel> {

    public static SellerInfoFragment newInstance(){
        return new SellerInfoFragment();
    }

    @Override
    protected BaseListAdapter<SellerInfoModel> getNewAdapter() {
        return null;
    }

    @Override
    protected void searchForPage(int page) {

    }

    @Override
    public void onItemClicked(SellerInfoModel sellerInfoModel) {

    }
}
