package com.tokopedia.tokocash.autosweepmf.view.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.home.SimpleWebViewActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.tokocash.R;
import com.tokopedia.tokocash.autosweepmf.view.activity.HelpActivity;
import com.tokopedia.tokocash.autosweepmf.view.activity.SetAutoSweepLimitActivity;
import com.tokopedia.tokocash.autosweepmf.view.contract.AutoSweepHomeContract;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepDetail;
import com.tokopedia.tokocash.autosweepmf.view.model.AutoSweepLimit;
import com.tokopedia.tokocash.autosweepmf.view.presenter.AutoSweepHomePagePresenter;
import com.tokopedia.tokocash.autosweepmf.view.util.CommonConstant;
import com.tokopedia.tokocash.di.TokoCashComponent;

import javax.inject.Inject;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Landing page for auto sweep features which can be added into any activity or fragment
 * It will taking the current tokocash balance as an argument param
 */
public class AutoSweepHomeFragment extends BaseDaggerFragment implements AutoSweepHomeContract.View, View.OnClickListener {

    private static final int AUTO_SWEEP_INACTIVE = 0;
    private static final int AUTO_SWEEP_ACTIVE = 1;
    private static final int MF_ACTIVE = 1;
    private static final int MF_INACTIVE = 0;
    private static final int MF_ON_HOLD = 2;
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private TextView mTextLimitTokocash, mTextLimitTokocashValue, mTextDescription;
    private Button mBtnPositive, mBtnNegative;
    private LinearLayout mContainerWarning;
    private TextView mTextWaningTitle, mTextWarningMessage, mTextError;
    private Switch mSwitchAutoSweep;
    private ViewFlipper mContainerMain;
    private int mAccountStatus = -1;
    private int mAutoSweepStatus = -1;
    private BottomSheetView mToolTip;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getBooleanExtra(CommonConstant.EVENT_KEY_NEEDED_RELOADING, false)) {
                mPresenter.getAutoSweepDetail();
            }
        }
    };

    @Inject
    AutoSweepHomePagePresenter mPresenter;

    public static AutoSweepHomeFragment newInstance(@NonNull Bundle bundle) {
        AutoSweepHomeFragment fragment = new AutoSweepHomeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.fragment_home_autosweepmf, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        initListener();
        mPresenter.getAutoSweepDetail();
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getAppContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void showLoading() {
        mContainerMain.setDisplayedChild(CONTAINER_LOADER);
    }

    @Override
    public void hideLoading() {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
    }

    @Override
    public void showToast(@NonNull String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    /**
     * mutualfund_account_status
     * 0=> user do not have mutual fund mutualfund_account
     * 1=> user have active mutual fund account
     * 2=> user have pending for mutual fund registrion (On Hold)
     * <p>
     * autosweep_status
     * 0=> Autosweep turn off
     * 1=> AutoSweep turn On
     */
    @Override
    public void onSuccessAutoSweepDetail(@NonNull AutoSweepDetail data) {
        if (data.getAccountStatus() == MF_ACTIVE) {
            onAccountActive(data);
        } else if (data.getAccountStatus() == MF_INACTIVE) {
            onAccountInActive();
        } else if (data.getAccountStatus() == MF_ON_HOLD) {
            onAccountHold(data);
        }

        if (data.getAutoSweepStatus() == AUTO_SWEEP_ACTIVE) {
            onAutoSweepActive();
        } else if (data.getAutoSweepStatus() == AUTO_SWEEP_INACTIVE) {
            onAutoSweepInActive();
        }

        //init tooltip
        if (data.getTooltipContent() != null && !data.getTooltipContent().trim().isEmpty()) {
            initToolTip(data.getTitle(), data.getTooltipContent());
        }
    }

    @Override
    public void onErrorAutoSweepDetail(@NonNull String error) {
        showError(error);
    }

    @Override
    public void onAccountHold(AutoSweepDetail data) {
        mAccountStatus = MF_ON_HOLD;
        mTextDescription.setVisibility(View.GONE);

        if (data.getAmountLimit() > 0) {
            mTextLimitTokocash.setVisibility(View.VISIBLE);
            mTextLimitTokocashValue.setVisibility(View.VISIBLE);
            mTextLimitTokocashValue.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat((int)
                    data.getAmountLimit(), false));
        }

        if (data.getTitle() != null
                && !data.getTitle().isEmpty()) {
            mContainerWarning.setVisibility(View.VISIBLE);
            mTextWaningTitle.setText(data.getTitle());
            mTextWarningMessage.setText(data.getContent());
        }
    }

    @Override
    public void onAccountInActive() {
        mAccountStatus = MF_INACTIVE;
        mTextDescription.setVisibility(View.VISIBLE);
        mTextLimitTokocash.setVisibility(View.GONE);
        mTextLimitTokocashValue.setVisibility(View.GONE);
        mContainerWarning.setVisibility(View.GONE);
    }

    @Override
    public void onAccountActive(@NonNull AutoSweepDetail data) {
        mAccountStatus = MF_ACTIVE;
        mTextDescription.setVisibility(View.GONE);
        mContainerWarning.setVisibility(View.GONE);

        if (data.getAmountLimit() > 0) {
            mTextLimitTokocash.setVisibility(View.VISIBLE);
            mTextLimitTokocashValue.setVisibility(View.VISIBLE);
            mTextLimitTokocashValue.setText(CurrencyFormatUtil.convertPriceValueToIdrFormat((int)
                    data.getAmountLimit(), false));
        }
    }

    @Override
    public void onAutoSweepActive() {
        mAutoSweepStatus = AUTO_SWEEP_ACTIVE;
        mSwitchAutoSweep.setVisibility(View.VISIBLE);
        mSwitchAutoSweep.setChecked(true);
        mBtnPositive.setText(R.string.mf_action_goto_dashboard);
    }

    @Override
    public void onAutoSweepInActive() {
        mAutoSweepStatus = AUTO_SWEEP_INACTIVE;
        mSwitchAutoSweep.setVisibility(View.GONE);
        mSwitchAutoSweep.setChecked(false);
        mBtnPositive.setText(R.string.mf_action_enable);
    }

    @Override
    public void showDialog(@StringRes int title, @StringRes int content) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivity());
        adb.setTitle(title);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            adb.setMessage(Html.fromHtml(getString(content), Html.FROM_HTML_MODE_LEGACY));
        } else {
            adb.setMessage(Html.fromHtml(getString(content)));
        }

        adb.setPositiveButton(R.string.mf_action_enable_autosweep, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                navigateToLimitPage();
            }
        });

        adb.setNegativeButton(R.string.mf_label_back, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        final AlertDialog dialog = adb.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                R.color.grey_warm));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
    }

    @Override
    public void showError(String message) {
        mTextError.setError(message);
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
    }

    @Override
    public void learMore() {
        startActivity(HelpActivity.getCallingIntent(getAppContext()));
    }

    @Override
    public void navigateToLimitPage() {
        Bundle extras = new Bundle();
        extras.putString(CommonConstant.EXTRA_AVAILABLE_TOKOCASH,
                getArguments().getString(CommonConstant.EXTRA_AVAILABLE_TOKOCASH,
                        CommonConstant.NOT_AVAILABLE));
        startActivity(SetAutoSweepLimitActivity.getCallingIntent(getApplicationContext(),
                extras));
    }

    @Override
    public void onAttach(Context context) {
        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver,
                new IntentFilter(CommonConstant.EVENT_AUTOSWEEPMF_STATUS_CHANGED));
        super.onAttach(context);

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoCashComponent.class).inject(this);
    }

    @Override
    public void openWebView(String url) {
        Intent intent = SimpleWebViewActivity.getIntent(getActivityContext(), url);
        startActivity(intent);
    }

    @Override
    public void onSuccessAutoSweepStatus(@NonNull AutoSweepLimit data) {
        //Success mean limit has reset, now wee need to re-fetch details again
        mPresenter.getAutoSweepDetail();
    }

    @Override
    public void onErrorAutoSweepStatus(@NonNull String error) {
        NetworkErrorHelper.showCloseSnackbar(getActivity(), error);
    }

    @Override
    public Context getAppContext() {
        return getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void onClick(View source) {
        if (source.getId() == R.id.button_positive) {
            if (mAutoSweepStatus == AUTO_SWEEP_ACTIVE) {
                openWebView(TkpdBaseURL.AutoSweep.WEB_LINK_MF_DASHBOARD);
            } else {
                if (mAccountStatus == MF_INACTIVE) {
                    showDialog(R.string.mf_titile_mf_account_in_process, R.string.mf_message_mutual_fund_not_listed);
                } else {
                    showDialog(R.string.mf_title_mutual_fund_not_listed, R.string.mf_message_mf_account_in_process);
                }
            }
        } else if (source.getId() == R.id.button_negative) {
            learMore();
        } else if (source.getId() == R.id.text_warning_title) {
            if (mToolTip != null && !mToolTip.isShowing()) {
                mToolTip.show();
            }
        } else if (source.getId() == R.id.text_limit_tokocash_value) {
            navigateToLimitPage();
        }
    }

    private void initViews(View view) {
        mTextLimitTokocash = view.findViewById(R.id.text_limit_tokocash);
        mTextLimitTokocashValue = view.findViewById(R.id.text_limit_tokocash_value);
        mTextDescription = view.findViewById(R.id.text_description);
        mTextWaningTitle = view.findViewById(R.id.text_warning_title);
        mTextWarningMessage = view.findViewById(R.id.text_warning_message);
        mTextError = view.findViewById(R.id.text_error);

        mBtnPositive = view.findViewById(R.id.button_positive);
        mBtnNegative = view.findViewById(R.id.button_negative);

        mSwitchAutoSweep = view.findViewById(R.id.switch_auto_sweep);
        mContainerMain = view.findViewById(R.id.container_main);
        mContainerWarning = view.findViewById(R.id.container_warning);
    }

    private void initListener() {
        mBtnPositive.setOnClickListener(this);
        mBtnNegative.setOnClickListener(this);
        mTextWaningTitle.setOnClickListener(this);
        mTextLimitTokocashValue.setOnClickListener(this);

        mSwitchAutoSweep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    //Disabling the autosweep so that passing zero
                    mPresenter.updateAutoSweepStatus(false, 0);
                }
            }
        });
    }

    private void initToolTip(String title, String content) {
        mToolTip = new BottomSheetView(getActivityContext());
        mToolTip.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(title)
                .setBody(content)
                .setUrlButton(TkpdBaseURL.AutoSweep.WEB_LINK_MF_DASHBOARD, getString(R.string.mf_action_goto_dashboard))
                .build());
        mToolTip.setListener(new BottomSheetView.ActionListener() {
            @Override
            public void clickOnTextLink(String status) {
            }

            @Override
            public void clickOnButton(String url, String appLink) {
                openWebView(TkpdBaseURL.AutoSweep.WEB_LINK_MF_DASHBOARD);
            }
        });
    }
}
