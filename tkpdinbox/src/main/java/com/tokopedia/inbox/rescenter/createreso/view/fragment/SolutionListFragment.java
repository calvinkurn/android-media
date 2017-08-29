package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.activity.SolutionDetailActivity;
import com.tokopedia.inbox.rescenter.createreso.view.adapter.SolutionListAdapter;
import com.tokopedia.inbox.rescenter.createreso.view.di.DaggerCreateResoComponent;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListAdapterListener;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionListFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

import javax.inject.Inject;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionListFragment extends BaseDaggerFragment implements SolutionListFragmentListener.View, SolutionListAdapterListener {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    public static final String SOLUTION_DATA = "solution_data";

    public static final int REQUEST_SOLUTION = 1001;

    ResultViewModel resultViewModel;

    RecyclerView rvSolution;
    SolutionListAdapter adapter;
    LinearLayout llFreeReturn;
    TextView tvFreeReturn;

    @Inject
    SolutionListFragmentPresenter presenter;

    public static SolutionListFragment newInstance(ResultViewModel resultViewModel) {
        SolutionListFragment fragment = new SolutionListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
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
        resultViewModel = arguments.getParcelable(RESULT_VIEW_MODEL_DATA);
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

        llFreeReturn.setVisibility(View.GONE);

        rvSolution.setLayoutManager(new LinearLayoutManager(getActivity()));

        presenter.initResultViewModel(resultViewModel);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void onItemClicked(SolutionViewModel solutionViewModel) {
        presenter.solutionClicked(solutionViewModel);
    }

    @Override
    public void moveToSolutionDetail(SolutionViewModel solutionViewModel) {
        Intent intent = new Intent(getActivity(), SolutionDetailActivity.class);
        intent.putExtra(SOLUTION_DATA, solutionViewModel);
        intent.putExtra(RESULT_VIEW_MODEL_DATA, resultViewModel);
        startActivityForResult(intent, REQUEST_SOLUTION);
    }

    @Override
    public void populateDataToView(SolutionResponseViewModel solutionResponseViewModel) {
        adapter = new SolutionListAdapter(getActivity(), solutionResponseViewModel.getSolutionViewModelList(), this);
        rvSolution.setAdapter(adapter);
        if (solutionResponseViewModel.getFreeReturn() != null) {
            llFreeReturn.setVisibility(View.VISIBLE);
            tvFreeReturn.setText(Html.fromHtml(solutionResponseViewModel.getFreeReturn().getInfo()));
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
    public void showSuccessToast() {

    }

    @Override
    public void showErrorToast(String error) {
        Toast.makeText(getActivity(), "error : " + error, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SOLUTION) {
            if (resultCode == Activity.RESULT_OK) {
                submitData((ResultViewModel) data.getParcelableExtra(RESULT_VIEW_MODEL_DATA));
            }
        }
    }
}
