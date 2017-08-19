package com.tokopedia.tkpd.tkpdreputation.inbox.data.mapper;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.ErrorMessageException;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.tkpd.tkpdreputation.R;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.Datum;
import com.tokopedia.tkpd.tkpdreputation.inbox.data.pojo.inboxdetail.InboxReputationDetailPojo;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailDomain;
import com.tokopedia.tkpd.tkpdreputation.inbox.domain.model.inboxdetail.InboxReputationDetailItemDomain;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author by nisie on 8/19/17.
 */

public class InboxReputationDetailMapper implements Func1<Response<TkpdResponse>, InboxReputationDetailDomain> {
    @Override
    public InboxReputationDetailDomain call(Response<TkpdResponse> response) {

        if (response.isSuccessful()) {
            if (!response.body().isNullData()) {
                InboxReputationDetailPojo data = response.body()
                        .convertDataObj(InboxReputationDetailPojo.class);
                return mappingToDomain(data);
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
            if (response.body().getErrorMessages() == null
                    && response.body().getErrorMessages().isEmpty()) {
                throw new RuntimeException(String.valueOf(response.code()));
            } else {
                throw new ErrorMessageException(response.body().getErrorMessageJoined());
            }
        }
    }

    private InboxReputationDetailDomain mappingToDomain(InboxReputationDetailPojo data) {
//        return new InboxReputationDetailDomain(convertToListReview(data));
        return null;
    }

    private List<InboxReputationDetailItemDomain> convertToListReview(InboxReputationDetailPojo data) {
        List<InboxReputationDetailItemDomain> list = new ArrayList<>();
        for (Datum pojo : data.getData()) {
            list.add(convertToReputationItem(pojo));
        }

        return list;
    }

    private InboxReputationDetailItemDomain convertToReputationItem(Datum pojo) {
        return new InboxReputationDetailItemDomain();
    }
}
