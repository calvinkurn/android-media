package com.tokopedia.topads.dashboard.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.topads.R;
import com.tokopedia.seller.base.view.fragment.BasePresenterFragment;
import com.tokopedia.topads.dashboard.constant.TopAdsConstant;
import com.tokopedia.topads.dashboard.data.model.data.DataCredit;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAddCreditPresenter;
import com.tokopedia.topads.dashboard.view.presenter.TopAdsAddCreditPresenterImpl;
import com.tokopedia.topads.dashboard.view.activity.TopAdsPaymentCreditActivity;
import com.tokopedia.topads.dashboard.view.adapter.TopAdsCreditAdapter;
import com.tokopedia.topads.dashboard.view.listener.TopAdsAddCreditFragmentListener;

import java.util.List;

public class TopAdsAddCreditFragment extends BasePresenterFragment<TopAdsAddCreditPresenter> implements TopAdsAddCreditFragmentListener {

    private static final String EXTRA_SELECTION_POSITION = "EXTRA_SELECTION_POSITION";

    private RecyclerView recyclerView;
    private Button submitButton;

    private TopAdsCreditAdapter adapter;

    public static Fragment createInstance() {
        Fragment fragment = new TopAdsAddCreditFragment();
        return fragment;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsAddCreditPresenterImpl(getActivity(), this);
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
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        submitButton = (Button) view.findViewById(R.id.button_submit);
        adapter = new TopAdsCreditAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCredit();
            }
        });
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
        if (!adapter.isChecked()) {
            adapter.setCheckedPosition(getDefaultSelection(creditList));
        }
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

    private int getDefaultSelection(List<DataCredit> creditList) {
        for (int i = 0; i < creditList.size(); i++) {
            DataCredit dataCredit = creditList.get(i);
            if (dataCredit.getSelected() > 0) {
                return i;
            }
        }
        return 0;
    }

    private void chooseCredit() {
        if(adapter.getSelectedCredit() != null) {
            getActivity().setResult(Activity.RESULT_OK);
            Intent intent = new Intent(getActivity(), TopAdsPaymentCreditActivity.class);
            intent.putExtra(TopAdsConstant.EXTRA_CREDIT, adapter.getSelectedCredit());
            startActivity(intent);
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unSubscribe();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_SELECTION_POSITION, adapter.getCheckedPosition());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) {
            return;
        }
        adapter.setCheckedPosition(savedInstanceState.getInt(EXTRA_SELECTION_POSITION));
    }
}