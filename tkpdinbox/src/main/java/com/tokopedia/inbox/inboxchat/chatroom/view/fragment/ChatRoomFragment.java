package com.tokopedia.inbox.inboxchat.chatroom.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
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
import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.ImageGallery;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.newgallery.GalleryActivity;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.productdetail.PdpRouter;
import com.tokopedia.core.router.productdetail.passdata.ProductPass;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.RequestPermissionUtil;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.design.bottomsheet.BottomSheetBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.design.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.attachinvoice.view.activity.AttachInvoiceActivity;
import com.tokopedia.inbox.attachinvoice.view.resultmodel.SelectedInvoice;
import com.tokopedia.inbox.attachproduct.analytics.AttachProductAnalytics;
import com.tokopedia.inbox.attachproduct.view.activity.AttachProductActivity;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;
import com.tokopedia.inbox.contactus.ContactUsConstant;
import com.tokopedia.inbox.inboxchat.chatlist.activity.SendMessageActivity;
import com.tokopedia.inbox.inboxchat.chatlist.adapter.viewholder.chatlist.ListChatViewHolder;
import com.tokopedia.inbox.inboxchat.chatlist.viewmodel.InboxChatViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.data.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.reply.Attachment;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.reply.WebSocketResponse;
import com.tokopedia.inbox.inboxchat.chatroom.domain.pojo.replyaction.ReplyActionData;
import com.tokopedia.inbox.inboxchat.chatroom.view.activity.ChatMarketingThumbnailActivity;
import com.tokopedia.inbox.inboxchat.chatroom.view.activity.ChatRoomActivity;
import com.tokopedia.inbox.inboxchat.chatroom.view.activity.TimeMachineActivity;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.ChatRoomAdapter;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.ChatRoomTypeFactory;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.ChatRoomTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.chatroom.view.adapter.QuickReplyAdapter;
import com.tokopedia.inbox.inboxchat.chatroom.view.listener.ChatRoomContract;
import com.tokopedia.inbox.inboxchat.chatroom.view.presenter.ChatRoomPresenter;
import com.tokopedia.inbox.inboxchat.chatroom.view.presenter.WebSocketInterface;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.BaseChatViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.ChatRoomViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.SendableViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.invoiceattachment.AttachInvoiceSentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.message.MessageViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.productattachment.ProductAttachmentViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.quickreply.QuickReplyListViewModel;
import com.tokopedia.inbox.inboxchat.chatroom.view.viewmodel.quickreply.QuickReplyViewModel;
import com.tokopedia.inbox.inboxchat.chattemplate.view.activity.TemplateChatActivity;
import com.tokopedia.inbox.inboxchat.chattemplate.view.adapter.TemplateChatAdapter;
import com.tokopedia.inbox.inboxchat.chattemplate.view.adapter.TemplateChatTypeFactory;
import com.tokopedia.inbox.inboxchat.chattemplate.view.adapter.TemplateChatTypeFactoryImpl;
import com.tokopedia.inbox.inboxchat.common.InboxChatConstant;
import com.tokopedia.inbox.inboxchat.common.analytics.TopChatAnalytics;
import com.tokopedia.inbox.inboxchat.common.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.common.util.Events;
import com.tokopedia.inbox.inboxchat.common.util.ImageUploadHandlerChat;
import com.tokopedia.inbox.inboxchat.viewmodel.chatroom.ChatRatingViewModel;
import com.tokopedia.inbox.inboxmessage.InboxMessageConstant;

import java.util.ArrayList;
import java.util.Arrays;
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

import static com.tokopedia.inbox.inboxchat.chatroom.view.activity.ChatRoomActivity.PARAM_SENDER_ROLE;
import static com.tokopedia.inbox.inboxchat.chatroom.view.activity.ChatRoomActivity.PARAM_WEBSOCKET;

/**
 * Created by stevenfredian on 9/19/17.
 */

