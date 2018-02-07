package com.tokopedia.transaction.bcaoneklik.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.bcaoneklik.adapter.autentication.AuthenticationListAdapter;
import com.tokopedia.transaction.bcaoneklik.di.creditcardauth.CreditCardAuthenticationComponent;
import com.tokopedia.transaction.bcaoneklik.di.creditcardauth.DaggerCreditCardAuthenticationComponent;
import com.tokopedia.transaction.bcaoneklik.listener.CreditCardAuthenticationView;
import com.tokopedia.transaction.bcaoneklik.listener.ListPaymentTypeView;
import com.tokopedia.transaction.bcaoneklik.model.creditcard.authenticator.AuthenticatorPageModel;
import com.tokopedia.transaction.bcaoneklik.presenter.CreditCardAuthenticationPresenterImpl;

import javax.inject.Inject;

/**
 * Created by kris on 10/10/17. Tokopedia
 */

public class CreditCardAuthenticationActivity extends TActivity
        implements CreditCardAuthenticationView {

    private static final String EMAIL = "email";
    private static final int DOUBLE_AUTHENTICATION_STATE = 0;

    private TkpdProgressDialog dialog;

    @SuppressWarnings("unused")
    @DeepLink(Constants.Applinks.CREDIT_CARD_AUTH_SETTING)
    public static Intent getCallingIntentCreditCardAuthentication(Context context, Bundle extras) {
        Intent intent = new Intent(context, CreditCardAuthenticationActivity.class);
        AuthenticatorPageModel authenticatorPageModel = new AuthenticatorPageModel();
        authenticatorPageModel.setState(DOUBLE_AUTHENTICATION_STATE);
        authenticatorPageModel.setUserEmail(extras.getString(EMAIL));
        intent.putExtra(CREDIT_CARD_STATUS_KEY, authenticatorPageModel);
        intent.putExtras(extras);
        return intent;
    }

    @Inject
    CreditCardAuthenticationPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        inflateView(R.layout.credit_card_authentication_page);
        initInjector();
        presenter.setListener(this);
        dialog = new TkpdProgressDialog(this, TkpdProgressDialog.NORMAL_PROGRESS);
        AuthenticatorPageModel authenticatorPageModel = getIntent()
                .getParcelableExtra(CREDIT_CARD_STATUS_KEY);
        RecyclerView mainRecyclerView = findViewById(R.id.main_recycler_view);
        View submitButton = findViewById(R.id.cc_auth_setting_submit_button);
        submitButton.setOnClickListener(onSubmitButtonClickedListener(authenticatorPageModel));
        AuthenticationListAdapter adapter = new AuthenticationListAdapter(
                authenticatorPageModel,
                presenter.initiateLogicModel(authenticatorPageModel)
        );
        mainRecyclerView.setAdapter(adapter);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter.notifyDataSetChanged();
    }

    private void initInjector() {
        CreditCardAuthenticationComponent component = DaggerCreditCardAuthenticationComponent
                .builder()
                .appComponent(getApplicationComponent())
                .build();
        component.inject(this);
    }

    private View.OnClickListener onSubmitButtonClickedListener(final AuthenticatorPageModel model) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.showDialog();
                presenter.updateWhiteListStatus(model);
            }
        };
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void finishUpdateStatus(String message) {
        dialog.dismiss();
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        setResult(ListPaymentTypeView.EDIT_AUTHENTICATION_PAGE);
        finish();
    }

    @Override
    public void showErrorMessage(String errorMessage) {
        dialog.dismiss();
        NetworkErrorHelper.showSnackbar(this, errorMessage);
    }

    @Override
    protected void setupToolbar() {
        toolbar = findViewById(com.tokopedia.core.R.id.app_bar);
        toolbar.setTitle(getTitle());
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        if (isLightToolbarThemes()) {
            setLightToolbarStyle();
        }
    }

    private void setLightToolbarStyle() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
            toolbar.setBackgroundResource(com.tokopedia.core.R.color.white);
        } else {
            toolbar.setBackgroundResource(com.tokopedia.core.R.drawable.bg_white_toolbar_drop_shadow);
        }

        Drawable drawable = ContextCompat.getDrawable(
                this, com.tokopedia.core.R.drawable.ic_toolbar_overflow_level_two_black);
        drawable.setBounds(5, 5, 5, 5);
        toolbar.setOverflowIcon(drawable);

        if (getSupportActionBar() != null)
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black_24dp);

        toolbar.setTitleTextAppearance(this, com.tokopedia.core.R.style.WebViewToolbarText);
        toolbar.setSubtitleTextAppearance(this, com.tokopedia.core.R.style
                .WebViewToolbarSubtitleText);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
