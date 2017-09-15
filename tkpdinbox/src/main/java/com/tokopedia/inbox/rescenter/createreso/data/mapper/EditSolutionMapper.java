package com.tokopedia.inbox.rescenter.createreso.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ResponseStatus;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.productproblem.AmountResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditFreeReturnResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditSolutionResponse;
import com.tokopedia.inbox.rescenter.createreso.data.pojo.solution.EditSolutionResponseResponse;
import com.tokopedia.inbox.rescenter.createreso.domain.model.productproblem.AmountDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.EditSolutionResponseDomain;
import com.tokopedia.inbox.rescenter.createreso.domain.model.solution.FreeReturnDomain;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by yoasfs on 24/08/17.
 */

public class EditSolutionMapper implements Func1<Response<TkpdResponse>, EditSolutionResponseDomain> {

    private static final String DEFAULT_ERROR = "Terjadi kesalahan, mohon coba kembali.";
    private static final String ERROR_MESSAGE = "message_error";


    public EditSolutionMapper() {
    }

    @Override
    public EditSolutionResponseDomain call(Response<TkpdResponse> response) {
        return mappingResponse(response);
    }


    private EditSolutionResponseDomain mappingResponse(Response<TkpdResponse> response) {
        EditSolutionResponseResponse editSolutionResponseResponse =
                response.body().convertDataObj(EditSolutionResponseResponse.class);
        EditSolutionResponseDomain model = new EditSolutionResponseDomain(
                editSolutionResponseResponse.getSolution().size() != 0 ?
                        mappingSolutionDomain(editSolutionResponseResponse.getSolution()) :
                        new ArrayList<EditSolutionDomain>(),
                editSolutionResponseResponse.getFreeReturn() != null ?
                        mappingFreeReturnDomain(editSolutionResponseResponse.getFreeReturn()) :
                        null);
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
    }

    private List<EditSolutionDomain> mappingSolutionDomain(List<EditSolutionResponse> response) {
        List<EditSolutionDomain> domainList = new ArrayList<>();
        for (EditSolutionResponse solutionResponse : response) {
            EditSolutionDomain solutionDomain = new EditSolutionDomain(solutionResponse.getId(),
                    solutionResponse.getName(),
                    solutionResponse.getAmount() != null ?
                            mappingAmountDomain(solutionResponse.getAmount()) :
                            null);
            domainList.add(solutionDomain);
        }
        return domainList;
    }

    private AmountDomain mappingAmountDomain(AmountResponse response) {
        return new AmountDomain(response.getIdr(),
                response.getInteger());
    }

    private FreeReturnDomain mappingFreeReturnDomain(EditFreeReturnResponse response) {
        return new FreeReturnDomain(response.getInfo());
    }
}
