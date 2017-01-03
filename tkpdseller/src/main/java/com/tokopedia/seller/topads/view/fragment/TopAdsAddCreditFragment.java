package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.presenter.TopAdsAddCreditPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsAddCreditPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsPaymentCreditActivity;
import com.tokopedia.seller.topads.view.adapter.TopAdsCreditAdapter;
import com.tokopedia.seller.topads.view.listener.TopAdsAddCreditFragmentListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TopAdsAddCreditFragment extends BasePresenterFragment<TopAdsAddCreditPresenter> implements TopAdsAddCreditFragmentListener {

    private static String TAG = TopAdsAddCreditFragment.class.getSimpleName();

    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;

    @BindView(R2.id.button_submit)
    Button submitButton;

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
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {
        populateData();
    }

    private void populateData() {
        submitButton.setVisibility(View.GONE);
        presenter.populateCreditList();
        adapter.showLoadingFull(true);
    }

    @Override
    public void onCreditListLoaded(@NonNull List<DataCredit> creditList) {
        hideLoading();
        adapter.setData(creditList);
        submitButton.setVisibility(View.VISIBLE);
        submitButton.setEnabled(true);
    }

    @Override
    public void onLoadCreditListError() {
        hideLoading();
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                hideLoading();
                populateData();
            }
        });
    }

    private void hideLoading() {
        adapter.showLoadingFull(false);
        adapter.showEmpty(false);
        adapter.showRetry(false);
        submitButton.setVisibility(View.GONE);
    }

    @OnClick(R2.id.button_submit)
    void chooseCredit() {
        Intent intent = new Intent(getActivity(), TopAdsPaymentCreditActivity.class);
        intent.putExtra(TopAdsConstant.EXTRA_CREDIT, adapter.getSelectedCredit());
        startActivity(intent);
        getActivity().finish();
    }
}