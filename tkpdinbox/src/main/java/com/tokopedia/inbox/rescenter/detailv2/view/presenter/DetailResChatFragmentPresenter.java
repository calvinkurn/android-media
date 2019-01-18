package com.tokopedia.inbox.rescenter.detailv2.view.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.tokopedia.imagepicker.picker.main.view.ImagePickerActivity.PICKER_RESULT_PATHS;

/**
 * Created by yoasfs on 09/10/17.
 */

public class DetailResChatFragmentPresenter
        extends BaseDaggerPresenter<DetailResChatFragmentListener.View>
        implements DetailResChatFragmentListener.Presenter {

    private static final int PARAM_MIN_REPLY_CHAR_COUNT = 7;
    private static final int PARAM_MAX_REPLY_CHAR_COUNT = 5000;
    private static final int PARAM_LIMIT_CONVERSATION = 20;
    private static final int MAXIMAL_VIDEO_CONTENT_ALLOW = 1;
    private DetailResChatFragmentListener.View mainView;
    private GetResChatUseCase getResChatUseCase;
    private GetResChatMoreUseCase getResChatMoreUseCase;
    private SendDiscussionUseCase sendDiscussionUseCase;
    private SendDiscussionV2UseCase sendDiscussionV2UseCase;
    private AcceptSolutionUseCase acceptSolutionUseCase;
    private AskHelpResolutionUseCase askHelpResolutionUseCase;
    private CancelResolutionUseCase cancelResolutionUseCase;
    private InputAddressUseCase inputAddressUseCase;
    private EditAddressUseCase editAddressUseCase;
    private FinishResolutionUseCase finishResolutionUseCase;
    private String resolutionId;
    private Context context;
    private String[] videoExtensions = {
            "mp4", "m4v", "mov", "ogv"
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
    public void attachView(DetailResChatFragmentListener.View view) {
        this.mainView = view;
        super.attachView(view);
    }


    @Override
    public void detachView() {
        getResChatUseCase.unsubscribe();
        getResChatMoreUseCase.unsubscribe();
        sendDiscussionUseCase.unsubscribe();
        sendDiscussionV2UseCase.unsubscribe();
        acceptSolutionUseCase.unsubscribe();
        askHelpResolutionUseCase.unsubscribe();
        cancelResolutionUseCase.unsubscribe();
        inputAddressUseCase.unsubscribe();
        editAddressUseCase.unsubscribe();
        finishResolutionUseCase.unsubscribe();
    }

    @Override
    public void initView(String resolutionId) {
        this.resolutionId = resolutionId;
        loadConversation(resolutionId);
    }

    @Override
    public void initContext(Context context) {
        this.context = context;
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
            mainView.disableIvSend();
            postReply(message, attachmentList);
        } else {
            mainView.errorInputMessage(context.getResources().getString(R.string.string_error_min_max_words));
        }
    }

    private void postReply(String message, List<AttachmentViewModel> attachmentList) {
        sendDiscussionV2UseCase.execute(
                SendDiscussionV2UseCase
                        .getSendReplyParams(resolutionId, message, attachmentList),
                new ReplyDiscussionSubscriber(mainView));
    }

    @Override
    public void handleImageResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> imageUrlOrPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (imageUrlOrPathList != null && imageUrlOrPathList.size() > 0) {
                onAddImageAttachment(imageUrlOrPathList.get(0),
                        AttachmentViewModel.FILE_IMAGE);
            } else {
                onFailedAddAttachment();
            }
        } else {
            onFailedAddAttachment();
        }
    }

    @Override
    public void handleVideoResult(int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK && data != null) {
            ArrayList<String> videoPathList = data.getStringArrayListExtra(PICKER_RESULT_PATHS);
            if (videoPathList != null && videoPathList.size() > 0) {
                String videoPath = videoPathList.get(0);
                if (checkAttachmentVideo(videoPath)) {
                    onAddImageAttachment(videoPath, AttachmentViewModel.FILE_VIDEO);
                }
            } else {
                onFailedAddAttachment();
            }
        } else {
            onFailedAddAttachment();
        }
    }

    public boolean isDeviceSupportVideo() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            mainView.showSnackBar(context.getString(R.string.error_reply_discussion_resolution_version_minimum));
            return false;
        }
        return true;
    }

    public boolean isAllowToAddMoreVideo() {
        int countVideoAlreadyAdded = 0;
        for (AttachmentViewModel model : mainView.getAttachmentListFromAdapter()) {
            if (model.isVideo()) {
                countVideoAlreadyAdded++;
                break;
            }
        }
        if (countVideoAlreadyAdded == MAXIMAL_VIDEO_CONTENT_ALLOW) {
            mainView.showSnackBar(context.getString(R.string.error_reply_discussion_resolution_reach_max));
            return false;
        }
        return true;
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

    private boolean checkAttachmentVideo(String videoPath) {
        boolean isExtensionAllow = false;
        for (String extension : videoExtensions) {
            if (videoPath != null && videoPath.toLowerCase(Locale.US).endsWith(extension)) {
                isExtensionAllow = true;
            }
        }
        if (!isExtensionAllow) {
            mainView.showSnackBar(context.getString(R.string.error_reply_discussion_resolution_file_not_allowed));
            return false;
        }

        return isAllowToAddMoreVideo();
    }
}
