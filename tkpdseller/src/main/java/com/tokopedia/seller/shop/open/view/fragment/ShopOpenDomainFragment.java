package com.tokopedia.seller.shop.open.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.lib.widget.TkpdTextInputLayout;

import com.tokopedia.seller.R;

/**
 * Created by Hendry on 3/17/2017.
 */

public class ShopOpenDomainFragment extends BasePresenterFragment {

    private View buttonSubmit;
    private TkpdTextInputLayout textInputShopName;
    private TkpdTextInputLayout textInputDomainName;

    public static ShopOpenDomainFragment newInstance() {
        Bundle args = new Bundle();
        // TODO need arguments?
        ShopOpenDomainFragment fragment = new ShopOpenDomainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_shop_open_domain;
    }

    @Override
    protected void initView(View view) {
        TextView textHello = (TextView) view.findViewById(R.id.text_hello);
        buttonSubmit = view.findViewById(R.id.button_submit);
        textInputShopName = (TkpdTextInputLayout) view.findViewById(R.id.text_input_shop_name);
        textInputDomainName = (TkpdTextInputLayout) view.findViewById(R.id.text_input_domain_name);

        String helloName = getString( R.string.hello_blank_name ) +
                " " +
                SessionHandler.getLoginName(getActivity());
        textHello.setText(helloName);

        buttonSubmit.setEnabled(false);
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void setActionVar() {

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
}
