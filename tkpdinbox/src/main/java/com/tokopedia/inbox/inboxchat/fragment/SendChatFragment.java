package com.tokopedia.inbox.inboxchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.nishikino.model.EventTracking;
import com.tokopedia.core.base.di.component.DaggerAppComponent;
import com.tokopedia.core.base.di.module.AppModule;
import com.tokopedia.core.base.presentation.BaseDaggerFragment;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.inbox.R;
import com.tokopedia.inbox.inboxchat.activity.SendMessageActivity;
import com.tokopedia.inbox.inboxchat.analytics.TopChatTrackingEventLabel;
import com.tokopedia.inbox.inboxchat.di.DaggerInboxChatComponent;
import com.tokopedia.inbox.inboxchat.listener.SendChat;
import com.tokopedia.inbox.inboxchat.presenter.SendChatPresenter;
import com.tokopedia.inbox.inboxchat.viewholder.ListChatViewHolder;

import javax.inject.Inject;

/**
 * @author by nisie on 10/25/17.
 */

public class SendChatFragment extends BaseDaggerFragment
        implements SendChat.View {

    private View replyView;
    private ImageView avatar;
    private TextView user;
    private TextView label;
    private ImageView sendButton;
    private EditText replyColumn;
    private ImageView attachButton;
    private View mainHeader;
    private Toolbar toolbar;
    private TextView tempDate;
    private TextView tempMessage;
    private View tempMessageView;

    @Inject
    SendChatPresenter presenter;

    public static Fragment createInstance(Bundle extras) {
        Fragment fragment = new SendChatFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    protected String getScreenName() {
        return AppScreen.SCREEN_PEOPLE_SEND_MESSAGE;
    }

    @Override
    protected void initInjector() {
        DaggerAppComponent daggerAppComponent = (DaggerAppComponent) DaggerAppComponent.builder()
                .appModule(new AppModule(getContext()))
                .build();
        DaggerInboxChatComponent daggerInboxChatComponent =
                (DaggerInboxChatComponent) DaggerInboxChatComponent.builder()
                        .appComponent(daggerAppComponent).build();
        daggerInboxChatComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_send_chat, container, false);
        toolbar = (Toolbar) getActivity().findViewById(R.id.app_bar);
        mainHeader = toolbar.findViewById(R.id.main_header);
        mainHeader.setVisibility(View.INVISIBLE);
        replyView = rootView.findViewById(R.id.add_comment_area);
        sendButton = (ImageView) rootView.findViewById(R.id.send_but);
        replyColumn = (EditText) rootView.findViewById(R.id.new_comment);
        attachButton = (ImageView) rootView.findViewById(R.id.add_url);
        tempMessageView = rootView.findViewById(R.id.temp_message);
        tempMessage = (TextView) rootView.findViewById(R.id.message);
        tempDate = (TextView) rootView.findViewById(R.id.hour);
        presenter.attachView(this);
        prepareView();
        initListener();
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


    private void initListener() {
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UnifyTracking.eventSendMessagePage();
                presenter.sendMessage(replyColumn.getText().toString(),
                        getArguments().getString(SendMessageActivity.PARAM_SOURCE),
                        getArguments().getString(SendMessageActivity.PARAM_SHOP_ID),
                        getArguments().getString(SendMessageActivity.PARAM_USER_ID));
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UnifyTracking.eventInsertAttachment(TopChatTrackingEventLabel.Category.CHAT_DETAIL,
                        TopChatTrackingEventLabel.Action.CHAT_DETAIL_INSERT,
                        TopChatTrackingEventLabel.Name.CHAT_DETAIL);

                presenter.getAttachProductDialog(
                        getArguments().getString(SendMessageActivity
                                .PARAM_SHOP_ID, ""),
                        getArguments().getString(SendMessageActivity.PARAM_ROLE, "")
                );
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHeader();
    }

    @Override
    public void addUrlToReply(String url) {
        UnifyTracking.eventSendAttachment(TopChatTrackingEventLabel.Category.CHAT_DETAIL,
                TopChatTrackingEventLabel.Action.CHAT_DETAIL_ATTACHMENT,
                TopChatTrackingEventLabel.Name.CHAT_DETAIL);
        replyColumn.setText(replyColumn.getText() + "\n" + url);
        replyColumn.setSelection(replyColumn.length());
    }

    @Override
    public void removeError() {
        replyColumn.setError(null);
    }

    @Override
    public void setContentError(String errorMessage) {
        replyColumn.setError(errorMessage);
    }

    @Override
    public void showDummyMessage(String message) {
        tempMessageView.setVisibility(View.VISIBLE);
        tempMessage.setText(message);
        tempDate.setVisibility(View.VISIBLE);
        tempDate.setText(R.string.title_sending);
    }

    @Override
    public void setActionsEnabled(boolean isEnabled) {
        sendButton.setEnabled(isEnabled);
        replyColumn.setEnabled(isEnabled);
    }

    @Override
    public void onSuccessSendMessage() {
        CommonUtils.UniversalToast(getActivity(), getString(R.string.success_send_msg));
        getActivity().finish();
    }

    @Override
    public void onErrorSendMessage(String errorMessage) {
        NetworkErrorHelper.showSnackbar(getActivity(), errorMessage);
    }

    @Override
    public void removeDummyMessage() {
        tempMessageView.setVisibility(View.GONE);
        tempMessage.setText("");
    }

    private void setHeader() {
        if (toolbar != null) {
            mainHeader.setVisibility(View.VISIBLE);
            avatar = (ImageView) toolbar.findViewById(R.id.user_avatar);
            user = (TextView) toolbar.findViewById(R.id.title);
            label = (TextView) toolbar.findViewById(R.id.label);
            TextView subtitle = (TextView) toolbar.findViewById(R.id.subtitle);
            subtitle.setVisibility(View.GONE);
            String url = getArguments().getString(SendMessageActivity.PARAM_AVATAR, "");
            if (!TextUtils.isEmpty(url))
                ImageHandler.loadImageCircle2(getActivity(), avatar, url, R.drawable
                        .ic_image_avatar_boy);
            user.setText(getArguments().getString(SendMessageActivity.PARAM_OWNER_FULLNAME));
            if (!TextUtils.isEmpty(getArguments().getString(SendMessageActivity.PARAM_ROLE, ""))
                    && !getArguments().getString(SendMessageActivity.PARAM_ROLE, "").equals(SendMessageActivity
                    .ROLE_USER)) {
                label.setText(getArguments().getString(SendMessageActivity.PARAM_ROLE));
                label.setVisibility(View.VISIBLE);
            } else {
                label.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}
