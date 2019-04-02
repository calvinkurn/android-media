package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.activity.FreeReturnActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionDetailActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionListActivity;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.SolutionListAdapter;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListAdapterListener;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionListFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ComplaintResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemOrderResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ProblemResult;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.FreeReturnViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionComplaintModel;
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
 * Created by yoasfs on 24/08/17.
 */

public class SolutionListFragment extends BaseDaggerFragment
        implements SolutionListFragmentListener.View, SolutionListAdapterListener {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    public static final String EDIT_APPEAL_MODEL_DATA = "edit_appeal_model_data";
    public static final String SOLUTION_DATA = "solution_data";

    public static final int REQUEST_SOLUTION = 1001;

    ResultViewModel resultViewModel;
    EditAppealSolutionModel editAppealSolutionModel;
    FreeReturnViewModel freeReturnViewModel;
    SolutionResponseViewModel solutionResponseViewModel;


    RecyclerView rvSolution;
    SolutionListAdapter adapter;
    LinearLayout llFreeReturn, llSolution, llCurrentSolution;
    TextView tvFreeReturn, tvCurrentSolution;
    ProgressBar progressBar;

    boolean isEditAppeal;

    @Inject
    SolutionListFragmentPresenter presenter;

    public static SolutionListFragment newInstance(ResultViewModel resultViewModel) {
        SolutionListFragment fragment = new SolutionListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public static SolutionListFragment newEditAppealInstance(EditAppealSolutionModel editAppealSolutionModel) {
        SolutionListFragment fragment = new SolutionListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EDIT_APPEAL_MODEL_DATA, editAppealSolutionModel);
        fragment.setArguments(bundle);
        return fragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_solution_list, container, false);
        rvSolution = (RecyclerView) view.findViewById(R.id.rv_solution);
        llCurrentSolution = (LinearLayout) view.findViewById(R.id.ll_current_solution);
        tvCurrentSolution = (TextView) view.findViewById(R.id.tv_current_solution);
        llFreeReturn = (LinearLayout) view.findViewById(R.id.ll_free_return);
        tvFreeReturn = (TextView) view.findViewById(R.id.tv_free_return);
        llSolution = (LinearLayout) view.findViewById(R.id.solution_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        llFreeReturn.setVisibility(View.GONE);
        llCurrentSolution.setVisibility(View.GONE);
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

    private void setupArguments(Bundle arguments) {
        if (arguments.getParcelable(RESULT_VIEW_MODEL_DATA) instanceof ResultViewModel) {
            resultViewModel = arguments.getParcelable(RESULT_VIEW_MODEL_DATA);
        } else if (arguments.getParcelable(EDIT_APPEAL_MODEL_DATA) instanceof EditAppealSolutionModel) {
            editAppealSolutionModel = arguments.getParcelable(EDIT_APPEAL_MODEL_DATA);
            isEditAppeal = true;
        }
    }

    private void initView() {
        rvSolution.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (!isEditAppeal)
            presenter.initResultViewModel(resultViewModel);
        else
            presenter.initEditAppeal(editAppealSolutionModel);
    }

    private void setViewListener() {
        tvFreeReturn.setOnClickListener(view -> startActivity(FreeReturnActivity
                .newInstance(getActivity(), freeReturnViewModel.getLink())));
    }

    @Override
    public void onItemClicked(SolutionViewModel solutionViewModel) {
        presenter.solutionClicked(solutionViewModel);
        if (isEditAppeal) {
            if (SolutionListActivity.isEditFromChatReso(editAppealSolutionModel)) {
                TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatClickSolutionEditPage(
                        editAppealSolutionModel.resolutionId,
                        solutionViewModel.getSolutionName()).getEvent());
            } else {
                TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatClickSolutionAppealPage(
                        editAppealSolutionModel.resolutionId,
                        solutionViewModel.getSolutionName()).getEvent());
            }
        }
    }

    @Override
    public boolean showCheckOnItem(int solutionId) {
        return solutionId == (resultViewModel != null ? resultViewModel.solution : solutionResponseViewModel.getCurrentSolution().getId());
    }

    @Override
    public void showSuccessGetSolution(SolutionResponseViewModel solutionResponseViewModel) {
        hideLoading();
        if (solutionResponseViewModel.getCurrentSolution() != null) {
            llCurrentSolution.setVisibility(View.VISIBLE);
            if (solutionResponseViewModel.getCurrentSolution().getAmount() != null) {
                tvCurrentSolution.setText(solutionResponseViewModel.getCurrentSolution().getMessage()
                        .replace(
                                getActivity().getResources().getString(R.string.string_return_value),
                                CurrencyFormatter.formatDotRupiah(
                                        String.valueOf(solutionResponseViewModel.getCurrentSolution().getAmount().getInteger()))));
            } else {
                tvCurrentSolution.setText(solutionResponseViewModel.getCurrentSolution().getMessage());
            }
        }
        if (isEditAppeal) {
            editAppealSolutionModel.name = solutionResponseViewModel.getCurrentSolution().getName();
            editAppealSolutionModel.solutionName = solutionResponseViewModel.getCurrentSolution().getMessage();
            editAppealSolutionModel.complaints =
                    convertComplaintResults(solutionResponseViewModel.getComplaints());
        }
        llSolution.setVisibility(View.VISIBLE);
        this.solutionResponseViewModel = solutionResponseViewModel;
        presenter.updateLocalData(solutionResponseViewModel);
        populateDataToView(solutionResponseViewModel);
    }

    private List<ComplaintResult> convertComplaintResults(List<SolutionComplaintModel> dataList) {
        List<ComplaintResult> resultList = new ArrayList<>();
        for (SolutionComplaintModel data : dataList) {
            ComplaintResult complaintResult = new ComplaintResult();
            ProblemResult problemResult = new ProblemResult();
            ProblemOrderResult orderResult = new ProblemOrderResult();
            if (data.getOrder() != null) {
                orderResult.detail.id = data.getOrder().getDetail().getId();
                problemResult.id = data.getOrder().getDetail().getId();
            }
            problemResult.name = data.getProblem().getName();
            problemResult.type = data.getProblem().getType();
            problemResult.trouble = data.getProblem().getTrouble();
            problemResult.quantity = data.getProblem().getQty();
            problemResult.amount = data.getProblem().getAmount().getInteger();
            problemResult.remark = data.getProblem().getRemark();
            if (data.getShipping() != null) {
                complaintResult.isChecked = data.getShipping().isChecked();
            }
            complaintResult.problem = problemResult;
            complaintResult.order = orderResult;
            resultList.add(complaintResult);
        }
        return resultList;
    }

    @Override
    public void moveToSolutionDetail(SolutionViewModel solutionViewModel) {
        Intent intent;
        solutionResponseViewModel.setSelectedSolution(solutionViewModel);
        if (resultViewModel != null) {
            intent = SolutionDetailActivity.getCreateInstance(
                    getActivity(),
                    resultViewModel,
                    solutionViewModel,
                    solutionResponseViewModel);
        } else {
            intent = SolutionDetailActivity.getEditInstance(
                    getActivity(),
                    editAppealSolutionModel,
                    solutionViewModel,
                    solutionResponseViewModel);
        }
        startActivityForResult(intent, REQUEST_SOLUTION);
    }

    @Override
    public void populateDataToView(SolutionResponseViewModel solutionResponseViewModel) {
        adapter = new SolutionListAdapter(getActivity(),
                solutionResponseViewModel.getSolutionViewModelList(),
                this);
        if (isEditAppeal) {
            for (SolutionViewModel model : solutionResponseViewModel.getSolutionViewModelList()) {
                if (SolutionListActivity.isEditFromChatReso(editAppealSolutionModel)) {
                    TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatImpressionSolutionEditPage(
                            editAppealSolutionModel.resolutionId, model.getSolutionName()
                    ).getEvent());
                } else {
                    TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatImpressionSolutionAppealPage(
                            editAppealSolutionModel.resolutionId, model.getSolutionName()
                    ).getEvent());
                }
            }
        }
        rvSolution.setAdapter(adapter);
        if (solutionResponseViewModel.getFreeReturn() != null) {
            freeReturnViewModel = solutionResponseViewModel.getFreeReturn();
            llFreeReturn.setVisibility(View.VISIBLE);
            tvFreeReturn.setText(MethodChecker.fromHtml(solutionResponseViewModel.getFreeReturn().getInfo()));
        }
    }

    @Override
    public void submitData(ResultViewModel resultViewModel) {
        Intent output = new Intent();
        output.putExtra(RESULT_VIEW_MODEL_DATA, resultViewModel);
        getActivity().setResult(Activity.RESULT_OK, output);
        getActivity().finish();
    }

    @Override
    public void showErrorGetSolution(String error) {
        llSolution.setVisibility(View.GONE);
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, () -> {
            if (isEditAppeal) {
                presenter.initEditAppeal(editAppealSolutionModel);
            } else {
                presenter.initResultViewModel(resultViewModel);
            }
        });
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

    @Override
    public void showDialogCompleteEditAppeal(final SolutionViewModel solutionViewModel) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_edit_solution);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);
        TextView tvMessage2 = (TextView) dialog.findViewById(R.id.tv_message_2);
        TextView tvSolution = (TextView) dialog.findViewById(R.id.tv_solution);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btnBack = (Button) dialog.findViewById(R.id.btn_back);
        Button btnEditSolution = (Button) dialog.findViewById(R.id.btn_edit_solution);
        if (solutionResponseViewModel.getMessage() != null) {
            tvMessage2.setVisibility(View.VISIBLE);
            tvSolution.setVisibility(View.GONE);
            tvMessage.setVisibility(View.GONE);
            tvMessage2.setText(updateSolutionString(solutionViewModel));
        } else {
            tvSolution.setText(updateSolutionString(solutionViewModel));
        }
        if (editAppealSolutionModel.isEdit) {
            tvTitle.setText(getActivity().getString(R.string.string_edit_title));
            tvMessage.setText(getActivity().getString(R.string.string_edit_message));
        } else {
            tvTitle.setText(getActivity().getString(R.string.string_appeal_title));
            tvMessage.setText(getActivity().getString(R.string.string_appeal_message));
        }

        btnBack.setOnClickListener(view -> dialog.dismiss());
        ivClose.setOnClickListener(view -> dialog.dismiss());

        btnEditSolution.setOnClickListener(view -> {
            showLoading();
            presenter.submitEditAppeal(solutionViewModel);
            dialog.dismiss();
        });

        dialog.show();
    }

    public Spanned updateSolutionString(SolutionViewModel solutionViewModel) {
        Spanned text;
        if (solutionResponseViewModel.getMessage() == null) {
            text = MethodChecker.fromHtml(solutionViewModel.getAmount() != null && solutionViewModel.getSolutionName() != null ?
                    solutionViewModel.getSolutionName().replace(
                            getActivity().getResources().getString(R.string.string_return_value),
                            CurrencyFormatter.formatDotRupiah(solutionViewModel.getAmount().getIdr())) :
                    solutionViewModel.getName());
        } else {
            text = MethodChecker.fromHtml(solutionResponseViewModel.getMessage().getMessage().replace(
                    getActivity().getResources().getString(R.string.string_solution_message),
                    solutionViewModel.getAmount() != null && solutionViewModel.getSolutionName() != null ?
                            solutionViewModel.getSolutionName().replace(
                                    getActivity().getResources().getString(R.string.string_return_value),
                                    CurrencyFormatter.formatDotRupiah(solutionViewModel.getAmount().getIdr())) :
                            solutionViewModel.getName()));
        }
        return text;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SOLUTION) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    submitData((ResultViewModel) data.getParcelableExtra(RESULT_VIEW_MODEL_DATA));
                } else {
                    getActivity().setResult(Activity.RESULT_OK);
                    getActivity().finish();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
