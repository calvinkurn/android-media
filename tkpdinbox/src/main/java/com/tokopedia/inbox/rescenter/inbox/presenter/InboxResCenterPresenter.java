package com.tokopedia.inbox.rescenter.inbox.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.inbox.rescenter.inbox.model.Paging;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterInboxData;

import java.util.Map;

/**
 * Created on 3/23/16.
 */
public interface InboxResCenterPresenter {

    void setActionOnRefreshing(@NonNull Activity activity);

    void setActionOnFilterClick(@NonNull Activity activity);

    void setActionOnLazyLoad(@NonNull Activity activity);

    void setActionOnFABClicked(View view);

    void setActionOnFirstTimeLaunch(@NonNull Activity activity);

    void setActionOnRetryClick(boolean isRefreshData, Activity activity);

    void setOnFragmentResuming(@NonNull Activity activity);

    void setOnFragmentSavingState(Bundle state);

    void setOnFragmentRestoringState(Bundle savedState);

    void setOnFragmentDestroyView(@NonNull  Activity activity);

    Map<String, String> generateParams(Context context);

    void onErrorGetCache();

    void onSuccessGetCache(@NonNull ResCenterInboxData inboxData);

    void setOnSuccessInitInboxData(ResCenterInboxData data);

    void setOnSuccessLoadMoreData(ResCenterInboxData data);

    void setOnResponseTimeOut(NetworkErrorHelper.RetryClickedListener retryClickedListener);

    @SuppressWarnings("unused")
    void showViewOnTimeOut(boolean showAtHeader);

    void setOnResponseNull();

    void setBeforeInitInboxData();

    void setBeforeLoadMoreData();

    void setOnRequestError(String message);

    void setAllowConnection(boolean allowConnection);

    boolean isAllowConnection();

    void setNextPageStatus(Paging paging);

    void resetPage();

    void finishRequest();

    void setOnRequestSuccess();

    void setActionOnItemListClickListener(Context context, String resolutionID, String shopName, String username);

    InboxResCenterActivity.Model getResCenterTabModel();
}
