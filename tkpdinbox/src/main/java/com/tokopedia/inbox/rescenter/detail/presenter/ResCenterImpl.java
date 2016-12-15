package com.tokopedia.inbox.rescenter.detail.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.inbox.rescenter.detail.activity.ResCenterActivity;
import com.tokopedia.inbox.rescenter.detail.fragment.DetailResCenterFragment;
import com.tokopedia.inbox.rescenter.detail.listener.ResCenterView;
import com.tokopedia.inbox.rescenter.detail.model.passdata.ActivityParamenterPassData;
import com.tokopedia.inbox.rescenter.detail.service.DetailResCenterReceiver;
import com.tokopedia.inbox.rescenter.detail.service.DetailResCenterService;

/**
 * Created on 2/9/16.
 */
public class ResCenterImpl implements ResCenterPresenter {

    private final ResCenterView view;

    public ResCenterImpl(ResCenterActivity activity) {
        this.view = activity;
    }

    @Override
    public void initFragment(Context context, Uri uri, Bundle bundle) {
        view.inflateFragment(
                DetailResCenterFragment.newInstance(generateResCenterPass(bundle, uri)),
                DetailResCenterFragment.class.getSimpleName()
        );
    }

    private ActivityParamenterPassData generateResCenterPass(Bundle bundle, Uri uri) {
        ActivityParamenterPassData activityParamenterPassData = null;
        if (bundle != null) {
            activityParamenterPassData = bundle.getParcelable(ResCenterActivity.EXTRA_RES_CENTER_PASS);
            if (activityParamenterPassData == null) {
                activityParamenterPassData = ActivityParamenterPassData.Builder.aResCenterPass()
                        .setResCenterId(bundle.getString("EXTRA_RESOLUTION_ID", ""))
                        .build();
            }
        }

        return activityParamenterPassData;

    }

    @Override
    public void changeSolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver) {
        DetailResCenterService.startActionChangeSolution(context, resolutionID, mReceiver);
    }

    @Override
    public void replyConversation(Context context, String resolutionID, DetailResCenterReceiver mReceiver) {
        DetailResCenterService.startActionReply(context, resolutionID, mReceiver);
    }

    @Override
    public void actionUpdateShippingRefNum(Context context, String resolutionID, DetailResCenterReceiver mReceiver) {
        DetailResCenterService.startActionUpdateShippingRefNum(context, resolutionID, mReceiver);
    }

    @Override
    public void actionInputShippingRefNum(Context context, String resolutionID, DetailResCenterReceiver mReceiver) {
        DetailResCenterService.startActionInputShippingRefNum(context, resolutionID, mReceiver);
    }

    @Override
    public void actionFinishReturSolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver) {
        DetailResCenterService.startActionFinishReturSolution(context, resolutionID, mReceiver);
    }

    @Override
    public void actionAcceptSolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver) {
        DetailResCenterService.startActionAcceptSolution(context, resolutionID, mReceiver);
    }

    @Override
    public void actionAcceptAdminSolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver) {
        DetailResCenterService.startActionAcceptAdminSolution(context, resolutionID, mReceiver);
    }

    @Override
    public void actionCancelResolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver) {
        DetailResCenterService.startActionCancelResolution(context, resolutionID, mReceiver);
    }

    @Override
    public void actionReportResolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver) {
        DetailResCenterService.startActionReportResolution(context, resolutionID, mReceiver);
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData, DetailResCenterFragment fragment) {
        if (fragment != null) {
            fragment.onReceiveServiceResult(resultCode, resultData);
        }
    }

}
