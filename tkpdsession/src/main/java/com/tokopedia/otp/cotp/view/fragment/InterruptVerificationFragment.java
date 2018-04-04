package com.tokopedia.otp.cotp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.analytics.OTPAnalytics;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.viewmodel.InterruptVerificationViewModel;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.session.R;

import javax.inject.Inject;

/**
 * @author by nisie on 1/4/18.
 */

public class InterruptVerificationFragment extends BaseDaggerFragment {

    InterruptVerificationViewModel viewModel;
    ImageView icon;
    TextView message;
    TextView requestOtpButton;
    TextView chooseOtherMethod;


    @Inject
    GlobalCacheManager globalCacheManager;


    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new InterruptVerificationFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    @Override
    protected String getScreenName() {
        if (viewModel != null && !TextUtils.isEmpty(viewModel.getAppScreenName())) {
            return viewModel.getAppScreenName();
        } else
            return OTPAnalytics.Screen.SCREEN_INTERRUPT_VERIFICATION_DEFAULT;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);

        DaggerSessionComponent daggerSessionComponent = (DaggerSessionComponent)
                DaggerSessionComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerSessionComponent.inject(this);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (globalCacheManager != null
                && globalCacheManager.getConvertObjData(VerificationActivity.PASS_MODEL, VerificationPassModel.class) != null
                && globalCacheManager.getConvertObjData(VerificationActivity.PASS_MODEL,
                VerificationPassModel.class).getInterruptModel() != null) {
            VerificationPassModel passModel = globalCacheManager.getConvertObjData(VerificationActivity.PASS_MODEL,
                    VerificationPassModel.class);
            viewModel = passModel.getInterruptModel();
            viewModel.setHasOtherMethod(passModel.canUseOtherMethod());
        } else {
            getActivity().finish();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_interrupt_verification, parent, false);
        icon = view.findViewById(R.id.icon);
        message = view.findViewById(R.id.message);
        requestOtpButton = view.findViewById(R.id.request_otp_button);
        chooseOtherMethod = view.findViewById(R.id.choose_other_method);
        prepareView();
        return view;
    }

    private void prepareView() {
        ImageHandler.loadImageWithIdWithoutPlaceholder(icon, viewModel.getIconId());
        message.setText(MethodChecker.fromHtml(viewModel.getPromptText()));
        requestOtpButton.setText(viewModel.getButtonText());

        requestOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() instanceof VerificationActivity) {
                    ((VerificationActivity) getActivity()).goToDefaultVerificationPage(viewModel.getMode());
                }
            }
        });

        if (viewModel.isHasOtherMethod()) {
            chooseOtherMethod.setVisibility(View.VISIBLE);
            chooseOtherMethod.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getActivity() instanceof VerificationActivity) {
                        ((VerificationActivity) getActivity()).goToSelectVerificationMethod();
                    }
                }
            });
        } else {
            chooseOtherMethod.setVisibility(View.GONE);
        }
    }
}
