package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.GalleryBrowser;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gallery.MediaItem;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AcceptSolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.AskHelpResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.CancelResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResChatUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.SendDiscussionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.SendDiscussionV2UseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.AcceptSolutionSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.GetDetailResChatSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.ReplyDiscussionSubscriber;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Created by yoasfs on 09/10/17.
 */

public class DetailResChatFragmentPresenter
        extends BaseDaggerPresenter<DetailResChatFragmentListener.View>
        implements DetailResChatFragmentListener.Presenter {

    public static final int PARAM_MIN_REPLY_CHAR_COUNT = 7;
    public static final int PARAM_MAX_REPLY_CHAR_COUNT = 5000;
    private static final int MAXIMAL_VIDEO_CONTENT_ALLOW = 1;
    private String[] extensions = {
            "jpg", "jpeg", "png", "mp4", "m4v", "mov", "ogv"
    };

    DetailResChatFragmentListener.View mainView;

    GetResChatUseCase getResChatUseCase;
    SendDiscussionUseCase sendDiscussionUseCase;
    SendDiscussionV2UseCase sendDiscussionV2UseCase;
    AcceptSolutionUseCase acceptSolutionUseCase;
    AskHelpResolutionUseCase askHelpResolutionUseCase;
    CancelResolutionUseCase cancelResolutionUseCase;
    ImageUploadHandler uploadImageDialog;

    String resolutionId;
    Context context;

    @Inject
    public DetailResChatFragmentPresenter(GetResChatUseCase getResChatUseCase,
                                          SendDiscussionUseCase sendDiscussionUseCase,
                                          SendDiscussionV2UseCase sendDiscussionV2UseCase,
                                          AcceptSolutionUseCase acceptSolutionUseCase,
                                          AskHelpResolutionUseCase askHelpResolutionUseCase,
                                          CancelResolutionUseCase cancelResolutionUseCase) {
        this.getResChatUseCase = getResChatUseCase;
        this.acceptSolutionUseCase = acceptSolutionUseCase;
        this.sendDiscussionUseCase = sendDiscussionUseCase;
        this.sendDiscussionV2UseCase = sendDiscussionV2UseCase;
        this.askHelpResolutionUseCase = askHelpResolutionUseCase;
        this.cancelResolutionUseCase = cancelResolutionUseCase;
    }

    @Override
    public void initUploadImageHandler(Context context, ImageUploadHandler imageUploadHandler) {
        this.context = context;
        this.uploadImageDialog = imageUploadHandler;
    }

    @Override
    public void attachView(DetailResChatFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }


    @Override
    public void detachView() {
        getResChatUseCase.unsubscribe();
        sendDiscussionUseCase.unsubscribe();
        sendDiscussionV2UseCase.unsubscribe();
        acceptSolutionUseCase.unsubscribe();
        askHelpResolutionUseCase.unsubscribe();
        cancelResolutionUseCase.unsubscribe();
    }

    @Override
    public void initView(String resolutionId) {
        this.resolutionId = resolutionId;
        loadConversation(resolutionId);
    }

    public void loadConversation(String resolutionId) {
        mainView.showProgressBar();
        getResChatUseCase.execute(
                getResChatUseCase.getResChatUseCaseParam(String.valueOf(resolutionId)),
                new GetDetailResChatSubscriber(mainView));
    }

    @Override
    public void btnAcceptSolutionClicked() {
        mainView.showProgressBar();
        acceptSolutionUseCase.execute(AcceptSolutionUseCase.getParams(resolutionId), new AcceptSolutionSubscriber(mainView));
    }

    @Override
    public void sendIconPressed(String message, List<AttachmentViewModel> attachmentList) {
        if (message.length() >= PARAM_MIN_REPLY_CHAR_COUNT && message.length() <= PARAM_MAX_REPLY_CHAR_COUNT) {
            postReply(message, attachmentList);
        } else {
            mainView.errorInputMessage("Minimal 7 karakter dan maksimal 5000 karakter");
        }
    }

    private void postReply(String message, List<AttachmentViewModel> attachmentList) {

        if (TrackingUtils.getGtmString(AppEventTracking.GTM.RESOLUTION_CENTER_UPLOAD_VIDEO).equals("true")) {
            sendDiscussionV2UseCase.execute(
                    SendDiscussionV2UseCase
                            .getSendReplyParams(resolutionId, message, attachmentList),
                    new ReplyDiscussionSubscriber(mainView));
        } else {
            sendDiscussionUseCase.execute(SendDiscussionUseCase.getSendReplyParams(resolutionId, message, attachmentList),
                    new ReplyDiscussionSubscriber(mainView));
        }
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
    }


    private boolean checkAttachmentValidation(MediaItem item) {
        boolean isExtensionAllow = false;
        for (String extension : extensions) {
            String path = item.getRealPath();
            if (path != null && path.toLowerCase(Locale.US).endsWith(extension)) {
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
