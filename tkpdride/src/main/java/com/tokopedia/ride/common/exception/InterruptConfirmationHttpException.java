package com.tokopedia.ride.common.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.ride.common.exception.model.InterruptConfirmationExceptionEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by alvarisi on 3/29/17.
 */

public class InterruptConfirmationHttpException extends IOException {
    private String tosUrl;
    private String message;
    private String type;

    public InterruptConfirmationHttpException() {
        super("Request data is invalid, please check message");
    }

    public InterruptConfirmationHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public InterruptConfirmationHttpException(String errorMessage) {
        super(errorMessage);
        String newResponseString = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(errorMessage);
            if (jsonObject.has("data")) {
                newResponseString = jsonObject.getString("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            newResponseString = null;
        }
        Gson gson = new GsonBuilder().create();
        try {
            InterruptConfirmationExceptionEntity entity = gson.fromJson(newResponseString, InterruptConfirmationExceptionEntity.class);
            if (entity.getCode() != null && entity.getCode().length() > 0) {
                message = entity.getMessage();
                setType(entity.getCode());

                switch (entity.getCode()) {
                    case "tos_accept_confirmation":
                        tosUrl = entity.getMeta().getTosAcceptConfirmationEntity().getHref();
                        break;
                    case "surge_confirmation":
                        tosUrl = entity.getMeta().getSurgeConfirmationEntity().getHref();
                        break;
                    case "wallet_activation":
                        tosUrl = entity.getMeta().getWalletActivationEntity().getHref();
                        break;
                    case "wallet_topup":
                        tosUrl = entity.getMeta().getWalletTopupEntity().getHref();
                        break;
                }

            } else {
                throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }
        } catch (JsonSyntaxException exception) {
            initCause(exception);
        } catch (RuntimeException e) {
            initCause(e);
        }
    }

    public String getTosUrl() {
        return tosUrl;
    }

    public void setTosUrl(String tosUrl) {
        this.tosUrl = tosUrl;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}