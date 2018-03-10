package com.tokopedia.inbox.inboxchat.presenter;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;

import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.PagingHandler;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;
import com.tokopedia.inbox.inboxchat.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.InboxChatConstant;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.domain.usecase.AttachImageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.GetReplyListUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.ReplyMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.SendMessageUseCase;
import com.tokopedia.inbox.inboxchat.domain.usecase.template.GetTemplateUseCase;
import com.tokopedia.inbox.inboxchat.helper.AttachmentChatHelper;
import com.tokopedia.inbox.inboxchat.presenter.subscriber.GetReplySubscriber;
import com.tokopedia.inbox.inboxchat.uploadimage.domain.model.UploadImageDomain;
import com.tokopedia.inbox.inboxchat.util.ImageUploadHandlerChat;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachProductViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.GetTemplateViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.OppositeChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.SendMessageViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.TemplateChatModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import rx.Subscriber;

import static com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel.GET_CHAT_MODE;
import static com.tokopedia.inbox.inboxmessage.InboxMessageConstant.PARAM_MESSAGE_ID;

/**
 * Created by stevenfredian on 9/26/17.
 */

public class ChatRoomPresenter extends BaseDaggerPresenter<ChatRoomContract.View> implements ChatRoomContract.Presenter {

    private static final String ROLE_SHOP = "shop";

    private final GetReplyListUseCase getReplyListUseCase;
    private final ReplyMessageUseCase replyMessageUseCase;
    private final AttachImageUseCase attachImageUseCase;
    private GetTemplateUseCase getTemplateUseCase;
    private SessionHandler sessionHandler;
    public PagingHandler pagingHandler;
    boolean isRequesting;
    private OkHttpClient client;
    private WebSocket ws;
    private String magicString;
    private ChatWebSocketListenerImpl listener;
    private boolean flagTyping;
    private int attempt;
    private boolean isFirstTime;
    private ImageUploadHandlerChat imageUploadHandler;
    private String cameraFileLoc;

    final static String USER = "Pengguna";
    final static String ADMIN = "Administrator";
    final static String OFFICIAL = "Official";
    final static String SELLER = "shop";
    private CountDownTimer countDownTimer;
    private SendMessageUseCase sendMessageUseCase;

    @Inject
    ChatRoomPresenter(GetReplyListUseCase getReplyListUseCase,
                      ReplyMessageUseCase replyMessageUseCase,
                      GetTemplateUseCase getTemplateUseCase,
                      SendMessageUseCase sendMessageUseCase,
                      AttachImageUseCase attachImageUseCase,
                      SessionHandler sessionHandler) {
        this.getReplyListUseCase = getReplyListUseCase;
        this.replyMessageUseCase = replyMessageUseCase;
        this.getTemplateUseCase = getTemplateUseCase;
        this.sendMessageUseCase = sendMessageUseCase;
        this.attachImageUseCase = attachImageUseCase;
        this.sessionHandler = sessionHandler;
    }

