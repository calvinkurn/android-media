package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.Logger;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.model.data.DataDeposit;
import com.tokopedia.seller.topads.model.data.Summary;
import com.tokopedia.seller.topads.presenter.TopAdsAddCreditPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsAddCreditPresenterImpl;
import com.tokopedia.seller.topads.presenter.TopAdsDashboardProductPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsPaymentCreditActivity;
import com.tokopedia.seller.topads.view.adapter.TopAdsCreditAdapter;
import com.tokopedia.seller.topads.view.listener.TopAdsAddCreditFragmentListener;
import com.tokopedia.seller.topads.view.listener.TopAdsDashboardFragmentListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.OnClick;

public class TopAdsAddCreditFragment extends BasePresenterFragment<TopAdsAddCreditPresenter> implements TopAdsAddCreditFragmentListener {

    private static String TAG = TopAdsAddCreditFragment.class.getSimpleName();

    @Bind(R2.id.recycler_view)
    RecyclerView recyclerView;

    private TopAdsCreditAdapter adapter;

    public static TopAdsAddCreditFragment createInstance() {
        TopAdsAddCreditFragment fragment = new TopAdsAddCreditFragment();
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsAddCreditPresenterImpl(getActivity(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_add_credit;
    }

    @Override
    protected void initView(View view) {
        adapter = new TopAdsCreditAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        presenter.populateCreditList();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onCreditListLoaded(@NonNull List<DataCredit> creditList) {
        adapter.setCreditList(creditList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadCreditListError() {

    }

    @OnClick(R2.id.button_choose_credit)
    void chooseCredit() {
        Intent intent = new Intent(getActivity(), TopAdsPaymentCreditActivity.class);
        intent.putExtra(TopAdsConstant.EXTRA_CREDIT, adapter.getSelectedCredit());
        startActivity(intent);
    }
}