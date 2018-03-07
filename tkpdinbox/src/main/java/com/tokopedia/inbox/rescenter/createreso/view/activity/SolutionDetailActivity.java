package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionDetailActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionDetailActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;
import com.tokopedia.inbox.util.analytics.InboxAnalytics;

/**
 * Created by yoasfs on 28/08/17.
 */

public class SolutionDetailActivity extends
        BasePresenterActivity<SolutionDetailActivityListener.Presenter>
        implements SolutionDetailActivityListener.View, HasComponent {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    public static final String SOLUTION_DATA = "solution_data";
    public static final String EDIT_APPEAL_MODEL_DATA = "edit_appeal_model_data";

    SolutionViewModel solutionViewModel;
    ResultViewModel resultViewModel;
    EditAppealSolutionModel editAppealSolutionModel;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        solutionViewModel = extras.getParcelable(SOLUTION_DATA);
        if (extras.getParcelable(RESULT_VIEW_MODEL_DATA) != null) {
            resultViewModel = extras.getParcelable(RESULT_VIEW_MODEL_DATA);
        } else {
            editAppealSolutionModel = extras.getParcelable(EDIT_APPEAL_MODEL_DATA);
        }
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
        if (resultViewModel != null) {
            presenter.initFragment(resultViewModel,
                    solutionViewModel);
        } else {
            presenter.initEditAppealFragment(editAppealSolutionModel,
                    solutionViewModel);
            toolbar.setTitle(R.string.string_input_amount);
        }
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

    @Override
    public void onBackPressed() {
        if (editAppealSolutionModel != null) {
            if (editAppealSolutionModel.isEdit) {
                if (editAppealSolutionModel.isChatReso) {
                    UnifyTracking.eventTracking(InboxAnalytics.eventResoChatCloseSolutionEditDetailPage(
                            editAppealSolutionModel.resolutionId,
                            editAppealSolutionModel.getSolutionName()));
                } else {

                }
            } else {
                if (editAppealSolutionModel.isChatReso) {
                    UnifyTracking.eventTracking(InboxAnalytics.eventResoChatCloseSolutionAppealDetailPage(
                            editAppealSolutionModel.resolutionId,
                            editAppealSolutionModel.getSolutionName()));
                } else {

                }
            }
        }
        super.onBackPressed();
    }
}
