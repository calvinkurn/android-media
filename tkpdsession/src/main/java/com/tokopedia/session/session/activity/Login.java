package com.tokopedia.session.session.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.login.LoginManager;
import com.google.android.gms.plus.model.people.Person;
import com.tkpd.library.utils.DownloadResultReceiver;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.handler.UserAuthenticationAnalytics;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.fragment.FragmentSecurityQuestion;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.v4.NetworkConfig;
import com.tokopedia.core.presenter.BaseView;
import com.tokopedia.core.router.CustomerRouter;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionCartRouter;
import com.tokopedia.core.service.DownloadService;
import com.tokopedia.core.service.ErrorNetworkReceiver;
import com.tokopedia.core.service.constant.DownloadServiceConstant;
import com.tokopedia.core.session.model.CreatePasswordModel;
import com.tokopedia.core.session.model.LoginFacebookViewModel;
import com.tokopedia.core.session.model.LoginGoogleModel;
import com.tokopedia.core.session.model.LoginViewModel;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.AppWidgetUtil;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.otp.phoneverification.view.activity.PhoneVerificationActivationActivity;
import com.tokopedia.session.activation.view.viewmodel.LoginTokenViewModel;
import com.tokopedia.session.register.view.activity.RegisterEmailActivity;
import com.tokopedia.session.register.view.fragment.RegisterInitialFragment;
import com.tokopedia.session.session.fragment.LoginFragment;
import com.tokopedia.session.session.fragment.RegisterPassPhoneFragment;
import com.tokopedia.session.session.intentservice.LoginResultReceiver;
import com.tokopedia.session.session.intentservice.LoginService;
import com.tokopedia.session.session.intentservice.OTPResultReceiver;
import com.tokopedia.session.session.intentservice.OTPService;
import com.tokopedia.session.session.intentservice.RegisterResultReceiver;
import com.tokopedia.session.session.intentservice.RegisterService;
import com.tokopedia.session.session.intentservice.ResetPasswordResultReceiver;
import com.tokopedia.session.session.intentservice.ResetPasswordService;
import com.tokopedia.session.session.model.LoginModel;
import com.tokopedia.session.session.presenter.SessionImpl;

import org.parceler.Parcels;

import java.util.List;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by m.normansyah on 04/11/2015.
 * <p/>
 * inside Login activity :
 * 1. Login {@link LoginFragment}
 * 2. Security Question {@link FragmentSecurityQuestion}
 * 3. Facebook Login Fragment {@link LoginFragment}
 * 4. Google Login Fragment {@link LoginFragment}
 * <p/>
 * inside session package :
 * 1. Logout Fragment currently dialog is discard when rotate.
 */
