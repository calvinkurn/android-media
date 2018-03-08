package com.tokopedia.inbox.inboxchat.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.KeyboardHandler;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.adapter.Visitable;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.bottomsheet.BottomSheetBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.design.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.attachproduct.analytics.AttachProductAnalytics;
import com.tokopedia.inbox.attachproduct.view.activity.AttachProductActivity;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;
import com.tokopedia.inbox.inboxchat.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.InboxChatConstant;
import com.tokopedia.inbox.inboxchat.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.activity.ChatMarketingThumbnailActivity;
import com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity;
import com.tokopedia.inbox.inboxchat.activity.SendMessageActivity;
import com.tokopedia.inbox.inboxchat.activity.TemplateChatActivity;
import com.tokopedia.inbox.inboxchat.activity.TimeMachineActivity;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomAdapter;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.ChatRoomTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.adapter.TemplateChatAdapter;
import com.tokopedia.inbox.inboxchat.adapter.TemplateChatTypeFactory;
import com.tokopedia.inbox.inboxchat.adapter.TemplateChatTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.analytics.TopChatTrackingEventLabel;
import com.tokopedia.inbox.inboxchat.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.domain.model.reply.Attachment;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentAttributes;
import com.tokopedia.inbox.inboxchat.domain.model.reply.AttachmentProductProfile;
import com.tokopedia.inbox.inboxchat.domain.model.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.domain.model.websocket.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.helper.AttachmentChatHelper;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.presenter.ChatRoomPresenter;
import com.tokopedia.inbox.inboxchat.uploadimage.ImageUpload;
import com.tokopedia.inbox.inboxchat.util.Events;
import com.tokopedia.inbox.inboxchat.util.ImageUploadHandlerChat;
import com.tokopedia.inbox.inboxchat.viewholder.ListChatViewHolder;
import com.tokopedia.inbox.inboxchat.viewmodel.AttachProductViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxchat.viewmodel.MyChatViewModel;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;
import rx.functions.Action1;
import rx.functions.Func1;

import static com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity.PARAM_SENDER_ROLE;
import static com.tokopedia.inbox.inboxchat.activity.ChatRoomActivity.PARAM_WEBSOCKET;

/**
 * Created by stevenfredian on 9/19/17.
 */

