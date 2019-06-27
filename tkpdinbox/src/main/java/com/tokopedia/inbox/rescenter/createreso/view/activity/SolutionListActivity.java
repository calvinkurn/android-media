package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.fragment.SolutionListFragment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;
import com.tokopedia.inbox.util.analytics.InboxAnalytics;
import com.tokopedia.track.TrackApp;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionListActivity extends BaseSimpleActivity {

    private static final String ARGS_PARAM_PASS_DATA = "ARGS_PARAM_PASS_DATA";
    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    private static final String ARGS_PARAM_FLAG_EDIT = "ARGS_PARAM_FLAG_EDIT";

    private static final String PARAM_IS_CREATE_RESO = "param_is_create_reso";


    private ResultViewModel resultViewModel;
    private EditAppealSolutionModel editAppealSolutionModel;
    private boolean isCreateReso;

    public static Intent newBuyerEditInstance(Context context,
                                              String resolutionID,
                                              boolean isChatReso) {
        Intent intent = new Intent(context, SolutionListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PARAM_PASS_DATA,
                generateEditPassData(true, resolutionID, false, isChatReso));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newCreateInstance(Context context,
                                           ResultViewModel resultViewModel) {
        Intent intent = new Intent(context, SolutionListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(RESULT_VIEW_MODEL_DATA, resultViewModel);
        bundle.putBoolean(PARAM_IS_CREATE_RESO, true);
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newSellerEditInstance(Context context,
                                               String resolutionID,
                                               boolean isChatReso) {
        Intent intent = new Intent(context, SolutionListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PARAM_PASS_DATA,
                generateEditPassData(true, resolutionID, true, isChatReso));
        intent.putExtras(bundle);
        return intent;
    }

    public static Intent newAppealInstance(Context context,
                                           String resolutionID,
                                           Boolean isChatReso) {
        Intent intent = new Intent(context, SolutionListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(ARGS_PARAM_PASS_DATA,
                generateEditPassData(false, resolutionID, false, isChatReso));
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        Bundle extras = getIntent().getExtras();
        isCreateReso = extras.getBoolean(PARAM_IS_CREATE_RESO, false);
        if (extras.getParcelable(RESULT_VIEW_MODEL_DATA) != null) {
            resultViewModel = extras.getParcelable(RESULT_VIEW_MODEL_DATA);
            return SolutionListFragment.newInstance(resultViewModel);
        } else {
            editAppealSolutionModel = extras.getParcelable(ARGS_PARAM_PASS_DATA);
            if (editAppealSolutionModel.isEdit) {
                toolbar.setTitle(R.string.string_edit_title);
            } else {
                toolbar.setTitle(R.string.string_appeal_title);
            }
            return SolutionListFragment.newEditAppealInstance(editAppealSolutionModel);
        }
    }

    public static EditAppealSolutionModel generateEditPassData(boolean isEdit,
                                                               String resolutionId,
                                                               boolean isSeller,
                                                               boolean isChatReso) {
        return new EditAppealSolutionModel(isEdit, resolutionId, isSeller, isChatReso);
    }

    @Override
    public void onBackPressed() {
        if (!isCreateReso) {
            if (isEditFromChatReso(editAppealSolutionModel)) {
                TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatClickCloseEditPage(
                        editAppealSolutionModel.resolutionId).getEvent());
            } else {
                TrackApp.getInstance().getGTM().sendGeneralEvent(InboxAnalytics.eventResoChatClickCloseAppealPage(
                        editAppealSolutionModel.resolutionId).getEvent());
            }
        }
        super.onBackPressed();
    }

    public static boolean isEditFromChatReso(EditAppealSolutionModel model) {
        return model.isEdit() && model.isChatReso();
    }
}
