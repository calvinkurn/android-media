package com.tokopedia.inbox.inboxmessageold.model;

import java.util.HashMap;
import java.util.Map;

import static com.tokopedia.inbox.inboxmessageold.fragment.SendMessageFragment.PARAM_SOURCE;

/**
 * Created by Nisie on 5/26/16.
 */
public class SendMessagePass {

    private static final String PARAM_MESSAGE = "message";
    private static final String PARAM_MESSAGE_SUBJECT = "message_subject";
    private static final String PARAM_TO_SHOP_ID = "to_shop_id";
    private static final String PARAM_TO_USER_ID = "to_user_id";

    String message;
    String message_subject;
    String to_shop_id;
    String to_user_id;
    String source;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage_subject() {
        return message_subject;
    }

    public void setMessage_subject(String message_subject) {
        this.message_subject = message_subject;
    }

    public String getTo_shop_id() {
        return to_shop_id;
    }

    public void setTo_shop_id(String to_shop_id) {
        this.to_shop_id = to_shop_id;
    }

    public String getTo_user_id() {
        return to_user_id;
    }

    public void setTo_user_id(String to_user_id) {
        this.to_user_id = to_user_id;
    }

    public Map<String, String> getSendMessageParam() {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_MESSAGE, getMessage());
        param.put(PARAM_MESSAGE_SUBJECT, getMessage_subject());
        param.put(PARAM_TO_SHOP_ID, getTo_shop_id());
        param.put(PARAM_TO_USER_ID, getTo_user_id());
        param.put(PARAM_SOURCE, getSource());
        return param;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }
}
