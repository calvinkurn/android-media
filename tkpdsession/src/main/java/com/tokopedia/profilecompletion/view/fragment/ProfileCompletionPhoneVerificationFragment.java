package com.tokopedia.profilecompletion.view.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.snackbar.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler;
import com.tokopedia.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;
import com.tokopedia.profilecompletion.view.viewmodel.ProfileCompletionViewModel;
import com.tokopedia.session.R;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.util.CustomPhoneNumberUtil;

/**
 * Created by nisie on 2/22/17.
 */

public class ProfileCompletionPhoneVerificationFragment extends PhoneVerificationFragment {

    public static final String TAG = "verif";

    private ProfileCompletionContract.View  parentView;
    private ProfileCompletionContract.Presenter parentPresenter;

    protected TextView verifyButton;
    private ProfileCompletionViewModel data;
    protected TextView skipFragment;
    private UserSession userSession;

    public ProfileCompletionPhoneVerificationFragment() {

    }

    public static ProfileCompletionPhoneVerificationFragment createInstance
            (ProfileCompletionContract.View view) {
        return new ProfileCompletionPhoneVerificationFragment(view);
    }

    public ProfileCompletionPhoneVerificationFragment(ProfileCompletionContract.View view) {
        this.parentView = view;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        if (parentView != null) {
            parentPresenter = parentView.getPresenter();
            userSession = parentView.getUserSession();
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void findView(View view) {
        super.findView(view);
        initView();
    }

    @Override
    public void initInjector() {
        super.initInjector();
    }

    @Override
    public void setViewListener() {
        super.setViewListener();
        skipButton.setVisibility(View.GONE);
        skipFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parentPresenter.skipView(TAG);
            }
        });
    }

    protected void initView() {
        if (parentView == null && getActivity() instanceof ProfileCompletionActivity)
            parentView = ((ProfileCompletionActivity) getActivity())
                    .getProfileCompletionContractView();
        data = parentView.getData();
        verifyButton = (TextView) parentView.getView().findViewById(R.id.proceed);
        verifyButton.setText(getResources().getString(R.string.continue_form));
        verifyButton.setVisibility(View.GONE);

        skipFragment = (TextView) parentView.getView().findViewById(R.id.skip);
        skipFragment.setVisibility(View.GONE);

        if (data.getPhone() != null) {
            phoneNumberEditText.setText(CustomPhoneNumberUtil.transform(data.getPhone()));
        } else {
            SnackbarManager.make(getActivity(),
                    getString(R.string.please_fill_phone_number),
                    Snackbar.LENGTH_LONG)
                    .show();
        }

        KeyboardHandler.DropKeyboard(getActivity(), getView());
    }

    @Override
    public void onSuccessVerifyPhoneNumber() {
        parentView.getUserSession().setIsMSISDNVerified(true);
        userSession.setPhoneNumber(getPhoneNumber());
        parentView.onSuccessEditProfile(EditUserProfileUseCase.EDIT_VERIF);
        CommonUtils.UniversalToast(getActivity(), getString(R.string
                .success_verify_phone_number));
    }
}
