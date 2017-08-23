package com.tokopedia.inbox.inboxticket.presenter;

import android.content.Intent;

/**
 * Created by Nisie on 4/22/16.
 */
public interface InboxTicketDetailFragmentPresenter {
    void getViewMore();

    void onRefresh();

    void setCache();

    void onSendButtonClicked();

    void sendReply();

    void onActivityResult(int requestCode, int resultCode, Intent data);

    void actionImagePicker();

    void actionCamera();

    void onDestroyView();

    void commentRating(String isHelpful);
}
