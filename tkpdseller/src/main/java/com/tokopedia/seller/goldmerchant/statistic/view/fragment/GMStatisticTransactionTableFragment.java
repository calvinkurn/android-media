package com.tokopedia.seller.goldmerchant.statistic.view.fragment;

import android.support.v4.app.Fragment;

import com.tokopedia.seller.base.view.adapter.BaseListAdapter;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.GMStatisticTransactionTableAdapter;
import com.tokopedia.seller.goldmerchant.statistic.view.adapter.model.GMStatisticTransactionTableModel;
import com.tokopedia.seller.goldmerchant.statistic.view.presenter.GMStatisticTransactionTablePresenter;
import com.tokopedia.seller.topads.keyword.view.fragment.TopAdsBaseListFragment;

/**
 * @author normansyahputa on 7/13/17.
 */
public class GMStatisticTransactionTableFragment extends TopAdsBaseListFragment<GMStatisticTransactionTablePresenter, GMStatisticTransactionTableModel> {

    public static final String TAG = "GMStatisticTransactionT";

    public static Fragment createInstance() {
        return new GMStatisticTransactionFragment();
    }

    @Override
    public void onItemClicked(GMStatisticTransactionTableModel gmStatisticTransactionTableModel) {

    }

    @Override
    protected BaseListAdapter getNewAdapter() {
        return new GMStatisticTransactionTableAdapter();
    }
}
