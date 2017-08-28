package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionDetailActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;

/**
 * Created by yoasfs on 28/08/17.
 */

public class SolutionDetailActivity extends BasePresenterActivity<SolutionDetailActivityListener.Presenter> implements SolutionDetailActivityListener.View {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    public static final String SOLUTION_DATA = "solution_data";

    SolutionViewModel solutionViewModel;
    ResultViewModel resultViewModel;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        solutionViewModel = extras.getParcelable(SOLUTION_DATA);
        resultViewModel = extras.getParcelable(RESULT_VIEW_MODEL_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new SolutionDetailActivityPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_solution_detail;
    }

    @Override
    protected void initView() {
        presenter.initFragment(resultViewModel, solutionViewModel);
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void inflateFragment(Fragment fragment, String TAG) {
        if (getFragmentManager().findFragmentByTag(TAG) == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, TAG)
                    .commit();
        }
    }
}
