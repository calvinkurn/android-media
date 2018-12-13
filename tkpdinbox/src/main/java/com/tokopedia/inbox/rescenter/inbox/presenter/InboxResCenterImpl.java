package com.tokopedia.inbox.rescenter.inbox.presenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResCenterActivity;
import com.tokopedia.inbox.rescenter.detailv2.view.activity.DetailResChatActivity;
import com.tokopedia.inbox.rescenter.inbox.activity.InboxResCenterActivity;
import com.tokopedia.inbox.rescenter.inbox.facade.Facade;
import com.tokopedia.inbox.rescenter.inbox.facade.FacadeImpl;
import com.tokopedia.inbox.rescenter.inbox.facade.NetworkParam;
import com.tokopedia.inbox.rescenter.inbox.listener.InboxResCenterView;
import com.tokopedia.inbox.rescenter.inbox.model.Paging;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterCounterPending;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterHeader;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterInboxData;
import com.tokopedia.inbox.rescenter.inbox.model.ResCenterInboxDataPass;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;

import java.util.Map;

/**
 * Created on 3/23/16.
 */
public class InboxResCenterImpl implements InboxResCenterPresenter {

    private static final String TAG = InboxResCenterImpl.class.getSimpleName();
    private static final String STATE_CONNECTION = "STATE_CONNECTION";
    private static final String CURRENT_MODEL_TAB = "CURRENT_MODEL_TAB";

    private final Facade facade;
    private final InboxResCenterView listener;
    private final PagingHandler pagingHandler;

    private boolean allowConnection;
    private InboxResCenterActivity.Model resCenterTabModel;

    public InboxResCenterImpl(InboxResCenterView view, InboxResCenterActivity.Model resCenterTabModel) {
        this.resCenterTabModel = resCenterTabModel;
        this.listener = view;
        this.pagingHandler = new PagingHandler();
        this.facade = new FacadeImpl(this);
        setAllowConnection(true);
    }

    @Override
    public void setAllowConnection(boolean allowConnection) {
        this.allowConnection = allowConnection;
    }

    @Override
    public boolean isAllowConnection() {
        return allowConnection;
    }

    @Override
    public void setActionOnItemListClickListener(Context context, String resolutionID, String shopName, String username) {
        if (username.equals("")) {
            //current user as buyer, need seller shopname
            context.startActivity(DetailResChatActivity.newBuyerInstance(context, resolutionID, shopName));
        } else {
            //current user as seller, need buyer username
            context.startActivity(DetailResChatActivity.newSellerInstance(context, resolutionID, username));
        }
        UnifyTracking.eventResolutionDetail(listener.getRootView().getContext(), getResCenterTabModel().titleFragment);
    }

    @Override
    public void setActionOnRefreshing(@NonNull Activity activity) {
        if (isAllowConnection()) {
            resetPage();
            facade.initInboxData(activity, generateParams(activity));
        }
    }

    @Override
    public void setActionOnFilterClick(@NonNull Activity activity) {
        setActionOnRefreshing(activity);
    }

    @Override
    public void setActionOnLazyLoad(@NonNull Activity activity) {
        if (isAllowLazyLoad()) {
            Log.d(TAG + "hang", "paging hasnext : true");
            pagingHandler.nextPage();
            facade.loadMoreInboxData(activity, generateParams(activity));
        } else {
            Log.d(TAG + "hang", "paging hasnext : false");
        }
    }

    private boolean isAllowLazyLoad() {
        return pagingHandler.CheckNextPage() && isAllowConnection();
    }

    @Override
    public void setActionOnFABClicked(View view) {
        listener.setBottomSheetDialog();
    }

    @Override
    public void setActionOnRetryClick(boolean isRefreshData, Activity activity) {
        setAllowConnection(true);
        if (isRefreshData) {
            facade.initInboxData(activity, generateParams(activity));
        } else {
            facade.loadMoreInboxData(activity, generateParams(activity));
        }
    }

    @Override
    public void setActionOnFirstTimeLaunch(@NonNull Activity activity) {
        if (isAllowConnection()) {
            facade.initInboxData(activity, generateParams(activity));
        }
    }

    @Override
    public Map<String, String> generateParams(Context context) {
        ResCenterInboxDataPass pass = new ResCenterInboxDataPass();
        pass.setRequestAs(getResCenterTabModel().typeFragment);
        pass.setRequestAsString(getResCenterTabModel().titleFragment);

        LocalCacheManager.Filter cacheManager = LocalCacheManager.Filter
                .Builder(String.valueOf(getResCenterTabModel().typeFragment), getResCenterTabModel().titleFragment)
                .getCache();
        pass.setFilterStatus(cacheManager.getFilterStatus());
        pass.setFilterStatusString(cacheManager.getFilterStatusText());
        pass.setReadUnreadStatus(cacheManager.getFilterRead());
        pass.setReadUnreadStatusString(cacheManager.getFilterReadText());
        pass.setSortType(cacheManager.getFilterTime());
        pass.setSortTypeString(cacheManager.getFilterTimeText());

        pass.setRequestPage(pagingHandler.getPage());
        return AuthUtil.generateParams(context, NetworkParam.paramGetInbox(pass));
    }

