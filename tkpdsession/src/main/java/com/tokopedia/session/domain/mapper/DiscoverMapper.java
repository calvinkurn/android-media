package com.tokopedia.session.domain.mapper;

import android.text.TextUtils;

import com.tokopedia.network.ErrorMessageException;
import com.tokopedia.network.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.session.data.viewmodel.DiscoverViewModel;
import com.tokopedia.session.domain.pojo.discover.DiscoverItemPojo;
import com.tokopedia.session.domain.pojo.discover.DiscoverPojo;
import com.tokopedia.session.register.view.viewmodel.DiscoverItemViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 10/10/17.
 */

public class DiscoverMapper implements Func1<Response<TkpdResponse>, DiscoverViewModel> {

    @Inject
    public DiscoverMapper() {
    }

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
                    throw new ErrorMessageException("");
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
