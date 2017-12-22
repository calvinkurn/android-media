package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.EditAddressUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.FinishResolutionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResChatMoreUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.GetResChatUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.InputAddressUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.SendDiscussionUseCase;
import com.tokopedia.inbox.rescenter.detailv2.domain.interactor.SendDiscussionV2UseCase;
import com.tokopedia.inbox.rescenter.detailv2.view.listener.DetailResChatFragmentListener;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.AcceptSolutionSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.AskHelpSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.CancelComplaintSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.EditAddressSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.FinishResolutionSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.GetDetailResChatMoreSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.GetDetailResChatSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.InputAddressAcceptAdminSolutionSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.InputAddressAcceptSolutionSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.InputAddressMigrateVersionSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.subscriber.ReplyDiscussionSubscriber;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;

import java.io.File;
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
    public static final int PARAM_LIMIT_CONVERSATION = 20;
    private static final int MAXIMAL_VIDEO_CONTENT_ALLOW = 1;
    DetailResChatFragmentListener.View mainView;
    GetResChatUseCase getResChatUseCase;
    GetResChatMoreUseCase getResChatMoreUseCase;
    SendDiscussionUseCase sendDiscussionUseCase;
    SendDiscussionV2UseCase sendDiscussionV2UseCase;
    AcceptSolutionUseCase acceptSolutionUseCase;
    AskHelpResolutionUseCase askHelpResolutionUseCase;
    CancelResolutionUseCase cancelResolutionUseCase;
    InputAddressUseCase inputAddressUseCase;
    EditAddressUseCase editAddressUseCase;
    FinishResolutionUseCase finishResolutionUseCase;
    ImageUploadHandler uploadImageDialog;
    String resolutionId;
    Context context;
    private String[] extensions = {
            "jpg", "jpeg", "png", "mp4", "m4v", "mov", "ogv"
    };

    @Inject
    public DetailResChatFragmentPresenter(GetResChatUseCase getResChatUseCase,
                                          GetResChatMoreUseCase getResChatMoreUseCase,
                                          SendDiscussionUseCase sendDiscussionUseCase,
                                          SendDiscussionV2UseCase sendDiscussionV2UseCase,
                                          AcceptSolutionUseCase acceptSolutionUseCase,
                                          AskHelpResolutionUseCase askHelpResolutionUseCase,
                                          CancelResolutionUseCase cancelResolutionUseCase,
                                          InputAddressUseCase inputAddressUseCase,
                                          EditAddressUseCase editAddressUseCase,
                                          FinishResolutionUseCase finishResolutionUseCase) {
        this.getResChatUseCase = getResChatUseCase;
        this.getResChatMoreUseCase = getResChatMoreUseCase;
        this.acceptSolutionUseCase = acceptSolutionUseCase;
        this.sendDiscussionUseCase = sendDiscussionUseCase;
        this.sendDiscussionV2UseCase = sendDiscussionV2UseCase;
        this.askHelpResolutionUseCase = askHelpResolutionUseCase;
        this.cancelResolutionUseCase = cancelResolutionUseCase;
        this.inputAddressUseCase = inputAddressUseCase;
        this.editAddressUseCase = editAddressUseCase;
        this.finishResolutionUseCase = finishResolutionUseCase;
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
        getResChatMoreUseCase.unsubscribe();
    }

    @Override
    public void initView(String resolutionId) {
        this.resolutionId = resolutionId;
        loadConversation(resolutionId);
    }

    public void loadConversation(String resolutionId) {
        mainView.showProgressBar();
        getResChatUseCase.execute(
                GetResChatUseCase.getResChatUseCaseParam(
                        String.valueOf(resolutionId),
                        PARAM_LIMIT_CONVERSATION),
                new GetDetailResChatSubscriber(mainView));
    }

    @Override
    public void doLoadMore(String resolutionId, String convId, DetailResChatDomain detailResChatDomain) {
        mainView.showChatProgressBar();
        getResChatMoreUseCase.execute(
                GetResChatMoreUseCase.getResChatUseCaseParam(
                        String.valueOf(resolutionId),
                        PARAM_LIMIT_CONVERSATION,
                        convId),
                new GetDetailResChatMoreSubscriber(mainView, detailResChatDomain));
    }

    @Override
    public void actionAcceptSolution() {
        mainView.showProgressBar();
        acceptSolutionUseCase.execute(
                AcceptSolutionUseCase.getParams(resolutionId),
                new AcceptSolutionSubscriber(mainView));
    }

    @Override
    public void actionCancelComplaint() {
        mainView.showProgressBar();
        cancelResolutionUseCase.execute(
                CancelResolutionUseCase.getParams(resolutionId),
                new CancelComplaintSubscriber(mainView));
    }

    @Override
    public void actionAskHelp() {
        mainView.showProgressBar();
        askHelpResolutionUseCase.execute(
                AskHelpResolutionUseCase.getParams(resolutionId),
                new AskHelpSubscriber(mainView));
    }

    @Override
    public void actionFinish() {
        mainView.showProgressBar();
        finishResolutionUseCase.execute(
                FinishResolutionUseCase.getParams(resolutionId),
                new FinishResolutionSubscriber(mainView));

    }

    @Override
    public void inputAddressAcceptSolution(String addressId) {
        mainView.showProgressBar();
        inputAddressUseCase.execute(
                InputAddressUseCase.getInputAddressParam(
                        addressId,
                        resolutionId,
                        InputAddressUseCase.DEFAULT_BY_PASS),
                new InputAddressAcceptSolutionSubscriber(mainView));
    }

    @Override
    public void inputAddressMigrateVersion(String addressId) {
        mainView.showProgressBar();
        inputAddressUseCase.execute(
                InputAddressUseCase.getInputAddressMigrateVersionParam(
                        addressId,
                        resolutionId),
                new InputAddressMigrateVersionSubscriber(mainView));

    }

    @Override
    public void inputAddressAcceptAdminSolution(String addressId) {
        mainView.showProgressBar();
        inputAddressUseCase.execute(
                InputAddressUseCase.getInputAddressParam(
                        addressId,
                        resolutionId,
                        InputAddressUseCase.ADMIN_BY_PASS),
                new InputAddressAcceptAdminSolutionSubscriber(mainView));
    }

    @Override
    public void actionEditAddress(String addressId, String oldAddressId, String conversationId) {
        mainView.showProgressBar();
        editAddressUseCase.execute(
                EditAddressUseCase.getEditAddressParam(
                        addressId,
                        oldAddressId,
                        conversationId,
                        resolutionId),
                new EditAddressSubscriber(mainView));
    }

    @Override
    public void sendIconPressed(String message, List<AttachmentViewModel> attachmentList) {
        if (message.length() >= PARAM_MIN_REPLY_CHAR_COUNT && message.length() <= PARAM_MAX_REPLY_CHAR_COUNT) {
            getView().showDummyText();
            mainView.showSnackBar(context.getResources().getString(R.string.string_sending_message));
            mainView.disableIvSend();
            postReply(message, attachmentList);
        } else {
            mainView.errorInputMessage(context.getResources().getString(R.string.string_error_min_max_words));
        }
    }

    private void postReply(String message, List<AttachmentViewModel> attachmentList) {

        if (TrackingUtils.getGtmString(AppEventTracking.GTM.RESOLUTION_CENTER_UPLOAD_VIDEO).equals("true")) {
            sendDiscussionV2UseCase.execute(
                    SendDiscussionV2UseCase
                            .getSendReplyParams(resolutionId, message, attachmentList),
                    new ReplyDiscussionSubscriber(mainView));
        } else {
            sendDiscussionUseCase.execute(
                    SendDiscussionUseCase.getSendReplyParams(
                            resolutionId,
                            message,
                            attachmentList),
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
        mainView.showSnackBar(context.getString(R.string.failed_upload_image));
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
            mainView.showSnackBar(context.getString(R.string.error_reply_discussion_resolution_file_not_allowed));
            return false;
        }

        if (item.isImage() && (item.height < 300 || item.width < 300)) {
            mainView.showSnackBar(context.getString(R.string.error_reply_discussion_resolution_min_size));
            return false;
        }

        if (item.isImage()) {
            File file = new File(item.getRealPath());
            long length = file.length() / 1024;
            if (length >= 15000) {
                mainView.showSnackBar(context.getString(R.string.error_reply_discussion_resolution_reach_max_size_image));
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
            mainView.showSnackBar(context.getString(R.string.error_reply_discussion_resolution_reach_max));
            return false;
        }

        if (item.isVideo()) {
            File file = new File(item.getRealPath());
            long length = file.length() / 1024;
            if (length >= 20000) {
                mainView.showSnackBar(context.getString(R.string.error_reply_discussion_resolution_reach_max_size_video));
                return false;
            }
        }

        return true;
    }
}
