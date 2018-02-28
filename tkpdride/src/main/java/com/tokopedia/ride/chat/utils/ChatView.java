package com.tokopedia.ride.chat.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tokopedia.ride.R;
import com.tokopedia.ride.bookingride.view.adapter.ChatViewListAdapter;

import java.util.ArrayList;

/**
 * Created by sachinbansal on 2/13/18.
 */

public class ChatView extends RelativeLayout {

    private static final int FLAT = 0;
    private static final int ELEVATED = 1;

    private CardView inputFrame;
    private ListView chatListView;
    private EditText inputEditText;

    private ViewBuilderInterface viewBuilder;
    private FloatingActionButton actionButton;
    private boolean previousFocusState = false, useEditorAction, isTyping;

    private Runnable typingTimerRunnable = new Runnable() {
        @Override
        public void run() {
            if (isTyping) {
                isTyping = false;
                if (typingListener != null) typingListener.userStoppedTyping();
            }
        }
    };
    private TypingListener typingListener;
    private OnSentMessageListener onSentMessageListener;
    private OnSendSMSRetry onSendSMSRetry;
    private ChatViewListAdapter chatViewListAdapter;

    private int inputFrameBackgroundColor, backgroundColor;
    private int inputTextSize, inputTextColor, inputHintColor;


    private Drawable sendButtonIcon, buttonDrawable;
    private TypedArray attributes, textAppearanceAttributes;
    private Context context;
    private int id = 0;
    private int sendButtonBackgroundTint, sendButtonIconTint;


    ChatView(Context context) {
        this(context, null);
    }

