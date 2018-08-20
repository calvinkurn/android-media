package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionDetailFragment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.SolutionViewModel;
import com.tokopedia.inbox.util.analytics.InboxAnalytics;

/**
 * Created by yoasfs on 28/08/17.
 */

public class SolutionDetailActivity extends BaseSimpleActivity {

    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    public static final String SOLUTION_DATA = "solution_data";
    public static final String EDIT_APPEAL_MODEL_DATA = "edit_appeal_model_data";

    SolutionViewModel solutionViewModel;
    ResultViewModel resultViewModel;
    EditAppealSolutionModel editAppealSolutionModel;

    @Override
    protected Fragment getNewFragment() {
        Bundle extras = getIntent().getExtras();
        solutionViewModel = extras.getParcelable(SOLUTION_DATA);
        if (extras.getParcelable(RESULT_VIEW_MODEL_DATA) != null) {
            resultViewModel = extras.getParcelable(RESULT_VIEW_MODEL_DATA);
            return SolutionDetailFragment.newInstance(resultViewModel, solutionViewModel);
        } else {
            editAppealSolutionModel = extras.getParcelable(EDIT_APPEAL_MODEL_DATA);
            return SolutionDetailFragment.newEditAppealDetailInstance(
                    editAppealSolutionModel,
                    solutionViewModel);
        }
    }

    @Override
    public void onBackPressed() {
        if (editAppealSolutionModel != null) {
            if (SolutionListActivity.isEditFromChatReso(editAppealSolutionModel)) {
                UnifyTracking.eventTracking(InboxAnalytics.eventResoChatCloseSolutionEditDetailPage(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.getSolutionName()));
            } else {
                UnifyTracking.eventTracking(InboxAnalytics.eventResoChatCloseSolutionAppealDetailPage(
                        editAppealSolutionModel.resolutionId,
                        editAppealSolutionModel.getSolutionName()));
            }
        }
        super.onBackPressed();
    }

}
