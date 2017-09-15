package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.app.Activity;
import android.app.Dialog;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.di.DaggerCreateResoComponent;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionDetailFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.di.DaggerCreateResoComponent;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import javax.inject.Inject;

/**
 * Created by yoasfs on 28/08/17.
 */

public class SolutionDetailFragment extends BaseDaggerFragment implements SolutionDetailFragmentListener.View {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    public static final String EDIT_APPEAL_MODEL_DATA = "edit_appeal_model_data";
    public static final String SOLUTION_DATA = "solution_data";

    TkpdTextInputLayout tilAmount;
    EditText etAmount;
    ProgressBar progressBar;
    Button btnContinue;

    SolutionViewModel solutionViewModel;
    EditAppealSolutionModel editAppealSolutionModel;
    ResultViewModel resultViewModel;

    @Inject
    SolutionDetailFragmentPresenter presenter;


    public static SolutionDetailFragment newInstance(ResultViewModel resultViewModel,
                                                     SolutionViewModel solutionViewModel) {
        SolutionDetailFragment fragment = new SolutionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SOLUTION_DATA, solutionViewModel);
        bundle.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SolutionDetailFragment newEditAppealDetailInstance(EditAppealSolutionModel editAppealSolutionModel,
                                                             SolutionViewModel solutionViewModel) {
        SolutionDetailFragment fragment = new SolutionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SOLUTION_DATA, solutionViewModel);
        bundle.putParcelable(EDIT_APPEAL_MODEL_DATA, editAppealSolutionModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerCreateResoComponent daggerCreateResoComponent =
                (DaggerCreateResoComponent) DaggerCreateResoComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerCreateResoComponent.inject(this);
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
        if (arguments.getParcelable(RESULT_VIEW_MODEL_DATA) != null) {
            resultViewModel = arguments.getParcelable(RESULT_VIEW_MODEL_DATA);
        } else {
            editAppealSolutionModel = arguments.getParcelable(EDIT_APPEAL_MODEL_DATA);
        }
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
        progressBar = new ProgressBar(context);
        if (resultViewModel != null) {
            presenter.initResultViewModel(resultViewModel, solutionViewModel);
        } else {
            presenter.initEditAppealSolutionModel(editAppealSolutionModel, solutionViewModel);
        }

        tilAmount.setHint(context.getResources().getString(R.string.string_money_amount_returned));

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
    public void updatePriceEditText(String price) {
        etAmount.setText(price);
    }

    @Override
    public void updateAmountError(String message) {
        tilAmount.setError(message);
    }

    @Override
    public void updateBottomButton(int refundAmount) {
        if (refundAmount == 0) {
            buttonDisabled(btnContinue);
        } else {
            buttonSelected(btnContinue);
        }
    }

    @Override
    public void successEditSolution() {
        Toast.makeText(context, "Sukses mengubah solusi", Toast.LENGTH_SHORT).show();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();

    }

    @Override
    public void errorEditSolution(String error) {
        NetworkErrorHelper.showSnackbar(getActivity(), error);

    }

    public void buttonSelected(Button button) {
        button.setClickable(true);
        button.setEnabled(true);
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_button_enable));
        button.setTextColor(ContextCompat.getColor(context, R.color.white));
    }

    public void buttonDisabled(Button button) {
        button.setClickable(false);
        button.setEnabled(false);
        button.setBackground(ContextCompat.getDrawable(context, R.drawable.bg_button_disable));
        button.setTextColor(ContextCompat.getColor(context, R.color.black_70));
    }

    @Override
    public void showDialogCompleteEditAppeal(EditAppealSolutionModel editAppealSolutionModel) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.layout_edit_solution);
        TextView tvSolution = (TextView) dialog.findViewById(R.id.tv_solution);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btnBack = (Button) dialog.findViewById(R.id.btn_back);
        Button btnEditSolution = (Button) dialog.findViewById(R.id.btn_edit_solution);

        updateSolutionString(editAppealSolutionModel, tvSolution);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnEditSolution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.submitEditAppeal();
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void updateSolutionString(EditAppealSolutionModel editAppealSolutionModel, TextView textView) {
        textView.setText(editAppealSolutionModel.refundAmount != 0 ?
                editAppealSolutionModel.solutionName + " sebesar " + editAppealSolutionModel.refundAmount :
                editAppealSolutionModel.solutionName);
    }

    @Override
    public void showErrorToast(String error) {

    }

    @Override
    public void showLoading() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideLoading() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void submitData(ResultViewModel resultViewModel) {
        Intent output = new Intent();
        output.putExtra(RESULT_VIEW_MODEL_DATA, resultViewModel);
        getActivity().setResult(Activity.RESULT_OK, output);
        getActivity().finish();
    }
}