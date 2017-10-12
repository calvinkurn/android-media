package com.tokopedia.session.register.data.mapper;

import android.text.TextUtils;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.R;
import com.tokopedia.session.register.data.model.DiscoverViewModel;
import com.tokopedia.session.register.data.pojo.DiscoverItemPojo;
import com.tokopedia.session.register.data.pojo.DiscoverPojo;
import com.tokopedia.session.register.view.viewmodel.DiscoverItemViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverMapper implements Func1<Response<TkpdResponse>, DiscoverViewModel> {
    @Override
    public DiscoverViewModel call(Response<TkpdResponse> response) {

        if (response.isSuccessful()) {
            if ((!response.body().isNullData()
                    && response.body().getErrorMessageJoined().equals(""))
                    || (!response.body().isNullData()
                    && response.body().getErrorMessages() == null)) {
                DiscoverPojo pojo = response.body().convertDataObj(DiscoverPojo.class);
                return mappingToViewModel(pojo);
            } else {
                if (response.body().getErrorMessages() != null
                        && !response.body().getErrorMessages().isEmpty()) {
                    throw new ErrorMessageException(response.body().getErrorMessageJoined());
                } else {
                    throw new ErrorMessageException(MainApplication.getAppContext().getString
                            (R.string.default_request_error_unknown));
                }
            }
        } else {
            String messageError = ErrorHandler.getErrorMessage(response);
            if (!TextUtils.isEmpty(messageError)) {
                throw new ErrorMessageException(messageError);
            } else {
                throw new RuntimeException(String.valueOf(response.code()));
            }
        }
    }

    private DiscoverViewModel mappingToViewModel(DiscoverPojo pojo) {
        return new DiscoverViewModel(
                convertToDiscoverItem(pojo.getProviders()),
                pojo.getUrlBackground()
        );
    }

    private ArrayList<DiscoverItemViewModel> convertToDiscoverItem(List<DiscoverItemPojo> providers) {
        ArrayList<DiscoverItemViewModel> list = new ArrayList<>();
        for (DiscoverItemPojo pojo : providers) {
            list.add(new DiscoverItemViewModel(pojo.getId(),
                    pojo.getName(),
                    pojo.getUrl(),
                    pojo.getImage(),
                    pojo.getColor()));
        }
        return list;
    }
}
