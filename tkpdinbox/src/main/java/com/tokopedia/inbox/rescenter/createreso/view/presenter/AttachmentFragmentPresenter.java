package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gallery.MediaItem;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.Attachment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;

import java.io.File;
import java.util.List;
import java.util.Locale;

/**
 * Created by yoasfs on 31/08/17.
 */

public class AttachmentFragmentPresenter extends BaseDaggerPresenter<AttachmentFragmentListener.View>
        implements AttachmentFragmentListener.Presenter {

    private static final int MAXIMAL_VIDEO_CONTENT_ALLOW = 1;

    private Context context;
    private ResultViewModel resultViewModel;
    private AttachmentFragmentListener.View mainView;
    private Attachment attachment;
    private ImageUploadHandler uploadImageDialog;

    private String[] extensions = {
            "jpg", "jpeg", "png", "mp4", "m4v", "mov", "ogv"
    };

    public AttachmentFragmentPresenter(Context context,
                                       AttachmentFragmentListener.View mainView,
                                       ImageUploadHandler imageUploadHandler) {
        this.context = context;
        this.mainView = mainView;
        attachment = new Attachment();
        uploadImageDialog = imageUploadHandler;
    }


    @Override
    public void initResultViewModel(ResultViewModel resultViewModel) {
        this.resultViewModel = resultViewModel;
        if (resultViewModel.attachmentList != null && resultViewModel.attachmentList.size() != 0) {
            attachment.attachmentViewModelList = resultViewModel.attachmentList;
            attachment.information = resultViewModel.message.remark;
            mainView.populateDataToView(resultViewModel);
            mainView.updateView(attachment);
        }
    }

    @Override
    public void onInformationStringChanged(String information) {
        attachment.information = information.length() < 30 ? "" : information;
        updateView();
    }

    @Override
    public void onAdapterChanged(List<AttachmentViewModel> attachmentViewModelList) {
        updateView();
    }

    @Override
    public void btnContinueClicked() {
        resultViewModel.message.remark = attachment.information;
        int i = 0;
        for (AttachmentViewModel attachmentViewModel : mainView.getAttachmentListFromAdapter()) {
            attachmentViewModel.setAttachmentId(String.valueOf(i));
            i++;
        }
        resultViewModel.attachmentList = mainView.getAttachmentListFromAdapter();
        resultViewModel.attachmentCount = mainView.getAttachmentListFromAdapter().size();
        mainView.submitData(resultViewModel);
    }

    @Override
    public void handleDefaultOldUploadImageHandlerResult(int resultCode, Intent data) {
        switch (resultCode) {
            case GalleryBrowser.RESULT_CODE:
                if (data != null && data.getStringExtra(ImageGallery.EXTRA_URL) != null) {
                    onAddImageAttachment(data.getStringExtra(ImageGallery.EXTRA_URL),
                            AttachmentViewModel.FILE_IMAGE);
                } else {
                    onFailedAddAttachment();
                }
                break;
            case Activity.RESULT_OK:
                if (uploadImageDialog != null && uploadImageDialog.getCameraFileloc() != null) {
                    onAddImageAttachment(uploadImageDialog.getCameraFileloc(),
                            AttachmentViewModel.FILE_IMAGE);
                } else {
                    onFailedAddAttachment();
                }
                break;
        }
    }

    @Override
    public void handleNewGalleryResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (data != null && data.getParcelableExtra("EXTRA_RESULT_SELECTION") != null) {
                MediaItem item = data.getParcelableExtra("EXTRA_RESULT_SELECTION");
                if (checkAttachmentValidation(item)) {
                    onAddImageAttachment(item.getRealPath(), getTypeFile(item));
                }
            } else {
                onFailedAddAttachment();
            }
        }
    }

    private int getTypeFile(MediaItem item) {
        if (item.isVideo()) {
            return AttachmentViewModel.FILE_VIDEO;
        } else if (item.isImage()) {
            return AttachmentViewModel.FILE_IMAGE;
        } else {
            return AttachmentViewModel.UNKNOWN;
        }
    }

    private void onFailedAddAttachment() {
        mainView.showSnackBarError(context.getString(R.string.failed_upload_image));
    }

    private void onAddImageAttachment(String fileLoc, int typeFile) {
        AttachmentViewModel attachmentViewModel = new AttachmentViewModel();
        attachmentViewModel.setFileLoc(fileLoc);
        attachmentViewModel.setFileType(typeFile);
        mainView.addAttachmentFile(attachmentViewModel);
        updateView();
    }

    private void updateView() {
        mainView.updateView(attachment);
    }

    private boolean checkAttachmentValidation(MediaItem item) {
        boolean isExtensionAllow = false;
        for (String extension : extensions) {
            String path = item.getRealPath();
            if (path != null && path.toLowerCase(Locale.US).endsWith(extension)) {
                Log.d("hangnadi validation", "checkAttachmentValidation: " + extension + "\npath : " + path);
                isExtensionAllow = true;
            }
        }
        if (!isExtensionAllow) {
            mainView.showSnackBarError(context.getString(R.string.error_reply_discussion_resolution_file_not_allowed));
            return false;
        }

        if (item.isImage() && (item.height < 300 || item.width < 300)) {
            mainView.showSnackBarError(context.getString(R.string.error_reply_discussion_resolution_min_size));
            return false;
        }

        if (item.isImage()) {
            File file = new File(item.getRealPath());
            long length = file.length() / 1024;
            if (length >= 15000) {
                mainView.showSnackBarError(context.getString(R.string.error_reply_discussion_resolution_reach_max_size_image));
                return false;
            }
        }

        int countVideoAlreadyAdded = 0;
        if (item.isVideo()) {
            for (AttachmentViewModel model : mainView.getAttachmentListFromAdapter()) {
                if (model.isVideo()) {
                    countVideoAlreadyAdded++;
                }
            }
        }
        if (countVideoAlreadyAdded == MAXIMAL_VIDEO_CONTENT_ALLOW) {
            mainView.showSnackBarError(context.getString(R.string.error_reply_discussion_resolution_reach_max));
            return false;
        }

        if (item.isVideo()) {
            File file = new File(item.getRealPath());
            long length = file.length() / 1024;
            if (length >= 20000) {
                mainView.showSnackBarError(context.getString(R.string.error_reply_discussion_resolution_reach_max_size_video));
                return false;
            }
        }

        return true;
    }
}
