package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.design.text.TkpdTextInputLayout;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionListActivity;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionDetailFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;
import com.tokopedia.inbox.rescenter.di.DaggerResolutionComponent;
import com.tokopedia.inbox.rescenter.utils.CurrencyFormatter;
import com.tokopedia.inbox.util.analytics.InboxAnalytics;

import javax.inject.Inject;

/**
 * Created by yoasfs on 28/08/17.
 */

public class SolutionDetailFragment extends BaseDaggerFragment
        implements SolutionDetailFragmentListener.View {

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
        View view = inflater.inflate(R.layout.fragment_solution_detail, container, false);
        tilAmount = (TkpdTextInputLayout) view.findViewById(R.id.til_amount);
        etAmount = (EditText) view.findViewById(R.id.et_amount);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        presenter.attachView(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupArguments(getArguments());
        initView();
        setViewListener();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerResolutionComponent resolutionComponent =
                (DaggerResolutionComponent)DaggerResolutionComponent.builder()
                        .baseAppComponent(((BaseMainApplication)getActivity().getApplicationContext())
                                .getBaseAppComponent()).build();
        resolutionComponent.inject(this);
    }


    private void setupArguments(Bundle arguments) {
        solutionViewModel = arguments.getParcelable(SOLUTION_DATA);
        if (arguments.getParcelable(RESULT_VIEW_MODEL_DATA) != null) {
            resultViewModel = arguments.getParcelable(RESULT_VIEW_MODEL_DATA);
        } else {
            editAppealSolutionModel = arguments.getParcelable(EDIT_APPEAL_MODEL_DATA);
        }
    }

    private void initView() {
        if (resultViewModel != null) {
            presenter.initResultViewModel(resultViewModel, solutionViewModel);
        } else {
            presenter.initEditAppealSolutionModel(editAppealSolutionModel, solutionViewModel);
        }

        tilAmount.setHint(getActivity().getResources().getString(R.string.string_money_amount_returned));
        if (editAppealSolutionModel != null) {
            if (SolutionListActivity.isEditFromChatReso(editAppealSolutionModel)) {
                UnifyTracking.eventTracking(InboxAnalytics.eventResoChatImpressionSolutionEditDetailPage(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.getSolutionName()));
            } else {
                UnifyTracking.eventTracking(InboxAnalytics.eventResoChatImpressionSolutionAppealDetailPage(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.getSolutionName()));
            }
        }
    }

    private void setViewListener() {
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

        btnContinue.setOnClickListener(view -> {
            presenter.onContinueButtonClicked();
            if (editAppealSolutionModel == null) {
                UnifyTracking.eventCreateResoStep2Continue();
            } else {
                if (SolutionListActivity.isEditFromChatReso(editAppealSolutionModel)) {
                    UnifyTracking.eventTracking(
                            InboxAnalytics.eventResoChatClickSolutionContinueEditDetailPage(
                                    editAppealSolutionModel.resolutionId, editAppealSolutionModel.solutionName));
                } else {
                    UnifyTracking.eventTracking(
                            InboxAnalytics.eventResoChatClickSolutionContinueAppealDetailPage(
                                    editAppealSolutionModel.resolutionId, editAppealSolutionModel.solutionName));
                }
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
        etAmount.setSelection(etAmount.getText().toString().length());
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
    public void successEditSolution(String message) {
        hideLoading();
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();

    }

    @Override
    public void errorEditSolution(String error) {
        hideLoading();
        NetworkErrorHelper.showSnackbar(getActivity(), error);

    }

    public void buttonSelected(Button button) {
        button.setClickable(true);
        button.setEnabled(true);
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_enable));
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.white));
    }

    public void buttonDisabled(Button button) {
        button.setClickable(false);
        button.setEnabled(false);
        button.setBackground(MethodChecker.getDrawable(getActivity(), R.drawable.bg_button_disable));
        button.setTextColor(MethodChecker.getColor(getActivity(), R.color.black_70));
    }

    @Override
    public void showDialogCompleteEditAppeal(EditAppealSolutionModel editAppealSolutionModel) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_edit_solution);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);
        TextView tvSolution = (TextView) dialog.findViewById(R.id.tv_solution);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btnBack = (Button) dialog.findViewById(R.id.btn_back);
        Button btnEditSolution = (Button) dialog.findViewById(R.id.btn_edit_solution);

        if (editAppealSolutionModel.isEdit) {
            tvTitle.setText(getActivity().getString(R.string.string_edit_title));
            tvMessage.setText(getActivity().getString(R.string.string_edit_message));
        } else {
            tvTitle.setText(getActivity().getString(R.string.string_appeal_title));
            tvMessage.setText(getActivity().getString(R.string.string_appeal_message));
        }

        updateSolutionString(editAppealSolutionModel, tvSolution);

        btnBack.setOnClickListener(view -> dialog.dismiss());
        ivClose.setOnClickListener(view -> dialog.dismiss());

        btnEditSolution.setOnClickListener(view -> {
            presenter.submitEditAppeal();
            dialog.dismiss();
        });

        dialog.show();
    }

    public void updateSolutionString(EditAppealSolutionModel editAppealSolutionModel, TextView textView) {
        textView.setText(editAppealSolutionModel.refundAmount != 0 && editAppealSolutionModel.solutionName != null ?
                editAppealSolutionModel.solutionName.replace(
                        getActivity().getResources().getString(R.string.string_return_value),
                        CurrencyFormatter.formatDotRupiah(String.valueOf(editAppealSolutionModel.refundAmount))) :
                editAppealSolutionModel.getName());
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
        if (editAppealSolutionModel != null) {
            if (SolutionListActivity.isEditFromChatReso(editAppealSolutionModel)) {
                UnifyTracking.eventTracking(InboxAnalytics.eventResoChatClickSolutionEditDetailPage(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.getSolutionName()));
            } else {
                UnifyTracking.eventTracking(InboxAnalytics.eventResoChatClickSolutionAppealDetailPage(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.getSolutionName()));
            }
        }
        Intent output = new Intent();
        output.putExtra(RESULT_VIEW_MODEL_DATA, resultViewModel);
        getActivity().setResult(Activity.RESULT_OK, output);
        getActivity().finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}