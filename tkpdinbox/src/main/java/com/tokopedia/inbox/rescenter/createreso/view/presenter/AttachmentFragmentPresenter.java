package com.tokopedia.inbox.rescenter.createreso.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.createreso.view.listener.AttachmentFragmentListener;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.ResultViewModel;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.Attachment;
import com.tokopedia.inbox.rescenter.createreso.view.viewmodel.attachment.AttachmentViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

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
    public void handleImageResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                onAddImageAttachment(imageUrlOrPathList.get(0),
                        AttachmentViewModel.FILE_IMAGE);
            }else{
                onFailedAddAttachment();
            }
        }else{
            onFailedAddAttachment();
        }
    }

    @Override
    public void handleVideoResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            ArrayList<String> videoPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (videoPathList != null && videoPathList.size() > 0) {
                String videoPath = videoPathList.get(0);
                if (checkAttachmentValidation(videoPath)){
                    onAddImageAttachment(videoPath, AttachmentViewModel.FILE_VIDEO);
                }
            }else{
                onFailedAddAttachment();
            }
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

    private boolean checkAttachmentValidation(String realPath) {
        boolean isExtensionAllow = false;
        for (String extension : extensions) {
            if (realPath != null && realPath.toLowerCase(Locale.US).endsWith(extension)) {
                Log.d("hangnadi validation", "checkAttachmentValidation: " + extension + "\npath : " + realPath);
                isExtensionAllow = true;
            }
        }
        if (!isExtensionAllow) {
            mainView.showSnackBarError(context.getString(R.string.error_reply_discussion_resolution_file_not_allowed));
            return false;
        }

        int countVideoAlreadyAdded = 0;
        for (AttachmentViewModel model : mainView.getAttachmentListFromAdapter()) {
            if (model.isVideo()) {
                countVideoAlreadyAdded++;
            }
        }
        if (countVideoAlreadyAdded == MAXIMAL_VIDEO_CONTENT_ALLOW) {
            mainView.showSnackBarError(context.getString(R.string.error_reply_discussion_resolution_reach_max));
            return false;
        }

        File file = new File(realPath);
        long length = file.length() / 1024;
        if (length >= 20000) {
            mainView.showSnackBarError(context.getString(R.string.error_reply_discussion_resolution_reach_max_size_video));
            return false;
        }

        return true;
    }
}