    @Override
    public void setBeforeInitInboxData() {
        setNoResultView(false);
        setLoadingView(true);
        listener.setFilterAble(false);
    }

    @Override
    public void setBeforeLoadMoreData() {
        setBeforeInitInboxData();
    }

    private void setLoadingView(boolean isLoading) {
        if (isLoading) {
            listener.addLoading();
        } else {
            listener.removeLoading();
        }
    }

    @Override
    public void setOnFragmentResuming(@NonNull Activity activity) {

    }

    @Override
    public void setOnFragmentDestroyView(@NonNull Activity activity) {
        facade.forceFinish();
    }

    @Override
    public void setOnFragmentSavingState(Bundle state) {
        listener.saveCurrentInboxList(state);
        state.putParcelable(CURRENT_MODEL_TAB, getResCenterTabModel());
        state.putBoolean(STATE_CONNECTION, allowConnection);
    }

    @Override
    public void setOnFragmentRestoringState(Bundle savedState) {
        listener.restorePreviousInboxList(savedState);
        resCenterTabModel = savedState.getParcelable(CURRENT_MODEL_TAB);
        allowConnection = savedState.getBoolean(STATE_CONNECTION);
    }

    @Override
    public void setOnSuccessInitInboxData(ResCenterInboxData data) {
        setNextPageStatus(data.getPaging());
        listener.replaceCache(data.getList());
        setHeaderView(data);
    }

    @Override
    public void setOnSuccessLoadMoreData(ResCenterInboxData data) {
        setNextPageStatus(data.getPaging());
        listener.addInboxItem(data.getList());
    }

    @Override
    public void onErrorGetCache() {
        // ignore if failed get cache
    }

    @Override
    public void onSuccessGetCache(@NonNull ResCenterInboxData inboxData) {
        setLoadingView(false);
        setNextPageStatus(inboxData.getPaging());
        listener.setRefreshing(true);
        listener.showCache(inboxData.getList());
        setHeaderView(inboxData);
    }

    private void setHeaderView(ResCenterInboxData inboxData) {
        ResCenterHeader header = createHeaderItem(inboxData.getResCenterCounterPending(), inboxData.getResCenterPendingAmount());
        if (header != null) {
            listener.addHeaderItem(header);
        } else {
            listener.removeHeaderItem();
        }
    }

    private ResCenterHeader createHeaderItem(ResCenterCounterPending counterPending, String pendingAmount) {
        if (counterPending == null && pendingAmount == null) {
            return null;
        } else {
            ResCenterHeader header = new ResCenterHeader();
            header.setCounterPending(counterPending);
            header.setPendingAmount(pendingAmount);
            return header;
        }
    }

    @Override
    public void setNextPageStatus(Paging paging) {
        pagingHandler.setHasNext(PagingHandler.CheckHasNext(paging.getUriNext()));
    }

    @Override
    public void resetPage() {
        pagingHandler.resetPage();
    }

    @Override
    public void finishRequest() {
        setLoadingView(false);
        listener.setRefreshing(false);
    }

    @Override
    public void setOnRequestSuccess() {
        listener.setFilterAble(true);
    }

    @Override
    public void setOnResponseTimeOut(NetworkErrorHelper.RetryClickedListener retryClickedListener) {
        setLoadingView(false);
        listener.setRefreshing(false);
        listener.setFilterAble(false);
        if (listener.getList().isEmpty()) {
            listener.showTimeOutEmtpyState(retryClickedListener);
        } else {
            listener.setFilterAble(true);
            setAllowConnection(true);
            listener.setRetryView();
        }
    }

    @Override
    public void showViewOnTimeOut(boolean showAtHeader) {
        if (showAtHeader) {
            listener.addRetryItemOnHeader();
        } else {
            listener.addRetryItemOnBottom();
        }
    }

    @Override
    public void setOnResponseNull() {
        listener.clearCurrentList();
        setNoResultView(true);
    }

    private void setNoResultView(boolean isNoResult) {
        if (isNoResult) {
            listener.addNoResult();
        } else {
            listener.removeNoResult();
        }
    }

    @Override
    public void setOnRequestError(String message) {
        if (listener.getList().isEmpty()) {
            listener.showMessageErrorEmptyState(message);
        } else {
            listener.showMessageErrorSnackBar(message);
        }
    }

    @Override
    public InboxResCenterActivity.Model getResCenterTabModel() {
        return resCenterTabModel;
    }

}
