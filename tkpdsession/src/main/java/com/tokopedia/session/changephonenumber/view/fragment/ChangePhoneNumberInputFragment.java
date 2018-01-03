package com.tokopedia.session.changephonenumber.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.database.CacheUtil;
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerSessionComponent;
import com.tokopedia.di.SessionComponent;
import com.tokopedia.di.SessionModule;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;
import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
import com.tokopedia.otp.domain.interactor.RequestOtpUseCase;
import com.tokopedia.session.R;
import com.tokopedia.session.changephonenumber.view.customview.BottomSheetInfo;
import com.tokopedia.session.changephonenumber.view.listener.ChangePhoneNumberInputFragmentListener;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by milhamj on 20/12/17.
 */

public class ChangePhoneNumberInputFragment extends BaseDaggerFragment implements ChangePhoneNumberInputFragmentListener.View {
    public static final String PARAM_PHONE_NUMBER = "phone_number";
    public static final String PARAM_WARNING_LIST = "warning_list";
    public static final String PARAM_EMAIL = "email";
    public static final int REQUEST_VERIFY_CODE = 1;

    @Inject
    ChangePhoneNumberInputFragmentListener.Presenter presenter;
    TkpdProgressDialog progressDialog;
    private TextView oldPhoneNumber;
    private EditText newPhoneNumber;
    private TextView nextButton;
    private String phoneNumber;
    private ArrayList<String> warningList;
    private String email;
    private Unbinder unbinder;
    private BottomSheetInfo bottomSheetInfo;
    private TextWatcher phoneNumberTextWatcher;

    public static ChangePhoneNumberInputFragment newInstance(String phoneNumber, String email, ArrayList<String> warningList) {
        ChangePhoneNumberInputFragment fragment = new ChangePhoneNumberInputFragment();
        Bundle bundle = new Bundle();
        bundle.putString(PARAM_PHONE_NUMBER, phoneNumber);
        bundle.putStringArrayList(PARAM_WARNING_LIST, warningList);
        bundle.putString(PARAM_EMAIL, email);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View parentView = inflater.inflate(R.layout.fragment_change_phone_number_input, container, false);
        unbinder = ButterKnife.bind(this, parentView);
        presenter.attachView(this);
        initVar();
        initView(parentView);
        setViewListener();

        if (warningList != null) {
            if (warningList.size() > 0) {
                setHasOptionsMenu(true);
                createBottomSheetView();
            }
        }

        return parentView;
    }

    private void initVar() {
        phoneNumber = getArguments().getString(PARAM_PHONE_NUMBER);
        warningList = getArguments().getStringArrayList(PARAM_WARNING_LIST);
        email = getArguments().getString(PARAM_EMAIL);
    }

    private void initView(View view) {
        oldPhoneNumber = view.findViewById(R.id.old_phone_number_value);
        newPhoneNumber = view.findViewById(R.id.new_phone_number_value);
        nextButton = view.findViewById(R.id.next_button);

        oldPhoneNumber.setText(CustomPhoneNumberUtil.transform(phoneNumber));
    }

