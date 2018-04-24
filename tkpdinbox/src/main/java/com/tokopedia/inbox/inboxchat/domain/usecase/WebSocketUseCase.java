package com.tokopedia.inbox.inboxchat.domain.usecase;

import android.os.CountDownTimer;

import com.google.gson.JsonObject;
import com.tokopedia.abstraction.common.data.model.session.UserSession;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.inbox.attachinvoice.view.resultmodel.SelectedInvoice;
import com.tokopedia.inbox.attachproduct.view.resultmodel.ResultProduct;
import com.tokopedia.inbox.inboxchat.ChatWebSocketConstant;
import com.tokopedia.inbox.inboxchat.ChatWebSocketListenerImpl;
import com.tokopedia.inbox.inboxchat.InboxChatConstant;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * @author by StevenFredian on 24/04/18.
 */

public class WebSocketUseCase {

    private ChatWebSocketListenerImpl listener;
    private CountDownTimer countDownTimer;
    private OkHttpClient client;
    private String url;
    private WebSocket ws;
    private UserSession userSession;


    public WebSocketUseCase(String magicString, UserSession session, ChatWebSocketListenerImpl listene) {
        client = new OkHttpClient();
        url = magicString;
        userSession = session;
        listener = listene;
        countDownTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                createWebSocket();
            }
        };
        createWebSocket();
    }

    private void createWebSocket() {
        Request request = new Request.Builder().url(url)
                .header("Origin", TkpdBaseURL.WEB_DOMAIN)
                .header("Accounts-Authorization",
                        "Bearer " + userSession.getAccessToken())
                .build();
        ws = client.newWebSocket(request, listener);
    }

    public void recreateWebSocket() {
        countDownTimer.start();
    }

    public void closeConnection() {
        try {
            client.dispatcher().executorService().shutdown();
            ws.close(1000, "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void unsubscribe() {
        countDownTimer.cancel();
    }

    public void execute(JsonObject json) {
        ws.send(json.toString());
    }

    public JsonObject getParamSendInvoiceAttachment(String messageId, SelectedInvoice invoice){
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE);

        JsonObject data = new JsonObject();
        data.addProperty("message_id", Integer.parseInt(messageId));
        data.addProperty("message", invoice.getInvoiceNo());
        SimpleDateFormat date = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        data.addProperty("start_time", date.format(Calendar.getInstance().getTime()));
        data.addProperty("attachment_type", 7);

        JsonObject payload = new JsonObject();
        payload.addProperty("type_id", invoice.getInvoiceType());
        payload.addProperty("type", invoice.getInvoiceTypeStr());

        JsonObject payloadAttributes = new JsonObject();
        payloadAttributes.addProperty("id", invoice.getInvoiceId());
        payloadAttributes.addProperty("code", invoice.getInvoiceNo());
        payloadAttributes.addProperty("title", invoice.getTopProductName());
        payloadAttributes.addProperty("description", invoice.getDescription());
        payloadAttributes.addProperty("create_time", invoice.getDate());
        payloadAttributes.addProperty("image_url", invoice.getTopProductImage());
        payloadAttributes.addProperty("href_url", invoice.getInvoiceUrl());
        payloadAttributes.addProperty("status_id", invoice.getStatusId());
        payloadAttributes.addProperty("status", invoice.getStatus());
        payloadAttributes.addProperty("total_amount", invoice.getAmount());

        payload.add("attributes", payloadAttributes);
        data.add("payload", payload);
        json.add("data", data);
        
        return json;
    }

    public JsonObject getParamSendProductAttachment(String messageId, ResultProduct product) throws JSONException{
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE);
        JsonObject data = new JsonObject();
        data.addProperty("message_id", Integer.valueOf(messageId));
        data.addProperty("message", product.getProductUrl());
        SimpleDateFormat date = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        data.addProperty("start_time", date.format(Calendar.getInstance().getTime()));
        data.addProperty("attachment_type", 3);
        data.addProperty("product_id", product.getProductId());

        JsonObject productProfile = new JsonObject();
        productProfile.addProperty("name", product.getName());
        productProfile.addProperty("price", product.getPrice());
        productProfile.addProperty("image_url", product.getProductImageThumbnail());
        productProfile.addProperty("url", product.getProductUrl());
        data.add("product_profile", productProfile);
        json.add("data", data);
        return json;
    }

    public JsonObject getParamSendReply(String messageId, String reply){
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE);
        JsonObject data = new JsonObject();
        data.addProperty("message_id", Integer.valueOf(messageId));
        data.addProperty("message", reply);
        SimpleDateFormat date = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        data.addProperty("start_time", date.format(Calendar.getInstance().getTime()));
        json.add("data", data);
        return json;
    }

    public JsonObject getParamSendImage(String messageId, String path){
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_REPLY_MESSAGE);
        JsonObject data = new JsonObject();
        data.addProperty("message_id", Integer.valueOf(messageId));
        data.addProperty("message", InboxChatConstant.UPLOADING);
        SimpleDateFormat date = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        data.addProperty("start_time", date.format(Calendar.getInstance().getTime()));
        data.addProperty("file_path", path);
        data.addProperty("attachment_type", 2);
        json.add("data", data);
        return json;
    }

    public JsonObject getReadMessage(String messageId){

        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_READ_MESSAGE);
        JsonObject data = new JsonObject();
        data.addProperty("msg_id", Integer.valueOf(messageId));
        json.add("data", data);
        return json;
    }

    public JsonObject getParamStartTyping(String messageId){
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_TYPING);
        JsonObject data = new JsonObject();
        data.addProperty("msg_id", Integer.valueOf(messageId));
        json.add("data", data);
        return json;
    }

    public JsonObject getParamStopTyping(String messageId){
        JsonObject json = new JsonObject();
        json.addProperty("code", ChatWebSocketConstant.EVENT_TOPCHAT_END_TYPING);
        JsonObject data = new JsonObject();
        data.addProperty("msg_id", Integer.valueOf(messageId));
        json.add("data", data);
        return json;
    }
}
