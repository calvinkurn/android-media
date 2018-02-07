package com.tokopedia.inbox.rescenter.detailv2.view.listener;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;
import com.tokopedia.core.util.ImageUploadHandler;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ButtonDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationListDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.ConversationProductDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.DetailResChatDomain;
import com.tokopedia.inbox.rescenter.detailv2.view.viewmodel.detailreschat.NextActionDomain;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.AttachmentViewModel;
import com.tokopedia.inbox.rescenter.discussion.view.viewmodel.DiscussionItemViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yoasfs on 10/6/17.
 */

public interface DetailResChatFragmentListener {

    interface View extends CustomerView {

        void successGetConversation(DetailResChatDomain detailResChatDomain);

        void showProgressBar();

        void dismissProgressBar();

        void showChatProgressBar();

        void dismissChatProgressBar();

        void errorGetConversation(String error);

        void successGetConversationMore(ConversationListDomain conversationListDomain);

        void errorGetConversationMore(String error);

        void errorInputMessage(String error);

        void successReplyDiscussion(DiscussionItemViewModel discussionItemViewModel);

        void errorReplyDiscussion(String error);

        void showSnackBar(String message);

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

        void intentToEditAddress(int conversationId, int oldAddressId);

        void initNextStep(NextActionDomain nextActionDomain);

        void initActionButton(ButtonDomain buttonDomain);

        void onRefreshChatAdapter();

        void onAddItemAdapter(List<Visitable> items);

        void onAddItemWithPositionAdapter(int position, List<Visitable> items);

        void doAppealSolution();

        void doEditSolution();

        void doTrackShipping(String shipmentID, String shipmentRef);

        void doEditAwb(String conversationId,
                       String shippingId, String shippingRefNum);

        void goToProductDetail(ConversationProductDomain product);

        void goToProductList(ConversationProductDomain product);

        void openImagePreview(ArrayList<String> imageUrls, int position);

        void openVideoPlayer(String videoUrl);

        void showDummyText();

        void enableIvSend();

        void disableIvSend();
    }

    interface Presenter extends CustomerPresenter<DetailResChatFragmentListener.View> {

        void initView(String resolutionId);

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

        void doLoadMore(String resolutionId, String convId, DetailResChatDomain detailResChatDomain);
    }
}