@RuntimePermissions
public class ChatRoomFragment extends BaseDaggerFragment
        implements ChatRoomContract.View, InboxMessageConstant, InboxChatConstant, WebSocketInterface {
    private static final String CONTACT_US_PATH_SEGMENT = "toped-contact-us";
    private static final String BASE_DOMAIN_SHORTENED = "tkp.me";
    private static final String APPLINK_SCHEME = "tokopedia";
    private static final String CONTACT_US_URL_BASE_DOMAIN = TkpdBaseURL.BASE_CONTACT_US;
    private static final String ROLE_SHOP = "shop";
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
    private RecyclerView rvQuickReply;
    private ProgressBar progressBar;
    private View replyView;

    private ImageView avatar;
    private TextView user;
    private TextView onlineDesc;
    private TextView label;

    ChatRoomAdapter adapter;
    TemplateChatAdapter templateAdapter;
    QuickReplyAdapter quickReplyAdapter;

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
    private String title, avatarImage;

    private RemoteConfig remoteConfig;
    private boolean uploading;
    private boolean isChatBot;

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
            boolean isChatBotArguments = getArguments().getString(TkpdInboxRouter.IS_CHAT_BOT,
                    "false").equals("true");
            isChatBot = (getArguments().getBoolean(TkpdInboxRouter.IS_CHAT_BOT, false) ||
                    isChatBotArguments);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_room, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        initVar();
        toolbar = getActivity().findViewById(R.id.app_bar);
        mainHeader = toolbar.findViewById(R.id.main_header);
        mainHeader.setVisibility(View.INVISIBLE);
        progressBar = rootView.findViewById(R.id.progress);
        recyclerView = rootView.findViewById(R.id.reply_list);
        templateRecyclerView = rootView.findViewById(R.id.list_template);
        rvQuickReply = rootView.findViewById(R.id.list_quick_reply);
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
        if (getArguments().getBoolean(SendMessageActivity.IS_HAS_ATTACH_BUTTON) && !isChatBot) {
            attachButton.setVisibility(View.VISIBLE);
        } else {
            attachButton.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(getArguments().getString(SendMessageActivity.PARAM_CUSTOM_MESSAGE,
                ""))) {
            String customMessage = "\n" + getArguments().getString(SendMessageActivity
                    .PARAM_CUSTOM_MESSAGE, "");
            replyColumn.setText(customMessage
            );
        }

        if (isChatBot) {
            attachButton.setVisibility(View.GONE);
        }
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    private void showRetryFor(ImageUploadViewModel model) {
        adapter.showRetryFor(model, true);
    }

    @Override
    public void onRetrySendImage(final ImageUploadViewModel element) {
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
                                adapter.remove(element);
                                String fileLoc = element.getImageUrl();
                                ImageUploadViewModel temp = generateChatViewModelWithImage(fileLoc);
                                presenter.startUpload(Collections.singletonList(temp), networkType);
                                adapter.addReply(temp);
                                break;
                            case DELETE:
                                adapter.remove(element);
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
                if (isNotEmpty) {
                    presenter.setIsTyping(getArguments().getString(ChatRoomActivity
                            .PARAM_MESSAGE_ID));
                    if (needCreateWebSocket()) {
                        maximize.setVisibility(isChatBot ? View.GONE : View.VISIBLE);
                    }
                    pickerButton.setVisibility(isChatBot ? View.VISIBLE : View.GONE);
                    attachButton.setVisibility(View.GONE);
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
            sendButton.setOnClickListener(generateSendClickListener());
        } else {
            sendButton.setOnClickListener(getSendInitMessage());
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

                UnifyTracking.eventAttachment(TopChatAnalytics.Category.CHAT_DETAIL,
                        TopChatAnalytics.Action.CHAT_DETAIL_ATTACH,
                        TopChatAnalytics.Name.CHAT_DETAIL);

                AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
                myAlertDialog.setMessage(getActivity().getString(R.string.dialog_upload_option));
                myAlertDialog.setPositiveButton(getActivity().getString(R.string.title_gallery),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ChatRoomFragmentPermissionsDispatcher.actionImagePickerWithCheck
                                        (ChatRoomFragment.this);
                            }
                        });
                myAlertDialog.setNegativeButton(getActivity().getString(R.string.title_camera),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ChatRoomFragmentPermissionsDispatcher.actionCameraWithCheck
                                        (ChatRoomFragment.this);
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
                UnifyTracking.eventInsertAttachment(TopChatAnalytics.Category.CHAT_DETAIL,
                        TopChatAnalytics.Action.CHAT_DETAIL_INSERT,
                        TopChatAnalytics.Name.CHAT_DETAIL);
                presenter.getAttachProductDialog(
                        getArguments().getString(ChatRoomActivity
                                .PARAM_SENDER_ID, ""),
                        getArguments().getString(ChatRoomActivity.PARAM_SENDER_NAME, ""),
                        getArguments().getString(PARAM_SENDER_ROLE, "")
                );
            }
        });
    }

    private View.OnClickListener generateSendClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scrollToBottom();
                rvQuickReply.setVisibility(View.GONE);
                if (templateAdapter != null && templateAdapter.getList().size() != 0) {
                    templateRecyclerView.setVisibility(View.VISIBLE);
                }
                presenter.sendMessage(networkType);
                UnifyTracking.sendChat(TopChatAnalytics.Category.CHAT_DETAIL,
                        TopChatAnalytics.Action.CHAT_DETAIL_SEND,
                        TopChatAnalytics.Name.CHAT_DETAIL);


            }
        };
    }

    @Override
    public void startAttachProductActivity(String shopId, String shopName, boolean isSeller) {
        Intent intent = AttachProductActivity.createInstance(getActivity(), shopId, shopName,
                isSeller);
        startActivityForResult(intent, AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE);
    }

    private void setPickerButton() {
        if (needCreateWebSocket()) {
            pickerButton.setVisibility(View.VISIBLE);
            attachButton.setVisibility(isChatBot ? View.GONE : View.VISIBLE);
        } else {
            pickerButton.setVisibility(View.GONE);
            attachButton.setVisibility(View.GONE);
        }
    }

    @Override
    public void addTemplateString(String message) {
        String labelCategory = TopChatAnalytics.Category.INBOX_CHAT;
        if (!getArguments().getBoolean(PARAM_WEBSOCKET)) {
            if (getArguments().getString(PARAM_SENDER_TAG).equals(ChatRoomActivity.ROLE_SELLER)) {
                labelCategory = TopChatAnalytics.Category.SHOP_PAGE;
            }
            if (getArguments().getString(SendMessageActivity.PARAM_CUSTOM_MESSAGE, "").length() >
                    0) {
                labelCategory = TopChatAnalytics.Category.PRODUCT_PAGE;
            }
        }

        UnifyTracking.eventClickTemplate(labelCategory,
                TopChatAnalytics.Action.TEMPLATE_CHAT_CLICK,
                TopChatAnalytics.Name.INBOX_CHAT);
        String text = replyColumn.getText().toString();
        int index = replyColumn.getSelectionStart();
        replyColumn.setText(String.format("%s %s %s", text.substring(0, index), message, text
                .substring(index)));
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

        ((PdpRouter) getActivity().getApplication()).openImagePreviewFromChat(getActivity(),
                strings, new ArrayList<String>(), title, fullTime);
    }

    @Override
    public void onGoToImagePreview(String imageUrl, String replyTime) {
        ArrayList<String> strings = new ArrayList<>();
        strings.add(imageUrl);

        ((TkpdInboxRouter) getActivity().getApplication()).openImagePreviewFromChat(getActivity(),
                strings, new ArrayList<String>(), title, replyTime);
    }

    @Override
    public void onGoToWebView(String url, String id) {
        if (!TextUtils.isEmpty(url)) {
            UnifyTracking.eventClickThumbnailMarketing(TopChatAnalytics.Category.INBOX_CHAT,
                    TopChatAnalytics.Action.CLICK_THUMBNAIL,
                    TopChatAnalytics.Name.INBOX_CHAT,
                    id
            );

            Uri uri = Uri.parse(url);
            KeyboardHandler.DropKeyboard(getActivity(), getView());
            if (uri != null) {
                boolean isTargetDomainTokopedia = uri.getHost().endsWith("tokopedia.com");
                boolean isTargetTkpMeAndNotRedirect = (TextUtils.equals(uri.getHost(), BASE_DOMAIN_SHORTENED) &&
                        !TextUtils.equals(uri.getEncodedPath(), "/r"));
                boolean isNeedAuthToken = (isTargetDomainTokopedia || isTargetTkpMeAndNotRedirect);

                if (uri.getScheme().equals(APPLINK_SCHEME)) {
                    ((TkpdInboxRouter) getActivity().getApplicationContext())
                            .actionNavigateByApplinksUrl(getActivity(), url, new Bundle());
                } else if (uri.getPathSegments().contains(CONTACT_US_PATH_SEGMENT)) {
                    Intent intent = ((TkpdInboxRouter) MainApplication
                            .getAppContext())
                            .getContactUsIntent(getContext());
                    intent.putExtra(ContactUsConstant.PARAM_URL,
                            URLGenerator.generateURLContactUs(url, getContext()));
                    intent.putExtra(ContactUsConstant.IS_CHAT_BOT, true);
                    startActivity(intent);
                } else if (isChatBot && isNeedAuthToken) {
                    startActivity(ChatMarketingThumbnailActivity.getCallingIntent(getActivity(),
                            URLGenerator.generateURLSessionLoginV4(url, getContext())));
                } else {
                    startActivity(ChatMarketingThumbnailActivity.getCallingIntent(getActivity(), url));
                }
            }
        }
    }

    @Override
    public boolean needCreateWebSocket() {
        return getArguments().getBoolean(PARAM_WEBSOCKET);
    }

    @Override
    public void hideNotifier() {
        notifier.setVisibility(View.GONE);
    }

    private void initVar() {
        if (needCreateWebSocket()) {
            networkType = MODE_WEBSOCKET;
        } else {
            networkType = MODE_API;
        }
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

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        LinearLayoutManager templateLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager
                .HORIZONTAL, false);

        recyclerView.setLayoutManager(layoutManager);
        templateRecyclerView.setLayoutManager(templateLayoutManager);
        rvQuickReply.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int index = layoutManager.findLastVisibleItemPosition();
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

        recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = recyclerView.getRootView().getHeight() - recyclerView.getHeight();
                if (heightDiff > dpToPx(getActivity(), 200)) { // if more than 200 dp, it's
                    // probably a keyboard...
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
                    && !getArguments().getString(PARAM_SENDER_TAG, "")
                    .equals(ListChatViewHolder.USER)) {
                label.setText(getArguments().getString(PARAM_SENDER_TAG));
                label.setVisibility(View.VISIBLE);
            } else {
                label.setVisibility(View.GONE);
            }
            setOnlineDesc(getActivity().getString(R.string.just_now));
            toolbar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    presenter.onGoToDetail(getArguments().getString(PARAM_SENDER_ID),
                            getArguments().getString(PARAM_SENDER_ROLE));
                }
            });
        }
    }

    @Override
    public void displayReplyField(boolean textAreaReply) {
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
    public void showError(String error) {
        if (adapter.getItemCount() == 0) {
            NetworkErrorHelper.showEmptyState(getActivity(), getView(), error, new
                    NetworkErrorHelper.RetryClickedListener() {
                        @Override
                        public void onRetryClicked() {
                            mode = getArguments().getInt(PARAM_MODE, InboxChatViewModel
                                    .GET_CHAT_MODE);
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

    @Override
    public void setUploadingMode(boolean mode) {
        uploading = mode;
    }

    @Override
    public void scrollToBottom() {
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void scrollToBottomWithCheck() {
        int index = layoutManager.findFirstCompletelyVisibleItemPosition();
        if (index < 3) {
            scrollToBottom();
        }
    }

    @Override
    public void setHeaderModel(String nameHeader, String imageHeader) {
        this.avatarImage = imageHeader;
        this.title = nameHeader;
    }

    @Override
    public void resetReplyColumn() {
        replyColumn.setText("");
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
    public void setViewEnabled(boolean isEnabled) {

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
            ImageHandler.loadImageCircle2(getActivity(), avatar, null, R.drawable
                    .ic_image_avatar_boy);
            user.setText(getArguments().getString(PARAM_SENDER_NAME));
            label.setText(getArguments().getString(PARAM_SENDER_TAG));
            setOnlineDesc(getActivity().getString(R.string.just_now));
        }
    }

    @Override
    public void hideMainLoading() {
        progressBar.setVisibility(View.GONE);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.detachView();
    }

    private View.OnClickListener getSendInitMessage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventSendMessagePage();
                presenter.initMessage(replyColumn.getText().toString(),
                        getArguments().getString(ChatRoomActivity.PARAM_SOURCE),
                        getArguments().getString(ChatRoomActivity.PARAM_SENDER_ID),
                        getArguments().getString(ChatRoomActivity.PARAM_USER_ID));
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
                    ImageUploadViewModel temp = generateChatViewModelWithImage(fileLoc);
                    presenter.startUpload(Collections.singletonList(temp), networkType);
                    adapter.addReply(temp);
                }
                break;

            case ImageGallery.TOKOPEDIA_GALLERY:
                if (data == null) {
                    break;
                }
                String imageUrl = data.getStringExtra(GalleryActivity.IMAGE_URL);
                List<ImageUploadViewModel> list = new ArrayList<>();

                if (!TextUtils.isEmpty(imageUrl)) {
                    ImageUploadViewModel temp = generateChatViewModelWithImage(imageUrl);
                    list.add(temp);
                } else {
                    ArrayList<String> imageUrls = data.getStringArrayListExtra(GalleryActivity
                            .IMAGE_URLS);
                    if (imageUrls != null) {
                        for (int i = 0; i < imageUrls.size(); i++) {
                            ImageUploadViewModel temp = generateChatViewModelWithImage(imageUrls.get(i));
                            list.add(temp);
                        }
                    }
                }
                adapter.addReply(list);
                presenter.startUpload(list, networkType);
                break;
            case AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_REQ_CODE:
                if (data == null)
                    break;
                if (!data.hasExtra(AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY))
                    break;
                ArrayList<ResultProduct> resultProducts = data.getParcelableArrayListExtra
                        (AttachProductActivity.TOKOPEDIA_ATTACH_PRODUCT_RESULT_KEY);
                attachProductRetrieved(resultProducts);
                break;
            case AttachInvoiceActivity.TOKOPEDIA_ATTACH_INVOICE_REQ_CODE:
                if (data == null)
                    break;
                if (!data.hasExtra(AttachInvoiceActivity
                        .TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY))
                    break;
                SelectedInvoice selectedInvoice = data.getParcelableExtra(AttachInvoiceActivity
                        .TOKOPEDIA_ATTACH_INVOICE_SELECTED_INVOICE_KEY);
                attachInvoiceRetrieved(selectedInvoice);
                break;
            default:
                break;
        }
    }

    public void onBackPressed() {
        if (uploading) {
            showDialogConfirmToAbortUpload();
        } else {
            ((ChatRoomActivity) getActivity()).destroy();
        }
    }

    private void showDialogConfirmToAbortUpload() {
        final AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(getActivity());
        myAlertDialog.setTitle(getActivity().getString(R.string.exit_chat_title));
        myAlertDialog.setMessage(getActivity().getString(R.string.exit_chat_body));
        myAlertDialog.setPositiveButton(getActivity().getString(R.string.exit_chat_yes), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ((ChatRoomActivity) getActivity()).destroy();
                    }
                });
        myAlertDialog.setNegativeButton(getActivity().getString(R.string.cancel), new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        Dialog dialog = myAlertDialog.create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
    }

    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE})
    public void actionCamera() {
        presenter.openCamera();
    }

    @Override
    public boolean shouldHandleUrlManually(String url) {
        String urlManualHandlingList[] = {CONTACT_US_URL_BASE_DOMAIN};
        return (Arrays.asList(urlManualHandlingList).contains(url) || isChatBot);
    }

    @Override
    public void showSnackbarError(String string) {
        NetworkErrorHelper.showSnackbar(getActivity(), string);
    }

    @NeedsPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    public void actionImagePicker() {
        Intent intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                .getGalleryIntent(getActivity(), false, 1, true);
        startActivityForResult(intent, com.tokopedia.core.ImageGallery.TOKOPEDIA_GALLERY);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[]
            grantResults) {
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
        RequestPermissionUtil.onShowRationale(getActivity(), request, Manifest.permission
                .READ_EXTERNAL_STORAGE);
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
        RequestPermissionUtil.onPermissionDenied(getActivity(), Manifest.permission
                .READ_EXTERNAL_STORAGE);
    }

    @OnNeverAskAgain(Manifest.permission.READ_EXTERNAL_STORAGE)
    void showNeverAskForStorage() {
        RequestPermissionUtil.onNeverAskAgain(getActivity(), Manifest.permission
                .READ_EXTERNAL_STORAGE);
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

    //Getter
    @Override
    public String getKeyword() {
        return getArguments().getString(PARAM_KEYWORD);
    }

    @Override
    public String getReplyMessage() {
        return replyColumn.getText().toString();
    }

    @Override
    public UserSession getUserSession() {
        return ((AbstractionRouter) getActivity().getApplication()).getSession();
    }

    //Clicker (from viewholder) with it's tracker
    @Override
    public void productClicked(Integer productId, String productName, String productPrice, Long
            dateTimeReply, String url) {
        trackProductClicked();
        String senderRole = getArguments().getString(PARAM_SENDER_ROLE, "");
        if (!GlobalConfig.isSellerApp() || !senderRole.equals(ROLE_SHOP)) {
            if (MainApplication.getAppContext() instanceof TkpdInboxRouter) {
                TkpdInboxRouter router = (TkpdInboxRouter) MainApplication.getAppContext();
                ProductPass productPass = ProductPass.Builder.aProductPass()
                        .setProductId(productId)
                        .setProductPrice(productPrice)
                        .setProductName(productName)
                        .setDateTimeInMilis(dateTimeReply)
                        .build();

                Intent intent = router.getProductDetailIntent(getContext(), productPass);
                startActivity(intent);
            }
        } else {
            //Necessary to do it this way to prevent PDP opened in seller app
            //otherwise someone other than the owner can access PDP with topads promote page
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(browserIntent);
        }
    }


    private void trackProductClicked() {
        if ((getActivity().getApplicationContext() instanceof AbstractionRouter)) {
            AbstractionRouter abstractionRouter = (AbstractionRouter) getActivity()
                    .getApplicationContext();
            abstractionRouter.getAnalyticTracker().sendEventTracking(
                    AttachProductAnalytics.getEventClickChatAttachedProductImage().getEvent()
            );
        }
    }

    @Override
    public void onInvoiceSelected(SelectedInvoice selectedInvoice) {
        attachInvoiceRetrieved(selectedInvoice);
    }

    @Override
    public void showSearchInvoiceScreen() {
        String msgId = getArguments().getString(PARAM_MESSAGE_ID);
        String userId = SessionHandler.getLoginID(getContext());
        Intent intent = AttachInvoiceActivity.createInstance(getActivity(), userId
                , Integer.parseInt(msgId));
        startActivityForResult(intent, AttachInvoiceActivity.TOKOPEDIA_ATTACH_INVOICE_REQ_CODE);
    }

    @Override
    public void onClickRating(ChatRatingViewModel element, int rating) {
        UserSession userSession = ((AbstractionRouter) getContext().
                getApplicationContext()).getSession();
        int userId = 0;
        if (userSession != null && !TextUtils.isEmpty(userSession.getUserId())) {
            userId = Integer.valueOf(userSession.getUserId());
        }
        presenter.setChatRating(element, userId, rating);
    }

    @Override
    public void onGoToTimeMachine(String url) {
        startActivity(TimeMachineActivity.getCallingIntent(getActivity(), url));
    }


    //CHECK CONDITION

    @Override
    public boolean isChatBot() {
        return isChatBot;
    }

    @Override
    public boolean isMyMessage(int fromUid) {
        return String.valueOf(fromUid).equals(SessionHandler.getLoginID(MainApplication
                .getAppContext()));
    }

    @Override
    public boolean isMyMessage(String fromUid) {
        return fromUid.equals(SessionHandler.getLoginID(MainApplication
                .getAppContext()));
    }

    @Deprecated
    @Override
    public boolean isCurrentThread(int msgId) {
        return getArguments().getString(PARAM_MESSAGE_ID, "").equals(String.valueOf(msgId));
    }

    public boolean isCurrentThread(String msgId) {
        return getArguments().getString(PARAM_MESSAGE_ID, "").equals(msgId);
    }

    public boolean isAllowedTemplate() {
        return (remoteConfig.getBoolean(ENABLE_TOPCHAT));
    }


    //ADD INCOMING MESSAGE

    private AttachInvoiceSentViewModel generateInvoice(SelectedInvoice selectedInvoice) {

        return new AttachInvoiceSentViewModel(
                getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID),
                sessionHandler.getLoginName(),
                selectedInvoice.getInvoiceNo(),
                selectedInvoice.getDescription(),
                selectedInvoice.getTopProductImage(),
                selectedInvoice.getAmount(),
                SendableViewModel.generateStartTime()
        );
    }

    public ImageUploadViewModel generateChatViewModelWithImage(String imageUrl) {
        scrollToBottom();

        ImageUploadViewModel model = new ImageUploadViewModel(
                getArguments().getString(InboxMessageConstant.PARAM_SENDER_ID),
                String.valueOf(System.currentTimeMillis() / MILIS_TO_SECOND),
                imageUrl,
                SendableViewModel.generateStartTime()
        );
        return model;
    }

    private ProductAttachmentViewModel generateProductChatViewModel(ResultProduct product) {
        return new ProductAttachmentViewModel(
                sessionHandler.getLoginID(),
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getProductUrl(),
                product.getProductImageThumbnail(),
                SendableViewModel.generateStartTime());
    }

    private void attachInvoiceRetrieved(SelectedInvoice selectedInvoice) {
        String msgId = getArguments().getString(PARAM_MESSAGE_ID);
        AttachInvoiceSentViewModel generatedInvoice = generateInvoice(selectedInvoice);
        presenter.sendInvoiceAttachment(msgId, selectedInvoice, generatedInvoice.getStartTime());
        adapter.addReply(generatedInvoice);
        scrollToBottom();
    }

    public void attachProductRetrieved(ArrayList<ResultProduct> resultProducts) {
        UnifyTracking.eventSendAttachment(TopChatAnalytics.Category.CHAT_DETAIL,
                TopChatAnalytics.Action.CHAT_DETAIL_ATTACHMENT,
                TopChatAnalytics.Name.CHAT_DETAIL);

        String msgId = getArguments().getString(PARAM_MESSAGE_ID);
        for (ResultProduct result : resultProducts) {
            ProductAttachmentViewModel item = generateProductChatViewModel(result);
            presenter.sendProductAttachment(msgId, result, item.getStartTime());
            adapter.addReply(item);
            scrollToBottom();
        }
    }

    private void addViewMessage(ReplyActionData replyData, String reply) {
        setViewEnabled(true);
        MessageViewModel messageViewModel = new MessageViewModel(
                String.valueOf(replyData.getChat().getMsgId()),
                replyData.getChat().getSenderId(),
                replyData.getChat().getFrom(),
                "",
                "",
                "",
                replyData.getChat().getReplyTime(),
                "",
                reply,
                false,
                false,
                true
        );

        adapter.addReply(messageViewModel);
        resetReplyColumn();
        setResult();
    }

    @Override
    public void addTimeMachine() {
        adapter.showTimeMachine();
    }

    @Override
    public void addDummyMessage(String dummyText, String startTime) {

        MessageViewModel dummyChat = new MessageViewModel(
                getArguments().getString(InboxMessageConstant.PARAM_MESSAGE_ID),
                sessionHandler.getLoginID(),
                sessionHandler.getLoginName(),
                startTime,
                dummyText.length() != 0 ? dummyText : getReplyMessage()
        );
        adapter.addReply(dummyChat);
        scrollToBottom();
    }

    //Callback
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
                    sendButton.setOnClickListener(generateSendClickListener());

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
    public void onErrorWebSocket() {
        if (getActivity() != null && presenter != null) {
            networkType = MODE_API;
            sendButton.setOnClickListener(generateSendClickListener());
            notifyConnectionWebSocket();
            presenter.recreateWebSocket();
        }
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
                            if (layoutManager.findFirstCompletelyVisibleItemPosition() < 2) {
                                scrollToBottom();
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
                                if (layoutManager.findFirstCompletelyVisibleItemPosition() < 2) {
                                    scrollToBottom();
                                }
                            }
                        }
                    });
                }
                break;
            case ChatWebSocketConstant.EVENT_TOPCHAT_READ_MESSAGE:
                adapter.setReadStatus();
                break;
            default:
                break;
        }
    }

    @Override
    public void onReceiveMessage(final BaseChatViewModel message) {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isCurrentThread(message.getMessageId())) {
                        processReceiveMessage(message);
                    }
                }
            });
        }
    }

    private void processReceiveMessage(BaseChatViewModel message) {
        rvQuickReply.setVisibility(View.GONE);
        if (templateAdapter != null && templateAdapter.getList().size() != 0) {
            templateRecyclerView.setVisibility(View.VISIBLE);
        }

        setViewEnabled(true);
        removeDummyReplyIfExist(message);
        removeIsTyping();

        if (message instanceof QuickReplyListViewModel) {
            showQuickReplyView((QuickReplyListViewModel) message);
            if (!TextUtils.isEmpty(message.getMessage())) {
                addMessageToList(message);
            }
        } else {
            addMessageToList(message);
        }

        if (isMyMessage(message.getFromUid())) {
            scrollToBottom();
            resetReplyColumn();
        } else {
            scrollToBottomWithCheck();
            readMessage(message.getMessageId());
        }

        setResult();
    }

    private void removeDummyReplyIfExist(BaseChatViewModel message) {
        if (isMyMessage(message.getFromUid())) {
            if (message instanceof ProductAttachmentViewModel) {
                getAdapter().removeLastProductWithId(((ProductAttachmentViewModel) message).getProductId());
            } else if (message instanceof SendableViewModel) {
                getAdapter().removeLastMessageWithStartTime(((SendableViewModel) message).getStartTime());
            } else {
                getAdapter().removeLast();
            }
        }
    }

    private void readMessage(String messageId) {
        if (!TextUtils.isEmpty(messageId)) {
            presenter.readMessage(messageId);
        }
    }

    private void addMessageToList(BaseChatViewModel message) {
        adapter.addReply((Visitable) message);
        setResult();
    }

    private void removeIsTyping() {
        if (adapter.isTyping()) {
            adapter.removeTyping();
        }
    }

    @Override
    public void showQuickReplyView(QuickReplyListViewModel model) {
        if (model.getQuickReplies().size() != 0) {
            rvQuickReply.setVisibility(View.VISIBLE);
            templateRecyclerView.setVisibility(View.GONE);
            quickReplyAdapter = new QuickReplyAdapter(model, this);
            rvQuickReply.setAdapter(quickReplyAdapter);
            rvQuickReply.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onQuickReplyClicked(QuickReplyViewModel quickReply) {
        rvQuickReply.setVisibility(View.GONE);
        if (templateAdapter != null && templateAdapter.getList().size() != 0) {
            templateRecyclerView.setVisibility(View.VISIBLE);
        }
        presenter.sendMessage(networkType, quickReply.getMessage());
    }

    @Override
    public void onSuccessSendReply(ReplyActionData replyData, String reply) {
        adapter.removeLast();
        addViewMessage(replyData, reply);
        if (quickReplyAdapter.getItemCount() != 0) {
            quickReplyAdapter.clearData();
        }
    }

    @Override
    public void onErrorSendReply() {
        adapter.removeLast();
        setViewEnabled(true);
        replyColumn.setText("");
        showError(getActivity().getString(R.string.delete_error).concat("\n").concat(getString(R
                .string.string_general_error)));
        if (quickReplyAdapter.getItemCount() != 0) {
            rvQuickReply.setVisibility(View.VISIBLE);
            templateRecyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onSuccessSendAttach(ReplyActionData data, ImageUploadViewModel model) {
        adapter.remove(model);
        addViewImage(data, UPLOADING);
    }

    private void addViewImage(ReplyActionData replyData, String message) {
        setViewEnabled(true);
        ImageUploadViewModel messageViewModel = new ImageUploadViewModel(
                String.valueOf(replyData.getChat().getMsgId()),
                replyData.getChat().getSenderId(),
                replyData.getChat().getFrom(),
                "",
                replyData.getChat().getAttachment().getId(),
                replyData.getChat().getAttachment().getType(),
                replyData.getChat().getReplyTime(),
                true,
                replyData.getChat().getAttachment().getAttributes().getImageUrl(),
                replyData.getChat().getAttachment().getAttributes().getThumbnail(),
                "",
                message
        );

        adapter.addReply(messageViewModel);
        resetReplyColumn();
        setResult();
    }

    @Override
    public void onSuccessSetRating(ChatRatingViewModel model) {
        adapter.changeRating(model);
    }

    @Override
    public void onErrorSetRating() {
        showError(getActivity().getString(R.string.delete_error).concat("\n").concat(getString(R
                .string.string_general_error)));
    }

    @Override
    public void onErrorUploadImages(String errorMessage, ImageUploadViewModel model) {
        showError(errorMessage);
        showRetryFor(model);
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
}
