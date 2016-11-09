package com.tokopedia.inbox.contactus.listener;

import android.app.Activity;

import com.tokopedia.inbox.contactus.activity.ContactUsActivity;
import com.tokopedia.inbox.contactus.model.contactuscategory.ContactUsCategory;

/**
 * Created by nisie on 8/12/16.
 */
public interface ContactUsCategoryFragmentView {
    void showProgressDialog();

    Activity getActivity();

    void finishLoading();

    void setCategory(ContactUsCategory result);

    ContactUsActivity.BackButtonListener getBackButtonListener();

    String getString(int resId);

    void showError(String error);
}