@RuntimePermissions
public class Login extends BaseActivity implements SessionView
        , DownloadResultReceiver.Receiver
        , LoginResultReceiver.Receiver
        , RegisterResultReceiver.Receiver
        , ResetPasswordResultReceiver.Receiver
        , OTPResultReceiver.Receiver
        , ErrorNetworkReceiver.ReceiveListener {

    public static final String INTENT_EXTRA_PARAM_EMAIL = "INTENT_EXTRA_PARAM_EMAIL";
    public static final String INTENT_EXTRA_PARAM_PASSWORD = "INTENT_EXTRA_PARAM_PASSWORD";
    private static final String INTENT_EXTRA_PARAM_TOKEN_MODEL = "INTENT_EXTRA_PARAM_TOKEN_MODEL";
    private static final String INTENT_LOGIN_TYPE = "INTENT_LOGIN_TYPE";

    public static final int TRUE_CALLER_REQUEST_CODE = 100;
    private static final int REQUEST_VERIFY_PHONE_NUMBER = 900;
    public static final String DEFAULT = "not";
    private static final int AUTOMATIC_LOGIN = 1;
    private static final int UNIQUE_CODE_LOGIN = 2;

    //    int whichFragmentKey;
    LocalCacheHandler cacheGTM;
    Session session;
    FragmentManager supportFragmentManager;
    Toolbar toolbar;
    DownloadResultReceiver mReceiver;
    LoginResultReceiver loginReceiver;
    RegisterResultReceiver registerReceiver;
    ResetPasswordResultReceiver resetPasswordReceiver;
    OTPResultReceiver otpReceiver;

    @DeepLink({Constants.Applinks.LOGIN})
    public static Intent getCallingApplinkIntent(Context context, Bundle bundle) {
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        if (SessionHandler.isV4Login(context)) {
            if (context.getApplicationContext() instanceof TkpdCoreRouter)
                return ((TkpdCoreRouter) context.getApplicationContext()).getHomeIntent(context);
            else throw new RuntimeException("Applinks intent unsufficient");
        } else {
            Intent intent = new Intent(context, Login.class);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY,
                    TkpdState.DrawerPosition.LOGIN);
            return intent
                    .setData(uri.build());
        }
    }

    @DeepLink({Constants.Applinks.REGISTER})
    public static Intent getCallingApplinkRegisterIntent(Context context, Bundle bundle) {
        if (SessionHandler.isV4Login(context)) {
            if (context.getApplicationContext() instanceof TkpdCoreRouter)
                return ((TkpdCoreRouter) context.getApplicationContext()).getHomeIntent(context);
            else throw new RuntimeException("Applinks intent unsufficient");
        } else {
            return getRegisterIntent(context);
        }

    }

    @NonNull
    public static Intent moveToCreateShop(Context context) {
        if (context == null)
            return null;

        Intent intent;
        intent = SellerRouter.getActivityShopCreateEdit(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        return intent;
    }

    public static Intent getAutomaticLoginIntent(Context context, String email, String password) {
        Intent callingIntent = new Intent(context, Login.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_EMAIL, email);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_PASSWORD, password);
        callingIntent.putExtra(INTENT_LOGIN_TYPE, AUTOMATIC_LOGIN);
        callingIntent.putExtra("login", DEFAULT);
        callingIntent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        if (GlobalConfig.isSellerApp())
            callingIntent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.SELLER_HOME);
        else
            callingIntent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
        return callingIntent;
    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, Login.class);
        callingIntent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        if (GlobalConfig.isSellerApp())
            callingIntent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.SELLER_HOME);
        else
            callingIntent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
        return callingIntent;
    }

    public static Intent getAutomaticLoginFromActivationIntent(Context context,
                                                               LoginTokenViewModel loginTokenViewModel) {
        Intent callingIntent = new Intent(context, Login.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_TOKEN_MODEL, loginTokenViewModel);
        callingIntent.putExtra(INTENT_LOGIN_TYPE, UNIQUE_CODE_LOGIN);
        callingIntent.putExtra("login", DEFAULT);
        callingIntent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        if (GlobalConfig.isSellerApp())
            callingIntent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.SELLER_HOME);
        else
            callingIntent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
        return callingIntent;
    }

    public static Intent getRegisterIntent(Context context) {
        Intent callingIntent = new Intent(context, Login.class);
        callingIntent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY,
                TkpdState.DrawerPosition.REGISTER);
        callingIntent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
        return callingIntent;
    }

    public static Intent getSellerRegisterIntent(Context context) {
        Intent callingIntent = new Intent(context, Login.class);
        callingIntent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY,
                TkpdState.DrawerPosition.REGISTER);
        callingIntent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.SELLER_HOME);
        return callingIntent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Tokopedia3);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }
        setContentView(R.layout.activity_login2);

        session = new SessionImpl(this);
        session.fetchExtras(getIntent());
        session.fetchDataAfterRotate(savedInstanceState);
        session.initDataInstance();

        supportFragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        supportFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                Log.d(TAG, "back stack changed [count : " + backCount);
                if (backCount == 0) {
                    // block where back has been pressed. since backstack is zero.
                    SessionHandler.clearUserData(Login.this);// because user is back that reset all data
                    SessionHandler.deleteRegisterNext(Login.this);
                    LoginManager.getInstance().logOut();
                    destroy();
                } else {
                    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.login_fragment);
                    if (fragment != null && session.getWhichFragment() == 0) {
                        session.setWhichFragment(TkpdState.DrawerPosition.LOGIN);
                    }
                    setToolbarTitle();
                    invalidateOptionsMenu();
                }
            }
        });

        setToolbarTitle();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setToolbarColor();

         /* Starting Download Service */
        mReceiver = new DownloadResultReceiver(new Handler());
        mReceiver.setReceiver(this);

        loginReceiver = new LoginResultReceiver(new Handler());
        loginReceiver.setReceiver(this);
        registerReceiver = new RegisterResultReceiver(new Handler());
        registerReceiver.setReceiver(this);
        resetPasswordReceiver = new ResetPasswordResultReceiver(new Handler());
        resetPasswordReceiver.setReceiver(this);
        otpReceiver = new OTPResultReceiver(new Handler());
        otpReceiver.setReceiver(this);

        if (getIntent().getExtras().getInt(INTENT_LOGIN_TYPE) == AUTOMATIC_LOGIN) {
            Bundle bundle = new Bundle();
            LoginViewModel loginViewModel = new LoginViewModel();
            loginViewModel.setPassword(getIntent().getExtras().getString(INTENT_EXTRA_PARAM_PASSWORD));
            loginViewModel.setUsername(getIntent().getExtras().getString(INTENT_EXTRA_PARAM_EMAIL));
            loginViewModel.setUuid(SessionHandler.getUUID(this));
            bundle.putParcelable(DownloadService.LOGIN_VIEW_MODEL_KEY, Parcels.wrap(loginViewModel));
            bundle.putBoolean(DownloadService.IS_NEED_LOGIN, true);
            LoginService.startLogin(this, loginReceiver, bundle, DownloadServiceConstant.LOGIN_ACCOUNTS_TOKEN);
        } else if (getIntent().getExtras().getInt(INTENT_LOGIN_TYPE) == UNIQUE_CODE_LOGIN) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(
                    DownloadService.LOGIN_TOKEN_MODEL_KEY,
                    getIntent().getParcelableExtra(INTENT_EXTRA_PARAM_TOKEN_MODEL));
            LoginService.startLogin(this, loginReceiver, bundle, DownloadServiceConstant.LOGIN_UNIQUE_CODE);
        }
    }


    private void setToolbarColor() {

        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources()
                .getColor(R.color.white)));
        toolbar.setTitleTextColor(getResources().getColor(R.color.grey_700));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.setElevation(10);
        }

        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        Drawable upArrow = ContextCompat.getDrawable(this, android.support.v7.appcompat.R.drawable.abc_ic_ab_back_material);
        if (upArrow != null) {
            upArrow.setColorFilter(ContextCompat.getColor(this, R.color.grey_700), PorterDuff.Mode.SRC_ATOP);
            getSupportActionBar().setHomeAsUpIndicator(upArrow);
        }
    }

    @Override
    public void moveToFragment(Fragment fragment, boolean isAddtoBackStack, String TAG, int type) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.login_fragment, fragment, TAG);
        try {
            if (isAddtoBackStack)
                fragmentTransaction.addToBackStack(null);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void moveToRegisterPassPhone(CreatePasswordModel
                                                model, List<String> createPasswordList, Bundle bundle) {
        Log.d(TAG, messageTAG + " moveToRegisterThird : " + model);
        if (isFragmentCreated(REGISTER_THIRD)) {
            Fragment fragment = RegisterPassPhoneFragment.newInstance(model, createPasswordList, bundle);
            moveToFragment(fragment, false, REGISTER_THIRD, TkpdState.DrawerPosition.REGISTER_THIRD);
        }
    }

    @Override
    public void moveTo(int type) {
        //[BUGFIX] AN-1640 Home: 'Register' page should navigate into
        // 'Home' page when the user click on device back arrow.
        Log.d(getClass().getSimpleName(), "moveTo " + type);
        switch (type) {
            case MOVE_TO_CART_TYPE:
                if (SessionHandler.isV4Login(this)) {
                    startActivity(TransactionCartRouter.createInstanceCartActivity(this));
                } else {
                    Intent intent = HomeRouter.getHomeActivityInterfaceRouter(this);
                    intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT, HomeRouter.INIT_STATE_FRAGMENT_HOME);
                    startActivity(intent);
                }
                break;
            case HOME:
                if (SessionHandler.isV4Login(this) && !SessionHandler.isMsisdnVerified()) {
                    Intent intent = new Intent(this, PhoneVerificationActivationActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivityForResult(intent, REQUEST_VERIFY_PHONE_NUMBER);
                } else {
                    loginToHome();
                }
                break;

            case SELLER_HOME:
                if (SessionHandler.isV4Login(this)) {
                    SessionHandler.getShopID(this);
                    AppWidgetUtil.sendBroadcastToAppWidget(this);
                    if (!SessionHandler.isUserHasShop(this)) {
                        UnifyTracking.eventLoginCreateShopSellerApp();
                    }
                    Intent intent;
                    if (SessionHandler.isUserHasShop(this)) {
                        intent = SellerAppRouter.getSellerHomeActivity(this);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent
                                .FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    } else {
                        intent = moveToCreateShop(this);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                    intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                            HomeRouter.INIT_STATE_FRAGMENT_HOME);
                    startActivity(intent);
                }
                break;
        }
    }

    private void loginToHome() {
        if (SessionHandler.isV4Login(this)) {
            Intent intent = HomeRouter.getHomeActivityInterfaceRouter(this);
            intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                    HomeRouter.INIT_STATE_FRAGMENT_HOME);
            startActivity(intent);
        } else {
            Intent intent = HomeRouter.getHomeActivityInterfaceRouter(this);
            intent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                    HomeRouter.INIT_STATE_FRAGMENT_HOME);
            startActivity(intent);
        }
    }

    @Override
    public void setToolbarTitle() {
        switch (session.getWhichFragment()) {
            case TkpdState.DrawerPosition.LOGIN:
                toolbar.setTitle(getString(R.string.title_activity_login));
                break;
            case TkpdState.DrawerPosition.REGISTER_THIRD:
                toolbar.setTitle(getString(R.string.title_activity_register));
                break;
            case TkpdState.DrawerPosition.SECURITY_QUESTION:
                toolbar.setTitle(getString(R.string.bar_security_question));
                break;
            case TkpdState.DrawerPosition.REGISTER_NEXT:
                toolbar.setTitle(getString(R.string.title_activity_register));
                break;
            case TkpdState.DrawerPosition.ACTIVATION_RESENT:
                toolbar.setTitle(getString(R.string.title_activity_activation));
                break;
            case TkpdState.DrawerPosition.FORGOT_PASSWORD:
                toolbar.setTitle(getString(R.string.title_activity_forgot_password));
                break;
            case TkpdState.DrawerPosition.REGISTER:
            case TkpdState.DrawerPosition.REGISTER_INITIAL:
                toolbar.setTitle("");
                break;
        }
    }

    @Override
    public void moveToRegister() {
        finish();
        startActivity(new Intent(this, RegisterEmailActivity.class));
    }

    @Override
    public void moveToRegisterInitial() {
        Fragment fragment = RegisterInitialFragment.newInstance();
        moveToFragment(fragment, false, REGISTER_INITIAL, TkpdState.DrawerPosition.REGISTER_INITIAL);

        // Change the header
        session.setWhichFragment(TkpdState.DrawerPosition.REGISTER_INITIAL);
        setToolbarTitle();
        invalidateOptionsMenu();
    }

    @Override
    public void moveToLogin() {
        Fragment loginFragment = LoginFragment.newInstance("", false, "", "", "");
        moveToFragment(loginFragment, false, LOGIN_FRAGMENT_TAG, TkpdState.DrawerPosition.LOGIN);

        // Change the header
        session.setWhichFragment(TkpdState.DrawerPosition.LOGIN);
        setToolbarTitle();
        invalidateOptionsMenu();
    }

    @Override
    public void verifyTruecaller() {
        if (GlobalConfig.isSellerApp()) {
            startActivityForResult(SellerAppRouter.getTruecallerIntent(this), TRUE_CALLER_REQUEST_CODE);
        } else {
            startActivityForResult(CustomerRouter.getTruecallerIntent(this), TRUE_CALLER_REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (session.getWhichFragment()) {
            case TkpdState.DrawerPosition.LOGIN:
                if (isFragmentCreated(LOGIN_FRAGMENT_TAG)) {
                    Log.d(TAG, messageTAG + LoginFragment.class.getSimpleName() + " is created !!!");
                    Intent intent = getIntent();
                    String mEmail = "";
                    boolean GoToIndex = false;
                    String login = "";
                    String name = "";
                    String url = "";
                    if (intent != null) {
                        mEmail = intent.getStringExtra(com.tokopedia.session.session.presenter.Login.EXTRA_EMAIL);
                        GoToIndex = intent.getBooleanExtra(com.tokopedia.session.session.presenter.Login.GO_TO_INDEX_KEY, false);
                        login = intent.getStringExtra("login");
                        url = intent.getStringExtra("url");
                        name = intent.getStringExtra("name");
                    }
                    Fragment loginFragment = LoginFragment.newInstance(mEmail, GoToIndex, login, name, url);
                    moveToFragment(loginFragment, true, LOGIN_FRAGMENT_TAG, TkpdState.DrawerPosition.LOGIN);
                } else {
                    Log.d(TAG, messageTAG + LoginFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
            case TkpdState.DrawerPosition.REGISTER:
                if (isFragmentCreated(REGISTER_FRAGMENT_TAG)) {
                    Fragment fragment = RegisterInitialFragment.newInstance();
                    moveToFragment(fragment, true, REGISTER_FRAGMENT_TAG, TkpdState.DrawerPosition.REGISTER);

                    session.setWhichFragment(TkpdState.DrawerPosition.REGISTER);
                    setToolbarTitle();
                    invalidateOptionsMenu();
                } else {
                    Log.d(TAG, messageTAG + RegisterInitialFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
            case TkpdState.DrawerPosition.SECURITY_QUESTION:
                if (isFragmentCreated(SECURITY_QUESTION_TAG)) {
                    Log.d(TAG, messageTAG + FragmentSecurityQuestion.class.getSimpleName() + " is created !!!");
                    Fragment fragment = FragmentSecurityQuestion
                            .newInstance(0, 0, "", "", null);
                    moveToFragment(fragment, false, SECURITY_QUESTION_TAG, TkpdState.DrawerPosition.SECURITY_QUESTION);
                } else {
                    Log.d(TAG, messageTAG + FragmentSecurityQuestion.class.getSimpleName() + " is not created !!!");
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public void moveToFragmentSecurityQuestion(int security1, int security2, int userId, String email) {
        Fragment fragment = FragmentSecurityQuestion
                .newInstance(security1, security2, String.valueOf(userId), email, null);
        moveToFragment(fragment, false, SECURITY_QUESTION_TAG, TkpdState.DrawerPosition.SECURITY_QUESTION);

        // Change the header
        session.setWhichFragment(TkpdState.DrawerPosition.SECURITY_QUESTION);
        setToolbarTitle();
        invalidateOptionsMenu();
    }

    @Override
    public void destroy() {
        if (SessionHandler.isV4Login(this)) {
            if (getApplication() instanceof IReactNativeRouter) {
                IReactNativeRouter reactNativeRouter = (IReactNativeRouter) getApplication();
                reactNativeRouter.sendLoginEmitter(SessionHandler.getLoginID(this));
            }
        }
        Log.d(getClass().getSimpleName(), "destroy");
        this.setResult(RESULT_OK);
        this.finish();
        session.finishTo();
    }

    /**
     * @param tag
     * @return true means fragment is null
     */
    @Override
    public boolean isFragmentCreated(String tag) {
        return supportFragmentManager.findFragmentByTag(tag) == null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
//        Log.d(TAG, messageTAG + "onSaveInstance whichFragment : " + whichFragmentKey);
//        outState.putInt(WHICH_FRAGMENT_KEY, whichFragmentKey);
        session.saveDataBeforeRotate(outState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
//        Log.d(TAG, messageTAG + "onSaveInstance  API21+ whichFragment : " + whichFragmentKey);
//        outState.putInt(WHICH_FRAGMENT_KEY, whichFragmentKey);
        session.saveDataBeforeRotate(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login, menu);
        menu.findItem(R.id.action_settings).setVisible(false);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (GlobalConfig.isSellerApp()) {

            }
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            Log.d(TAG, messageTAG + " R.id.home !!!");
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, messageTAG + " android.R.id.home !!!");
            getSupportFragmentManager().popBackStack();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @NeedsPermission(android.Manifest.permission.GET_ACCOUNTS)
    @Override
    public void updateUI(boolean isSignedIn, LoginGoogleModel model) {
        if (isSignedIn) {
            Fragment fragment = supportFragmentManager.findFragmentById(R.id.login_fragment);
            if (fragment instanceof LoginFragment && fragment.isVisible()) {
                ((LoginFragment) fragment).startLoginWithGoogle(LoginModel.GoogleType, model);
            }

            // [START] pass some data to register fragment
            fragment = supportFragmentManager.findFragmentById(R.id.login_fragment);
            if (fragment instanceof RegisterInitialFragment && fragment.isVisible()) {
                ((RegisterInitialFragment) fragment).startLoginWithGoogle(LoginModel.GoogleType, model);
            }
            // [END] pass some data to register fragment

        } else {
            // Show signed-out message and clear email field

            // Set button visibility
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LoginPermissionsDispatcher.onRequestPermissionsResult(Login.this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.GET_ACCOUNTS)
    void showDeniedForGetAccounts() {
        RequestPermissionUtil.onPermissionDenied(this, android.Manifest.permission.GET_ACCOUNTS);

    }

    @OnNeverAskAgain(android.Manifest.permission.GET_ACCOUNTS)
    void showNeverForGetAccounts() {
        RequestPermissionUtil.onNeverAskAgain(this, android.Manifest.permission.GET_ACCOUNTS);

    }

    @OnShowRationale(android.Manifest.permission.GET_ACCOUNTS)
    void showRationaleForGetAccounts(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(this, request, android.Manifest.permission.GET_ACCOUNTS);
    }

    @Override
    public String getGenderFromGoogle(Person user) {
        String response;
        switch (user.getGender()) {
            case 0:
                response = "male";
                break;
            case 1:
                response = "female";
                break;
            default:
                response = "male";
                break;
        }
        return response;
    }

    @Override
    public String getBirthdayFromGoogle(Person user) {
        String response = null;
        if (user.getBirthday() == null) {
            response = "01/01/1991";
        } else {
            String[] temp = user.getBirthday().split("-");
            response = temp[1] + "/" + temp[2] + "/" + temp[0];
        }
        return response;
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        int type = resultData.getInt(DownloadService.TYPE, DownloadService.INVALID_TYPE);

        Fragment fragment = findFragment(type);

        if (fragment != null && fragment instanceof BaseView && type != DownloadService.INVALID_TYPE) {
            switch (resultCode) {
                case DownloadService.STATUS_RUNNING:
                    showProgressDialog(fragment, type, resultData);
                    break;
                case DownloadService.STATUS_FINISHED:
                    finishTask(fragment, type, resultData);
                    break;
                case DownloadService.STATUS_ERROR:
                    sendMessageError(fragment, type, resultData);
                    break;
            }
        }
    }

    private void finishTask(Fragment fragment, int type, Bundle resultData) {
        switch (type) {
            case OTPService.ACTION_REQUEST_OTP_WITH_CALL:
                if (fragment instanceof FragmentSecurityQuestion && resultData.getString(OTPService.EXTRA_BUNDLE) != null) {
                    session.sendGTMEvent(resultData, type);
                    session.sendAnalyticsEvent(resultData, type);
                    ((FragmentSecurityQuestion) fragment).onSuccessRequestOTPWithCall(resultData.getString(OTPService.EXTRA_BUNDLE));
                }
            case OTPService.ACTION_REQUEST_OTP:
                if (fragment instanceof FragmentSecurityQuestion && resultData.getString(OTPService.EXTRA_BUNDLE) != null) {
                    session.sendGTMEvent(resultData, type);
                    session.sendAnalyticsEvent(resultData, type);
                    ((FragmentSecurityQuestion) fragment).onSuccessRequestOTP(resultData.getString(OTPService.EXTRA_BUNDLE));
                }
            case DownloadService.LOGIN_EMAIL:
            case DownloadService.SECURITY_QUESTION_GET:
            case DownloadService.ANSWER_SECURITY_QUESTION:
            case DownloadService.REGISTER:
            case DownloadService.REGISTER_PASS_PHONE:
            case DownloadService.LOGIN_ACCOUNTS_INFO:
            case DownloadService.MAKE_LOGIN:
                sendBroadcast(new Intent(ShopInfoActivity.LOGIN_ACTION));
                if (resultData.getBoolean(DownloadService.RETRY_FLAG, false)) {
                    boolean retry = resultData.getBoolean(DownloadService.RETRY_FLAG, false);
                    ((BaseView) fragment).ariseRetry(type, retry);
                } else {
                    if (resultData.getBoolean(DownloadService.LOGIN_MOVE_REGISTER_THIRD, false)) {// register new for third party
                        CreatePasswordModel model = Parcels.unwrap(resultData.getParcelable(DownloadService.LOGIN_GOOGLE_MODEL_KEY));
                        moveToRegisterPassPhone(model, null, resultData);
                    } else {
                        session.sendGTMEvent(resultData, type);
                        session.sendAnalyticsEvent(resultData, type);
                        ((BaseView) fragment).setData(type, resultData);
                        UserAuthenticationAnalytics.sendAnalytics(resultData);
                    }
                }
                break;
            case DownloadService.LOGIN_GOOGLE:
            case DownloadService.REGISTER_GOOGLE:
            case DownloadService.LOGIN_ACCOUNTS_TOKEN:
            case DownloadService.LOGIN_FACEBOOK:
            case DownloadService.LOGIN_WEBVIEW:
            case DownloadService.REGISTER_FACEBOOK:
                sendDataFromInternet(DownloadService.LOGIN_ACCOUNTS_INFO, resultData);
                break;
            case DownloadService.RESET_PASSWORD:
                ((BaseView) fragment).setData(type, resultData);
                break;
        }
    }

    private void sendMessageError(Fragment fragment, int type, Bundle resultData) {
        switch (resultData.getInt(DownloadService.NETWORK_ERROR_FLAG, DownloadService.INVALID_NETWORK_ERROR_FLAG)) {
            case NetworkConfig.BAD_REQUEST_NETWORK_ERROR:
                ((BaseView) fragment).onNetworkError(type, getString(R.string.default_request_error_unknown));
                break;
            case NetworkConfig.INTERNAL_SERVER_ERROR:
                ((BaseView) fragment).onNetworkError(type, getString(R.string.default_request_error_internal_server));
                break;
            case NetworkConfig.FORBIDDEN_NETWORK_ERROR:
                ((BaseView) fragment).onNetworkError(type, getString(R.string.default_request_error_forbidden_auth));
                break;
            case DownloadService.INVALID_NETWORK_ERROR_FLAG:
            default:
                String messageError = resultData.getString(DownloadService.MESSAGE_ERROR_FLAG, DownloadService.INVALID_MESSAGE_ERROR);
                if (!messageError.equals(DownloadService.INVALID_MESSAGE_ERROR)) {
                    ((BaseView) fragment).onMessageError(type, messageError);
                }
        }

        if (fragment instanceof FragmentSecurityQuestion && resultData.getString(OTPService.EXTRA_ERROR) != null) {
            ((FragmentSecurityQuestion) fragment).showError(resultData.getString(OTPService.EXTRA_ERROR));
        }
    }

    private void showProgressDialog(Fragment fragment, int type, Bundle resultData) {
        switch (type) {
            case DownloadService.LOGIN_EMAIL:
            case DownloadService.LOGIN_GOOGLE:
            case DownloadService.LOGIN_FACEBOOK:
            case DownloadService.LOGIN_ACCOUNTS_TOKEN:
            case DownloadService.LOGIN_ACCOUNTS_INFO:
            case DownloadService.LOGIN_UNIQUE_CODE:
            case DownloadService.LOGIN_ACCOUNTS_PROFILE:
            case DownloadService.MAKE_LOGIN:
            case DownloadService.LOGIN_WEBVIEW:
            case DownloadService.REGISTER_FACEBOOK:
                //[START] show progress bar
                if (fragment instanceof LoginFragment) {
                    boolean showDialog = resultData.getBoolean(DownloadService.LOGIN_SHOW_DIALOG, false);
                    ((LoginFragment) fragment).showProgress(showDialog);
                }
                if (fragment instanceof RegisterPassPhoneFragment) {
                    boolean showDialog = resultData.getBoolean(DownloadService.LOGIN_SHOW_DIALOG, false);
                    ((RegisterPassPhoneFragment) fragment).showProgress(showDialog);
                }
                break;
            case OTPService.ACTION_REQUEST_OTP:
            case OTPService.ACTION_REQUEST_OTP_WITH_CALL:
            case DownloadService.ANSWER_SECURITY_QUESTION:
                if (fragment instanceof FragmentSecurityQuestion) {
                    boolean showDialog = resultData.getBoolean(DownloadService.SECURITY_QUESTION_LOADING, false);
                    ((FragmentSecurityQuestion) fragment).displayProgress(showDialog);
                }
                break;
        }
    }

    private Fragment findFragment(int type) {
        Fragment fragment;
        switch (type) {
            case DownloadService.LOGIN_EMAIL:
                fragment = supportFragmentManager.findFragmentByTag(LOGIN_FRAGMENT_TAG);
                break;
            case DownloadService.SECURITY_QUESTION_GET:
            case OTPService.ACTION_REQUEST_OTP:
            case OTPService.ACTION_REQUEST_OTP_WITH_CALL:
            case DownloadService.ANSWER_SECURITY_QUESTION:
                fragment = supportFragmentManager.findFragmentByTag(SECURITY_QUESTION_TAG);
                break;
            case DownloadService.REGISTER:
                fragment = supportFragmentManager.findFragmentByTag(REGISTER_NEXT_TAG);
                break;
            case DownloadService.REGISTER_PASS_PHONE:
                fragment = supportFragmentManager.findFragmentByTag(REGISTER_THIRD);
                break;
            case DownloadService.REGISTER_GOOGLE:
            case DownloadService.LOGIN_GOOGLE:
            case DownloadService.LOGIN_FACEBOOK:
            case DownloadService.LOGIN_ACCOUNTS_TOKEN:
            case DownloadService.LOGIN_UNIQUE_CODE:

            case DownloadService.LOGIN_ACCOUNTS_INFO:
            case DownloadService.LOGIN_ACCOUNTS_PROFILE:
            case DownloadService.LOGIN_WEBVIEW:
            case DownloadService.MAKE_LOGIN:
            case DownloadService.REGISTER_FACEBOOK:
                fragment = supportFragmentManager.findFragmentById(R.id.login_fragment);
                break;
            case DownloadService.RESET_PASSWORD:
                fragment = supportFragmentManager.findFragmentByTag(FORGOT_PASSWORD_TAG);
                break;
            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
        return fragment;
    }

    @Override
    public void sendDataFromInternet(int type, Bundle data) {
        switch (type) {
            case OTPService.ACTION_REQUEST_OTP:
                OTPService.startAction(this, data, otpReceiver, OTPService.ACTION_REQUEST_OTP);
                break;
            case OTPService.ACTION_REQUEST_OTP_WITH_CALL:
                OTPService.startAction(this, data, otpReceiver, OTPService.ACTION_REQUEST_OTP_WITH_CALL);
                break;
            case DownloadService.ANSWER_SECURITY_QUESTION:
            case DownloadService.SECURITY_QUESTION_GET:
                DownloadService.startDownload(this, mReceiver, data, type);
                break;

            case DownloadService.REGISTER_GOOGLE:
            case DownloadService.LOGIN_GOOGLE:
                DownloadService.setLoginGoogleModel((LoginGoogleModel) Parcels.unwrap(data.getParcelable(DownloadServiceConstant.LOGIN_GOOGLE_MODEL_KEY)));
                LoginService.startLogin(this, loginReceiver, data, type);
                break;

            case DownloadService.LOGIN_FACEBOOK:
            case DownloadService.REGISTER_FACEBOOK:
                DownloadService.setLoginFacebookViewModel((LoginFacebookViewModel) Parcels.unwrap(data.getParcelable(DownloadServiceConstant.LOGIN_FACEBOOK_MODEL_KEY)));
                LoginService.startLogin(this, loginReceiver, data, type);
                break;


            case DownloadService.LOGIN_ACCOUNTS_TOKEN:
            case DownloadService.LOGIN_ACCOUNTS_INFO:
            case DownloadService.MAKE_LOGIN:
            case DownloadService.LOGIN_WEBVIEW:
                LoginService.startLogin(this, loginReceiver, data, type);
                break;

            case DownloadService.REGISTER:
            case DownloadService.REGISTER_PASS_PHONE:
                RegisterService.startRegister(this, registerReceiver, data, type);
                break;

            case DownloadService.RESET_PASSWORD:
                ResetPasswordService.startDownload(this, resetPasswordReceiver, data, type);
                break;


            default:
                throw new UnsupportedOperationException("please pass type when want to process it !!!");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void prevFragment() {
        supportFragmentManager.popBackStack();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @SuppressWarnings("Range")
    @Override
    public void showError(String text) {
        if (text != null) {
            SnackbarManager.make(this, text, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TRUE_CALLER_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                FragmentSecurityQuestion fragment = (FragmentSecurityQuestion) supportFragmentManager.findFragmentByTag(SECURITY_QUESTION_TAG);
                if (data != null && data.getStringExtra("phone") != null) {
                    fragment.onSuccessProfileShared(data.getStringExtra("phone"));
                    UnifyTracking.eventClickTruecallerConfirm();
                } else if (data != null && data.getStringExtra("error") != null) {
                    fragment.onFailedProfileShared(data.getStringExtra("error"));
                }
            }
        } else if (requestCode == REQUEST_VERIFY_PHONE_NUMBER) {
            if (GlobalConfig.isSellerApp()) {
                if (SessionHandler.isUserHasShop(this)) {
                    Intent intent = SellerAppRouter.getSellerHomeActivity(this);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    Intent intent = SellerRouter.getActivityShopCreateEdit(this);
                    startActivity(intent);
                    finish();
                }
            } else {
                loginToHome();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onForceLogout() {

    }

    @SuppressWarnings("Range")
    @Override
    public void onServerError() {
        final Snackbar snackBar = SnackbarManager.make(this, getString(R.string.msg_server_error_2), Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_report, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEmailComplain();
                    }
                });
        snackBar.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                snackBar.dismiss();
            }
        }, 10000);
    }

    @SuppressWarnings("Range")
    @Override
    public void onTimezoneError() {
        final Snackbar snackBar = SnackbarManager.make(this, getString(R.string.check_timezone),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.action_check, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(android.provider.Settings.ACTION_DATE_SETTINGS));
                    }
                });
        snackBar.show();
    }

    public void sendEmailComplain() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse(getString(com.tokopedia.session.R.string.mail_to) + getString(com.tokopedia.session.R.string.android_feedback_email)));
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(com.tokopedia.session.R.string.server_error_problem));
        intent.putExtra(Intent.EXTRA_TEXT, getString(com.tokopedia.session.R.string.application_version_text) + GlobalConfig.VERSION_CODE);
        startActivity(Intent.createChooser(intent, getString(com.tokopedia.session.R.string.send_email)));
    }

}
