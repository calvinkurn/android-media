package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.CreateSubmitResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.createreso.ResolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.CreateSubmitDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.createreso.ResolutionDomain;

import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 05/09/17.
 */

public class CreateSubmitMapper implements Func1<Response<TkpdResponse>, CreateSubmitDomain> {
    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";
    private static final String ERROR_MESSAGE = "message_error";

    @Override
    public CreateSubmitDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }

    private CreateSubmitDomain mappingResponse(Response<TkpdResponse> response) {
        try {
            CreateSubmitResponse createSubmitResponse = response.body().convertDataObj(CreateSubmitResponse.class);
            CreateSubmitDomain model = new CreateSubmitDomain(createSubmitResponse.getResolution() != null ? mappingResolutionDomain(createSubmitResponse.getResolution()) : null,
                    createSubmitResponse.getSuccessMessage());
            if (response.isSuccessful()) {
                if (response.raw().code() == ResponseStatus.SC_OK) {
                    model.setSuccess(true);
                } else {
                    try {
                        String msgError = "";
                        JSONObject jsonObject = new JSONObject(response.errorBody().string());
                        JSONArray jsonArray = jsonObject.getJSONArray(ERROR_MESSAGE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            msgError += jsonArray.get(i).toString() + " ";
                        }
                        throw new ErrorMessageException(msgError);
                    } catch (Exception e) {
                        throw new ErrorMessageException(DEFAULT_ERROR);
                    }
                }
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
            return model;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private ResolutionDomain mappingResolutionDomain(ResolutionResponse response) {
        return new ResolutionDomain(response.getId());
    }
}
