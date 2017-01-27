package com.tokopedia.session.register.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tkpd.library.ui.widget.MaterialSpinner;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.core.R2;
import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.util.CustomPhoneNumberUtil;
import com.tokopedia.session.R;
import com.tokopedia.session.register.RegisterConstant;
import com.tokopedia.session.register.presenter.RegisterStep2Presenter;
import com.tokopedia.session.register.presenter.RegisterStep2PresenterImpl;
import com.tokopedia.session.register.viewlistener.RegisterStep2ViewListener;

import butterknife.BindView;

/**
 * Created by nisie on 1/27/17.
 */

public class RegisterStep2Fragment extends BasePresenterFragment<RegisterStep2Presenter>
        implements RegisterStep2ViewListener, RegisterConstant {

    @BindView(R2.id.register_next_status)
    LinearLayout registerNextStatus;
    @BindView(R2.id.register_next_status_message)
    TextView registerNextStatusMessage;
    @BindView(R2.id.register_next_step_2)
    LinearLayout registerNextStep2;
    @BindView(R2.id.register_next_full_name)
    TextView registerNextFullName;
    @BindView(R2.id.register_next_phone_number)
    EditText registerNextPhoneNumber;
    @BindView(R2.id.register_finish_button)
    TextView registerFinish;
    @BindView(R2.id.register_next_detail_t_and_p)
    TextView registerNextTAndC;
    @BindView(R2.id.wrapper_phone)
    TextInputLayout wrapperPhone;
    @BindView(R2.id.wrapper_gender)
    TextInputLayout wrapperGender;
    @BindView(R2.id.spinner)
    MaterialSpinner spinner;
    @BindView(R2.id.wrapper_date)
    TextInputLayout wrapperDate;
    @BindView(R2.id.date)
    MaterialSpinner dateText;

    String name;
    TkpdProgressDialog progressDialog;

    public static RegisterStep2Fragment createInstance(Bundle bundle) {
        RegisterStep2Fragment fragment = new RegisterStep2Fragment();
        fragment.setArguments(bundle);
        return fragment;
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
    protected void initialPresenter() {
        presenter = new RegisterStep2PresenterImpl(this);
    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_register_step2;
    }

    @Override
    protected void initView(View view) {
        name = getArguments().getString(BUNDLE_NAME);
        registerNextFullName.setText("Halo, "+ name +"!");

        registerFinish.setBackgroundResource(com.tokopedia.core.R.drawable.bg_rounded_corners);

    }

    @Override
    public void onResume() {
        super.onResume();
        registerNextPhoneNumber.addTextChangedListener(watcher(wrapperPhone));
        registerNextPhoneNumber.addTextChangedListener(watcher(registerNextPhoneNumber));
    }

    private TextWatcher watcher(final EditText editText) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String phone = CustomPhoneNumberUtil.transform(s.toString());
                if(s.toString().length() != phone.length()) {
                    editText.removeTextChangedListener(this);
                    editText.setText(phone);
                    editText.setSelection(phone.length());
                    editText.addTextChangedListener(this);
                }
            }
        };
    }

    private TextWatcher watcher(final TextInputLayout wrapper) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    setWrapperError(wrapper, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0){
                    setWrapperError(wrapper, getString(com.tokopedia.core.R.string.error_field_required));
                }
            }
        };
    }


    @Override
    protected void setViewListener() {
        spinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup =new PopupMenu(spinner.getContext(),spinner);
                popup.getMenuInflater().inflate(com.tokopedia.core.R.menu.gender_menu,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        spinner.setText(item.getTitle());
                        setWrapperError(wrapperGender,null);
                        if(item.getTitle().equals("Pria")){
//                            presenter.updateData(RegisterNewNext.GENDER, RegisterViewModel.GENDER_MALE);
                        }else if(item.getTitle().equals("Wanita")){
//                            presenter.updateData(RegisterNewNext.GENDER, RegisterViewModel.GENDER_FEMALE);
                        }
                        KeyboardHandler.DropKeyboard(getActivity(),getView());
                        return true;
                    }
                });

                popup.show();
            }
        });

        registerFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.finishRegister();
            }
        });

    }

    private void setWrapperError(TextInputLayout wrapper, String s) {
        if(s == null) {
            wrapper.setError(s);
            wrapper.setErrorEnabled(false);
        }else {
            wrapper.setErrorEnabled(true);
            wrapper.setError(s);
        }
    }


    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void showLoadingProgress() {
        if (progressDialog == null && getActivity() != null) {
            progressDialog = new TkpdProgressDialog(getActivity(),
                    TkpdProgressDialog.NORMAL_PROGRESS);
        }

        if (getActivity() != null) {
            progressDialog.showDialog();
        }
    }
}
