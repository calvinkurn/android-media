package com.tokopedia.seller.topads.view.fragment;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.tokopedia.seller.R;
import com.tokopedia.seller.R2;
import com.tokopedia.seller.topads.domain.model.data.GroupAd;
import com.tokopedia.seller.topads.view.widget.TopAdsCustomAutoCompleteTextView;
import com.tokopedia.seller.topads.view.widget.TopAdsCustomRadioGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import rx.Subscription;

/**
 * Created by zulfikarrahman on 2/22/17.
 */

public class TopAdsAddPromoProductFragment extends TopAdsBaseManagePromoProductFragment {

    public static final int CHOICE_GROUP = 2;
    private TopAdsCustomRadioGroup radioGroup;

    private EditText inputNewGroup;
    private TextInputLayout textInputLayoutNewGroup;

    private TopAdsCustomAutoCompleteTextView inputChooseGroup;
    private TextInputLayout textInputLayoutChooseGroup;

    private ArrayAdapter<String> adapterChooseGroup;
    private List<String> groupNames;
    private List<GroupAd> groupAds = new ArrayList<>();
    private int choosenId;

    public static TopAdsAddPromoProductFragment createInstance() {
        TopAdsAddPromoProductFragment fragment = new TopAdsAddPromoProductFragment();
        return fragment;
    }

    @Override
    protected void onValidateForm() {
        switch (radioGroup.getCheckedRadioButtonId()){
            case R2.id.radio_promo_new_group:
                onSubmitPromoNewGroup();
                break;
            case R2.id.radio_promo_choose_group:
                onSubmitPromoChooseGroup();
                break;
            case R2.id.radio_promo_not_in_group:
                break;
            default:
                onErrorSubmit();
                break;
        }
    }

    private void onSubmitPromoChooseGroup() {
        if(inputChooseGroup.isEnabled()){
            onSubmitForm(CHOICE_GROUP, choosenId, inputChooseGroup.getText().toString());
        }else{
            textInputLayoutChooseGroup.setError(getString(R.string.label_top_ads_error_choose_one_group));
        }
    }

    private void onSubmitPromoNewGroup() {
        showLoading();
        presenter.checkIsGroupExist(inputNewGroup.getText().toString());
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_add_promo_product;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        radioGroup = (TopAdsCustomRadioGroup) view.findViewById(R.id.radio_group);
        inputNewGroup = (EditText) view.findViewById(R.id.input_new_group);
        textInputLayoutNewGroup = (TextInputLayout) view.findViewById(R.id.input_layout_new_group_name);
        inputChooseGroup = (TopAdsCustomAutoCompleteTextView) view.findViewById(R.id.choose_group_auto_text);
        textInputLayoutChooseGroup = (TextInputLayout) view.findViewById(R.id.input_layout_choose_group);
    }

    @Override
    protected void initialVar() {
        super.initialVar();
        groupNames = new ArrayList<>();
        adapterChooseGroup = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, groupNames);
    }

    @Override
    protected void setViewListener() {
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
        super.setViewListener();
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
        adapterChooseGroup.clear();
        textInputLayoutChooseGroup.setError(null);
        for(GroupAd groupAd : groupAds){
            adapterChooseGroup.add(groupAd.getName());
        }
    }

    @Override
    public void onGetGroupAdListError(String message) {
        textInputLayoutChooseGroup.setError(message);
    }
}
