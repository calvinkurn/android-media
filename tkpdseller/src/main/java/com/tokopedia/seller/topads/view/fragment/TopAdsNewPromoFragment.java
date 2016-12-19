package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.content.Intent;
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
import com.tokopedia.seller.topads.constant.TopAdsConstant;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.model.data.DataCredit;
import com.tokopedia.seller.topads.presenter.TopAdsNewPromoPresenter;
import com.tokopedia.seller.topads.presenter.TopAdsNewPromoPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsEditPromoActivity;
import com.tokopedia.seller.topads.view.activity.TopAdsPaymentCreditActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsNewPromoFragmentListener;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TopAdsNewPromoFragment extends BasePresenterFragment<TopAdsNewPromoPresenter> implements TopAdsNewPromoFragmentListener {


    @BindView(R2.id.radio_group)
    RadioGroup radioGroup;

    @BindView(R2.id.radio_button_new_group)
    RadioButton newGroupRadioButton;

    @BindView(R2.id.radio_button_exist_group)
    RadioButton existGroupRadioButton;

    @BindView(R2.id.radio_button_no_group)
    RadioButton noGroupRadioButton;

    @BindView(R2.id.edit_text_new_group_name)
    EditText newGroupNameEditText;

    @BindView(R2.id.edit_text_exist_group_name)
    EditText existGroupNameEditText;

    @BindView(R2.id.input_layout_new_group_name)
    TextInputLayout newGroupNameInputLayout;

    @BindView(R2.id.input_layout_exist_group_name)
    TextInputLayout existGroupNameInputLayout;

    private int newPromoType;

    public static TopAdsNewPromoFragment createInstance() {
        TopAdsNewPromoFragment fragment = new TopAdsNewPromoFragment();
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
        presenter = new TopAdsNewPromoPresenterImpl(getActivity(), this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_new_promo;
    }

    @Override
    protected void initView(View view) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if (checkedId == R.id.radio_button_new_group) {
                    showNewPromoGroup();
                } else if (checkedId == R.id.radio_button_exist_group) {
                    showNewPromoExistGroup();
                } else {
                    showNewPromoNoGroup();
                }
            }
        });
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {
        newPromoType = getActivity().getIntent().getIntExtra(TopAdsExtraConstant.EXTRA_NEW_PROMO_CHOICE, TopAdsExtraConstant.TYPE_NEW_PROMO_GROUP);
    }

    @Override
    protected void setActionVar() {
        if (newPromoType == TopAdsExtraConstant.TYPE_NEW_PROMO_GROUP) {
            showNewPromoGroup();
        } else if (newPromoType == TopAdsExtraConstant.TYPE_NEW_PROMO_EXIST_GROUP) {
            showNewPromoExistGroup();
        } else {
            showNewPromoNoGroup();
        }
    }

    @Override
    public void onGroupNameListLoaded(@NonNull List<DataCredit> creditList) {

    }

    @Override
    public void onLoadGroupNameListError() {

    }

    private void showNewPromoGroup() {
        resetView();
        if (!newGroupRadioButton.isChecked()) {
            newGroupRadioButton.setChecked(true);
        }
        newGroupNameInputLayout.setVisibility(View.VISIBLE);
        existGroupNameInputLayout.setVisibility(View.GONE);
    }

    private void showNewPromoExistGroup() {
        resetView();
        if (!existGroupRadioButton.isChecked()) {
            existGroupRadioButton.setChecked(true);
        }
        newGroupNameInputLayout.setVisibility(View.GONE);
        existGroupNameInputLayout.setVisibility(View.VISIBLE);
    }

    private void showNewPromoNoGroup() {
        resetView();
        if (!noGroupRadioButton.isChecked()) {
            noGroupRadioButton.setChecked(true);
        }
        newGroupNameInputLayout.setVisibility(View.GONE);
        existGroupNameInputLayout.setVisibility(View.GONE);
    }

    private void resetView() {
        newGroupNameEditText.setText("");
        existGroupNameEditText.setText("");
    }

    @OnClick(R2.id.button_submit)
    void chooseCredit() {
        Intent intent = new Intent(getActivity(), TopAdsEditPromoActivity.class);
        startActivity(intent);
    }
}