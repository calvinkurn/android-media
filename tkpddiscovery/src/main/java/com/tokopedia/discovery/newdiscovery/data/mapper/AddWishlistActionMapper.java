package com.tokopedia.discovery.newdiscovery.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.discovery.newdiscovery.domain.model.ActionResultModel;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by henrypriyono on 10/19/17.
 */

public class AddWishlistActionMapper implements Func1<Response<TkpdResponse>, ActionResultModel> {
    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";
    private static final String ERROR_MESSAGE = "message_error";

    @Override
    public ActionResultModel call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private ActionResultModel mappingResponse(Response<TkpdResponse> response) {
        ActionResultModel model = new ActionResultModel();

        if (response.isSuccessful()) {
            if (response.code() == ResponseStatus.SC_CREATED) {
                model.setSuccess(true);
            } else {
                StringBuilder msgError = new StringBuilder();
                try {
                    JSONObject jsonObject = new JSONObject(response.errorBody().string());
                    JSONArray jsonArray = jsonObject.getJSONArray(ERROR_MESSAGE);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        msgError.append(jsonArray.get(i).toString()).append(" ");
                    }
                } catch (Exception e) {
                    msgError = new StringBuilder(DEFAULT_ERROR);
                    e.printStackTrace();
                }
                throw new ErrorMessageException(msgError.toString());
            }
        } else {
            throw new RuntimeException(String.valueOf(response.code()));
        }
        return model;
    }
}
