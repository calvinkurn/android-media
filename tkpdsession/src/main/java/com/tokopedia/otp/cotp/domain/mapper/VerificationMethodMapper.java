package com.tokopedia.otp.cotp.domain.mapper;

import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.otp.cotp.domain.pojo.ListMethodItemPojo;
import com.tokopedia.otp.cotp.domain.pojo.ModeList;
import com.tokopedia.otp.cotp.view.viewmodel.ListVerificationMethod;
import com.tokopedia.otp.cotp.view.viewmodel.MethodItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 1/18/18.
 */

public class VerificationMethodMapper implements Func1<Response<String>, ListVerificationMethod> {

    private int IS_SUCCESS = 1;

    @Inject
    public VerificationMethodMapper() {
    }

    @Override
    public ListVerificationMethod call(Response<String> response) {

        String responseString = String.valueOf(response.body());
        try {
            JSONObject responseJson = new JSONObject(responseString);
            if (response.isSuccessful()
                    && !checkHasErrorHeader(responseJson)) {
                String responseBody = responseJson.getString("data");
                ListMethodItemPojo pojo = new GsonBuilder().create().fromJson(responseBody,
                        ListMethodItemPojo.class);
                if (pojo.getIsSuccess() == IS_SUCCESS) {
                    return convertToDomain(pojo);
                } else {
                    throw new ErrorMessageException("");
                }
            } else if (checkHasErrorHeader(responseJson)) {
                throw new ErrorMessageException(getErrorMessage(response));
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }

        } catch (JSONException e) {
            e.printStackTrace();
            throw new ErrorMessageException("");
        }


    }

    private String getErrorMessage(Response<String> response) {
        try {
            String responseString = String.valueOf(response.body());
            JSONObject responseJson = new JSONObject(responseString);
            JSONObject header = responseJson.getJSONObject("header");
            JSONArray listMessage = header.getJSONArray("messages");
            return listMessage.toString();
        } catch (JSONException e) {
            return "";
        }
    }

    private boolean checkHasErrorHeader(JSONObject responseJson) {
        try {
            JSONObject header = responseJson.getJSONObject("header");
            JSONArray listMessage = header.getJSONArray("messages");
            String errorCode = header.getString("error_code");
            return listMessage.length() > 0
                    && !TextUtils.isEmpty(errorCode);
        } catch (JSONException e) {
            return false;
        }
    }

    private ListVerificationMethod convertToDomain(ListMethodItemPojo pojo) {
        ArrayList<MethodItem> list = new ArrayList<>();
        for (ModeList modePojo : pojo.getModeList()) {
            list.add(new MethodItem(
                    modePojo.getModeText(),
                    modePojo.getOtpListImgUrl(),
                    modePojo.getOtpListText(),
                    modePojo.getAfterOtpListText()
            ));
        }
        return new ListVerificationMethod(list);
    }
}