    @Override
    public void attachView(ChatRoomContract.View view) {
        super.attachView(view);
        attempt = 0;
        isRequesting = false;
        this.pagingHandler = new PagingHandler();

        client = new OkHttpClient();
        magicString = TkpdBaseURL.CHAT_WEBSOCKET_DOMAIN + TkpdBaseURL.Chat.CHAT_WEBSOCKET +
                "?os_type=1" +
                "&device_id=" + GCMHandler.getRegistrationId(getView().getContext()) +
                "&user_id=" + SessionHandler.getLoginID(getView().getContext());
        listener = new ChatWebSocketListenerImpl(getView().getInterface());
        isFirstTime = true;

        imageUploadHandler = ImageUploadHandlerChat.createInstance(getView().getFragment());

        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                createWebSocket();
            }
        };

        if (getView().needCreateWebSocket()) {
            createWebSocket();
        } else {
            getView().setHeader();
            getView().hideMainLoading();
            getView().setTextAreaReply(true);
            getView().hideNotifier();
        }
        getTemplate();
    }

    @Override
    public void detachView() {
        super.detachView();
        countDownTimer.cancel();
        getReplyListUseCase.unsubscribe();
        getTemplateUseCase.unsubscribe();
        replyMessageUseCase.unsubscribe();
        sendMessageUseCase.unsubscribe();
        attachImageUseCase.unsubscribe();
    }

    public void createWebSocket() {
        Request request = new Request.Builder().url(magicString)
                .header("Origin", TkpdBaseURL.WEB_DOMAIN)
                .build();
        ws = client.newWebSocket(request, listener);
        attempt++;
    }

    public void recreateWebSocket() {
        countDownTimer.start();
    }


    @Override
    public void onGoToDetail(String id, String role) {
        if (role != null && id != null && !role.equals(ADMIN.toLowerCase()) && !role.equals(OFFICIAL.toLowerCase())) {
            if (role.equals(SELLER.toLowerCase())) {
                Intent intent = new Intent(getView().getActivity(), ShopInfoActivity.class);
                Bundle bundle = ShopInfoActivity.createBundle(String.valueOf(id), "");
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                getView().startActivity(intent);
            } else {
                getView().startActivity(
                        PeopleInfoNoDrawerActivity.createInstance(getView().getActivity(), String.valueOf(id))
                );
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
                getView().onErrorUploadImages(ErrorHandler.getErrorMessage(throwable,getView().getActivity()), model);
            }

            @Override
            public void onNext(ReplyActionData data) {
                getView().setUploadingMode(false);
                getView().onSuccessSendAttach(data, model);
            }
        });

    }

    @Override
    public void sendMessageWithApi(){
        if (isValidReply()) {
            getView().addDummyMessage();
            getView().setViewEnabled(false);
            final String reply = (getView().getReplyMessage());
            String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));
            RequestParams params = ReplyMessageUseCase.generateParam(messageId, reply);

            replyMessageUseCase.execute(params, new Subscriber<ReplyActionData>() {
                @Override
                public void onCompleted() {
                    isRequesting = false;
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


    @Override
    public void addMessageChatBalloon(WebSocketResponse response) {
        if (getView().isCurrentThread(response.getData().getMsgId())
                && getView().isMyMessage(response.getData().getFromUid())) {

            MyChatViewModel item = new MyChatViewModel();
            item.setReplyId(response.getData().getMsgId());
            item.setMsgId(response.getData().getMsgId());
            item.setSenderId(String.valueOf(response.getData().getFromUid()));
            item.setMsg(response.getData().getMessage().getCensoredReply());
            item.setReplyTime(response.getData().getMessage().getTimeStampUnix());
            item.setAttachment(response.getData().getAttachment());

            if(response.getData().getAttachment() != null &&
                    response.getData().getAttachment().getType().equals(AttachmentChatHelper.PRODUCT_ATTACHED)){
                AttachProductViewModel productItem = new AttachProductViewModel(item);
                Integer productId = response.getData().getAttachment().getAttributes().getProductId();
                getView().getAdapter().removeLastProductWithId(productId);
                getView().getAdapter().addReply(productItem);
            }
            else {

                getView().getAdapter().removeLast();
                getView().getAdapter().addReply(item);
            }
            getView().finishLoading();
            getView().resetReplyColumn();
            getView().scrollToBottom();
        } else if (getView().isCurrentThread(response.getData().getMsgId())) {
            OppositeChatViewModel item = new OppositeChatViewModel();
            item.setReplyId(response.getData().getMsgId());
            item.setMsgId(response.getData().getMsgId());
            item.setSenderId(String.valueOf(response.getData().getFromUid()));
            item.setMsg(response.getData().getMessage().getCensoredReply());
            item.setReplyTime(response.getData().getMessage().getTimeStampUnix());
            item.setAttachment(response.getData().getAttachment());
            if (getView().getAdapter().isTyping()) {
                getView().getAdapter().removeTyping();
            }
            if(response.getData().getAttachment() != null &&
                    response.getData().getAttachment().getType().equals(AttachmentChatHelper.PRODUCT_ATTACHED)){
                AttachProductViewModel productItem = new AttachProductViewModel(item);
                getView().getAdapter().addReply(productItem);
            }
            else
                getView().getAdapter().addReply(item);
            getView().finishLoading();
            getView().scrollToBottomWithCheck();
            try {
                readMessage(String.valueOf(response.getData().getMsgId()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initMessage(String message, String source, String toShopId, String toUserId) {
        if (isValidReply()) {
            getView().addInitialMessageBalloon();
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
    public void startUpload(final List<MyChatViewModel> list, final int network) {
        getView().setUploadingMode(true);
        String userId = SessionHandler.getTempLoginSession(getView().getActivity());
        String deviceId = GCMHandler.getRegistrationId(getView().getActivity());
        final String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));
        attachImageUseCase.execute(AttachImageUseCase.getParam(list, messageId, userId, deviceId), new Subscriber<UploadImageDomain>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable throwable) {
                getView().setUploadingMode(false);
                String error = ErrorHandler.getErrorMessage(throwable,getView().getActivity());
                if(throwable instanceof MessageErrorException){
                    error = throwable.getLocalizedMessage();
                }
                getView().onErrorUploadImages(error, list.get(0));
            }

            @Override
            public void onNext(UploadImageDomain uploadImageDomain) {
                if(network == InboxChatConstant.MODE_WEBSOCKET){
                    try {
                        sendImage(messageId, uploadImageDomain.getPicSrc());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else if(network == InboxChatConstant.MODE_API) {
                    uploadWithApi(uploadImageDomain.getPicSrc(), list.get(0));
                }
            }
        });
    }

    @Override
    public String getFileLocFromCamera() {
        return cameraFileLoc;
    }

    public void onLoadMore() {
        if (!isRequesting) {
            pagingHandler.nextPage();
            getReply();
        } else {
            getView().finishLoading();
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
        getView().setCanLoadMore(false);
        getView().setHeaderModel(replyData.getNameHeader(),replyData.getImageHeader());
        getView().setHeader();
        if (pagingHandler.getPage() == 1) {
            getView().getAdapter().setList(replyData.getChatList());
            getView().scrollToBottom();
            getView().hideMainLoading();
        } else {
            getView().getAdapter().addList(replyData.getChatList());
        }
        getView().setTextAreaReply(replyData.getTextAreaReply() == 1);
        getView().setCanLoadMore(replyData.isHasNext());

        if (!replyData.isHasNext() && replyData.isHasTimeMachine()) {
            getView().addTimeMachine();
        }
    }

    public void finishRequest() {
        isRequesting = false;
    }

    private boolean isValidReply() {
        boolean isValid = true;
        if (getView().getReplyMessage().trim().length() == 0) {
            isValid = false;
            getView().showError(getView().getString(R.string.error_empty_report));
        }
        return isValid;
    }

    public void sendMessageWithWebsocket() {
        if (isValidReply()) {
            getView().addDummyMessage();
            getView().setViewEnabled(false);

            final String reply = (getView().getReplyMessage());
            String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));

            try {
                sendReply(messageId, reply);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void onRefresh() {
        if (!isRequesting) {
            pagingHandler.resetPage();
            getReply();
        } else {
//            getView().getRefreshHandler().finishRefresh();
        }
    }

    public void sendProductAttachment(String messageId, ResultProduct product) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE);
        JSONObject data = new JSONObject();
        data.put("message_id", Integer.valueOf(messageId));
        data.put("message", product.getProductUrl());
        SimpleDateFormat date = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        data.put("start_time", date.format(Calendar.getInstance().getTime()));
        data.put("attachment_type",3);
        data.put("product_id", product.getProductId());

        JSONObject productProfile = new JSONObject();
        productProfile.put("name",product.getName());
        productProfile.put("price",product.getPrice());
        productProfile.put("image_url",product.getProductImageThumbnail());
        productProfile.put("url",product.getProductUrl());
        data.put("product_profile",productProfile);
        json.put("data", data);
        ws.send(json.toString());
    }

    public void sendReply(String messageId, String reply) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE);
        JSONObject data = new JSONObject();
        data.put("message_id", Integer.valueOf(messageId));
        data.put("message", reply);
        SimpleDateFormat date = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        data.put("start_time", date.format(Calendar.getInstance().getTime()));
        json.put("data", data);
        ws.send(json.toString());
        flagTyping = false;
    }

    public void sendImage(String messageId, String path) throws JSONException {
        getView().setUploadingMode(false);
        JSONObject json = new JSONObject();
        json.put("code", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE);
        JSONObject data = new JSONObject();
        data.put("message_id", Integer.valueOf(messageId));
        data.put("message", InboxChatConstant.UPLOADING);
        SimpleDateFormat date = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        data.put("start_time", date.format(Calendar.getInstance().getTime()));
        data.put("file_path", path);
        data.put("attachment_type", 2);
        json.put("data", data);
        ws.send(json.toString());
        flagTyping = false;

    }

    public void readMessage(String messageId) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("code", ChatWebSocketConstant.EVENT_TOPCHAT_READ_MESSAGE);
        JSONObject data = new JSONObject();
        data.put("msg_id", Integer.valueOf(messageId));
        json.put("data", data);
        ws.send(json.toString());
    }

    public void setIsTyping(String messageId) throws JSONException {
        if (!flagTyping && messageId != null) {
            JSONObject json = new JSONObject();
            json.put("code", ChatWebSocketConstant.EVENT_TOPCHAT_TYPING);
            JSONObject data = new JSONObject();
            data.put("msg_id", Integer.valueOf(messageId));
            json.put("data", data);
            ws.send(json.toString());
            flagTyping = true;
        }
    }

    public void stopTyping(String messageId) throws Exception {
        if (messageId != null) {
            JSONObject json = new JSONObject();
            json.put("code", ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING);
            JSONObject data = new JSONObject();
            data.put("msg_id", Integer.valueOf(messageId));
            json.put("data", data);
            ws.send(json.toString());
            flagTyping = false;
        }
    }

    @Override
    public void getAttachProductDialog(String shopId,String shopName ,String senderRole) {
        String id = "0";
        String shopNameLocal = "";
        if (senderRole.equals(ROLE_SHOP) && !TextUtils.isEmpty(shopId)) {
            id = String.valueOf(shopId);
            shopNameLocal = shopName;
        }
        else if (!TextUtils.isEmpty(sessionHandler.getShopID())
                && !sessionHandler.getShopID().equals("0")) {
            id = sessionHandler.getShopID();
            shopNameLocal = sessionHandler.getShopName();
        }
        getView().startAttachProductActivity(id,shopNameLocal,senderRole.equals(ROLE_SHOP));
    }

    @Override
    public void onOpenWebSocket() {
        if (isFirstTime) {
            isFirstTime = false;
            String messageId = (getView().getArguments().getString(PARAM_MESSAGE_ID));
            try {
                readMessage(messageId);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void closeWebSocket() {
        try {
            client.dispatcher().executorService().shutdown();
            ws.close(1000, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getTemplate() {
        getTemplateUseCase.execute(GetTemplateUseCase.generateParam(), new Subscriber<GetTemplateViewModel>() {
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
                    if (getView().isAllowedTemplate()) temp.add(new TemplateChatModel(false));
                    getView().setTemplate(temp);
                } else {
                    List<Visitable> temp = new ArrayList<>();
                    if (getView().isAllowedTemplate()) temp.add(new TemplateChatModel(false));
                    getView().setTemplate(temp);
                }
            }
        });
    }

}
