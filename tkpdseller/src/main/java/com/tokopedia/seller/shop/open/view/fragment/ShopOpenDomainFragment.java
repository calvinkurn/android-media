package com.tokopedia.seller.shop.open.view.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.seller.app.BaseDiFragment;
import com.tokopedia.seller.lib.widget.TkpdTextInputLayout;

import com.tokopedia.seller.R;
import com.tokopedia.seller.shop.setting.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopOpenDomainComponent;
import com.tokopedia.seller.shop.setting.di.component.ShopSettingLocationComponent;
import com.tokopedia.seller.shop.setting.di.module.ShopOpenDomainModule;
import com.tokopedia.seller.shop.setting.view.presenter.ShopOpenDomainPresenter;
import com.tokopedia.seller.shop.setting.view.presenter.ShopOpenDomainView;
import com.tokopedia.seller.shop.setting.view.presenter.ShopSettingLocationPresenter;

import rx.Subscription;

/**
 * Created by Hendry on 3/17/2017.
 */

public class ShopOpenDomainFragment
        extends BaseDiFragment<ShopOpenDomainComponent, ShopOpenDomainPresenter>
        implements ShopOpenDomainView {
    public static final String TAG = "ShopOpenDomain";

    private View buttonSubmit;
    private TkpdTextInputLayout textInputShopName;
    private TkpdTextInputLayout textInputDomainName;

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
    protected ShopOpenDomainComponent initInjection() {
        return DaggerShopOpenDomainComponent
                .builder()
                .shopOpenDomainModule(new ShopOpenDomainModule(this))
                .appComponent(getApplicationComponent())
                .build();
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
    protected void setActionVar() {

    }

    @Override
    public void onResume() {
        super.onResume();

        // untuk test
//        TomeService tomeService = new TomeService(SessionHandler.getAccessToken(getActivity()));
//        HashMap<String, String> params = new HashMap<>();
//        params.put(ShopNetworkConstant.PARAM_USER_ID, SessionHandler.getLoginID(getActivity()));
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
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }
}
