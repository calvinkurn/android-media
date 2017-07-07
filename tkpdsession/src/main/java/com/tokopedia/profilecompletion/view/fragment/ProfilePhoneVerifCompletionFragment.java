package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.base.data.executor.JobExecutor;
import com.tokopedia.core.base.presentation.UIThread;
import com.tokopedia.core.network.apiservices.accounts.AccountsService;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.otp.phoneverification.view.activity.ChangePhoneNumberActivity;
import com.tokopedia.otp.phoneverification.view.activity.TokoCashWebViewActivity;
import com.tokopedia.otp.phoneverification.view.fragment.ChangePhoneNumberFragment;
import com.tokopedia.otp.phoneverification.view.fragment.PhoneVerificationFragment;
import com.tokopedia.profilecompletion.data.factory.ProfileSourceFactory;
import com.tokopedia.profilecompletion.data.mapper.EditUserInfoMapper;
import com.tokopedia.profilecompletion.data.mapper.GetUserInfoMapper;
import com.tokopedia.profilecompletion.data.repository.ProfileRepository;
import com.tokopedia.profilecompletion.data.repository.ProfileRepositoryImpl;
import com.tokopedia.profilecompletion.domain.EditUserProfileUseCase;
import com.tokopedia.profilecompletion.domain.GetUserInfoUseCase;
import com.tokopedia.profilecompletion.domain.model.GetUserInfoDomainData;
import com.tokopedia.profilecompletion.view.listener.EditProfileListener;
import com.tokopedia.profilecompletion.view.listener.GetProfileListener;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenter;
import com.tokopedia.profilecompletion.view.presenter.ProfilePhoneVerifCompletionPresenter;
import com.tokopedia.profilecompletion.view.presenter.ProfilePhoneVerifCompletionPresenterImpl;
import com.tokopedia.session.R;

import java.util.concurrent.TimeUnit;

/**
 * @author by nisie on 6/19/17.
 */

