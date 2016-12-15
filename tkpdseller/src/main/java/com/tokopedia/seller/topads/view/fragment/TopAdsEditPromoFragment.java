package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.presenter.TopAdsEditPromoPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsEditPromoPresenterImpl;
import com.tokopedia.seller.topads.presenter.TopAdsNewPromoPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsNewPromoPresenterImpl;
import com.tokopedia.seller.topads.view.listener.TopAdsEditPromoFragmentListener;
import com.tokopedia.seller.topads.view.listener.TopAdsNewPromoFragmentListener;

import java.util.List;

import butterknife.BindView;

public class TopAdsEditPromoFragment extends BasePresenterFragment<TopAdsEditPromoPresenter> implements TopAdsEditPromoFragmentListener {

    public static TopAdsEditPromoFragment createInstance() {
        TopAdsEditPromoFragment fragment = new TopAdsEditPromoFragment();
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
        presenter = new TopAdsEditPromoPresenterImpl(getActivity(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_edit_promo;
    }

    @Override
    protected void initView(View view) {

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
    public void onGroupNameListLoaded(@NonNull List<DataCredit> creditList) {

    }

    @Override
    public void onLoadGroupNameListError() {

    }
}