package com.tokopedia.inbox.rescenter.shipping.customdialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Window;

import com.tokopedia.core.ImageGallery;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.create.customdialog.BaseUploadImageDialog;
import com.tokopedia.inbox.rescenter.utils.LocalCacheManager;

import java.util.ArrayList;
import java.util.UUID;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

/**
 * Created by hangnadi on 12/15/16.
 */

public class UploadImageShippingResCenterDialog extends BaseUploadImageDialog {

    private final String resolutionID;
    private final LocalCacheManager.AttachmentShippingResCenter cache;

    public UploadImageShippingResCenterDialog(Fragment fragment, String resolutionID) {
        this.fragment = fragment;
        this.resolutionID = resolutionID;
        this.context = fragment.getActivity();
        this.cache = LocalCacheManager
                .AttachmentShippingResCenter
                .Builder(this.fragment.getActivity().getApplication(), resolutionID);
    }

    public UploadImageShippingResCenterDialog(Activity activity, String resolutionID) {
        this.activity = activity;
        this.resolutionID = resolutionID;
        this.context = activity;
        this.cache = LocalCacheManager
                .AttachmentShippingResCenter
                .Builder(this.fragment.getActivity().getApplication(), resolutionID);
    }

    @Override
    protected void processImageDataFromCamera(String cameraFileLoc, UploadImageDialogListener listener) {
        cache.setImageLocalPath(cameraFileLoc)
                .setImageUUID(UUID.randomUUID().toString())
                .save();
        listener.onSuccess(LocalCacheManager
                .AttachmentShippingResCenter
                .Builder(this.fragment.getActivity().getApplication(), resolutionID).getCache());
    }

    @Override
    public void processImageDataFromGallery(Intent intent, UploadImageDialogListener listener) {
        ArrayList<String> imageUrlOrPathList = intent.getStringArrayListExtra(PICKER_RESULT_PATHS);
        if (imageUrlOrPathList!= null && imageUrlOrPathList.size() > 0) {
            cache.setImageLocalPath(imageUrlOrPathList.get(0))
                    .setImageUUID(UUID.randomUUID().toString())
                    .save();
            listener.onSuccess(LocalCacheManager
                    .AttachmentShippingResCenter
                    .Builder(this.fragment.getActivity().getApplication(), resolutionID).getCache());
        }else{
            listener.onFailed();
        }

    }

    public interface onRemoveAttachmentListener {
        void onRemoveClickListener();
        void onCancelClickListener();
    }

    public void showRemoveDialog(final onRemoveAttachmentListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.dialog_remove_attachment));
        builder.setPositiveButton(context.getString(R.string.title_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onRemoveClickListener();
            }
        }).setNegativeButton(context.getString(R.string.title_no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.onCancelClickListener();
            }
        });

        Dialog dialog = builder.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }
}
