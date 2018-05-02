package com.tokopedia.inbox.inboxchat.presenter;

import android.content.Intent;
import android.text.TextUtils;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.attachinvoice.view.resultmodel.SelectedInvoice;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;
import com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.InboxChatConstant;
import com.tokopedia.inbox.inboxchat.data.pojo.SetChatRatingPojo;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.domain.usecase.AttachImageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SendMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SetChatRatingUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.WebSocketUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.GetTemplateUseCase;
import com.tokopedia.inbox.inboxchat.helper.AttachmentChatHelper;
import com.tokopedia.inbox.inboxchat.presenter.subscriber.GetReplySubscriber;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.model.UploadImageDomain;
import com.tokopedia.inbox.inboxchat.util.ImageUploadHandlerChat;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSelectionViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachProductViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.DummyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.GetTemplateViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.SendMessageViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;
import com.tokopedia.inbox.inboxchat.viewmodel.mapper.AttachInvoiceMapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;

import static com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel.GET_CHAT_MODE;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_MESSAGE_ID;

/**
 * Created by stevenfredian on 9/26/17.
 */

public class ChatRoomPresenter extends BaseDaggerPresenter<ChatRoomContract.View>
        implements ChatRoomContract.Presenter {

    private static final String ROLE_SHOP = "shop";

    private final GetReplyListUseCase getReplyListUseCase;
    private final ReplyMessageUseCase replyMessageUseCase;
    private final AttachImageUseCase attachImageUseCase;
    private GetTemplateUseCase getTemplateUseCase;
    private SetChatRatingUseCase setChatRatingUseCase;
    private SessionHandler sessionHandler;
    public PagingHandler pagingHandler;
    boolean isRequesting;
    private String magicString;
    private ChatWebSocketListenerImpl listener;
    private boolean flagTyping;
    private int attempt;
    private boolean isFirstTime;
    private ImageUploadHandlerChat imageUploadHandler;
    private String cameraFileLoc;
    private int shopIdFromAPI = 0;
    final static String USER = "Pengguna";
    final static String ADMIN = "Administrator";
    final static String OFFICIAL = "Official";
    final static String SELLER = "shop";
    private SendMessageUseCase sendMessageUseCase;
    private WebSocketUseCase webSocketUseCase;

    @Inject
    ChatRoomPresenter(GetReplyListUseCase getReplyListUseCase,
                      ReplyMessageUseCase replyMessageUseCase,
                      GetTemplateUseCase getTemplateUseCase,
                      SendMessageUseCase sendMessageUseCase,
                      AttachImageUseCase attachImageUseCase,
                      SetChatRatingUseCase setChatRatingUseCase,
                      SessionHandler sessionHandler) {
        this.getReplyListUseCase = getReplyListUseCase;
        this.replyMessageUseCase = replyMessageUseCase;
        this.getTemplateUseCase = getTemplateUseCase;
        this.sendMessageUseCase = sendMessageUseCase;
        this.attachImageUseCase = attachImageUseCase;
        this.setChatRatingUseCase = setChatRatingUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void attachView(ChatRoomContract.View view) {
        super.attachView(view);
        attempt = 0;
        isRequesting = false;
        this.pagingHandler = new PagingHandler();

        magicString = TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN +
                TkpdBaseURL.Chat.CHAT_WEBSOCKET +
                "?os_type=1" +
                "&device_id=" + GCMHandler.getRegistrationId(getView().getContext()) +
                "&user_id=" + SessionHandler.getLoginID(getView().getContext());
        listener = new ChatWebSocketListenerImpl(getView().getInterface());
        isFirstTime = true;

        imageUploadHandler = ImageUploadHandlerChat.createInstance(getView().getFragment());


        if (getView().needCreateWebSocket()) {
            webSocketUseCase = new WebSocketUseCase(magicString, getView().getUserSession(), listener);
        } else {
            getView().setHeader();
            getView().hideMainLoading();
            getView().displayReplyField(true);
            getView().hideNotifier();
        }
        if (!getView().isChatBot()) {
            getTemplate();
        }
    }

    @Override
    public void detachView() {
        super.detachView();
        if(webSocketUseCase != null)
            webSocketUseCase.unsubscribe();
        getReplyListUseCase.unsubscribe();
        getTemplateUseCase.unsubscribe();
        replyMessageUseCase.unsubscribe();
        sendMessageUseCase.unsubscribe();
        attachImageUseCase.unsubscribe();
        setChatRatingUseCase.unsubscribe();
    }

    @Override
    public void onGoToDetail(String id, String role) {
        if (role != null && id != null && !role.equals(ADMIN.toLowerCase()) && !role.equals
                (OFFICIAL.toLowerCase())) {
            if (role.equals(SELLER.toLowerCase())) {
                Intent intent = ((TkpdInboxRouter) getView().getActivity().getApplicationContext
                        ()).getShopPageIntent(getView().getActivity(), String.valueOf(id));
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                getView().startActivity(intent);
            } else {
                if (getView().getActivity().getApplicationContext() instanceof TkpdInboxRouter) {
                    getView().startActivity(
                            ((TkpdInboxRouter) getView().getActivity().getApplicationContext())
                                    .getTopProfileIntent(getView().getContext(), id)
                    );
                }
            }

        }
    }

    public void uploadWithApi(final String path, final MyChatViewModel model) {
        String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));
        RequestParams params = ReplyMessageUseCase.generateParamAttachImage(messageId, path);

        replyMessageUseCase.execute(params, new Subscriber<ReplyActionData>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                getView().setUploadingMode(false);
                getView().onErrorUploadImages(
                        ErrorHandler.getErrorMessage(throwable, getView().getActivity()), model);
            }

            @Override
            public void onNext(ReplyActionData data) {
                getView().setUploadingMode(false);
                getView().onSuccessSendAttach(data, model);
            }
        });

    }


    @Override
    public void sendMessage(int networkType) {
        if (isValidReply()) {
            getView().addDummyMessage();
            getView().setViewEnabled(false);
            final String reply = (getView().getReplyMessage());
            String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));

            if(networkType == InboxChatConstant.MODE_WEBSOCKET) {
                sendReply(messageId, reply);
            }else if(networkType == InboxChatConstant.MODE_API) {
                RequestParams params = ReplyMessageUseCase.generateParam(messageId, reply);
                replyMessageUseCase.execute(params, new Subscriber<ReplyActionData>() {
                    @Override
                    public void onCompleted() {
//                        isRequesting = false;
                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().onErrorSendReply();
                    }

                    @Override
                    public void onNext(ReplyActionData data) {
                        getView().onSuccessSendReply(data, reply);
                    }
                });
            }
        }
    }


    @Override
    public void addMessageChatBalloon(WebSocketResponse response) {
        try {
            if (getView().isCurrentThread(response.getData().getMsgId())
                    && getView().isMyMessage(response.getData().getFromUid())) {

                MyChatViewModel item = new MyChatViewModel();
                item.setReplyId(response.getData().getMsgId());
                item.setMsgId(response.getData().getMsgId());
                item.setSenderId(String.valueOf(response.getData().getFromUid()));
                item.setMsg(response.getData().getMessage().getCensoredReply());
                item.setReplyTime(response.getData().getMessage().getTimeStampUnix());
                item.setAttachment(response.getData().getAttachment());
                if (response.getData().getAttachment() != null &&
                        response.getData().getAttachment().getType().equals(AttachmentChatHelper
                                .PRODUCT_ATTACHED)) {
                    AttachProductViewModel productItem = new AttachProductViewModel(item);
                    Integer productId = response.getData().getAttachment().getAttributes()
                            .getProductId();
                    getView().getAdapter().removeLastProductWithId(productId);
                    getView().getAdapter().addReply(productItem);
                } else if (response.getData().getAttachment() != null &&
                        response.getData().getAttachment().getType().equals(AttachmentChatHelper
                                .INVOICE_ATTACHED)) {
                    AttachInvoiceSentViewModel invoiceSentViewModel = new
                            AttachInvoiceSentViewModel(item);
                    getView().getAdapter().removeLast();
                    getView().getAdapter().addReply(invoiceSentViewModel);
                } else if (response.getData().getAttachment() != null &&
                        response.getData().getAttachment().getType().equals(AttachmentChatHelper
                                .INVOICE_LIST_ATTACHED)) {
                    AttachInvoiceSelectionViewModel invoiceSelectionViewModel =
                            AttachInvoiceMapper.attachmentToAttachInvoiceSelectionModel(item
                                    .getAttachment());
                    getView().getAdapter().removeLast();
                    getView().getAdapter().addReply(invoiceSelectionViewModel);
                } else {
                    getView().getAdapter().removeLast();
                    getView().getAdapter().addReply(item);
                }
                getView().resetReplyColumn();
                getView().scrollToBottom();
            } else if (getView() != null && getView().isCurrentThread(response.getData().getMsgId
                    ())) {
                OppositeChatViewModel item = new OppositeChatViewModel();
                item.setReplyId(response.getData().getMsgId());
                item.setMsgId(response.getData().getMsgId());
                item.setSenderId(String.valueOf(response.getData().getFromUid()));
                item.setMsg(response.getData().getMessage().getCensoredReply());
                item.setReplyTime(response.getData().getMessage().getTimeStampUnix());
                item.setReplyTimeNano(Long.parseLong(response.getData().getMessage()
                        .getTimeStampUnixNano()));
                item.setAttachment(response.getData().getAttachment());
                item.setShowRating(response.getData().isShowRating());
                item.setRatingStatus(response.getData().getRatingStatus());
                if (getView().getAdapter().isTyping()) {
                    getView().getAdapter().removeTyping();
                }
                if (response.getData().getAttachment() != null &&
                        response.getData().getAttachment().getType().equals(AttachmentChatHelper
                                .PRODUCT_ATTACHED)) {
                    AttachProductViewModel productItem = new AttachProductViewModel(item);
                    getView().getAdapter().addReply(productItem);
                } else if (response.getData().getAttachment() != null &&
                        response.getData().getAttachment().getType().equals(AttachmentChatHelper
                                .INVOICE_LIST_ATTACHED)) {
                    AttachInvoiceSelectionViewModel invoices = AttachInvoiceMapper
                            .attachmentToAttachInvoiceSelectionModel(response.getData()
                                    .getAttachment());
                    getView().getAdapter().addReply(invoices);
                } else {
                    getView().getAdapter().addReply(item);
                }
                getView().scrollToBottomWithCheck();
                readMessage(String.valueOf(response.getData().getMsgId()));
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initMessage(String message, String source, String toShopId, String toUserId) {
        if (isValidReply()) {
            getView().addDummyMessage();
            getView().disableAction();
            sendMessageUseCase.execute(SendMessageUseCase.getParam(
                    message,
                    toShopId,
                    toUserId,
                    source
            ), new Subscriber<SendMessageViewModel>() {
                @Override
                public void onCompleted() {

                }

                @Override
                public void onError(Throwable throwable) {
                    getView().onErrorInitMessage(ErrorHandler.getErrorMessage(throwable));
                }

                @Override
                public void onNext(SendMessageViewModel sendMessageViewModel) {
                    if (sendMessageViewModel.isSuccess())
                        getView().onSuccessInitMessage();
                    else
                        getView().onErrorInitMessage("");

                }
            });
        }
    }

    @Override
    public void openCamera() {
        cameraFileLoc = imageUploadHandler.actionCamera2();
    }

    @Override
    public void startUpload(final List<DummyChatViewModel> list, final int network) {
        getView().setUploadingMode(true);
        String userId = SessionHandler.getTempLoginSession(getView().getActivity());
        String deviceId = GCMHandler.getRegistrationId(getView().getActivity());
        final String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));
        attachImageUseCase.execute(AttachImageUseCase.getParam(list, messageId, userId, deviceId),
                new Subscriber<UploadImageDomain>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        getView().setUploadingMode(false);
                        String error = ErrorHandler.getErrorMessage(throwable, getView()
                                .getActivity());
                        if (throwable instanceof MessageErrorException) {
                            error = throwable.getLocalizedMessage();
                        }
                        getView().onErrorUploadImages(error, list.get(0));
                    }

                    @Override
                    public void onNext(UploadImageDomain uploadImageDomain) {
                        if (network == InboxChatConstant.MODE_WEBSOCKET) {
                            sendImage(messageId, uploadImageDomain.getPicSrc());
                        } else if (network == InboxChatConstant.MODE_API) {
                            uploadWithApi(uploadImageDomain.getPicSrc(), list.get(0));
                        }
                    }
                });
    }

    public void setChatRating(final OppositeChatViewModel element, int userId, final int rating) {
        setChatRatingUseCase.execute(
                SetChatRatingUseCase.
                        getParams(element.getMsgId(), userId, element.getReplyTimeNano(), rating),
                new Subscriber<SetChatRatingPojo>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable throwable) {
                        throwable.printStackTrace();
                        getView().onErrorSetRating();
                    }

                    @Override
                    public void onNext(SetChatRatingPojo setChatRatingPojo) {
                        element.setRatingStatus(rating);
                        getView().onSuccessSetRating(element);
                    }
                }
        );
    }

    @Override
    public String getFileLocFromCamera() {
        return cameraFileLoc;
    }

    public void onLoadMore() {
        if (!isRequesting) {
            pagingHandler.nextPage();
            getReply();
        }
    }

    public void getReply() {
        getReply(GET_CHAT_MODE);
    }

    @Override
    public void getReply(int mode) {
        RequestParams requestParam;
        if (mode == GET_CHAT_MODE) {
            requestParam = GetReplyListUseCase.generateParam(
                    getView().getArguments().getString(PARAM_MESSAGE_ID),
                    pagingHandler.getPage());
        } else {
            requestParam = GetReplyListUseCase.generateParamSearch(
                    getView().getArguments().getString(PARAM_MESSAGE_ID));
        }

        isRequesting = true;
        getReplyListUseCase.execute(requestParam, new GetReplySubscriber(getView(), this));
    }

    public void setResult(ChatRoomViewModel replyData) {
        shopIdFromAPI = replyData.getShopId();
        getView().setCanLoadMore(false);
        getView().setHeaderModel(replyData.getNameHeader(), replyData.getImageHeader());
        getView().setHeader();
        if (pagingHandler.getPage() == 1) {
            getView().getAdapter().setList(replyData.getChatList());
            getView().scrollToBottom();
            getView().hideMainLoading();
        } else {
            getView().getAdapter().addList(replyData.getChatList());
        }
        getView().displayReplyField(replyData.getTextAreaReply() == 1);
        getView().setCanLoadMore(replyData.isHasNext());

        if (!replyData.isHasNext() && replyData.isHasTimeMachine()) {
            getView().addTimeMachine();
        }
    }

    public void finishRequest() {
        isRequesting = false;
    }

    private boolean isValidReply() {
        if (getView().getReplyMessage().trim().length() == 0) {
            getView().showSnackbarError(getView().getString(R.string.error_empty_report));
            return false;
        }
        return true;
    }

    public void onRefresh() {
        if (!isRequesting) {
            pagingHandler.resetPage();
            getReply();
        } else {
//            getView().getRefreshHandler().finishRefresh();
        }
    }

    public void sendInvoiceAttachment(String messageId, SelectedInvoice invoice) {
        webSocketUseCase.execute(webSocketUseCase.getParamSendInvoiceAttachment(messageId, invoice));
    }

    public void sendProductAttachment(String messageId, ResultProduct product) {
        webSocketUseCase.execute(webSocketUseCase.getParamSendProductAttachment(messageId, product));
    }

    public void sendReply(String messageId, String reply) {
        webSocketUseCase.execute(webSocketUseCase.getParamSendReply(messageId, reply));
        flagTyping = false;
    }

    public void sendImage(String messageId, String path) {
        getView().setUploadingMode(false);
        webSocketUseCase.execute(webSocketUseCase.getParamSendImage(messageId, path));
        flagTyping = false;

    }

    public void readMessage(String messageId) {
        webSocketUseCase.execute(webSocketUseCase.getReadMessage(messageId));
    }

    public void setIsTyping(String messageId) {
        if (!flagTyping && messageId != null) {
            webSocketUseCase.execute(webSocketUseCase.getParamStartTyping(messageId));
            flagTyping = true;
        }
    }

    public void stopTyping(String messageId){
        if (messageId != null) {
            webSocketUseCase.execute(webSocketUseCase.getParamStopTyping(messageId));
            flagTyping = false;
        }
    }

    @Override
    public void getAttachProductDialog(String shopId, String shopName, String senderRole) {
        String id = "0";
        String shopNameLocal = "";
        if (senderRole.equals(ROLE_SHOP) && !TextUtils.isEmpty(shopId)) {
            id = String.valueOf(shopId);
            shopNameLocal = shopName;
        }
        else if(TextUtils.isEmpty(shopId) && this.shopIdFromAPI != 0){
            id = String.valueOf(this.shopIdFromAPI);
            shopNameLocal = shopName;
        }
        else if (!TextUtils.isEmpty(sessionHandler.getShopID())
                && !sessionHandler.getShopID().equals("0")) {
            id = sessionHandler.getShopID();
            shopNameLocal = sessionHandler.getShopName();
        }

        getView().startAttachProductActivity(id, shopNameLocal, senderRole.equals(ROLE_SHOP));
    }

    @Override
    public void onOpenWebSocket() {
        if (isFirstTime) {
            isFirstTime = false;
            String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));
            readMessage(messageId);
        }
    }

    @Override
    public void closeWebSocket() {
        if(webSocketUseCase!=null) {
            webSocketUseCase.closeConnection();
        }
    }

    public void getTemplate() {
        getTemplateUseCase.execute(GetTemplateUseCase.generateParam(),
                new Subscriber<GetTemplateViewModel>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        getView().setTemplate(null);
                    }

                    @Override
                    public void onNext(GetTemplateViewModel getTemplateViewModel) {
                        if (getTemplateViewModel.isEnabled()) {
                            List<Visitable> temp = getTemplateViewModel.getListTemplate();
                            if (temp == null) temp = new ArrayList<>();
                            if (getView().isAllowedTemplate())
                                temp.add(new TemplateChatModel(false));
                            getView().setTemplate(temp);
                        } else {
                            List<Visitable> temp = new ArrayList<>();
                            if (getView().isAllowedTemplate())
                                temp.add(new TemplateChatModel(false));
                            getView().setTemplate(temp);
                        }
                    }
                });
    }

    public void recreateWebSocket() {
        webSocketUseCase.recreateWebSocket();
    }
}
