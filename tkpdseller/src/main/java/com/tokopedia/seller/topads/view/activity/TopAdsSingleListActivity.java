package com.tokopedia.seller.topads.view.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.customwidget.SwipeToRefresh;
import com.tokopedia.core.util.RefreshHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.presenter.TopAdsListPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsListPresenterImpl;
import com.tokopedia.seller.topads.view.fragment.TopAdsPaymentCreditFragment;
import com.tokopedia.seller.topads.view.listener.TopAdsListPromoViewListener;
import com.tokopedia.seller.topads.view.adapter.TopAdsSingleListAdapter;
import com.tokopedia.seller.topads.view.adapter.TopAdsListAdapter;

import butterknife.BindView;

public class TopAdsSingleListActivity extends TActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.activity_top_ads_payment_credit);
        getFragmentManager().beginTransaction().disallowAddToBackStack()
                .add(R.id.container, TopAdsPaymentCreditFragment.createInstance(), TopAdsPaymentCreditFragment.class.getSimpleName())
                .commit();
    }

    @Override
    public String getScreenName() {
        return "TopAdsSingleListActivity";
    }
}
