package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.activity.FreeReturnActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionDetailActivity;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionListActivity;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.SolutionListAdapter;
import com.tokopedia.inbox.rescenter.createreso.view.di.DaggerCreateResoComponent;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListAdapterListener;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionListFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.FreeReturnViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;
import com.tokopedia.inbox.rescenter.utils.CurrencyFormatter;
import com.tokopedia.inbox.util.analytics.InboxAnalytics;

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


    RecyclerView rvSolution;
    SolutionListAdapter adapter;
    LinearLayout llFreeReturn, llSolution;
    TextView tvFreeReturn;
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
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerCreateResoComponent daggerCreateResoComponent =
                (DaggerCreateResoComponent) DaggerCreateResoComponent.builder()
                        .appComponent(appComponent)
                        .build();

        daggerCreateResoComponent.inject(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter.attachView(this);
        return super.onCreateView(inflater, container, savedInstanceState);
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
        if (arguments.getParcelable(RESULT_VIEW_MODEL_DATA) instanceof ResultViewModel) {
            resultViewModel = arguments.getParcelable(RESULT_VIEW_MODEL_DATA);
        } else if (arguments.getParcelable(EDIT_APPEAL_MODEL_DATA) instanceof EditAppealSolutionModel) {
            editAppealSolutionModel = arguments.getParcelable(EDIT_APPEAL_MODEL_DATA);
            isEditAppeal = true;
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_solution_list;
    }

    @Override
    protected void initView(View view) {
        rvSolution = (RecyclerView) view.findViewById(R.id.rv_solution);
        llFreeReturn = (LinearLayout) view.findViewById(R.id.ll_free_return);
        tvFreeReturn = (TextView) view.findViewById(R.id.tv_free_return);
        llSolution = (LinearLayout) view.findViewById(R.id.solution_layout);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        llFreeReturn.setVisibility(View.GONE);

        rvSolution.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (!isEditAppeal)
            presenter.initResultViewModel(resultViewModel);
        else
            presenter.initEditAppeal(editAppealSolutionModel);
    }

    @Override
    protected void setViewListener() {
        tvFreeReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(FreeReturnActivity
                        .newInstance(getActivity(), freeReturnViewModel.getLink()));
            }
        });
    }

    @Override
    public void onItemClicked(SolutionViewModel solutionViewModel) {
        presenter.solutionClicked(solutionViewModel);
        if (isEditAppeal) {
            if (SolutionListActivity.isEditFromChatReso(editAppealSolutionModel)) {
                UnifyTracking.eventTracking(InboxAnalytics.eventResoChatClickSolutionEditPage(
                        editAppealSolutionModel.resolutionId,
                        solutionViewModel.getSolutionName()));
            } else {
                UnifyTracking.eventTracking(InboxAnalytics.eventResoChatClickSolutionAppealPage(
                        editAppealSolutionModel.resolutionId,
                        solutionViewModel.getSolutionName()));
            }
        }
    }

    @Override
    public void showSuccessGetSolution(SolutionResponseViewModel solutionResponseViewModel) {
        hideLoading();
        llSolution.setVisibility(View.VISIBLE);
        presenter.updateLocalData(solutionResponseViewModel);
        populateDataToView(solutionResponseViewModel);
    }

    @Override
    public void moveToSolutionDetail(SolutionViewModel solutionViewModel) {
        Intent intent = new Intent(getActivity(), SolutionDetailActivity.class);
        intent.putExtra(SOLUTION_DATA, solutionViewModel);
        if (resultViewModel != null) {
            intent.putExtra(RESULT_VIEW_MODEL_DATA, resultViewModel);
        } else {
            intent.putExtra(EDIT_APPEAL_MODEL_DATA, editAppealSolutionModel);
        }
        startActivityForResult(intent, REQUEST_SOLUTION);
    }

    @Override
    public void populateDataToView(SolutionResponseViewModel solutionResponseViewModel) {
        adapter = new SolutionListAdapter(getActivity(),
                solutionResponseViewModel.getSolutionViewModelList(),
                this);
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
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                if (isEditAppeal) {
                    presenter.initEditAppeal(editAppealSolutionModel);
                } else {
                    presenter.initResultViewModel(resultViewModel);
                }
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
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_edit_solution);
        TextView tvTitle = (TextView) dialog.findViewById(R.id.tv_title);
        TextView tvMessage = (TextView) dialog.findViewById(R.id.tv_message);
        TextView tvSolution = (TextView) dialog.findViewById(R.id.tv_solution);
        ImageView ivClose = (ImageView) dialog.findViewById(R.id.iv_close);
        Button btnBack = (Button) dialog.findViewById(R.id.btn_back);
        Button btnEditSolution = (Button) dialog.findViewById(R.id.btn_edit_solution);

        updateSolutionString(solutionViewModel, tvSolution);

        if (editAppealSolutionModel.isEdit) {
            tvTitle.setText(context.getString(R.string.string_edit_title));
            tvMessage.setText(context.getString(R.string.string_edit_message));
        } else {
            tvTitle.setText(context.getString(R.string.string_appeal_title));
            tvMessage.setText(context.getString(R.string.string_appeal_message));
        }

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
                showLoading();
                presenter.submitEditAppeal(solutionViewModel);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void updateSolutionString(SolutionViewModel solutionViewModel, TextView textView) {
        textView.setText(solutionViewModel.getAmount() != null && solutionViewModel.getSolutionName() != null ?
                solutionViewModel.getSolutionName().replace(
                        context.getResources().getString(R.string.string_return_value),
                        CurrencyFormatter.formatDotRupiah(solutionViewModel.getAmount().getIdr())) :
                solutionViewModel.getName());
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
