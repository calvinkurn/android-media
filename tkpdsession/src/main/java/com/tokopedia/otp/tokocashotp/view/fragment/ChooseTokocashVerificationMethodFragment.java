package com.tokopedia.otp.tokocashotp.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.analytics.LoginPhoneNumberAnalytics;
import com.tokopedia.analytics.OTPAnalytics;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.otp.tokocashotp.view.activity.VerificationActivity;
import com.tokopedia.otp.tokocashotp.view.adapter.VerificationMethodAdapter;
import com.tokopedia.otp.tokocashotp.view.viewlistener.SelectVerification;
import com.tokopedia.otp.tokocashotp.view.viewmodel.MethodItem;
import com.tokopedia.profilecompletion.view.util.ProgressBarAnimation;
import com.tokopedia.session.R;

import java.util.ArrayList;

/**
 * @author by nisie on 11/29/17.
 */

public class ChooseTokocashVerificationMethodFragment extends BaseDaggerFragment implements
        SelectVerification.View {

    RecyclerView methodListRecyclerView;
    VerificationMethodAdapter adapter;
    TextView changePhoneNumberButton;

    @Override
    protected String getScreenName() {
        return OTPAnalytics.Screen.SCREEN_SELECT_VERIFICATION_METHOD;
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
    public void onStart() {
        super.onStart();
        ScreenTracking.screen(getScreenName());
    }

    public static Fragment createInstance(Bundle bundle) {
        Fragment fragment = new ChooseTokocashVerificationMethodFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_verification_method_tokocash, parent,
                false);
        methodListRecyclerView = view.findViewById(R.id.method_list);
        changePhoneNumberButton = view.findViewById(R.id.phone_inactive);
        prepareView();
        return view;
    }

    private void prepareView() {
        changePhoneNumberButton.setVisibility(View.GONE);
        adapter = VerificationMethodAdapter.createInstance(getList(), this);
        methodListRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager
                .VERTICAL, false));
        methodListRecyclerView.setAdapter(adapter);
    }

    private ArrayList<MethodItem> getList() {
        if (getArguments() != null && getArguments().getParcelableArrayList(VerificationActivity
                .PARAM_METHOD_LIST) != null)
            return getArguments().getParcelableArrayList(VerificationActivity.PARAM_METHOD_LIST);
        else return new ArrayList<>();
    }

    @Override
    public void onMethodSelected(int type) {
        if (getActivity() instanceof VerificationActivity) {
            switch (type) {
                case VerificationActivity.TYPE_SMS: {
                    UnifyTracking.eventTracking(LoginPhoneNumberAnalytics
                            .getChooseVerificationMethodTracking(VerificationActivity.TYPE_SMS));
                    ((VerificationActivity) getActivity()).goToSmsVerification();
                    break;
                }
                case VerificationActivity.TYPE_PHONE_CALL: {
                    UnifyTracking.eventTracking(LoginPhoneNumberAnalytics
                            .getChooseVerificationMethodTracking(VerificationActivity.TYPE_PHONE_CALL));
                    ((VerificationActivity) getActivity()).goToCallVerification();
                    break;
                }
                default:
                    throw new RuntimeException("TYPE NOT DEFINED");
            }
        }
    }
}