    public ChatView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, new ViewBuilder());
    }

    public ChatView(Context context, AttributeSet attrs, int defStyleAttr, ViewBuilderInterface viewBuilder) {

        super(context, attrs, defStyleAttr);
        this.viewBuilder = viewBuilder;
        init(context, attrs, defStyleAttr);

    }


    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        LayoutInflater.from(getContext()).inflate(R.layout.chat_view, this, true);
        this.context = context;
        initializeViews();
        getXMLAttributes(attrs, defStyleAttr);
        setViewAttributes();
        setListAdapter();
        setButtonClickListeners();
        setUserTypingListener();
        setUserStoppedTypingListener();
    }

    private void initializeViews() {
        chatListView = findViewById(R.id.chat_list);
        inputFrame = findViewById(R.id.input_frame);
        inputEditText = findViewById(R.id.input_edit_text);
        actionButton = findViewById(R.id.sendButton);
    }

    private void getXMLAttributes(AttributeSet attrs, int defStyleAttr) {
        attributes = context.obtainStyledAttributes(attrs, R.styleable.ChatView, defStyleAttr, R.style.ChatViewDefault);
        getChatViewBackgroundColor();
        getAttributesForBubbles();
        getAttributesForInputFrame();
        getAttributesForInputText();
        getAttributesForSendButton();
        getUseEditorAction();
        attributes.recycle();
    }

    private void setListAdapter() {
        chatViewListAdapter = new ChatViewListAdapter(context, new ViewBuilder());
        chatViewListAdapter.setOnRetryTap(new ChatViewListAdapter.OnRetryTap() {
            @Override
            public void onRetryTap(ChatMessage chatMessage) {

                onSendSMSRetry.onTapRetry(chatMessage);

            }
        });
        chatListView.setAdapter(chatViewListAdapter);
    }


    private void setViewAttributes() {
        setChatViewBackground();
        setInputFrameAttributes();
        setInputTextAttributes();
        setSendButtonAttributes();
        setUseEditorAction();
    }

    private void getChatViewBackgroundColor() {
        backgroundColor = attributes.getColor(R.styleable.ChatView_backgroundColor, -1);
    }

    private void getAttributesForBubbles() {

        float dip4 = context.getResources().getDisplayMetrics().density * 4.0f;
        int elevation = attributes.getInt(R.styleable.ChatView_bubbleElevation, ELEVATED);

    }


    private void getAttributesForInputFrame() {
        inputFrameBackgroundColor = attributes.getColor(R.styleable.ChatView_inputBackgroundColor, -1);
    }

    private void setInputFrameAttributes() {
        inputFrame.setCardBackgroundColor(inputFrameBackgroundColor);
    }

    private void setChatViewBackground() {
        this.setBackgroundColor(backgroundColor);
    }

    private void getAttributesForInputText() {
        setInputTextDefaults();
        if (hasStyleResourceSet()) {
            setTextAppearanceAttributes();
            setInputTextSize();
            setInputTextColor();
            setInputHintColor();
            textAppearanceAttributes.recycle();
        }
        overrideTextStylesIfSetIndividually();
    }

    private void setTextAppearanceAttributes() {
        final int textAppearanceId = attributes.getResourceId(R.styleable.ChatView_inputTextAppearance, 0);
        textAppearanceAttributes = getContext().obtainStyledAttributes(textAppearanceId, R.styleable.ChatViewInputTextAppearance);
    }

    private void setInputTextAttributes() {
        inputEditText.setTextColor(inputTextColor);
        inputEditText.setHintTextColor(inputHintColor);
        inputEditText.setTextSize(TypedValue.COMPLEX_UNIT_PX, inputTextSize);
    }

    private void getAttributesForSendButton() {
        sendButtonBackgroundTint = attributes.getColor(R.styleable.ChatView_sendBtnBackgroundTint, -1);
        sendButtonIconTint = attributes.getColor(R.styleable.ChatView_sendBtnIconTint, Color.WHITE);

        sendButtonIcon = attributes.getDrawable(R.styleable.ChatView_sendBtnIcon);
    }

    private void setSendButtonAttributes() {
        /*actionsMenu.getSendButton().setColorNormal(sendButtonBackgroundTint);
        actionsMenu.setIconDrawable(sendButtonIcon);

        buttonDrawable = actionsMenu.getIconDrawable();
        actionsMenu.setButtonIconTint(sendButtonIconTint);*/

        actionButton.setColorNormal(sendButtonBackgroundTint);
        actionButton.setIconDrawable(sendButtonIcon);

    }

    private void getUseEditorAction() {
        useEditorAction = attributes.getBoolean(R.styleable.ChatView_inputUseEditorAction, false);
    }

    private void setUseEditorAction() {
        if (useEditorAction) {
            setupEditorAction();
        } else {
            inputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        }
    }

    private boolean hasStyleResourceSet() {
        return attributes.hasValue(R.styleable.ChatView_inputTextAppearance);
    }

    private void setInputTextDefaults() {
        inputTextSize = context.getResources().getDimensionPixelSize(R.dimen.default_input_text_size);
        inputTextColor = ContextCompat.getColor(context, R.color.black);
        inputHintColor = ContextCompat.getColor(context, R.color.main_color_gray);
    }

    private void setInputTextSize() {
        if (textAppearanceAttributes.hasValue(R.styleable.ChatView_inputTextSize)) {
            inputTextSize = attributes.getDimensionPixelSize(R.styleable.ChatView_inputTextSize, inputTextSize);
        }
    }

    private void setInputTextColor() {
        if (textAppearanceAttributes.hasValue(R.styleable.ChatView_inputTextColor)) {
            inputTextColor = attributes.getColor(R.styleable.ChatView_inputTextColor, inputTextColor);
        }
    }

    private void setInputHintColor() {
        if (textAppearanceAttributes.hasValue(R.styleable.ChatView_inputHintColor)) {
            inputHintColor = attributes.getColor(R.styleable.ChatView_inputHintColor, inputHintColor);
        }
    }

    private void overrideTextStylesIfSetIndividually() {
        inputTextSize = (int) attributes.getDimension(R.styleable.ChatView_inputTextSize, inputTextSize);
        inputTextColor = attributes.getColor(R.styleable.ChatView_inputTextColor, inputTextColor);
        inputHintColor = attributes.getColor(R.styleable.ChatView_inputHintColor, inputHintColor);
    }

    private void setupEditorAction() {
        inputEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_AUTO_CORRECT | InputType.TYPE_TEXT_FLAG_CAP_SENTENCES);
        inputEditText.setImeOptions(EditorInfo.IME_ACTION_SEND);
        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    long stamp = System.currentTimeMillis();
                    String message = inputEditText.getText().toString();

                    if (!TextUtils.isEmpty(message)) {
                        sendMessage(message, stamp);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void setButtonClickListeners() {

        /*actionsMenu.getSendButton().setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (actionsMenu.isExpanded()) {
                    actionsMenu.collapse();
                    return;
                }

                long stamp = System.currentTimeMillis();
                String message = inputEditText.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    sendMessage(message, stamp);
                }

            }
        });

        actionsMenu.getSendButton().setOnLongClickListener(new OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                actionsMenu.expand();
                return true;
            }
        });*/

        actionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                long stamp = System.currentTimeMillis();
                String message = inputEditText.getText().toString();
                if (!TextUtils.isEmpty(message)) {
                    sendMessage(message, stamp);
                }
            }
        });
    }

    private void setUserTypingListener() {
        inputEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {

                    if (!isTyping) {
                        isTyping = true;
                        if (typingListener != null) typingListener.userStartedTyping();
                    }

                    removeCallbacks(typingTimerRunnable);
                    postDelayed(typingTimerRunnable, 1500);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setUserStoppedTypingListener() {
        inputEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (previousFocusState && !hasFocus && typingListener != null) {
                    typingListener.userStoppedTyping();
                }
                previousFocusState = hasFocus;
            }
        });
    }


    @Override
    protected boolean addViewInLayout(View child, int index, ViewGroup.LayoutParams params) {
        return super.addViewInLayout(child, index, params);
    }

    public String getTypedMessage() {
        return inputEditText.getText().toString();
    }

    public void setTypingListener(TypingListener typingListener) {
        this.typingListener = typingListener;
    }

    public void setOnSentMessageListener(OnSentMessageListener onSentMessageListener) {
        this.onSentMessageListener = onSentMessageListener;
    }

    public void setOnSendSMSRetry(OnSendSMSRetry onSendSMSRetry) {
        this.onSendSMSRetry = onSendSMSRetry;
    }

    private void sendMessage(String message, long stamp) {

        ChatMessage chatMessage = new ChatMessage(message, stamp, ChatMessage.Type.SENT);
        id++;
        chatMessage.setId(id);
        chatMessage.setDeliveryStatus(ChatMessage.DeliveryStatus.PENDING);
        if (onSentMessageListener != null && onSentMessageListener.sendMessage(chatMessage)) {
            chatViewListAdapter.addMessage(chatMessage);
            inputEditText.setText("");
        }
    }

    public void addMessage(ChatMessage chatMessage) {
        chatViewListAdapter.addMessage(chatMessage);
        chatViewListAdapter.notifyDataSetChanged();

    }

    public void addMessages(ArrayList<ChatMessage> messages) {
        chatViewListAdapter.addMessages(messages);
    }

    public void removeMessage(int position) {
        chatViewListAdapter.removeMessage(position);
    }

    public void clearMessages() {
        chatViewListAdapter.clearMessages();
    }

    public EditText getInputEditText() {
        return inputEditText;
    }

    public void updateMessageSentStatus(ChatMessage.DeliveryStatus deliveryStatus, int id) {
        ArrayList<ChatMessage> chatMessageArrayList = chatViewListAdapter.getChatMessages();

        if (chatMessageArrayList != null && !chatMessageArrayList.isEmpty()) {
            for (ChatMessage chatMessage : chatMessageArrayList) {
                if (chatMessage.getId() == id) {
                    chatMessage.setDeliveryStatus(deliveryStatus);
                }
            }
        }

        chatViewListAdapter.setChatMessages(chatMessageArrayList);
    }


    public interface TypingListener {

        void userStartedTyping();

        void userStoppedTyping();

    }

    public interface OnSentMessageListener {
        boolean sendMessage(ChatMessage chatMessage);
    }

    public interface OnSendSMSRetry {
        void onTapRetry(ChatMessage chatMessage);
    }

}
