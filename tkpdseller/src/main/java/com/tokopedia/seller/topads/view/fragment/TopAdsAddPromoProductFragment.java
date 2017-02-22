package com.tokopedia.seller.topads.view.fragment;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.tokopedia.seller.R;

/**
 * Created by zulfikarrahman on 2/22/17.
 */

public class TopAdsAddPromoProductFragment extends TopAdsBaseManagePromoProductFragment {

    RadioGroup radioGroup;
    EditText inputNewGroup;
    TextInputLayout textInputLayoutNewGroup;

    public static TopAdsAddPromoProductFragment createInstance() {
        TopAdsAddPromoProductFragment fragment = new TopAdsAddPromoProductFragment();
        return fragment;
    }

    @Override
    protected boolean validateForm() {
        return false;
    }

    @Override
    protected String getValueOption() {
        return null;
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_top_ads_add_promo_product;
    }

    @Override
    protected void initView(View view) {
        super.initView(view);
        radioGroup = (RadioGroup) view.findViewById(R.id.radio_group);
        inputNewGroup = (EditText) view.findViewById(R.id.input_new_group);
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
        super.setViewListener();
    }

    @Override
    public void onCheckGroupExistError(String message) {
        inputNewGroup.setError(message);
    }

    @Override
    public void onGroupExist() {
        inputNewGroup.setError(getString(R.string.top_ads_title_grup_exist));
    }

    @Override
    public void onGroupNotExist() {
        inputNewGroup.setError(null);
    }
}
