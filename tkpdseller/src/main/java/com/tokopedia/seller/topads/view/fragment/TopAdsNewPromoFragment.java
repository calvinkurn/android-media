package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.constant.TopAdsExtraConstant;
import com.tokopedia.seller.topads.domain.model.data.DataCredit;
import com.tokopedia.seller.topads.view.presenter.TopAdsNewPromoPresenter;
import com.tokopedia.seller.topads.view.presenter.TopAdsNewPromoPresenterImpl;
import com.tokopedia.seller.topads.view.activity.TopAdsEditPromoGroupActivity;
import com.tokopedia.seller.topads.view.listener.TopAdsNewPromoFragmentListener;

import java.util.List;

public class TopAdsNewPromoFragment extends BasePresenterFragment<TopAdsNewPromoPresenter> implements TopAdsNewPromoFragmentListener {

    RadioGroup radioGroup;
    RadioButton newGroupRadioButton;
    RadioButton existGroupRadioButton;
    RadioButton noGroupRadioButton;
    EditText newGroupNameEditText;
    EditText existGroupNameEditText;
    TextInputLayout newGroupNameInputLayout;
    TextInputLayout existGroupNameInputLayout;
    Button submitButton;

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
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        newGroupRadioButton = (RadioButton) view.findViewById(R.id.radio_button_new_group);
        existGroupRadioButton = (RadioButton) view.findViewById(R.id.radio_button_exist_group);
        noGroupRadioButton = (RadioButton) view.findViewById(R.id.radio_button_no_group);
        newGroupNameEditText = (EditText) view.findViewById(R.id.edit_text_new_group_name);
        existGroupNameEditText = (EditText) view.findViewById(R.id.edit_text_exist_group_name);
        newGroupNameInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_new_group_name);
        existGroupNameInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_exist_group_name);
        submitButton = (Button) view.findViewById(R.id.button_submit);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCredit();
            }
        });
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

    void chooseCredit() {
        Intent intent = new Intent(getActivity(), TopAdsEditPromoGroupActivity.class);
        startActivity(intent);
    }
}