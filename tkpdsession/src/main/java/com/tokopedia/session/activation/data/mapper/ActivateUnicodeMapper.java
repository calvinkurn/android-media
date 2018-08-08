package com.tokopedia.session.activation.data.mapper;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.session.activation.data.ActivateUnicodeModel;
import com.tokopedia.session.activation.data.pojo.ActivateUnicodeData;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by nisie on 4/17/17.
 */

public class ActivateUnicodeMapper implements Func1<Response<String>, ActivateUnicodeModel> {

    @Override
    public ActivateUnicodeModel call(Response<String> response) {
        return mappingResponse(response);
    }

    private ActivateUnicodeModel mappingResponse(Response<String> response) {
        ActivateUnicodeModel model = new ActivateUnicodeModel();

        if (response.isSuccessful()) {
            GsonBuilder gsonb = new GsonBuilder();
            Gson gson = gsonb.create();
            try {
                JSONObject jsonObj = new JSONObject(response.body());
                ActivateUnicodeData data = gson.fromJson(jsonObj.toString(), ActivateUnicodeData.class);
                if (data.getError() != null && data.getErrorDescription() != null
                        && !data.getError().equals("") && !data.getErrorDescription().equals("")) {
                    throw new ErrorMessageException(data.getErrorDescription());
                } else {
                    model.setActivateUnicodeData(data);
                    model.setSuccess(true);
                }
            } catch (JSONException e) {
                model.setSuccess(false);
            }

            model.setResponseCode(response.code());
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
