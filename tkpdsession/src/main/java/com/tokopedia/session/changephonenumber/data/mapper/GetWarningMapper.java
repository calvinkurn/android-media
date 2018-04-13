package com.tokopedia.session.changephonenumber.data.mapper;

import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.changephonenumber.data.model.GetWarningData;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by milhamj on 27/12/17.
 */

public class GetWarningMapper implements Func1<Response<TkpdResponse>, WarningViewModel> {
    @Inject
    public GetWarningMapper() {
    }

    @Override
    public WarningViewModel call(Response<TkpdResponse> tkpdResponseResponse) {
        WarningViewModel model = new WarningViewModel();
        if (tkpdResponseResponse.isSuccessful()) {
            if (!tkpdResponseResponse.body().isError() &&
                    (tkpdResponseResponse.body().getErrorMessageJoined().isEmpty() ||
                            tkpdResponseResponse.body().getErrorMessages() == null)
                    ) {
                GetWarningData data = tkpdResponseResponse.body().convertDataObj(
                        GetWarningData.class);
                model.setAction(data.getAction());
                model.setTokocash(data.getTokocash());
                model.setTokopediaBalance(data.getSaldo());
                model.setTokocashNumber(data.getTokocashNumber());
                model.setTokopediaBalanceNumber(data.getSaldoNumber());
                model.setWarningList(data.getWarning());
                model.setHasBankAccount(data.getHasBankAccount());
            } else {
                if (tkpdResponseResponse.body().getErrorMessages() != null &&
                        !tkpdResponseResponse.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(
                            tkpdResponseResponse.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException("");
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(tkpdResponseResponse.code()));
        }

        return model;
    }
}