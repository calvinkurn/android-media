package com.tokopedia.seller.shop.open.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.R;
import com.tokopedia.seller.lib.widget.TkpdTextInputLayout;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.setting.view.presenter.ShopOpenDomainPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopOpenDomainView;
import com.tokopedia.seller.topads.view.widget.PrefixEditText;

import rx.Subscription;

/**
 * Created by Hendry on 3/17/2017.
 */

public class ShopOpenDomainFragment
        extends BasePresenterFragment<ShopOpenDomainPresenter>
        implements ShopOpenDomainView {
    public static final String TAG = "ShopOpenDomain";
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
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {
        component = DaggerShopOpenDomainComponent
                .builder()
                .shopOpenDomainModule(new ShopOpenDomainModule())
                .appComponent(((HasComponent<AppComponent>) getActivity()).getComponent())
                .build();
        presenter = component.getPresenter();
        presenter.attachView(this);
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
        editTextInputShopName = textInputShopName.getEditText();
        editTextInputDomainName = (PrefixEditText) textInputDomainName.getEditText();


        String helloName = getString( R.string.hello_blank_name ) +
                " " +
                SessionHandler.getLoginName(getActivity());
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

        editTextInputDomainName.addTextChangedListener (new TextWatcher() {
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
    }

    @Override
    public void setDomainCheckResult(boolean existed) {
        if (existed) {
            textInputDomainName.setSuccess(getString(R.string.domain_name_available));
            checkEnableSubmit();
        }
        else {
            textInputDomainName.setError(getString(R.string.domain_name_not_available));
        }
    }

    @Override
    public void setShopCheckResult(boolean existed) {
        if (existed) {
            textInputShopName.setSuccess(getString(R.string.shop_name_available));
            checkEnableSubmit();
        }
        else {
            textInputShopName.setError(getString(R.string.shop_name_not_available));
        }
    }

    private void checkEnableSubmit(){
        if (textInputDomainName.isSuccess() &&
                textInputShopName.isSuccess()) {
            buttonSubmit.setEnabled(true);
        }
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
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onResume() {
        super.onResume();

        // untuk test
//        TomeService tomeService = new TomeService(SessionHandler.getAccessToken(getActivity()));
//        HashMap<String, String> params = new HashMap<>();
//        params.put(ShopOpenNetworkConstant.PARAM_USER_ID, SessionHandler.getLoginID(getActivity()));
//        subscription = tomeService.getApi().getDomainCheck(params)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .unsubscribeOn(Schedulers.io())
//                .subscribe(
//                    new Subscriber<Response<String>>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//                            Log.i("Test", e.toString());
//                        }
//
//                        @Override
//                        public void onNext(Response<String> stringResponse) {
//                            Log.i("Test", "test");
//                        }
//                    }
//                );
    }

    @Override
    public void onPause() {
        super.onPause();
        // subscription.unsubscribe();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

}