@RuntimePermissions
public class ChatRoomFragment extends BaseDaggerFragment
        implements ChatRoomContract.View, InboxMessageConstant, InboxChatConstant
        , WebSocketInterface {

    private static final String ENABLE_TOPCHAT = "topchat_template";
    public static final String TAG = "ChatRoomFragment";
    private static final long MILIS_TO_SECOND = 1000;
    @Inject
    ChatRoomPresenter presenter;

    @Inject
    SessionHandler sessionHandler;

    private int networkType;
    private RecyclerView recyclerView;
    private RecyclerView templateRecyclerView;
    private ProgressBar progressBar;
    private View replyView;

    private ImageView avatar;
    private TextView user;
    private TextView onlineDesc;
    private TextView label;

    ChatRoomAdapter adapter;
    TemplateChatAdapter templateAdapter;

    private ChatRoomTypeFactory typeFactory;
    private TemplateChatTypeFactory templateChatFactory;
    private LinearLayoutManager layoutManager;
    private ImageView sendButton;
    private EditText replyColumn;
    private ImageView attachButton;
    private View pickerButton;
    private View maximize;
    private View mainHeader;
    private Toolbar toolbar;
    private Observable<String> replyWatcher;
    private Observable<Boolean> replyIsTyping;
    private int mode;
    private View notifier;
    private LinearLayoutManager templateLayoutManager;
    private String title, avatarImage;

    private RemoteConfig remoteConfig;

    private boolean uploading;

    public static ChatRoomFragment createInstance(Bundle extras) {
        ChatRoomFragment fragment = new ChatRoomFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(PARAM_SENDER_NAME, "");
            avatarImage = getArguments().getString(PARAM_SENDER_IMAGE, "");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initVar();
        toolbar = getActivity().findViewById(R.id.app_bar);
        mainHeader = toolbar.findViewById(R.id.main_header);
        mainHeader.setVisibility(View.INVISIBLE);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = rootView.findViewById(R.id.reply_list);
        templateRecyclerView = rootView.findViewById(R.id.list_template);
        replyView = rootView.findViewById(R.id.add_comment_area);
        sendButton = rootView.findViewById(R.id.send_but);
        replyColumn = rootView.findViewById(R.id.new_comment);
        sendButton.requestFocus();
        attachButton = rootView.findViewById(R.id.add_url);
        pickerButton = rootView.findViewById(R.id.image_picker);
        maximize = rootView.findViewById(R.id.maximize);
        notifier = rootView.findViewById(R.id.notifier);
        replyWatcher = Events.text(replyColumn);
        recyclerView.setHasFixedSize(true);
        templateRecyclerView.setHasFixedSize(true);
        presenter.attachView(this);
        uploading = false;
        prepareView();
        initListener();
        remoteConfig = new FirebaseRemoteConfigImpl(getActivity());
        return rootView;
    }

    private void prepareView() {
        if (getArguments().getBoolean(SendMessageActivity.IS_HAS_ATTACH_BUTTON)) {
            attachButton.setVisibility(View.VISIBLE);
        } else {
            attachButton.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(getArguments().getString(SendMessageActivity.PARAM_CUSTOM_MESSAGE,
                ""))) {
            String customMessage = "\n" + getArguments().getString(SendMessageActivity
                    .PARAM_CUSTOM_MESSAGE, "");
            replyColumn.setText(customMessage);
        }
    }

    public boolean isAllowedTemplate() {
        return (remoteConfig.getBoolean(ENABLE_TOPCHAT));
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onErrorUploadImages(String errorMessage, MyChatViewModel model) {
        showError(errorMessage);
        showRetryFor(model);
    }

    private void showRetryFor(MyChatViewModel model) {
        adapter.showRetryFor(model, true);
    }

    @Override
    public void onRetrySend(final MyChatViewModel attachment) {

        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST);

        bottomSheetBuilder.addItem(RESEND, R.string.resend, null);
        bottomSheetBuilder.addItem(DELETE, R.string.delete, null);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case RESEND:
                                adapter.remove(attachment);
                                ImageUpload model = new ImageUpload();
                                model.setImageId(String.valueOf(System.currentTimeMillis() /
                                        MILIS_TO_SECOND));
                                model.setFileLoc(attachment.getAttachment().getAttributes().getImageUrl());
                                MyChatViewModel temp = addAttachImageBalloonToChatList(model);
                                presenter.startUpload(Collections.singletonList(temp), networkType);
                                break;
                            case DELETE:
                                adapter.remove(attachment);
                                break;
                        }
                    }
                })
                .createDialog();

        bottomSheetDialog.show();
    }

    private void initListener() {

        replyIsTyping = replyWatcher.map(new Func1<String, Boolean>() {
            @Override
            public Boolean call(String s) {
                return s.length() > 0;
            }
        });

        replyIsTyping.subscribe(new Action1<Boolean>() {
            @Override
            public void call(Boolean isNotEmpty) {
                try {
                    if (isNotEmpty) {
                        presenter.setIsTyping(getArguments().getString(ChatRoomActivity
                                .PARAM_MESSAGE_ID));
                        if(needCreateWebSocket()) {
                            maximize.setVisibility(View.VISIBLE);
                        }
                        pickerButton.setVisibility(View.GONE);
                        attachButton.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        replyIsTyping.debounce(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        try {
                            presenter.stopTyping(getArguments().getString(ChatRoomActivity
                                    .PARAM_MESSAGE_ID));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });


        if (needCreateWebSocket()) {
            sendButton.setOnClickListener(getSendWithWebSocketListener());
        } else {
            sendButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UnifyTracking.eventSendMessagePage();
                    presenter.initMessage(replyColumn.getText().toString(),
                            getArguments().getString(ChatRoomActivity.PARAM_SOURCE),
                            getArguments().getString(ChatRoomActivity.PARAM_SENDER_ID),
                            getArguments().getString(ChatRoomActivity.PARAM_USER_ID));
                }
            });
        }

        setPickerButton();

        maximize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                maximize.setVisibility(View.GONE);
                setPickerButton();
            }
        });

        pickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replyColumn.clearFocus();

                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
                myAlertDialog.setMessage(getActivity().getString(R.string.dialog_upload_option));
                myAlertDialog.setPositiveButton(getActivity().getString(R.string.title_gallery), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChatRoomFragmentPermissionsDispatcher.actionImagePickerWithCheck(ChatRoomFragment.this);
                    }
                });
                myAlertDialog.setNegativeButton(getActivity().getString(R.string.title_camera), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ChatRoomFragmentPermissionsDispatcher.actionCameraWithCheck(ChatRoomFragment.this);
                    }
                });
                Dialog dialog = myAlertDialog.create();
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.show();
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventInsertAttachment(TopChatTrackingEventLabel.Category.CHAT_DETAIL,
                        TopChatTrackingEventLabel.Action.CHAT_DETAIL_INSERT,
                        TopChatTrackingEventLabel.Name.CHAT_DETAIL);
                presenter.getAttachProductDialog(
                        getArguments().getString(ChatRoomActivity
                                .PARAM_SENDER_ID, ""),
                        getArguments().getString(ChatRoomActivity.PARAM_SENDER_NAME,""),
                        getArguments().getString(PARAM_SENDER_ROLE, "")
                );
            }
        });
    }

    @Override
    public void startAttachProductActivity(String shopId, String shopName, boolean isSeller) {
        Intent intent = AttachProductActivity.createInstance(getActivity(),shopId,shopName,isSeller);
        startActivityForResult(intent,AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE);
    }

    private void setPickerButton() {
        if (needCreateWebSocket()) {
            pickerButton.setVisibility(View.VISIBLE);
            attachButton.setVisibility(View.VISIBLE);
        }else{
            pickerButton.setVisibility(View.GONE);
            attachButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void addTemplateString(String message) {
        String labelCategory = TopChatTrackingEventLabel.Category.INBOX_CHAT;
        if (!getArguments().getBoolean(PARAM_WEBSOCKET)) {
            if (getArguments().getString(PARAM_SENDER_TAG).equals(ChatRoomActivity.ROLE_SELLER)) {
                labelCategory = TopChatTrackingEventLabel.Category.SHOP_PAGE;
            }
            if (getArguments().getString(SendMessageActivity.PARAM_CUSTOM_MESSAGE, "").length() > 0) {
                labelCategory = TopChatTrackingEventLabel.Category.PRODUCT_PAGE;
            }
        }

        UnifyTracking.eventClickTemplate(labelCategory,
                TopChatTrackingEventLabel.Action.TEMPLATE_CHAT_CLICK,
                TopChatTrackingEventLabel.Name.INBOX_CHAT);
        String text = replyColumn.getText().toString();
        int index = replyColumn.getSelectionStart();
        replyColumn.setText(String.format("%s %s %s", text.substring(0, index), message, text.substring(index)));
        replyColumn.setSelection(message.length() + text.substring(0, index).length() + 1);
    }

    @Override
    public void goToSettingTemplate() {
        Intent intent = TemplateChatActivity.createInstance(getActivity());
        startActivityForResult(intent, 100);
        getActivity().overridePendingTransition(R.anim.pull_up, android.R.anim.fade_out);
    }

    @Override
    public void onGoToGallery(Attachment attachment, String fullTime) {

        ArrayList<String> strings = new ArrayList<>();
        strings.add(attachment.getAttributes().getImageUrl());

        ((PdpRouter) getActivity().getApplication()).openImagePreviewFromChat(getActivity(), strings, new ArrayList<String>(), title, fullTime);
    }

    @Override
    public void onGoToWebView(String url, String id) {
        UnifyTracking.eventClickThumbnailMarketing(TopChatTrackingEventLabel.Category.INBOX_CHAT,
                TopChatTrackingEventLabel.Action.CLICK_THUMBNAIL,
                TopChatTrackingEventLabel.Name.INBOX_CHAT,
                id
        );
        KeyboardHandler.DropKeyboard(getActivity(), getView());
        startActivity(ChatMarketingThumbnailActivity.getCallingIntent(getActivity(), url));
    }

    @Override
    public boolean needCreateWebSocket() {
        return getArguments().getBoolean(PARAM_WEBSOCKET);
    }

    @Override
    public void hideNotifier() {
        notifier.setVisibility(View.GONE);
    }

    @Override
    public void onSuccessInitMessage() {
        CommonUtils.UniversalToast(getActivity(), getString(R.string.success_send_msg));
        getActivity().finish();
    }


    @Override
    public void onErrorInitMessage(String s) {
        adapter.removeLast();
        NetworkErrorHelper.showSnackbar(getActivity(), s);
        sendButton.setEnabled(true);
    }

    private void initVar() {
        networkType = MODE_API;
        typeFactory = new ChatRoomTypeFactoryImpl(this);
        templateChatFactory = new TemplateChatTypeFactoryImpl(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar.setVisibility(View.VISIBLE);

        adapter = new ChatRoomAdapter(typeFactory);
        adapter.setNav(getArguments().getString(PARAM_NAV, ""));
        mode = getArguments().getInt(PARAM_MODE, InboxChatViewModel.GET_CHAT_MODE);

        templateAdapter = new TemplateChatAdapter(templateChatFactory);

        if (needCreateWebSocket()) {
            presenter.getReply(mode);
        } else {
            progressBar.setVisibility(View.GONE);
        }

        adapter.notifyDataSetChanged();
        templateAdapter.notifyDataSetChanged();

        recyclerView.setAdapter(adapter);
        templateRecyclerView.setAdapter(templateAdapter);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        templateLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        templateRecyclerView.setLayoutManager(templateLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int index = layoutManager.findFirstCompletelyVisibleItemPosition();
                if (adapter.checkLoadMore(index)) {
                    presenter.onLoadMore();
                }
            }
        });

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KeyboardHandler.hideSoftKeyboard(getActivity());
            }
        });

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = recyclerView.getRootView().getHeight() - recyclerView.getHeight();
                if (heightDiff > dpToPx(getActivity(), 200)) { // if more than 200 dp, it's probably a keyboard...
//                    recyclerView.smoothScrollToPosition(0);
                }
            }
        });
    }

    public static float dpToPx(Context context, float valueInDp) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
    }


    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        AppComponent appComponent = getComponent(AppComponent.class);
        DaggerInboxChatComponent daggerInboxChatComponent =
                (DaggerInboxChatComponent) DaggerInboxChatComponent.builder()
                        .appComponent(appComponent).build();
        daggerInboxChatComponent.inject(this);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void finishLoading() {
    }

    @Override
    public void setHeader() {

        if (toolbar != null) {
            mainHeader.setVisibility(View.VISIBLE);
            avatar = toolbar.findViewById(R.id.user_avatar);
            user = toolbar.findViewById(R.id.title);
            onlineDesc = toolbar.findViewById(R.id.subtitle);
            label = toolbar.findViewById(R.id.label);
            ImageHandler.loadImageCircle2(getActivity(), avatar, avatarImage, R.drawable
                    .ic_image_avatar_boy);

            user.setText(title);
            if (!TextUtils.isEmpty(getArguments().getString(PARAM_SENDER_TAG, ""))
                    && !getArguments().getString(PARAM_SENDER_TAG, "").equals(ListChatViewHolder
                    .USER)) {
                label.setText(getArguments().getString(PARAM_SENDER_TAG));
                label.setVisibility(View.VISIBLE);
            } else {
                label.setVisibility(View.GONE);
            }
            setOnlineDesc(getActivity().getString(R.string.just_now));
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onGoToDetail(getArguments().getString(PARAM_SENDER_ID), getArguments().getString(PARAM_SENDER_ROLE));
                }
            });
        }
    }

    @Override
    public void setTextAreaReply(boolean textAreaReply) {
        if (textAreaReply) {
            replyView.setVisibility(View.VISIBLE);
        } else {
            replyView.setVisibility(View.GONE);
        }
    }

    @Override
    public ChatRoomAdapter getAdapter() {
        return adapter;
    }

    public void setOnlineDesc(final String when) {
        if (getActivity() != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (onlineDesc != null)
                        onlineDesc.setText(when);
                }
            });
        }
    }

    @Override
    public WebSocketInterface getInterface() {
        return this;
    }

    @Override
    public void onGoToTimeMachine(String url) {
        startActivity(TimeMachineActivity.getCallingIntent(getActivity(), url));
    }

    @Override
    public void addTimeMachine() {
        adapter.showTimeMachine();
    }

    @Override
    public String getKeyword() {
        return getArguments().getString(PARAM_KEYWORD);
    }

    @Override
    public void setResult(final ChatRoomViewModel model) {
        if (getActivity() != null
                && presenter != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    presenter.setResult(model);
                }
            });
        }
    }

    @Override
    public void notifyConnectionWebSocket() {
        if (getActivity() != null && presenter != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    notifier.setVisibility(View.VISIBLE);
                    TextView title = notifier.findViewById(R.id.title);
                    View action = notifier.findViewById(R.id.action);
                    title.setText(R.string.error_no_connection_retrying);
                    action.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void scrollToBottom() {
        recyclerView.scrollToPosition(adapter.getItemCount());
    }

    @Override
    public String getReplyMessage() {
        return replyColumn.getText().toString();
    }

    @Override
    public void showError(String error) {
        if (adapter.getItemCount() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new NetworkErrorHelper.RetryClickedListener() {
                @Override
                public void onRetryClicked() {
                    mode = getArguments().getInt(PARAM_MODE, InboxChatViewModel.GET_CHAT_MODE);
                    presenter.getReply(mode);
                }
            });
        } else {
            if (error.equals("")) {
                NetworkErrorHelper.showSnackbar(getActivity());
            } else {
                NetworkErrorHelper.showSnackbar(getActivity(), error);

            }
        }
    }

    public void onSuccessSendReply(final WebSocketResponse response) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setViewEnabled(true);
                    presenter.addMessageChatBalloon(response);
                    setResult();
                }
            });
        }
    }

    @Override
    public void onSuccessSendReply(ReplyActionData replyData, String reply) {
        adapter.removeLast();
        addView(replyData, reply);
    }


    @Override
    public void onSuccessSendAttach(ReplyActionData data, MyChatViewModel model) {
        adapter.remove(model);
        addView(data, UPLOADING);
    }

    @Override
    public void setUploadingMode(boolean mode) {
        uploading = mode;
    }

    @Override
    public void scrollToBottomWithCheck() {
        int index = layoutManager.findLastCompletelyVisibleItemPosition();
        if (Math.abs(index - adapter.getList().size()) < 3) {
            recyclerView.scrollToPosition(adapter.getItemCount() - 1);
        }
    }

    @Override
    public void setHeaderModel(String nameHeader, String imageHeader) {
        this.avatarImage = imageHeader;
        this.title = nameHeader;
    }

    private void addView(ReplyActionData replyData, String reply) {
        setViewEnabled(true);
        MyChatViewModel item = new MyChatViewModel();
        item.setReplyId(replyData.getChat().getMsgId());
        item.setMsgId(replyData.getChat().getMsgId());
        item.setSenderId(replyData.getChat().getSenderId());
        item.setSenderName(replyData.getChat().getFrom());
        item.setMsg(replyData.getChat().getMsg());
        item.setReplyTime(replyData.getChat().getReplyTime());
        item.setAttachment(replyData.getChat().getAttachment());
        item.setAttachmentId(Integer.parseInt(replyData.getChat().getAttachment().getId()));
        adapter.addReply(item);
        finishLoading();
        replyColumn.setText("");
        setResult();
    }

    @Override
    public void onErrorSendReply() {
        adapter.removeLast();
        setViewEnabled(true);
        finishLoading();
        replyColumn.setText("");
        showError(getActivity().getString(R.string.delete_error).concat("\n").concat(getString(R.string.string_general_error)));
    }

    @Override
    public void resetReplyColumn() {
        replyColumn.setText("");
    }

    @Override
    public boolean isMyMessage(int fromUid) {
        return String.valueOf(fromUid).equals(SessionHandler.getLoginID(MainApplication.getAppContext()));
    }

    @Override
    public void setTemplate(List<Visitable> listTemplate) {
        if (listTemplate == null) {
            templateRecyclerView.setVisibility(View.GONE);
        } else {
            templateRecyclerView.setVisibility(View.VISIBLE);
            templateAdapter.setList(listTemplate);
        }
    }


    @Override
    public boolean isCurrentThread(int msgId) {
        return getArguments().getString(PARAM_MESSAGE_ID).equals(String.valueOf(msgId));
    }

    @Override
    public void setViewEnabled(boolean isEnabled) {

    }

    @Override
    public void addDummyMessage() {
        MyChatViewModel item = new MyChatViewModel();
        item.setMsg(getReplyMessage());
        item.setMsgId(Integer.parseInt(getArguments().getString(InboxMessageConstant.PARAM_MESSAGE_ID)));
        item.setReplyTime(MyChatViewModel.SENDING_TEXT);
        item.setDummy(true);
        item.setSenderId(getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID));
        adapter.addReply(item);
        recyclerView.scrollToPosition(adapter.getList().size() - 1);
    }


    @Override
    public void addInitialMessageBalloon() {
        MyChatViewModel item = new MyChatViewModel();
        item.setMsg(getReplyMessage());
        item.setReplyTime(MyChatViewModel.SENDING_TEXT);
        item.setDummy(true);
        item.setSenderId(getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID));
        adapter.addReply(item);
        scrollToBottom();
    }

    private MyChatViewModel addAttachImageBalloonToChatList(ImageUpload imageUpload) {
        MyChatViewModel item = new MyChatViewModel();
        Attachment attachment = new Attachment();
        attachment.setType(AttachmentChatHelper.IMAGE_ATTACHED);
        AttachmentAttributes attachmentAttributes = new AttachmentAttributes();
        attachmentAttributes.setImageUrl(imageUpload.getFileLoc());
        attachment.setId(imageUpload.getImageId());
        attachment.setAttributes(attachmentAttributes);
        item.setAttachment(attachment);
        item.setReplyTime(MyChatViewModel.SENDING_TEXT);
        item.setDummy(true);
        item.setSenderId(getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID));
        adapter.addReply(item);
        recyclerView.scrollToPosition(adapter.getList().size() - 1);

        return item;
    }

    @Override
    public void disableAction() {
        sendButton.setEnabled(false);
    }


    private void setResult() {
        if (adapter != null && getActivity() != null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            if (adapter.getLastItem() != null)
                bundle.putParcelable(PARCEL, adapter.getLastItem());
            intent.putExtras(bundle);
            getActivity().setResult(Activity.RESULT_OK, intent);
        }
    }

    @Override
    public void setCanLoadMore(boolean hasNext) {
        if (hasNext) {
            adapter.showLoading();
        } else {
            adapter.removeLoading();
        }
    }

    public void restackList(Bundle data) {

        if (toolbar != null) {
            mainHeader.setVisibility(View.VISIBLE);
            avatar = toolbar.findViewById(R.id.user_avatar);
            user = toolbar.findViewById(R.id.title);
            onlineDesc = toolbar.findViewById(R.id.subtitle);
            label = toolbar.findViewById(R.id.label);
            ImageHandler.loadImageCircle2(getActivity(), avatar, null, R.drawable.ic_image_avatar_boy);
            user.setText(getArguments().getString(PARAM_SENDER_NAME));
            label.setText(getArguments().getString(PARAM_SENDER_TAG));
            setOnlineDesc(getActivity().getString(R.string.just_now));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }


    @Override
    public void hideMainLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onIncomingEvent(WebSocketResponse response) {
        switch (response.getCode()) {
            case ChatWebSocketConstant.EVENT_TOPCHAT_TYPING:
                if (String.valueOf(response.getData().getMsgId()).equals(getArguments().getString
                        (PARAM_MESSAGE_ID))) {
                    setOnlineDesc(getString(R.string.is_typing));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.showTyping();
                            if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getList().size() - 2) {
                                layoutManager.scrollToPosition(adapter.getList().size() - 1);
                            }
                        }
                    });

                }
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING:
                if (String.valueOf(response.getData().getMsgId()).equals(getArguments().getString
                        (PARAM_MESSAGE_ID))) {
                    setOnlineDesc(getActivity().getString(R.string.just_now));
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (adapter.isTyping()) {
                                adapter.removeTyping();
                                if (layoutManager.findLastCompletelyVisibleItemPosition() == adapter.getList().size() - 2) {
                                    layoutManager.scrollToPosition(adapter.getList().size());
                                }
                            }
                        }
                    });
                }
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_READ_MESSAGE:
                adapter.setReadStatus(response);
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE:
                onSuccessSendReply(response);
                break;
            default:
                break;
        }

    }

    @Override
    public void onErrorWebSocket() {
        if (getActivity() != null && presenter != null) {
            networkType = MODE_API;
            sendButton.setOnClickListener(getSendWithApiListener());
            notifyConnectionWebSocket();
            presenter.recreateWebSocket();
        }
    }

    @Override
    public void onOpenWebSocket() {
        if (getActivity() != null && presenter != null) {

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView title = notifier.findViewById(R.id.title);
                    title.setText(R.string.connected_websocket);
                    View action = notifier.findViewById(R.id.action);
                    action.setVisibility(View.GONE);
                    networkType = MODE_WEBSOCKET;
                    sendButton.setOnClickListener(getSendWithWebSocketListener());

                }
            });
            notifier.postDelayed(new Runnable() {
                @Override
                public void run() {
                    notifier.setVisibility(View.GONE);
                }
            }, 1500);
            presenter.onOpenWebSocket();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            presenter.stopTyping(getArguments().getString(ChatRoomActivity
                    .PARAM_MESSAGE_ID));
        } catch (Exception e) {
            e.printStackTrace();
        }
        presenter.closeWebSocket();
    }

    public View.OnClickListener getSendWithApiListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                presenter.sendMessageWithApi();
                UnifyTracking.sendChat(TopChatTrackingEventLabel.Category.CHAT_DETAIL,
                        TopChatTrackingEventLabel.Action.CHAT_DETAIL_SEND,
                        TopChatTrackingEventLabel.Name.CHAT_DETAIL);
            }
        };
    }

    public View.OnClickListener getSendWithWebSocketListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                presenter.sendMessageWithWebsocket();
                UnifyTracking.sendChat(TopChatTrackingEventLabel.Category.CHAT_DETAIL,
                        TopChatTrackingEventLabel.Action.CHAT_DETAIL_SEND,
                        TopChatTrackingEventLabel.Name.CHAT_DETAIL);
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 100:
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> strings = data.getStringArrayListExtra("string");
                    templateAdapter.update(strings);
                    break;
                }
                break;
            case ImageUploadHandlerChat.REQUEST_CODE:
                if (resultCode == Activity.RESULT_OK) {
                    String fileLoc = presenter.getFileLocFromCamera();
                    ImageUpload model = new ImageUpload();
                    model.setImageId(String.valueOf(System.currentTimeMillis() / 1000));
                    model.setFileLoc(fileLoc);
                    MyChatViewModel temp = addAttachImageBalloonToChatList(model);
                    presenter.startUpload(Collections.singletonList(temp), networkType);
                }
                break;

            case ImageGallery.TOKOPEDIA_GALLERY:
                if (data == null) {
                    break;
                }
                String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
                List<MyChatViewModel> list = new ArrayList<>();

                if (!TextUtils.isEmpty(imageUrl)) {
                    ImageUpload model = new ImageUpload();
                    model.setImageId(String.valueOf(System.currentTimeMillis() / 1000));
                    model.setFileLoc(imageUrl);
                    MyChatViewModel temp = addAttachImageBalloonToChatList(model);
                    list.add(temp);
                } else {
                    ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryActivity.IMAGE_URLS);
                    if (imageUrls != null) {
                        for (int i = 0; i < imageUrls.size(); i++) {
                            ImageUpload model = new ImageUpload();
                            model.setImageId(String.valueOf(System.currentTimeMillis() / 1000));
                            model.setFileLoc(imageUrls.get(i));
                            MyChatViewModel temp = addAttachImageBalloonToChatList(model);
                            list.add(temp);
                        }
                    }
                }

                presenter.startUpload(list, networkType);
                break;
            case AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE:
                if(data==null)
                    break;
                if(!data.hasExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY))
                    break;
                ArrayList<ResultProduct> resultProducts = data.getParcelableArrayListExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY);
                attachProductRetrieved(resultProducts);
                break;
            default:
                break;
        }
    }

    public void attachProductRetrieved(ArrayList<ResultProduct> resultProducts){
        UnifyTracking.eventSendAttachment(TopChatTrackingEventLabel.Category.CHAT_DETAIL,
                TopChatTrackingEventLabel.Action.CHAT_DETAIL_ATTACHMENT,
                TopChatTrackingEventLabel.Name.CHAT_DETAIL);

        String msgId = getArguments().getString(PARAM_MESSAGE_ID);
        for(ResultProduct result: resultProducts){
            try {
                addProductChatBalloonToChatList(result);
                presenter.sendProductAttachment(msgId,result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void addProductChatBalloonToChatList(ResultProduct product){
        AttachProductViewModel item = new AttachProductViewModel(true);
        Attachment attachment = new Attachment();
        attachment.setType(AttachmentChatHelper.PRODUCT_ATTACHED);
        AttachmentAttributes attachmentAttributes = new AttachmentAttributes();
        attachmentAttributes.setProductId(product.getProductId());
        AttachmentProductProfile productProfile = new AttachmentProductProfile();
        productProfile.setImageUrl(product.getProductImageThumbnail());
        productProfile.setName(product.getName());
        productProfile.setPrice(product.getPrice());
        productProfile.setUrl(product.getProductUrl());
        attachmentAttributes.setProductProfile(productProfile);
        attachment.setAttributes(attachmentAttributes);
        attachment.setId(product.getProductId().toString());
        item.setAttachment(attachment);
        item.setReplyTime(MyChatViewModel.SENDING_TEXT);
        item.setDummy(true);
        item.setMsg("");
        item.setSenderId(getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID));
        adapter.addReply(item);
        recyclerView.scrollToPosition(adapter.getList().size()-1);
    }

    public void onBackPressed(){
        if(uploading){
            showDialogConfirmToAbortUpload();
        } else {
            ((ChatRoomActivity) getActivity()).destroy();
        }
    }

    private void showDialogConfirmToAbortUpload() {
        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle(getActivity().getString(R.string.exit_chat_title));
        myAlertDialog.setMessage(getActivity().getString(R.string.exit_chat_body));
        myAlertDialog.setPositiveButton(getActivity().getString(R.string.exit_chat_yes), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ((ChatRoomActivity) getActivity()).destroy();
            }
        });
        myAlertDialog.setNegativeButton(getActivity().getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionCamera() {
        presenter.openCamera();
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                .getGalleryIntent(getActivity(), false, 1, true);
        startActivityForResult(intent, com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ChatRoomFragmentPermissionsDispatcher.onRequestPermissionsResult(
                ChatRoomFragment.this, requestCode, grantResults);
    }


    @OnShowRationale({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showRationaleForStorageAndCamera(final PermissionRequest request) {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onShowRationale(getActivity(), request, listPermission);
    }

    @OnShowRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showRationaleForStorage(final PermissionRequest request) {
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.CAMERA);
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.CAMERA);
    }

    @OnPermissionDenied(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showDeniedForStorage() {
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showDeniedForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onPermissionDenied(getActivity(), listPermission);
    }

    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE})
    void showNeverAskForStorageAndCamera() {
        List<String> listPermission = new ArrayList<>();
        listPermission.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        listPermission.add(Manifest.permission.CAMERA);

        RequestPermissionUtil.onNeverAskAgain(getActivity(), listPermission);
    }

    @Override
    public void productClicked(Integer productId, String productName, String productPrice, Long dateTimeReply) {
        trackProductClicked();
        TkpdInboxRouter router = (TkpdInboxRouter) MainApplication.getAppContext();
        Intent intent = router.getProductDetailIntent(getContext(),productId,productName,productPrice,dateTimeReply);
        startActivity(intent);
    }

    private void trackProductClicked(){
        if((getActivity().getApplicationContext() instanceof AbstractionRouter)){
            AbstractionRouter abstractionRouter = (AbstractionRouter)getActivity().getApplicationContext();
            abstractionRouter.getAnalyticTracker().sendEventTracking(
                    AttachProductAnalytics.getEventClickChatAttachedProductImage().getEvent()
            );
        }
    }
}
