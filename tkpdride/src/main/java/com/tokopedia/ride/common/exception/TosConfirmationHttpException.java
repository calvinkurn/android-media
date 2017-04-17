package com.tokopedia.ride.common.exception;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
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
    private String type;

    public TosConfirmationHttpException() {
        super("Request data is invalid, please check message");
    }

    public TosConfirmationHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public TosConfirmationHttpException(String errorMessage) {
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
            TosConfirmationExceptionEntity entity = gson.fromJson(newResponseString, TosConfirmationExceptionEntity.class);
            if (entity.getErrors().size() > 0) {
                switch (entity.getErrors().get(0).getCode()){
                    case "tos_confirm":
                        tosId = entity.getMeta().getTosAcceptConfirmationEntity().getTosId();
                        tosUrl = entity.getMeta().getTosAcceptConfirmationEntity().getHref();
                        message = entity.getErrors().get(0).getTitle();
                        setType(entity.getErrors().get(0).getCode());
                        break;
                    case "surge":
                        tosId = entity.getMeta().getSurgeConfirmationEntity().getSurgeConfirmationId();
                        tosUrl = entity.getMeta().getSurgeConfirmationEntity().getHref();
                        message = entity.getErrors().get(0).getTitle();
                        setType(entity.getErrors().get(0).getCode());
                        break;
                }

            } else {
                throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
            }
        } catch (JsonSyntaxException exception) {
            initCause(exception);
        } catch (RuntimeException e){
            initCause(e);
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}