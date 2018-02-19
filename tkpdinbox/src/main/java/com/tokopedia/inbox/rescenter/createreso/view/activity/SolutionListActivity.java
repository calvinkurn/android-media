package com.tokopedia.inbox.rescenter.createreso.view.activity;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.SolutionListActivityListener;
import com.tokopedia.inbox.rescenter.createreso.view.presenter.SolutionListActivityPresenter;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.solution.EditAppealSolutionModel;

/**
 * Created by yoasfs on 24/08/17.
 */

public class SolutionListActivity extends BasePresenterActivity<SolutionListActivityListener.Presenter>
        implements SolutionListActivityListener.View, HasComponent {

    private static final String ARGS_PARAM_PASS_DATA = "ARGS_PARAM_PASS_DATA";
    public static final String RESULT_VIEW_MODEL_DATA = "result_view_model_data";
    private static final String ARGS_PARAM_FLAG_EDIT = "ARGS_PARAM_FLAG_EDIT";

    private static final String PARAM_IS_CREATE_RESO = "param_is_create_reso";


    private ResultViewModel resultViewModel;
    private EditAppealSolutionModel editAppealSolutionModel;
    private boolean isCreateReso;

    public static Intent newBuyerEditInstance(Context context,
                                              String resolutionID,
                                              Boolean isChatReso) {
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
                                               Boolean isChatReso) {
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
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        if (extras.getParcelable(RESULT_VIEW_MODEL_DATA) != null) {
            resultViewModel = extras.getParcelable(RESULT_VIEW_MODEL_DATA);
        } else {
            editAppealSolutionModel = extras.getParcelable(ARGS_PARAM_PASS_DATA);
        }
        isCreateReso = extras.getBoolean(PARAM_IS_CREATE_RESO, false);
    }

    @Override
    protected void initialPresenter() {
        presenter = new SolutionListActivityPresenter(this, this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_solution_list;
    }

    @Override
    protected void initView() {
        if (resultViewModel != null) {
            presenter.initFragment(resultViewModel);
        } else {
            presenter.initEditAppealFragment(editAppealSolutionModel);
            if (editAppealSolutionModel.isEdit) {
                toolbar.setTitle(R.string.string_edit_title);
            } else {
                toolbar.setTitle(R.string.string_appeal_title);
            }
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

    public static EditAppealSolutionModel generateEditPassData(boolean isEdit,
                                                               String resolutionId,
                                                               boolean isSeller,
                                                               boolean isChatReso) {
        return new EditAppealSolutionModel(isEdit, resolutionId, isSeller, isChatReso);
    }

    @Override
    public void onBackPressed() {
        if (!isCreateReso) {
            if (editAppealSolutionModel.isEdit) {
                if (editAppealSolutionModel.isChatReso) {
                    UnifyTracking.eventResoChatChatClickCloseEditPage(
                            editAppealSolutionModel.resolutionId);
                } else {

                }
            } else {
                if (editAppealSolutionModel.isChatReso) {
                    UnifyTracking.eventResoChatChatClickCloseAppealPage(
                            editAppealSolutionModel.resolutionId);
                } else {

                }
            }
        }
        super.onBackPressed();
    }
}
