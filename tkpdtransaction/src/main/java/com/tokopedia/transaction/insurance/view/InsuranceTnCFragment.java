package com.tokopedia.transaction.insurance.view;

import android.app.Activity;
import android.net.http.SslError;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.transaction.R;
import com.tokopedia.transaction.R2;
import com.tokopedia.transaction.insurance.di.DaggerInsuranceTnCComponent;
import com.tokopedia.transaction.insurance.di.InsuranceTnCComponent;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by Irfan Khoirul on 11/12/17.
 */

public class InsuranceTnCFragment extends BasePresenterFragment<InsuranceTnCContract.Presenter>
        implements InsuranceTnCContract.View {

    @BindView(R2.id.web_view_terms_and_condition)
    WebView webViewTermsAndCondition;
    @BindView(R2.id.pb_loading)
    ProgressBar pbLoading;

    @Inject
    InsuranceTnCContract.Presenter presenter;

    public InsuranceTnCFragment() {
        // Required empty public constructor
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.loadWebViewData();
    }

    @Override
    protected void initialPresenter() {
        initializeInjector();
        presenter.attachView(this);
    }

    private void initializeInjector() {
        AppComponent appComponent = ((InsuranceTnCActivity) getActivity()).getApplicationComponent();
        InsuranceTnCComponent insuranceTnCComponent = DaggerInsuranceTnCComponent.builder()
                .appComponent(appComponent)
                .build();
        insuranceTnCComponent.inject(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_insurance_tnc;
    }

    @Override
    protected void initView(View view) {

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
    public void showWebView(String webViewData) {
        webViewTermsAndCondition.setWebViewClient(new TermsAndConditionsWebViewClient());
        webViewTermsAndCondition.setWebChromeClient(new WebChromeClient());
        webViewTermsAndCondition.loadData(webViewData, "text/html", "UTF-8");
    }

    @Override
    public void showLoading() {
        pbLoading.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        pbLoading.setVisibility(View.GONE);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private class TermsAndConditionsWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            hideLoading();
        }
    }

}
