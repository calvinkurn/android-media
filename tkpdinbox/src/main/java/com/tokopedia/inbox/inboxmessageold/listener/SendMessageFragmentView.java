package com.tokopedia.inbox.inboxmessageold.listener;

import android.view.View;

/**
 * Created by Nisie on 5/26/16.
 */
public interface SendMessageFragmentView {
    String getSubject();

    String getContent();

    void setSubjectError(String error);

    void setContentError(String error);

    void removeError();

    void showError(String error, View.OnClickListener listener);

    void finishLoading();

    void showLoading();

    void setActionsEnabled(boolean isEnabled);

    void showError(String string);

}
