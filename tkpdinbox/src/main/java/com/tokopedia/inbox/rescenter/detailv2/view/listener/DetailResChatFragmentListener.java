package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ButtonDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.util.List;

/**
 * Created by yoasfs on 10/6/17.
 */

public interface DetailResChatFragmentListener {

    interface View extends CustomerView {

        void successGetConversation(DetailResChatDomain detailResChatDomain, boolean isFirstInit);

        void showProgressBar();

        void dismissProgressBar();

        void errorGetConversation(String error, boolean isFirstInit);

        void errorInputMessage(String error);

        void successReplyDiscussion(DiscussionItemViewModel discussionItemViewModel);

        void errorReplyDiscussion(String error);

        void showSnackBarError(String message);

        void addAttachmentFile(AttachmentViewModel attachmentViewModel);

        List<AttachmentViewModel> getAttachmentListFromAdapter();

        void successAcceptSolution();

        void errorAcceptSolution(String error);

        void successCancelComplaint();

        void errorCancelComplaint(String error);

        void successAskHelp();

        void errorAskHelp(String error);

        void successInputAddress();

        void errorInputAddress(String error);

        void successEditAddress();

        void errorEditAddress(String error);

        void successFinishResolution();

        void errorFinishResolution(String error);

        void intentToSeeAllProducts();

        void initNextStep(NextActionDomain nextActionDomain);

        void initActionButton(ButtonDomain buttonDomain);

        void onRefreshChatAdapter(boolean isFirstInit);

        void onAddItemAdapter(List<Visitable> items);


    }

    interface Presenter extends CustomerPresenter<DetailResChatFragmentListener.View> {

        void initView(String resolutionId, boolean isFirstInit);

        void initUploadImageHandler(Context context, ImageUploadHandler imageUploadHandler);

        void sendIconPressed(String message, List<AttachmentViewModel> attachmentList);

        void handleDefaultOldUploadImageHandlerResult(int resultCode, Intent data);

        void handleNewGalleryResult(int resultCode, Intent data);

        void actionAcceptSolution();

        void actionCancelComplaint();

        void actionAskHelp();

        void actionFinish();

        void inputAddressAcceptSolution(String addressId);

        void inputAddressMigrateVersion(String addressId);

        void inputAddressAcceptAdminSolution(String addressId);

        void actionEditAddress(String addressId, String oldAddressId, String conversationId);
    }
}