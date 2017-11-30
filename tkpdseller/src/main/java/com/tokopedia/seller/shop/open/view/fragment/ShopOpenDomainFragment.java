package com.tokopedia.seller.shop.open.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.common.widget.PrefixEditText;
import com.tokopedia.seller.lib.widget.TkpdTextInputLayout;
import com.tokopedia.seller.shop.common.di.component.ShopComponent;
import com.tokopedia.seller.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.open.view.listener.ShopOpenDomainView;
import com.tokopedia.seller.shop.open.view.presenter.ShopOpenDomainPresenter;

import javax.inject.Inject;

import rx.Subscription;

/**
 * Created by Hendry on 3/17/2017.
 */

public class ShopOpenDomainFragment extends BaseDaggerFragment implements ShopOpenDomainView {

    public static final String TAG = "ShopOpenDomain";

    @Inject
    ShopOpenDomainPresenter presenter;
    ShopOpenDomainComponent component;
    private View buttonSubmit;
    private TkpdTextInputLayout textInputShopName;
    private EditText editTextInputShopName;
    private TkpdTextInputLayout textInputDomainName;
    private PrefixEditText editTextInputDomainName;
    // untuk test
    private Subscription subscription;

    public static ShopOpenDomainFragment newInstance() {
        Bundle args = new Bundle();
        // TODO need arguments?
        ShopOpenDomainFragment fragment = new ShopOpenDomainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void initInjector() {
        component = DaggerShopOpenDomainComponent
                .builder()
                .shopOpenDomainModule(new ShopOpenDomainModule())
                .shopComponent(getComponent(ShopComponent.class))
                .build();
        component.inject(this);

        presenter.attachView(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_open_domain, container, false);
        TextView textHello = view.findViewById(R.id.text_hello);
        buttonSubmit = view.findViewById(R.id.button_submit);
        textInputShopName = view.findViewById(R.id.text_input_shop_name);
        textInputDomainName = view.findViewById(R.id.text_input_domain_name);
        editTextInputShopName = textInputShopName.getEditText();
        editTextInputDomainName = (PrefixEditText) textInputDomainName.getEditText();

        String helloName = getString(R.string.hello_blank_name) + " " + SessionHandler.getLoginName(getActivity());
        textHello.setText(helloName);

        buttonSubmit.setEnabled(false);

        editTextInputShopName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputShopName.setSuccess("");
                buttonSubmit.setEnabled(false);
                presenter.checkShop(editTextInputShopName.getText().toString());
            }
        });

        editTextInputDomainName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                textInputDomainName.setSuccess("");
                buttonSubmit.setEnabled(false);
                presenter.checkDomain(editTextInputDomainName.getTextWithoutPrefix());
            }
        });
        return view;
    }

    @Override
    public void onSuccessCheckShopName(boolean existed) {
        if (existed) {
            textInputShopName.setSuccess(getString(R.string.shop_name_available));
            checkEnableSubmit();
        } else {
            textInputShopName.setError(getString(R.string.shop_name_not_available));
        }
    }

    @Override
    public void onErrorCheckShopName(Throwable t) {
        textInputShopName.setError(getString(R.string.shop_name_not_available));
    }

    @Override
    public void onSuccessCheckShopDomain(boolean existed) {
        if (existed) {
            textInputDomainName.setSuccess(getString(R.string.domain_name_available));
            checkEnableSubmit();
        } else {
            textInputDomainName.setError(getString(R.string.domain_name_not_available));
        }
    }

    @Override
    public void onErrorCheckShopDomain(Throwable t) {
        textInputDomainName.setError(getString(R.string.domain_name_not_available));
    }

    private void checkEnableSubmit() {
        if (textInputDomainName.isSuccess() && textInputShopName.isSuccess()) {
            buttonSubmit.setEnabled(true);
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }
}