package com.tokopedia.session.changephonenumber.data.mapper;

import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.changephonenumber.data.model.WarningData;
import com.tokopedia.session.changephonenumber.view.viewmodel.WarningViewModel;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by milhamj on 27/12/17.
 */

public class WarningMapper implements Func1<Response<TkpdResponse>, WarningViewModel> {
    @Inject
    public WarningMapper() {
    }

    @Override
    public WarningViewModel call(Response<TkpdResponse> tkpdResponseResponse) {
        WarningViewModel model = new WarningViewModel();
        if (tkpdResponseResponse.isSuccessful()) {
            if (!tkpdResponseResponse.body().isError() &&
                    (tkpdResponseResponse.body().getErrorMessageJoined().isEmpty() ||
                            tkpdResponseResponse.body().getErrorMessages() == null)
                    ) {
                WarningData data = tkpdResponseResponse.body().convertDataObj(WarningData.class);
                model.setAction(data.getAction());
                model.setTokocash(data.getTokocash());
                model.setTokopediaBalance(data.getSaldo());
                model.setWarningList(data.getWarning());
            } else {
                if (tkpdResponseResponse.body().getErrorMessages() != null &&
                        !tkpdResponseResponse.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(tkpdResponseResponse.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(ErrorMessageException.DEFAULT_ERROR);
                }
            }
        } else {
            throw new RuntimeException(String.valueOf(tkpdResponseResponse.code()));
        }

        return model;
    }
}