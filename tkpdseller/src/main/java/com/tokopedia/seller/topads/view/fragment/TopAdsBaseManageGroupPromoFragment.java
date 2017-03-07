package com.tokopedia.seller.topads.view.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.seller.R;
import com.tokopedia.seller.topads.di.TopAdsAddPromoPoductDI;
import com.tokopedia.seller.topads.data.model.data.GroupAd;
import com.tokopedia.seller.topads.view.adapter.TopAdsAutoCompleteAdapter;
import com.tokopedia.seller.topads.view.presenter.TopAdsManageGroupPromoPresenter;
import com.tokopedia.seller.topads.view.listener.TopAdsManageGroupPromoView;
import com.tokopedia.seller.topads.view.widget.TopAdsCustomAutoCompleteTextView;
import com.tokopedia.seller.topads.view.widget.TopAdsCustomRadioGroup;
import com.tokopedia.seller.topads.view.widget.TopAdsRadioExpandView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zulfikarrahman on 2/16/17.
 */

public abstract class TopAdsBaseManageGroupPromoFragment<T extends TopAdsManageGroupPromoPresenter> extends BasePresenterFragment<T>
        implements TopAdsManageGroupPromoView {

    private TopAdsCustomRadioGroup radioGroup;

    protected EditText inputNewGroup;
    private TextInputLayout textInputLayoutNewGroup;
    private TextView viewInfoNewGroup;
    protected TopAdsRadioExpandView viewRadioNewGroup;

    protected TopAdsCustomAutoCompleteTextView inputChooseGroup;
    private TextView viewInfoChooseGroup;
    private TextInputLayout textInputLayoutChooseGroup;
    protected TopAdsRadioExpandView viewRadioChooseGroup;

    protected TopAdsRadioExpandView viewRadioNotInGroup;
    private Button buttonNext;

    private TopAdsAutoCompleteAdapter adapterChooseGroup;
    private ArrayList<String> groupNames = new ArrayList<>();
    private List<GroupAd> groupAds = new ArrayList<>();
    protected String choosenId;
    private ProgressDialog progressDialog;

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    protected void initialPresenter() {
        presenter = (T) TopAdsAddPromoPoductDI.createPresenter(getActivity());
        presenter.attachView(this);
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
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_manage_promo_product;
    }

    @Override
    protected void initView(View view) {
        buttonNext = (Button) view.findViewById(R.id.button_next);
        radioGroup = (TopAdsCustomRadioGroup) view.findViewById(R.id.radio_group);
        inputNewGroup = (EditText) view.findViewById(R.id.input_new_group);
        textInputLayoutNewGroup = (TextInputLayout) view.findViewById(R.id.input_layout_new_group_name);
        inputChooseGroup = (TopAdsCustomAutoCompleteTextView) view.findViewById(R.id.choose_group_auto_text);
        textInputLayoutChooseGroup = (TextInputLayout) view.findViewById(R.id.input_layout_choose_group);
        viewRadioNewGroup = (TopAdsRadioExpandView) view.findViewById(R.id.view_radio_new_group);
        viewRadioChooseGroup = (TopAdsRadioExpandView) view.findViewById(R.id.view_radio_choose_group);
        viewRadioNotInGroup = (TopAdsRadioExpandView) view.findViewById(R.id.view_radio_not_in_group);
        viewInfoNewGroup = (TextView) view.findViewById(R.id.view_info_radio_new_group);
        viewInfoChooseGroup = (TextView) view.findViewById(R.id.view_info_radio_choose_group);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        buttonNext.setText(getTitleButtonNext());
        setVisibilityOption();
        setVisibilityInfoOption();
    }

    @Override
    protected void setViewListener() {
        buttonNext.setOnClickListener(onClickNext());
        inputNewGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.checkIsGroupExist(editable.toString());
            }
        });
        inputChooseGroup.setAdapter(adapterChooseGroup);
        inputChooseGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.searchGroupName(editable.toString());
            }
        });
        inputChooseGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                inputChooseGroup.lockView();
                if(groupAds.get(i) != null) {
                    choosenId = groupAds.get(i).getId();
                }
            }
        });
    }

    @Override
    protected void initialVar() {
        adapterChooseGroup = new TopAdsAutoCompleteAdapter(getActivity(), android.R.layout.simple_dropdown_item_1line);
        adapterChooseGroup.setListenerGetData(new TopAdsAutoCompleteAdapter.ListenerGetData() {
            @Override
            public ArrayList<String> getData() {
                return groupNames;
            }
        });
    }

    @Override
    protected void setActionVar() {

    }


    @Override
    public void onCheckGroupExistError(String message) {
        textInputLayoutNewGroup.setError(message);
    }

    @Override
    public void onGroupExist() {
        textInputLayoutNewGroup.setError(getString(R.string.top_ads_title_grup_exist));
    }

    @Override
    public void onGroupNotExist() {
        textInputLayoutNewGroup.setError(null);
    }

    @Override
    public void onGetGroupAdList(List<GroupAd> groupAds) {
        this.groupAds.clear();
        this.groupAds.addAll(groupAds);
        this.groupNames.clear();
        textInputLayoutChooseGroup.setError(null);
        for(GroupAd groupAd : groupAds){
            groupNames.add(groupAd.getName());
        }
        adapterChooseGroup.getFilter().filter(inputChooseGroup.getText());
    }

    @Override
    public void onGetGroupAdListError(String message) {
        textInputLayoutChooseGroup.setError(message);
    }

    @Override
    public void onGroupNotExistOnSubmitNewGroup() {
        onGroupNotExist();
        onSubmitFormNewGroup(inputNewGroup.getText().toString());
    }

    private View.OnClickListener onClickNext() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onValidateForm(radioGroup.getCheckedRadioButtonId());
            }
        };
    }

    @Override
    public void showLoading() {
        progressDialog.show();
    }

    @Override
    public void showErrorShouldFillGroupName() {
        textInputLayoutNewGroup.setError(getString(R.string.label_top_ads_error_empty_group_name));
    }

    @Override
    public void dismissLoading() {
        progressDialog.dismiss();
    }

    protected void onValidateForm(int checkedRadioButtonId) {
        if(checkedRadioButtonId == R.id.radio_promo_new_group){
            onSubmitPromoNewGroup();
        }else if(checkedRadioButtonId == R.id.radio_promo_choose_group){
            onSubmitPromoChooseGroup();
        }else if(checkedRadioButtonId == R.id.radio_promo_not_in_group){
            onSubmitPromoNotInGroup();
        }else {
            onErrorSubmit();
        }
    }

    private void onSubmitPromoNotInGroup() {
        onSubmitFormNotInGroup();
    }

    private void onSubmitPromoChooseGroup() {
        if(!inputChooseGroup.isEnabled()){
            onSubmitFormChooseGroup(choosenId);
        }else{
            textInputLayoutChooseGroup.setError(getString(R.string.label_top_ads_error_choose_one_group));
        }
    }

    private void onSubmitPromoNewGroup() {
        presenter.checkIsGroupExistOnSubmitNewGroup(inputNewGroup.getText().toString());
    }

    protected void onErrorSubmit() {
        NetworkErrorHelper.showSnackbar(getActivity(), getString(R.string.label_top_ads_error_choose_one_option));
    }

    private void setVisibilityOption() {
        if(getVisibleNewGroupOption()){
            viewRadioNewGroup.setVisibility(View.VISIBLE);
        }else{
            viewRadioNewGroup.setVisibility(View.GONE);
        }

        if(getVisibleChooseGroupOption()){
            viewRadioChooseGroup.setVisibility(View.VISIBLE);
        }else{
            viewRadioChooseGroup.setVisibility(View.GONE);
        }

        if(getVisibleNotInGroupOption()){
            viewRadioNotInGroup.setVisibility(View.VISIBLE);
        }else{
            viewRadioNotInGroup.setVisibility(View.GONE);
        }
    }

    private void setVisibilityInfoOption() {
        if(getVisibleInfoNewGroupOption()){
            viewInfoNewGroup.setVisibility(View.VISIBLE);
            viewInfoNewGroup.setText(getTextInfoNewGroupOption());
        }else{
            viewInfoNewGroup.setVisibility(View.GONE);
        }

        if(getVisibleInfoChooseGroupOption()){
            viewInfoChooseGroup.setText(getTextInfoChooseGroupOption());
            viewInfoChooseGroup.setVisibility(View.VISIBLE);
        }else{
            viewInfoChooseGroup.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    protected abstract String getTextInfoChooseGroupOption();

    protected abstract String getTextInfoNewGroupOption();

    protected abstract boolean getVisibleInfoChooseGroupOption();

    protected abstract boolean getVisibleInfoNewGroupOption();

    protected abstract boolean getVisibleNotInGroupOption();

    protected abstract boolean getVisibleChooseGroupOption();

    protected abstract boolean getVisibleNewGroupOption();

    protected abstract void onSubmitFormNewGroup(String groupName);

    protected abstract void onSubmitFormNotInGroup();

    protected abstract void onSubmitFormChooseGroup(String choosenId);

    protected abstract String getTitleButtonNext();
}
