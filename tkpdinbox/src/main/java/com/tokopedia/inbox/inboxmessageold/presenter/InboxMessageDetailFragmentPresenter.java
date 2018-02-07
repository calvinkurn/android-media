package com.tokopedia.inbox.inboxmessageold.presenter;

import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetail;
import com.tokopedia.inbox.inboxmessageold.model.inboxmessagedetail.InboxMessageDetailItem;

import java.util.Map;

/**
 * Created by Nisie on 5/19/16.
 */
public interface InboxMessageDetailFragmentPresenter {
    void initData();

    String getMessageBetween(InboxMessageDetail inboxMessageDetail);

    void onDestroyView();

    void getMessageDetail();

    void setResult(InboxMessageDetail inboxMessageDetail);

    Map<String, String> getMessageDetailParam();

    void onRefresh();

    void onLoadMore();

    void onGoToProfile(String userId);

    void flagSpam(int position, InboxMessageDetailItem inboxMessageDetailItem);

    void undoFlagSpam(final int position, InboxMessageDetailItem messageReplyId);

    void sendReply();

    void getUrl();

    void updateCache(InboxMessageDetailItem result);

    void updateCacheFlagSpam(int position);

    void updateCacheUndoFlagSpam(int position, InboxMessageDetailItem inboxMessageDetailItem);


    void setResultBundle();

}
