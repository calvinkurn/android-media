package com.tokopedia.inbox.rescenter.create.customdialog;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.database.model.ResCenterAttachment;

import java.util.List;

/**
 * Created on 4/14/16.
 */
public abstract class BaseUploadImageDialog {

    protected Context context;
    protected Activity activity;
    protected Fragment fragment;

    public interface UploadImageDialogListener {
        void onSuccess(List<ResCenterAttachment> data);
        void onFailed();
    }

    protected abstract void processImageDataFromGallery(Intent intent, UploadImageDialogListener listener);
}
