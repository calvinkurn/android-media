package com.tokopedia.session.session.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.customView.LoginTextView;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.base.BaseFragment;
import com.tokopedia.session.session.google.GoogleActivity;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.session.session.presenter.RegisterInitialPresenter;
import com.tokopedia.session.session.presenter.RegisterInitialPresenterImpl;
import com.tokopedia.session.session.presenter.RegisterInitialView;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.AppEventTracking;
import com.tokopedia.core.var.TkpdState;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by stevenfredian on 10/18/16.
 */

public class RegisterInitialFragment extends BaseFragment<RegisterInitialPresenter>
                                        implements RegisterInitialView{

    @BindView(R2.id.linearLayout)
    LinearLayout linearLayout;
    @BindView(R2.id.register)
    LoginTextView registerButton;
    @BindView(R2.id.login_button)
    TextView loginButton;

    List<LoginProviderModel.ProvidersBean> listProvider;
    private Snackbar snackbar;
    LocalCacheHandler cacheGTM;

    public static Fragment newInstance(){
        return new RegisterInitialFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View parentView = super.onCreateView(inflater, container, savedInstanceState);
        initView();
        showProgress(false);

        cacheGTM = new LocalCacheHandler(getActivity(), AppEventTracking.GTM_CACHE);
        cacheGTM.putString(AppEventTracking.GTMCacheKey.SESSION_STATE,
                AppEventTracking.GTMCacheValue.REGISTER);
        cacheGTM.applyEditor();

        return parentView;
    }

    private void initView() {
        registerButton.setColor(Color.WHITE);
        registerButton.setBorderColor(R.color.black);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storeCacheGTM(AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                        AppEventTracking.GTMCacheValue.EMAIL);
                ((SessionView) getActivity()).moveToRegister();
            }
        });
        String sourceString = "Sudah punya akun? Masuk";

        Spannable spannable = new SpannableString(sourceString);

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setUnderlineText(true);
                                  ds.setColor(getResources().getColor(R.color.tkpd_main_green));
                              }
                          }
                , sourceString.indexOf("Masuk")
                , sourceString.length()
                ,0);

        loginButton.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    @OnClick(R2.id.login_button)
    public void moveToLogin(){
        ((SessionView) getActivity()).moveToLogin();
    }

    @Override
    public void onResume() {
        presenter.initData(getActivity());
        TrackingUtils.screen(this);
        super.onResume();
    }

    @Override
    protected void initPresenter() {
        presenter = new RegisterInitialPresenterImpl(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_register_initial;
    }

    @Override
    public void addProgressBar() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = linearLayout.getChildCount()-1;
        if (!(linearLayout.getChildAt(lastPos) instanceof ProgressBar))
            linearLayout.addView(pb, linearLayout.getChildCount());
    }

    @Override
    public void removeProgressBar() {
        int lastPos = linearLayout.getChildCount() - 1;
        if (linearLayout.getChildAt(lastPos) instanceof ProgressBar)
            linearLayout.removeViewAt(linearLayout.getChildCount() - 1);
    }

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showError(String string) {
        SnackbarManager.make(getActivity(),string, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onMessageError(int type, String s) {
        switch (type) {
            case DownloadService.DISCOVER_LOGIN:
                removeProgressBar();
                snackbar = SnackbarManager.make(getActivity()
                        ,getString(R.string.error_download_provider), Snackbar.LENGTH_INDEFINITE)
                        .setAction(getString(R.string.title_try_again), retryDiscover());
                snackbar.show();
                break;
            default:
                showError(s);
                break;
        }
    }

    private View.OnClickListener retryDiscover() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.downloadProviderLogin(getActivity());
            }
        };
    }

    @Override
    public void finishActivity() {
        if (getActivity() != null && getActivity() instanceof SessionView) {
            ((SessionView) getActivity()).destroy();
        }
    }

    @Override
    public void showProvider(List<LoginProviderModel.ProvidersBean> data) {
        listProvider = data;
        if (listProvider != null && checkHasNoProvider()) {
            presenter.saveProvider(listProvider);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

            layoutParams.setMargins(0, 20, 0, 0);

            for (int i = 0; i < listProvider.size(); i++) {
                String color = listProvider.get(i).getColor();
                int colorInt;
                if(color==null) {
                    colorInt = Color.parseColor("#FFFFFF");
                }else{
                    colorInt = Color.parseColor(color);
                }
                LoginTextView tv = new LoginTextView(getActivity(),colorInt);
                tv.setTextRegister(listProvider.get(i).getName());
                tv.setImage(listProvider.get(i).getImage());
                tv.setRoundCorner(10);
                if (listProvider.get(i).getId().equalsIgnoreCase("facebook")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onFacebookClick();
                        }
                    });
                } else if (listProvider.get(i).getId().equalsIgnoreCase("gplus")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onGoogleClick();
                        }
                    });
                } else {
                    final int finalI = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loginProvideOnClick(listProvider.get(finalI).getUrl(),
                                    listProvider.get(finalI).getName());
                        }
                    });
                }
                if (linearLayout != null ) {
                    linearLayout.addView(tv, linearLayout.getChildCount(), layoutParams);
                }
            }
        }
    }

    private void loginProvideOnClick(final String url, final String name) {
        WebViewLoginFragment newFragment = WebViewLoginFragment
                .createInstance(url);
        newFragment.setTargetFragment(RegisterInitialFragment.this, 100);
        newFragment.show(getFragmentManager().beginTransaction(), "dialog");
        getActivity().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        storeCacheGTM(AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                name);
    }


    public void startLoginWithGoogle(String type, LoginGoogleModel loginGoogleModel) {
        presenter.startLoginWithGoogle(getActivity(), type, loginGoogleModel);
        storeCacheGTM(AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                AppEventTracking.GTMCacheValue.GMAIL
        );
    }

    private void onGoogleClick() {
        ((GoogleActivity) getActivity()).onSignInClicked();
        storeCacheGTM(AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                AppEventTracking.GTMCacheValue.GMAIL);
    }

    private void onFacebookClick() {
        showProgress(true);
        presenter.loginFacebook(getActivity());
        CommonUtils.dumper("LocalTag : TYPE : FACEBOOK");
        storeCacheGTM(AppEventTracking.GTMCacheKey.REGISTER_TYPE,
                AppEventTracking.GTMCacheValue.FACEBOOK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                Bundle bundle = data.getBundleExtra("bundle");
                if(bundle.getString("path").contains("error")){
                    snackbar = SnackbarManager.make(getActivity(), bundle.getString("message"), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if (bundle.getString("path").contains("code")){
                    presenter.loginWebView(getActivity(), bundle);
                }else if (bundle.getString("path").contains("activation-social")){
//                    ((SessionView) getActivity()).moveToActivationResend(registerName.getText().toString());
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean checkHasNoProvider() {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if(linearLayout.getChildAt(i) instanceof LoginTextView){
                return false;
            }
        }
        return true;
    }

    @Override
    public int getFragmentId() {
        return TkpdState.DrawerPosition.REGISTER_INITIAL;
    }

    @Override
    public void ariseRetry(int type, Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {
        if (presenter != null)
            presenter.setData(getActivity(),type, data);
    }

    @Override
    public void onNetworkError(int type, Object... data) {

    }

    @Override
    public void onMessageError(int type, Object... data) {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribeFacade();
        KeyboardHandler.DropKeyboard(getActivity(),getView());
    }

    private void storeCacheGTM(String key, String value) {
        cacheGTM.putString(key, value);
        cacheGTM.applyEditor();
    }
}
