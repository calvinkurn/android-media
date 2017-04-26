package com.tokopedia.ride.common.exception;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by alvarisi on 3/29/17.
 */

public class InvalidFareIdHttpException extends IOException {
    private String title;
    private String code;

    public InvalidFareIdHttpException() {
        super("Request data is invalid, please check message");
    }

    public InvalidFareIdHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidFareIdHttpException(String errorMessage) {
        super(errorMessage);
        JSONObject dataJsonObject = null;
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(errorMessage);
            dataJsonObject = jsonObject.optJSONObject("data");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (dataJsonObject != null) {
            title = dataJsonObject.optString("message");
            code = dataJsonObject.optString("code");
        }
    }
}