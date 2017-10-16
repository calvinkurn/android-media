package com.tokopedia.inbox.rescenter.detailv2.view.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.base.BaseDaggerFragment;
import com.tokopedia.inbox.rescenter.detailv2.di.component.DaggerResolutionDetailComponent;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.NextActionActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.customadapter.NextActionAdapter;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.NextActionFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.presenter.NextActionFragmentPresenter;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;

import javax.inject.Inject;

/**
 * Created by yoasfs on 16/10/17.
 */

public class NextActionFragment
        extends BaseDaggerFragment
        implements NextActionFragmentListener.View {


    private NextActionDomain nextActionDomain;
    private String resolutionId;

    private TextView tvProblem, tvSolution;
    private RecyclerView rvNextAction;
    private NextActionAdapter adapter;
    private ProgressBar progressBar;

    @Inject
    NextActionFragmentPresenter presenter;

    public static NextActionFragment newInstance(String resolutionId,
                                                    NextActionDomain nextActionDomain) {
        NextActionFragment fragment = new NextActionFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(NextActionActivity.PARAM_NEXT_ACTION, nextActionDomain);
        bundle.putString(NextActionActivity.PARAM_RESOLUTION_ID, resolutionId);
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
        DaggerResolutionDetailComponent daggerCreateResoComponent =
                (DaggerResolutionDetailComponent) DaggerResolutionDetailComponent.builder()
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
        state.putParcelable(NextActionActivity.PARAM_NEXT_ACTION, nextActionDomain);
        state.putString(NextActionActivity.PARAM_RESOLUTION_ID, resolutionId);
    }

    @Override
    public void onRestoreState(Bundle savedState) {
        resolutionId = savedState.getString(NextActionActivity.PARAM_RESOLUTION_ID);
        nextActionDomain = savedState.getParcelable(NextActionActivity.PARAM_NEXT_ACTION);
    }

    @Override
    protected void setupArguments(Bundle arguments) {
        resolutionId = arguments.getString(NextActionActivity.PARAM_RESOLUTION_ID);
        nextActionDomain = arguments.getParcelable(NextActionActivity.PARAM_NEXT_ACTION);

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_next_action;
    }

    @Override
    protected void initView(View view) {
        tvProblem = (TextView) view.findViewById(R.id.tv_problem);
        tvSolution = (TextView) view.findViewById(R.id.tv_solution);
        rvNextAction = (RecyclerView) view.findViewById(R.id.rv_next_step);
        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
        adapter = new NextActionAdapter(getActivity());

        rvNextAction.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvNextAction.setAdapter(adapter);

        presenter.initPresenter(nextActionDomain);

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void showProgressBar() {
        if (progressBar.getVisibility() == View.GONE) {
            progressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void dismissProgressBar() {
        if (progressBar.getVisibility() == View.VISIBLE) {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void populateMainView(NextActionDomain nextActionDomain) {
        if (nextActionDomain.getDetail().getLast() != null) {
            tvProblem.setText(nextActionDomain.getDetail().getLast().getProblem());
            tvSolution.setText(nextActionDomain.getDetail().getLast().getSolution().getName());
        }
        adapter.populateAdapter(nextActionDomain.getDetail().getStep());
    }
}
