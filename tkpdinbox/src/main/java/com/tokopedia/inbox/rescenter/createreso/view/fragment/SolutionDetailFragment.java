package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionListActivity;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.SolutionDetailAdapter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionDetailFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.typefactory.SolutionRefundTypeFactory;
import com.tokopedia.inbox.rescenter.createreso.view.typefactory.SolutionRefundTypeFactoryImpl;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionComplaintModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionOrderModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;
import com.tokopedia.inbox.rescenter.di.DaggerResolutionComponent;
import com.tokopedia.inbox.rescenter.utils.CurrencyFormatter;
import com.tokopedia.inbox.util.analytics.InboxAnalytics;
import com.tokopedia.track.TrackApp;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by yoasfs on 28/08/17.
 */

public class SolutionDetailFragment extends BaseDaggerFragment
        implements SolutionDetailFragmentListener.View {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    public static final String EDIT_APPEAL_MODEL_DATA = "edit_appeal_model_data";
    public static final String SOLUTION_RESPONSE_MODEL = "solution_response_data";
    public static final String SOLUTION_DATA = "solution_data";
    private TextView tvRefundTotal;
    private ProgressBar progressBar;
    private Button btnContinue;
    private RecyclerView recyclerView;
    private SolutionRefundTypeFactory typeFactory;
    private SolutionDetailAdapter adapter;


    private SolutionViewModel solutionViewModel;
    private EditAppealSolutionModel editAppealSolutionModel;
    private ResultViewModel resultViewModel;
    private SolutionResponseViewModel solutionResponseViewModel;

    @Inject
    SolutionDetailFragmentPresenter presenter;


    public static SolutionDetailFragment newInstance(ResultViewModel resultViewModel,
                                                     SolutionViewModel solutionViewModel,
                                                     SolutionResponseViewModel solutionResponseViewModel) {
        SolutionDetailFragment fragment = new SolutionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SOLUTION_DATA, solutionViewModel);
        bundle.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
        bundle.putParcelable(SOLUTION_RESPONSE_MODEL, solutionResponseViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SolutionDetailFragment newEditAppealDetailInstance(EditAppealSolutionModel editAppealSolutionModel,
                                                                     SolutionViewModel solutionViewModel,
                                                                     SolutionResponseViewModel solutionResponseViewModel) {
        SolutionDetailFragment fragment = new SolutionDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(SOLUTION_DATA, solutionViewModel);
        bundle.putParcelable(EDIT_APPEAL_MODEL_DATA, editAppealSolutionModel);
        bundle.putParcelable(SOLUTION_RESPONSE_MODEL, solutionResponseViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solution_detail, container, false);
        btnContinue = (Button) view.findViewById(R.id.btn_continue);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        tvRefundTotal = (TextView) view.findViewById(R.id.tv_refund_total);
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
        solutionResponseViewModel = arguments.getParcelable(SOLUTION_RESPONSE_MODEL);
        if (arguments.getParcelable(RESULT_VIEW_MODEL_DATA) != null) {
            resultViewModel = arguments.getParcelable(RESULT_VIEW_MODEL_DATA);
        } else {
            editAppealSolutionModel = arguments.getParcelable(EDIT_APPEAL_MODEL_DATA);
        }
    }

    private void initView() {
        presenter.initData(
                solutionViewModel,
                solutionResponseViewModel);

        if (editAppealSolutionModel != null) {
            if (SolutionListActivity.isEditFromChatReso(editAppealSolutionModel)) {
                TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatImpressionSolutionEditDetailPage(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.getSolutionName()).getEvent());
            } else {
                TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatImpressionSolutionAppealDetailPage(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.getSolutionName()).getEvent());
            }
        }
    }

    @Override
    public void initDataToList(List<Visitable> itemList) {
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        typeFactory = new SolutionRefundTypeFactoryImpl(this);
        adapter = new SolutionDetailAdapter(typeFactory, itemList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void setViewListener() {
        btnContinue.setOnClickListener(view -> {
            presenter.onContinueButtonClicked(resultViewModel, editAppealSolutionModel);
            if (editAppealSolutionModel == null) {
                eventCreateResoStep2Continue();
            } else {
                if (SolutionListActivity.isEditFromChatReso(editAppealSolutionModel)) {
                    TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatClickSolutionContinueEditDetailPage(
                                    editAppealSolutionModel.resolutionId, editAppealSolutionModel.solutionName).getEvent());
                } else {
                    TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatClickSolutionContinueAppealDetailPage(
                                    editAppealSolutionModel.resolutionId, editAppealSolutionModel.solutionName).getEvent());
                }
            }
        });
    }

    private void eventCreateResoStep2Continue(){
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                "clickResolution",
                "resolution center",
                "click solution",
                "solution - continue");
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
        if (solutionResponseViewModel.getMessage() != null) {
            updateSolutionString(editAppealSolutionModel, tvMessage);
            tvSolution.setVisibility(View.GONE);
        } else {
            updateSolutionString(editAppealSolutionModel, tvSolution);
        }
        btnBack.setOnClickListener(view -> dialog.dismiss());
        ivClose.setOnClickListener(view -> dialog.dismiss());

        btnEditSolution.setOnClickListener(view -> {
            presenter.submitEditAppeal(editAppealSolutionModel);
            dialog.dismiss();
        });

        dialog.show();
    }

    public void updateSolutionString(EditAppealSolutionModel editAppealSolutionModel, TextView textView) {
        if (solutionResponseViewModel.getMessage() == null) {
            textView.setText(editAppealSolutionModel.refundAmount != 0 && editAppealSolutionModel.solutionName != null ?
                    editAppealSolutionModel.solutionName.replace(
                            getActivity().getResources().getString(R.string.string_return_value),
                            CurrencyFormatter.formatDotRupiah(String.valueOf(editAppealSolutionModel.refundAmount))) :
                    editAppealSolutionModel.getName());
        } else {
            textView.setText(MethodChecker.fromHtml(solutionResponseViewModel.getMessage().getMessage().replace(
                    getActivity().getResources().getString(R.string.string_solution_message),
                    editAppealSolutionModel.getRefundAmount() != 0 && editAppealSolutionModel.getSolutionName() != null ?
                            editAppealSolutionModel.getSolutionName().replace(
                                    getActivity().getResources().getString(R.string.string_return_value),
                                    CurrencyFormatter.formatDotRupiah(String.valueOf(editAppealSolutionModel.getRefundAmount()))) :
                            editAppealSolutionModel.getName()))
            );
        }
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
    public ComplaintResult getComplaintResult(SolutionOrderModel orderModel) {
        List<ComplaintResult> complaintResults = resultViewModel != null ?
                resultViewModel.complaints :
                editAppealSolutionModel.complaints;
        ComplaintResult result = new ComplaintResult();
        for (ComplaintResult complaintResult : complaintResults) {
            if (orderModel == null){
                if (complaintResult.problem.id == 0){
                    result = complaintResult;
                    break;
                }
            } else {
                if(complaintResult.order.detail.id == orderModel.getDetail().getId()) {
                    result = complaintResult;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public void initAmountToResult(ComplaintResult complaintResult) {
        List<ComplaintResult> tempResult = new ArrayList<>();
        if (resultViewModel != null) {
            for (ComplaintResult result : resultViewModel.complaints) {
                if (complaintResult.problem.id == result.problem.id) {
                    result.problem.amount = complaintResult.problem.amount;
                }
                tempResult.add(result);
            }
        } else {
            for (ComplaintResult result : editAppealSolutionModel.complaints) {
                if (complaintResult.problem.id == result.problem.id) {
                    result.problem.amount = complaintResult.problem.amount;
                }
                tempResult.add(result);
            }
        }
        if (resultViewModel != null) {
            resultViewModel.complaints = tempResult;
        } else {
            editAppealSolutionModel.complaints = tempResult;
        }
        calculateTotalRefund(new ComplaintResult());
        buttonSelected(btnContinue);
    }

    @Override
    public void initCheckedItem() {
        if (editAppealSolutionModel != null) {
            editAppealSolutionModel.complaints.get(0).isChecked = true;
        }
    }

    @Override
    public void addRemoveOngkirComplaint(SolutionComplaintModel model) {
        List<ComplaintResult> complaintResults = resultViewModel != null ?
                resultViewModel.complaints :
                editAppealSolutionModel.complaints;
        boolean isComplaintOnList = false;
        for (ComplaintResult complaintResult : complaintResults) {
            if (complaintResult.problem.id == 0) {
                isComplaintOnList = true;
            }
        }
        if (!isComplaintOnList) {
            addOngkirComplaint(model);
        } else {
            removeOngkirComplaint();
        }
    }

    private void addOngkirComplaint(SolutionComplaintModel model) {
        ComplaintResult complaintResult = new ComplaintResult();
        complaintResult.problem.name = "Ada selisih ongkos kirim";
        complaintResult.problem.amount = model.getProblem().getMaxAmount().getInteger();
        complaintResult.problem.trouble = model.getProblem().getTrouble();
        complaintResult.problem.type = model.getProblem().getType();
        complaintResult.problem.id = 0;
        if (resultViewModel != null) {
            resultViewModel.complaints.add(complaintResult);
        } else {
            editAppealSolutionModel.complaints.add(complaintResult);
        }
        calculateTotalRefund(complaintResult);
    }

    private void removeOngkirComplaint() {
        List<ComplaintResult> complaintResults = resultViewModel != null ?
                resultViewModel.complaints :
                editAppealSolutionModel.complaints;
        List<ComplaintResult> tempResult = new ArrayList<>();
        for (ComplaintResult complaintResult : complaintResults) {
            if (complaintResult.problem.id != 0) {
                tempResult.add(complaintResult);
            }
        }
        if (resultViewModel != null) {
            resultViewModel.complaints = tempResult;
        } else {
            editAppealSolutionModel.complaints = tempResult;
        }
        calculateTotalRefund(new ComplaintResult());
    }

    @Override
    public void calculateTotalRefund(ComplaintResult complaintResult) {
        int totalValue = 0;
        if (resultViewModel != null) {
            for (ComplaintResult result : resultViewModel.complaints) {
                if (complaintResult.problem.id == result.problem.id) {
                    result.problem.amount = complaintResult.problem.amount;
                }
                totalValue += result.problem.amount;
            }
            resultViewModel.refundAmount = totalValue;
        } else {
            for (ComplaintResult result : editAppealSolutionModel.complaints) {
                if (complaintResult.problem.id == result.problem.id) {
                    result.problem.amount = complaintResult.problem.amount;
                }
                totalValue += result.problem.amount;
            }
            editAppealSolutionModel.refundAmount = totalValue;
        }
        tvRefundTotal.setText(CurrencyFormatter.formatDotRupiah(String.valueOf(totalValue)));
        if (totalValue == 0) buttonDisabled(btnContinue);
        else buttonSelected(btnContinue);
    }

    @Override
    public void submitData(ResultViewModel resultViewModel) {
        if (editAppealSolutionModel != null) {
            if (SolutionListActivity.isEditFromChatReso(editAppealSolutionModel)) {
                TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatClickSolutionEditDetailPage(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.getSolutionName()).getEvent());
            } else {
                TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatClickSolutionAppealDetailPage(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.getSolutionName()).getEvent());
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