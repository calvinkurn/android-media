package com.tokopedia.ride.common.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.ride.common.exception.model.TosConfirmationExceptionEntity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by alvarisi on 3/29/17.
 */

public class TosConfirmationHttpException extends IOException {
    private String tosUrl;
    private String tosId;
    private String message;

    public TosConfirmationHttpException() {
        super("Request data is invalid, please check message");
    }

    public TosConfirmationHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public TosConfirmationHttpException(String errorMessage) {
        super(errorMessage);
        String newResponseString = null;
        try {
            JSONObject jsonObject = new JSONObject(errorMessage);
            if (jsonObject.has("data")) {
                newResponseString = jsonObject.getString("data");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            newResponseString = null;
        }
        Gson gson = new GsonBuilder().create();
        try {

            TosConfirmationExceptionEntity entity = gson.fromJson(newResponseString, TosConfirmationExceptionEntity.class);
            tosId = entity.getMeta().getTosAcceptConfirmationEntity().getTosId();
            tosUrl = entity.getMeta().getTosAcceptConfirmationEntity().getHref();
            if (entity.getErrors().size() > 0)
                message = entity.getErrors().get(0).getTitle();
            else
                message = "Terjadi kesalahan";
        } catch (JsonSyntaxException exception) {
            initCause(exception);
        }
    }

    public String getTosUrl() {
        return tosUrl;
    }

    public void setTosUrl(String tosUrl) {
        this.tosUrl = tosUrl;
    }

    public String getTosId() {
        return tosId;
    }

    public void setTosId(String tosId) {
        this.tosId = tosId;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}