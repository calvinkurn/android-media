package com.tokopedia.inbox.rescenter.detail.presenter;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.inbox.rescenter.detail.listener.ResCenterView;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.DetailResCenterData;
import com.tokopedia.inbox.rescenter.detail.model.passdata.ActivityParamenterPassData;

public interface DetailResCenterPresenter {

    void setInteractionListener(ResCenterView listener);

    void onFirstTimeLaunched(@NonNull Fragment fragment, @NonNull ActivityParamenterPassData activityParamenterPassData);

    void processReply();

    void onButtonSendClick(Context context, String param);

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void requestResCenterDetail(@NonNull Context context, @NonNull ActivityParamenterPassData activityParamenterPassData);

    void requestTrackDelivery(@NonNull Context context,
                              @NonNull String url);

    void onEditShippingClickListener(@NonNull Context context,
                                     @NonNull String url);

    void onButtonAttachmentClick(Context context);

    void onNewShippingClickListener(Context context);

    void actionAcceptSolution();

    void actionReportResolution();

    void actionAcceptAdminSolution();

    void actionFinishReturSolution();

    void actionCancelResolution();

    void onReceiveServiceResult(int resultCode, Bundle resultData);

    void refreshPage(Context context, ActivityParamenterPassData activityParamenterPassData);

    void saveState(Bundle state, ActivityParamenterPassData activityParamenterPassData, DetailResCenterData detailData);

    void restoreState(Bundle savedState);

    void onDestroyView();

    void actionInputAddress(Context context, String addressID);

    void actionInputAddressMigrateVersion(Context context, String addressID);

    void actionInputAddressAcceptAdminSolution(Context context, String addressID);

    void actionEditAddress(Context context, String addressId, String ahrefEditAddressURL);

    void actionImagePicker();

    void actionCamera();
}