    private void setViewListener() {
        phoneNumberTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                presenter.onNewNumberTextChanged(editable);
            }
        };
        newPhoneNumber.addTextChangedListener(phoneNumberTextWatcher);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.validateNumber(cleanPhoneNumber(newPhoneNumber));
            }
        });
    }

    private void createBottomSheetView() {
        bottomSheetInfo = new BottomSheetInfo(getContext(), warningList);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_change_phone_number_input, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
            bottomSheetInfo.show();
        }
        return true;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.detachView();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        SessionComponent sessionComponent =
                DaggerSessionComponent.builder()
                        .appComponent(appComponent)
                        .sessionModule(new SessionModule())
                        .build();
        sessionComponent.inject(this);
    }

    @Override
    public void enableNextButton() {
        nextButton.setClickable(true);
        nextButton.setEnabled(true);
        nextButton.setBackground(MethodChecker.getDrawable(getContext(), R.drawable.green_button_rounded_unify));
        nextButton.setTextColor(MethodChecker.getColor(getContext(), R.color.white));
    }

    @Override
    public void disableNextButton() {
        nextButton.setClickable(false);
        nextButton.setEnabled(false);
        nextButton.setBackground(MethodChecker.getDrawable(getContext(), R.drawable.grey_button_rounded));
        nextButton.setTextColor(MethodChecker.getColor(getContext(), R.color.black_12));
    }

    @Override
    public void correctPhoneNumber(String newNumber) {
        newPhoneNumber.removeTextChangedListener(phoneNumberTextWatcher);
        newPhoneNumber.setText(newNumber);
        newPhoneNumber.setSelection(newNumber.length());
        newPhoneNumber.addTextChangedListener(phoneNumberTextWatcher);
    }

    @Override
    public void showLoading() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog
                    .NORMAL_PROGRESS);

        if (!progressDialog.isProgress())
            progressDialog.showDialog();
    }

    @Override
    public void dismissLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void onValidateNumberSuccess(Boolean isSuccess) {
        if (isSuccess != null && isSuccess) {
            goToVerification();
        } else {
            showErrorSnackbar();
        }
    }

    @Override
    public void onValidateNumberError(String message) {
        showErrorSnackbar(message);
    }

    @Override
    public void onValidateNumberFailed() {
        showErrorSnackbar();
    }

    @Override
    public void onSubmitNumberSuccess(Boolean isSuccess) {
        if (isSuccess != null && isSuccess) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        } else {
            showErrorSnackbar();
        }
    }

    @Override
    public void onSubmitNumberError(String message) {
        showErrorSnackbar(message);
    }

    @Override
    public void onSubmitNumberFailed() {
        showErrorSnackbar();
    }

    private void showErrorSnackbar() {
        showErrorSnackbar(null);
    }

    private void showErrorSnackbar(String message) {
        if (message != null) {
            NetworkErrorHelper.showSnackbar(getActivity(), message);
        } else {
            NetworkErrorHelper.showSnackbar(getActivity());
        }
    }

    private void goToVerification() {
        GlobalCacheManager cacheManager = new GlobalCacheManager();

        VerificationPassModel passModel = new VerificationPassModel(cleanPhoneNumber(newPhoneNumber), email,
                getListAvailableMethod(cleanPhoneNumber(newPhoneNumber)), RequestOtpUseCase.OTP_TYPE_SECURITY_QUESTION);
        cacheManager.setKey(VerificationActivity.PASS_MODEL);
        cacheManager.setValue(CacheUtil.convertModelToString(passModel,
                new TypeToken<VerificationPassModel>() {
                }.getType()));
        cacheManager.store();


        Intent intent = VerificationActivity.getCallingIntent(getActivity(),
                VerificationActivity.TYPE_SMS);
        startActivityForResult(intent, REQUEST_VERIFY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VERIFY_CODE && resultCode == Activity.RESULT_OK) {
            presenter.submitNumber(cleanPhoneNumber(newPhoneNumber));
        }
    }

    private ArrayList<MethodItem> getListAvailableMethod(String phone) {
        ArrayList<MethodItem> list = new ArrayList<>();
        list.add(new MethodItem(
                VerificationActivity.TYPE_SMS,
                com.tokopedia.session.R.drawable.ic_verification_sms,
                MethodItem.getSmsMethodText(phone)
        ));
        list.add(new MethodItem(
                VerificationActivity.TYPE_PHONE_CALL,
                com.tokopedia.session.R.drawable.ic_verification_call,
                MethodItem.getCallMethodText(phone)
        ));

        return list;
    }

    private String cleanPhoneNumber(EditText newPhoneNumber) {
        String newPhoneNumberString = newPhoneNumber.getText().toString();
        return newPhoneNumberString.replace("-", "");
    }
}
