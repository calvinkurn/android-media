package com.tokopedia.session.changename.view.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.tkpd.library.ui.utilities.TkpdProgressDialog;
import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.di.DaggerUserComponent;
import com.tokopedia.di.UserComponent;
import com.tokopedia.session.R;
import com.tokopedia.session.changename.di.DaggerChangeNameComponent;
import com.tokopedia.session.changename.view.listener.ChangeNameListener;
import com.tokopedia.session.changename.view.presenter.ChangeNamePresenter;

import javax.inject.Inject;

/**
 * @author by yfsx on 22/03/18.
 */

public class ChangeNameFragment extends BaseDaggerFragment implements ChangeNameListener.View {

    private static final int COUNT_CHAR_MIN = 3;
    private static final int COUNT_CHAR_MAX = 128;


    private EditText etName;
    private TextView btnContinue;
    private TkpdProgressDialog progressDialog;

    @Inject
    ChangeNamePresenter presenter;

    public static ChangeNameFragment newInstance(Bundle bundle) {
        ChangeNameFragment fragment = new ChangeNameFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_name, container, false);
        etName = (EditText) view.findViewById(R.id.et_name);
        btnContinue = (TextView) view.findViewById(R.id.btn_continue);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        setView();
        setViewListener();
    }

    private void setView() {
        disableNextButton();
        btnContinue.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    }

    private void setViewListener() {
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showConfirmationDialog(etName.getText().toString());
            }
        });

        etName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isValidName(charSequence.toString())) {
                    enableNextButton();
                } else {
                    disableNextButton();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean isValidName(String name) {
        if (name.length() < COUNT_CHAR_MIN) {
            return false;
        }
        else if (name.length() > COUNT_CHAR_MAX) {
            return false;
        }
        return true;
    }

    private void showConfirmationDialog(final String name) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.name_confirmation));
        builder.setMessage(getResources().getString(R.string.name_confirmation_content));
        builder.setPositiveButton(getResources().getString(R.string.name_confirmation_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                presenter.submitName(name);
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.name_confirmation_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.black_54));
        dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(MethodChecker.getColor(getActivity(), R.color.tkpd_main_green));
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        UserComponent userComponent = DaggerUserComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplicationContext()).getBaseAppComponent()).build();

        DaggerChangeNameComponent.builder()
                .userComponent(userComponent)
                .build().inject(this);
    }

    @Override
    public void enableNextButton() {
        enableButton(btnContinue);
    }

    @Override
    public void disableNextButton() {
        disableButton(btnContinue);
    }

    @Override
    public void onErrorSubmitName(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);
    }

    @Override
    public void onSuccessSubmitName() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void dismissLoading() {
        if (progressDialog != null)
            progressDialog.dismiss();
    }

    @Override
    public void showLoading() {
        if (progressDialog == null)
            progressDialog = new TkpdProgressDialog(getActivity(), TkpdProgressDialog
                    .NORMAL_PROGRESS);

        progressDialog.showDialog();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    private void enableButton(TextView button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setEnabled(true);
    }

    private void disableButton(TextView button) {
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_12));
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setEnabled(false);
    }
}
