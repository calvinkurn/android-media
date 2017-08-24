package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionListActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionListActivity extends BasePresenterActivity<SolutionListActivityListener.Presenter> implements SolutionListActivityListener.View, HasComponent {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";

    ResultViewModel resultViewModel;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        resultViewModel = extras.getParcelable(RESULT_VIEW_MODEL_DATA);
    }

    @Override
    protected void initialPresenter() {
        presenter = new SolutionListActivityPresenter(this ,this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_solution_list;
    }

    @Override
    protected void initView() {

        presenter.initFragment(resultViewModel);
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

    @Override
    public Object getComponent() {
        return getApplicationComponent();
    }
}
