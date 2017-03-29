package com.tokopedia.ride.common.network;

import android.content.Intent;
import android.util.Log;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.exception.SessionExpiredException;
import com.tokopedia.core.network.retrofit.interceptors.TkpdAuthInterceptor;
import com.tokopedia.core.network.retrofit.interceptors.TkpdBaseInterceptor;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.ride.common.exception.TosConfirmationHttpException;
import com.tokopedia.ride.common.exception.UnprocessableEntityHttpException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by alvarisi on 3/14/17.
 */

public class RideInterceptor extends TkpdBaseInterceptor {
    private static final String TAG = RideInterceptor.class.getSimpleName();
    private static final String HEADER_X_APP_VERSION = "X-APP-VERSION";
    private String authorizationString;
    private String userId;

    public RideInterceptor(String authorizationString, String userId) {
        this.authorizationString = authorizationString;
        this.userId = userId;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder newRequest = getBearerHeaderBuilder(chain.request(), authorizationString, userId);

        final Request finalRequest = newRequest.build();
        Response response = getResponse(chain, finalRequest);

        String bodyResponse = response.body().string();
        try {
            JSONObject jsonResponse = new JSONObject(bodyResponse);
            String JSON_ERROR_KEY = "message_error";
            if (jsonResponse.has(JSON_ERROR_KEY)) {
                handleError(jsonResponse.getString(JSON_ERROR_KEY));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return constructNewResponse(response, bodyResponse);
    }

    private void handleError(String errorMessage) throws SessionExpiredException,
            UnprocessableEntityHttpException {
        if (errorMessage.equals("invalid_request") || errorMessage.equals("invalid_grant"))
            throw new SessionExpiredException(errorMessage);
        else
            throw new UnprocessableEntityHttpException(errorMessage);
    }

    private Response constructNewResponse(Response response, String responseString) {
        try {
            JSONObject jsonObject = new JSONObject(responseString);
            if (jsonObject.has("data")) {
                String newResponseString = jsonObject.getString("data");
                return createNewResponse(response, newResponseString);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return response;
        }

        return response;
    }

    private Response createNewResponse(Response oldResponse, String oldBodyResponse) {
        ResponseBody body = ResponseBody.create(oldResponse.body().contentType(), oldBodyResponse);

        Response.Builder builder = new Response.Builder();
        builder.body(body)
                .headers(oldResponse.headers())
                .message(oldResponse.message())
                .handshake(oldResponse.handshake())
                .protocol(oldResponse.protocol())
                .cacheResponse(oldResponse.cacheResponse())
                .priorResponse(oldResponse.priorResponse())
                .code(oldResponse.code())
                .request(oldResponse.request())
                .networkResponse(oldResponse.networkResponse());

        return builder.build();
    }

    private Request.Builder getBearerHeaderBuilder(Request request, String oAuth, String userId) {
        return request.newBuilder()
                .header("Tkpd-UserId", userId)
                .header("Authorization", oAuth)
                .header("X-Device", "android-" + GlobalConfig.VERSION_NAME)
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header(HEADER_X_APP_VERSION, "android-" + String.valueOf(GlobalConfig.VERSION_NAME))
                .method(request.method(), request.body());
    }

    protected Response getResponse(Chain chain, Request request) throws IOException {
        Response response = chain.proceed(request);
        if (!response.isSuccessful()) {
            switch (response.code()) {
                case 409:
                    String errorMessage = response.body().string();
                    throw new TosConfirmationHttpException(errorMessage);
                default:
                    throw new RuntimeException("Terjadi kesalahan Server");
            }
        }
        return response;
    }
}
