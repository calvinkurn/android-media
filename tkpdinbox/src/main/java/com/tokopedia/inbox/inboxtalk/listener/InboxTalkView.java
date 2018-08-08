package com.tokopedia.inbox.inboxtalk.listener;

import android.app.Activity;

import com.tokopedia.core.talk.model.model.InboxTalk;
import com.tokopedia.core.var.RecyclerViewItem;

import java.util.List;

/**
 * Created by stevenfredian on 4/5/16.
 */
public interface InboxTalkView {

    void showError(String string);

    void onConnectionResponse(List<InboxTalk> list, int paging, int isUnread);

    void onTimeoutResponse(String error, int page);

    void onTimeoutResponse(int page);

    void onStateResponse(List<RecyclerViewItem> list, int position, int page, boolean hasNext, String filterString);

    void onCacheResponse(List<InboxTalk> list, int isUnread);

    void onCacheNoResult();

    Activity getActivity();

    void setLoadingFooter();

    void removeLoadingFooter();

    void cancelRequest();

    void setMenuListEnabled(boolean isEnabled);
}
