package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionDetailFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

/**
 * Created by yoasfs on 28/08/17.
 */

public class SolutionDetailFragment extends BaseDaggerFragment implements SolutionDetailFragmentListener.View {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    public static final String SOLUTION_DATA = "solution_data";

    TkpdTextInputLayout tilAmount;
    EditText etAmount;
    Button btnContinue;


    SolutionDetailFragmentPresenter presenter;
    SolutionViewModel solutionViewModel;
    ResultViewModel resultViewModel;


    public static SolutionDetailFragment newInstance(ResultViewModel resultViewModel, SolutionViewModel solutionViewModel) {
        SolutionDetailFragment fragment = new SolutionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SOLUTION_DATA, solutionViewModel);
        bundle.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter = new SolutionDetailFragmentPresenter(getActivity());
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

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
    protected void setupArguments(Bundle arguments) {
        solutionViewModel = arguments.getParcelable(SOLUTION_DATA);
        resultViewModel = arguments.getParcelable(RESULT_VIEW_MODEL_DATA);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_solution_detail;
    }

    @Override
    protected void initView(View view) {
        tilAmount = (TkpdTextInputLayout) view.findViewById(R.id.til_amount);
        etAmount = (EditText) view.findViewById(R.id.et_amount);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        presenter.initResultViewModel(resultViewModel, solutionViewModel);

        tilAmount.setHint("Jumlah uang yang ingin dikembalikan");

    }

    @Override
    protected void setViewListener() {
        etAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.onAmountChanged(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.onContinueButtonClicked();
            }
        });
    }

    @Override
    public void populateDataToView(ResultViewModel resultViewModel, SolutionViewModel solutionViewModel) {
        if (resultViewModel.refundAmount != 0) {
            etAmount.setText(String.valueOf(resultViewModel.refundAmount));
        }

    }

    @Override
    public void updateAmountError(String message) {
        tilAmount.setError(message);
    }

    @Override
    public void updateBottomButton(ResultViewModel resultViewModel) {
        if (resultViewModel.refundAmount == 0) {
            buttonDisabled(btnContinue);
        } else {
            buttonSelected(btnContinue);
        }
    }

    public void buttonSelected(Button button) {
        button.setClickable(true);
        button.setEnabled(true);
        button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setTextColor(Color.parseColor("#ffffff"));
    }

    public void buttonDisabled(Button button) {
        button.setClickable(false);
        button.setEnabled(false);
        button.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setTextColor(ContextCompat.getColor(getActivity(), R.color.black_70));
    }

    @Override
    public void showSuccessToast() {

    }

    @Override
    public void showErrorToast(String error) {

    }

    @Override
    public void submitData(ResultViewModel resultViewModel) {
        Intent output = new Intent();
        output.putExtra(RESULT_VIEW_MODEL_DATA, resultViewModel);
        getActivity().setResult(Activity.RESULT_OK, output);
        getActivity().finish();
    }
}