package com.tokopedia.inbox.contactus.listener;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.contactus.model.ImageUpload;
import com.tokopedia.inbox.contactus.model.solution.SolutionResult;

import java.util.ArrayList;

/**
 * Created by nisie on 8/15/16.
 */
public interface CreateTicketFormFragmentView {
    String getTicketCategoryId();

    Activity getActivity();

    void showLoading();

    void finishLoading();

    void setResult(SolutionResult ticketFormData);

    EditText getMessage();

    String getString(int resId);

    void showError(String error);

    Bundle getArguments();

    ArrayList<ImageUpload> getAttachment();

    void removeErrorEmptyState();

    void showErrorEmptyState(String error, NetworkErrorHelper.RetryClickedListener retryClickedListener);

    void showErrorValidation(EditText view, String error);

    String getPhoneNumber();

    TextView getAttachmentNote();

    EditText getName();

    EditText getEmail();
}