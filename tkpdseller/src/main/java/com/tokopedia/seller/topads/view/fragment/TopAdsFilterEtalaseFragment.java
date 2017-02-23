package com.tokopedia.seller.topads.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.core.customadapter.RetryDataBinder;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.etalase.Etalase;
import com.tokopedia.core.shopinfo.models.etalasemodel.EtalaseModel;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.domain.model.other.RadioButtonItem;
import com.tokopedia.seller.topads.view.listener.TopAdsEtalaseListView;
import com.tokopedia.seller.topads.view.presenter.RetrofitPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsEtalaseListPresenterImpl;

import java.util.ArrayList;
import java.util.List;

public class TopAdsFilterEtalaseFragment extends TopAdsFilterRadioButtonFragment<TopAdsEtalaseListPresenterImpl>
    implements TopAdsEtalaseListView{

    private int selectedEtalaseId;

    public static TopAdsFilterEtalaseFragment createInstance(int etalaseID) {
        TopAdsFilterEtalaseFragment fragment = new TopAdsFilterEtalaseFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, etalaseID);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_filter_content_etalase;
    }

    @Override
    protected void setupArguments(Bundle bundle) {
        super.setupArguments(bundle);
        selectedEtalaseId = bundle.getInt(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE, selectedEtalaseId);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        adapter.setOnRetryListenerRV(new RetryDataBinder.OnRetryListener() {
            @Override
            public void onRetryCliked() {
                presenter.populateEtalaseList();
            }
        });
    }

    @Override
    protected void initialPresenter() {
        presenter = new TopAdsEtalaseListPresenterImpl(getActivity(), this);
    }

    @Override
    protected List<RadioButtonItem> getRadioButtonList() {
        // will populate radio item from API
        return null;
    }

    private void updateSelectedPosition(List<RadioButtonItem> radioButtonItemList) {
        if (selectedAdapterPosition > -1) {
            return;
        }
        for (int i = 0; i < radioButtonItemList.size(); i++) {
            RadioButtonItem radioButtonItem = radioButtonItemList.get(i);
            if (Integer.valueOf(radioButtonItem.getValue()) == selectedEtalaseId) {
                selectedAdapterPosition = i;
                break;
            }
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getString(R.string.label_top_ads_etalase);
    }

    @Override
    public Intent addResult(Intent intent) {
        intent.putExtra(TopAdsExtraConstant.EXTRA_FILTER_SELECTED_ETALASE,
                Integer.parseInt(getSelectedRadioValue()));
        return intent;
    }

    @Override
    public void onLoadSuccess(@NonNull List<com.tokopedia.core.shopinfo.models.etalasemodel.List> etalaseModelList) {
        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        radioButtonItemList.add(getDefaultRadioButton());

        for (int i = 0; i < etalaseModelList.size(); i++) {
            RadioButtonItem radioButtonItem = new RadioButtonItem();
            radioButtonItem.setName(etalaseModelList.get(i).etalaseName);
            radioButtonItem.setValue(etalaseModelList.get(i).etalaseId);
            radioButtonItem.setPosition(i);
            radioButtonItemList.add(radioButtonItem);
        }

        updateSelectedPosition(radioButtonItemList);

        setAdapterData(radioButtonItemList);
    }

    @Override
    public void onLoadSuccessEtalaseEmpty() {
        List<RadioButtonItem> radioButtonItemList = new ArrayList<>();
        radioButtonItemList.add(getDefaultRadioButton());

        selectedAdapterPosition = 0;

        setAdapterData(radioButtonItemList);
    }

    private RadioButtonItem getDefaultRadioButton (){
        RadioButtonItem defaultRadioButton = new RadioButtonItem();
        defaultRadioButton.setName(getString(R.string.title_all_etalase));
        defaultRadioButton.setValue("0");
        defaultRadioButton.setPosition(0);
        return defaultRadioButton;
    }

    @Override
    public void onLoadConnectionError() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.error_connection_problem));
        adapter.showRetry(true);
    }

    @Override
    public void onLoadError(String message) {
        NetworkErrorHelper.showSnackbar(getActivity(), message);
        adapter.showRetry(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.populateEtalaseList();
    }

    @Override
    public void showLoad(boolean isShow) {
        adapter.showLoadingFull(isShow);
    }
}