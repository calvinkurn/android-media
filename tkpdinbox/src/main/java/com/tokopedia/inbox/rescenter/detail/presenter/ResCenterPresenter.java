package com.tokopedia.inbox.rescenter.detail.presenter;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.inbox.rescenter.detail.fragment.DetailResCenterFragment;
import com.tokopedia.inbox.rescenter.detail.service.DetailResCenterReceiver;

/**
 * Created by hangnadi on 2/9/16.
 */
public interface ResCenterPresenter {

    void initFragment(Context context, Uri uri, Bundle bundle);

    void changeSolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver);

    void replyConversation(Context context, String resolutionID, DetailResCenterReceiver mReceiver);

    void actionAcceptSolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver);

    void actionAcceptAdminSolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver);

    void actionFinishReturSolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver);

    void actionCancelResolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver);

    void actionReportResolution(Context context, String resolutionID, DetailResCenterReceiver mReceiver);

    void onReceiveResult(int resultCode, Bundle resultData, DetailResCenterFragment fragment);
}
