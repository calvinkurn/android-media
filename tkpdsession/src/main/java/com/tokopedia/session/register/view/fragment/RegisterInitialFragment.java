package com.tokopedia.session.register.view.fragment;

import android.Manifest;
import android.app.Activity;
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
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.tkpd.library.utils.KeyboardHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.customView.LoginTextView;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginProviderModel;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.session.R;
import com.tokopedia.session.google.GoogleSignInActivity;
import com.tokopedia.session.register.view.activity.RegisterEmailActivity;
import com.tokopedia.session.session.activity.Login;
import com.tokopedia.session.session.model.LoginModel;
import com.tokopedia.session.session.presenter.RegisterInitialPresenter;
import com.tokopedia.session.session.presenter.RegisterInitialPresenterImpl;
import com.tokopedia.session.session.presenter.RegisterInitialView;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

import static com.tokopedia.session.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT;
import static com.tokopedia.session.google.GoogleSignInActivity.KEY_GOOGLE_ACCOUNT_TOKEN;
import static com.tokopedia.session.google.GoogleSignInActivity.RC_SIGN_IN_GOOGLE;

/**
 * Created by stevenfredian on 10/18/16.
 */
@RuntimePermissions
public class RegisterInitialFragment extends Fragment
        implements RegisterInitialView {

    private static final String ARGS_MESSAGE = "message";

    protected RegisterInitialPresenter presenter;

    LinearLayout linearLayout;
    LoginTextView registerButton;
    TextView loginButton;
    ScrollView container;
    RelativeLayout progressBar;

    private List<LoginProviderModel.ProvidersBean> listProvider;
    private Snackbar snackbar;
    private CallbackManager callbackManager;

    public static RegisterInitialFragment newInstance() {
        return new RegisterInitialFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialPresenter();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(getFragmentLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        setViewListener();
    }

    protected void initView(View view) {
        UserAuthenticationAnalytics.setActiveRegister();

        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        registerButton = (LoginTextView) view.findViewById(R.id.register);
        loginButton = (TextView) view.findViewById(R.id.login_button);
        container = (ScrollView) view.findViewById(R.id.container);
        progressBar = (RelativeLayout) view.findViewById(R.id.progress_bar);

        registerButton.setColor(Color.WHITE);
        registerButton.setBorderColor(R.color.black);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventRegisterChannel(AppEventTracking.GTMCacheValue.EMAIL);
                UserAuthenticationAnalytics.setActiveAuthenticationMedium(
                        AppEventTracking.GTMCacheValue.EMAIL);
                UnifyTracking.eventMoRegistrationStart(AppEventTracking.GTMCacheValue.EMAIL);
                finishActivity();
                startActivity(new Intent(getActivity(), RegisterEmailActivity.class));

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
                                  ds.setColor(MethodChecker.getColor(
                                          getActivity(), R.color.tkpd_main_green
                                          )
                                  );
                              }
                          }
                , sourceString.indexOf("Masuk")
                , sourceString.length()
                , 0);

        loginButton.setText(spannable, TextView.BufferType.SPANNABLE);
    }

    protected void setViewListener() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                getActivity().startActivity(Login.getCallingIntent(getActivity()));
            }
        });
    }

    public String getScreenName() {
        return AppScreen.SCREEN_REGISTER;
    }

    @Override
    public void onResume() {
        presenter.initData(getActivity());
        ScreenTracking.screen(getScreenName());
        super.onResume();
    }

    @Override
    public void addProgressBar() {
        ProgressBar pb = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        int lastPos = linearLayout.getChildCount() - 1;
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
    public void showProgress(boolean isShow) {
        if (progressBar != null)
            progressBar.setVisibility(isShow ? View.VISIBLE : View.GONE);
        if (container != null)
            container.setVisibility(isShow ? View.GONE : View.VISIBLE);
        if (loginButton != null)
            loginButton.setVisibility(isShow ? View.GONE : View.VISIBLE);
    }

    @Override
    public void showError(String string) {
        NetworkErrorHelper.showSnackbar(getActivity(), string);
    }

    @Override
    public void onMessageError(int type, String s) {
        switch (type) {
            case DownloadService.DISCOVER_LOGIN:
                removeProgressBar();
                NetworkErrorHelper.createSnackbarWithAction(getActivity(),
                        getString(R.string.error_download_provider),
                        new NetworkErrorHelper.RetryClickedListener() {
                            @Override
                            public void onRetryClicked() {
                                presenter.downloadProviderLogin(getActivity());

                            }
                        }).showRetrySnackbar();
                loginButton.setEnabled(false);
                break;
            default:
                showError(s);
                break;
        }
    }

    @Override
    public void finishActivity() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    @Override
    public void moveToFragmentSecurityQuestion(int security1, int security2, int userId) {
        if (getActivity() != null) {// && !((AppCompatActivity)mContext).isFinishing()
            ((SessionView) getActivity()).moveToFragmentSecurityQuestion(security1, security2, userId, "");
        }
    }

    @Override
    public void showProvider(List<LoginProviderModel.ProvidersBean> data) {
        listProvider = data;
        loginButton.setEnabled(true);
        if (listProvider != null && checkHasNoProvider()) {
            presenter.saveProvider(listProvider);

            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    getResources().getDimensionPixelSize(R.dimen.btn_login_height));

            layoutParams.setMargins(0, 20, 0, 15);

            for (int i = 0; i < listProvider.size(); i++) {
                String color = listProvider.get(i).getColor();
                int colorInt;
                if (color == null) {
                    colorInt = Color.parseColor("#FFFFFF");
                } else {
                    colorInt = Color.parseColor(color);
                }
                LoginTextView tv = new LoginTextView(getActivity(), colorInt);
                tv.setTextRegister(listProvider.get(i).getName());
                tv.setImage(listProvider.get(i).getImage());
                tv.setRoundCorner(10);
                if (listProvider.get(i).getId().equalsIgnoreCase("facebook")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            UnifyTracking.eventRegisterChannel(AppEventTracking.SOCIAL_MEDIA.FACEBOOK);
                            onFacebookClick();
                        }
                    });
                } else if (listProvider.get(i).getId().equalsIgnoreCase("gplus")) {
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            RegisterInitialFragmentPermissionsDispatcher
                                    .onGoogleClickWithCheck(RegisterInitialFragment.this);
                            UnifyTracking.eventRegisterChannel(AppEventTracking.SOCIAL_MEDIA.GOOGLE_PLUS);
                            onGoogleClickd();
                        }
                    });
                } else {
                    final int finalI = i;
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loginProvideOnClick(listProvider.get(finalI).getUrl(),
                                    listProvider.get(finalI).getName());
                            UnifyTracking.eventRegisterChannel(listProvider.get(finalI).getName());
                        }
                    });
                }
                if (linearLayout != null) {
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
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(name);
    }


    public void startLoginWithGoogle(String type, LoginGoogleModel loginGoogleModel) {
        presenter.startLoginWithGoogle(getActivity(), type, loginGoogleModel);
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(
                AppEventTracking.GTMCacheValue.GMAIL);
        UnifyTracking.eventMoRegistrationStart(
                AppEventTracking.GTMCacheValue.GMAIL);
    }

    @NeedsPermission(Manifest.permission.GET_ACCOUNTS)
    public void onGoogleClick() {
//        ((GoogleActivity) getActivity()).onSignInClicked();
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(
                AppEventTracking.GTMCacheValue.GMAIL);
    }

    private void onGoogleClickd() {
        Intent intent = new Intent(getActivity(), GoogleSignInActivity.class);
        startActivityForResult(intent, RC_SIGN_IN_GOOGLE);
    }


    private void onFacebookClick() {
        if (AccessToken.getCurrentAccessToken() != null) {
            LoginManager.getInstance().logOut();
        }
        processFacebookLogin();
        UserAuthenticationAnalytics.setActiveAuthenticationMedium(
                AppEventTracking.GTMCacheValue.FACEBOOK);
    }

    private void processFacebookLogin() {
        presenter.doFacebookLogin(this, callbackManager);
        UnifyTracking.eventMoRegistrationStart(
                com.tokopedia.core.analytics.AppEventTracking.GTMCacheValue.FACEBOOK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_CANCELED) {
                    KeyboardHandler.DropKeyboard(getActivity(), getView());
                    break;
                }
                Bundle bundle = data.getBundleExtra("bundle");
                if (bundle.getString("path").contains("error")) {
                    NetworkErrorHelper.showSnackbar(getActivity(), bundle.getString(ARGS_MESSAGE));
                } else if (bundle.getString("path").contains("code")) {
                    presenter.loginWebView(getActivity(), bundle);
                }
                break;

            case RC_SIGN_IN_GOOGLE :
                if (data != null) {
                    GoogleSignInAccount googleSignInAccount = data.getParcelableExtra(KEY_GOOGLE_ACCOUNT);
                    String accessToken = data.getStringExtra(KEY_GOOGLE_ACCOUNT_TOKEN);

                    LoginGoogleModel model = new LoginGoogleModel();
                    model.setFullName(googleSignInAccount.getDisplayName());
                    model.setGoogleId(googleSignInAccount.getId());
                    model.setEmail(googleSignInAccount.getEmail());
                    model.setAccessToken(accessToken);
                    startLoginWithGoogle(LoginModel.GoogleType, model);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean checkHasNoProvider() {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof LoginTextView) {
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
    public void ariseRetry(int type, @Nullable Object... data) {

    }

    @Override
    public void setData(int type, Bundle data) {
        if (presenter != null)
            presenter.setData(getActivity(), type, data);
    }

    @Override
    public void onNetworkError(int type, Object... data) {
        onMessageError(type, data);
    }

    @Override
    public void onMessageError(int type, Object... data) {
        showProgress(false);
        if (data != null) {
            snackbar = SnackbarManager.make(getActivity(), (String) data[0], Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.unSubscribeFacade();
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        if (snackbar != null && snackbar.isShown()) snackbar.dismiss();
    }

    protected void initialPresenter() {
        presenter = new RegisterInitialPresenterImpl(this);
    }

    protected int getFragmentLayout() {
        return R.layout.fragment_register_initial;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        RegisterInitialFragmentPermissionsDispatcher.onRequestPermissionsResult(RegisterInitialFragment.this, requestCode, grantResults);
    }

    @OnShowRationale(Manifest.permission.GET_ACCOUNTS)
    void showRationaleForGetAccounts(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.GET_ACCOUNTS);
    }

    @OnPermissionDenied(Manifest.permission.GET_ACCOUNTS)
    void showDeniefForGetAccounts() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.GET_ACCOUNTS);
    }

    @OnNeverAskAgain(Manifest.permission.GET_ACCOUNTS)
    void showNeverAskForGetAccounts() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.GET_ACCOUNTS);
    }


}
