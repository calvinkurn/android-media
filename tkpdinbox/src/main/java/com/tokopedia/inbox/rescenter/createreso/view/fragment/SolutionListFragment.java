package com.tokopedia.inbox.rescenter.createreso.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.createreso.view.di.DaggerCreateResoComponent;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionListFragmentPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionResponseViewModel;

import javax.inject.Inject;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionListFragment extends BaseDaggerFragment implements SolutionListFragmentListener.View {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";

    ResultViewModel resultViewModel;

    RecyclerView rvSolution;

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

        presenter.initResultViewModel(resultViewModel);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void populateDataToView(SolutionResponseViewModel solutionResponseViewModel) {

    }

    @Override
    public void showSuccessToast() {
    }

    @Override
    public void showErrorToast(String error) {
        Toast.makeText(getActivity(), "error : " + error, Toast.LENGTH_SHORT).show();
    }

}