public class ProfilePhoneVerifCompletionFragment
        extends PhoneVerificationFragment {

    private static final String TOKOCASH = "TokoCash";
    public static final String TAG = "verif";
    private ProfileCompletionFragment parentView;
    private View skip;
    private View proceed;
    private GetUserInfoDomainData data;
    private ProfileCompletionPresenter parentPresenter;
    private View instruction;


    public static ProfilePhoneVerifCompletionFragment createInstance(ProfileCompletionFragment view) {
        ProfilePhoneVerifCompletionFragment fragment = new ProfilePhoneVerifCompletionFragment();
        fragment.parentView = view;
        return fragment;
    }

    @Override
    protected void initialPresenter() {

        super.initialPresenter();

        Bundle bundle = new Bundle();
        SessionHandler sessionHandler = new SessionHandler(getActivity());
        String authKey = sessionHandler.getAccessToken(getActivity());
        authKey = sessionHandler.getTokenType(getActivity()) + " " + authKey;
        bundle.putString(AccountsService.AUTH_KEY, authKey);

        AccountsService accountsService = new AccountsService(bundle);

        ProfileSourceFactory profileSourceFactory =
                new ProfileSourceFactory(
                        getActivity(),
                        accountsService,
                        new GetUserInfoMapper(),
                        new EditUserInfoMapper()
                );

        ProfileRepository profileRepository = new ProfileRepositoryImpl(profileSourceFactory);

        GetUserInfoUseCase getUserInfoUseCase = new GetUserInfoUseCase(
                new JobExecutor(),
                new UIThread(),
                profileRepository
        );

        EditUserProfileUseCase editUserProfileUseCase = new EditUserProfileUseCase(
                new JobExecutor(),
                new UIThread(),
                profileRepository
        );

        parentPresenter = parentView.getPresenter();
    }

    @Override
    protected void onFirstTimeLaunched() {
        super.onFirstTimeLaunched();
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile_phone_verif_completion;
    }

    @Override
    protected void initView(View view) {
        findView(view);
        data = parentView.getData();
        proceed = parentView.getView().findViewById(R.id.proceed);
        proceed.setEnabled(false);
        proceed.setBackgroundColor(getResources().getColor(R.color.black_12));
        skipButton = (TextView) parentView.getView().findViewById(R.id.skip);
        phoneNumberEditText.setText(CustomPhoneNumberUtil.transform(data.getPhone()));

        KeyboardHandler.DropKeyboard(getActivity(), getView());

        Spannable spannable = new SpannableString(getString(R.string.via_call));

        spannable.setSpan(new ClickableSpan() {
                              @Override
                              public void onClick(View view) {

                              }

                              @Override
                              public void updateDrawState(TextPaint ds) {
                                  ds.setColor(MethodChecker.getColor(getActivity(),
                                          com.tokopedia.core.R.color.tkpd_main_green));
                              }
                          }
                , getString(R.string.via_call).indexOf(getString(R.string.via_call_short))
                , getString(R.string.via_call).indexOf(getString(R.string.via_call_short))
                        + getString(R.string.via_call_short).length()
                , 0);

        requestOtpCallButton.setText(spannable, TextView.BufferType.SPANNABLE);

        Spannable tokoCashSpannable = new SpannableString(getString(R.string.tokocash_phone_verification_text));

        tokoCashSpannable.setSpan(new ClickableSpan() {
                                      @Override
                                      public void onClick(View view) {

                                      }

                                      @Override
                                      public void updateDrawState(TextPaint ds) {
                                          ds.setColor(MethodChecker.getColor(getActivity(),
                                                  com.tokopedia.core.R.color.tkpd_main_green));
                                      }
                                  }
                , getString(R.string.tokocash_phone_verification_text).indexOf(TOKOCASH)
                , getString(R.string.tokocash_phone_verification_text).indexOf(TOKOCASH) + TOKOCASH.length()
                , 0);

        tokocashText.setText(tokoCashSpannable, TextView.BufferType.SPANNABLE);
    }

    @Override
    protected void findView(View view) {
        verifyButton = (TextView) view.findViewById(R.id.verify_button);
        phoneNumberEditText = (TextView) view.findViewById(R.id.phone_number);
        changePhoneNumberButton = (TextView) view.findViewById(R.id.change_phone_number_button);
        instruction = view.findViewById(R.id.verification_instruction);
        requestOtpButton = (TextView) view.findViewById(R.id.send_otp);
        requestOtpCallButton = (TextView) view.findViewById(R.id.send_otp_call);
        countdownText = (TextView) view.findViewById(R.id.countdown_text);
        inputOtpView = view.findViewById(R.id.input_otp_view);
        otpEditText = (EditText) view.findViewById(R.id.input_otp);
        tokocashText = (TextView) view.findViewById(R.id.tokocash_text);
    }

    @Override
    protected void setViewListener() {
        verifyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.verifyPhoneNumber();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parentPresenter.skipView(TAG);
            }
        });

        requestOtpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestOtp();
            }
        });

        requestOtpCallButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestOtpWithCall();

            }
        });
        changePhoneNumberButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(
                        ChangePhoneNumberActivity.getChangePhoneNumberIntent(
                                getActivity(),
                                phoneNumberEditText.getText().toString()),
                        ChangePhoneNumberFragment.ACTION_CHANGE_PHONE_NUMBER);
            }
        });

        otpEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 6) {
                    verifyButton.setEnabled(true);
                    MethodChecker.setBackground(verifyButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.green_button));
                    verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));

                } else {
                    verifyButton.setEnabled(false);
                    MethodChecker.setBackground(verifyButton,
                            MethodChecker.getDrawable(getActivity(), R.drawable.grey_button_rounded));
                    verifyButton.setTextColor(MethodChecker.getColor(getActivity(), R.color.grey_500));
                }
            }
        });


        tokocashText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(TokoCashWebViewActivity.getIntentCall(getActivity()));
            }
        });
    }

    @Override
    protected void runAnimation() {
        countDownTimer = new CountDownTimer(cacheHandler.getRemainingTime() * 1000, COUNTDOWN_INTERVAL_SECOND) {
            public void onTick(long millisUntilFinished) {
                instruction.setVisibility(View.GONE);
                countdownText.setVisibility(View.VISIBLE);
                countdownText.setText(
                        "Kirim Ulang (" + String.format(FORMAT,
                                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished))
                                + ")");
                requestOtpCallButton.setVisibility(View.GONE);
            }

            public void onFinish() {
                instruction.setVisibility(View.VISIBLE);
                tokocashText.setVisibility(View.GONE);
                enableOtpButton();
            }

        }.start();
    }

    @Override
    public void onSuccessVerifyPhoneNumber() {
        finishProgressDialog();
        setViewEnabled(true);
        SessionHandler.setIsMSISDNVerified(true);
        SessionHandler.setPhoneNumber(phoneNumberEditText.getText().toString().replace("-", ""));
        parentPresenter.skipView(TAG);

        CommonUtils.UniversalToast(getActivity(), getString(R.string.success_verify_phone_number));
    }
}
