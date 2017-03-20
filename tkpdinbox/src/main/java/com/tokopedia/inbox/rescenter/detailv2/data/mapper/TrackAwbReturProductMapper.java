package com.tokopedia.inbox.rescenter.detailv2.data.mapper;

import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.inbox.rescenter.detail.model.detailresponsedata.ResCenterTrackShipping;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProduct;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.TrackingAwbReturProductHistory;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/16/17.
 */

public class TrackAwbReturProductMapper implements Func1<Response<TkpdResponse>, TrackingAwbReturProduct> {

    public TrackAwbReturProductMapper() {
    }

    @Override
    public TrackingAwbReturProduct call(Response<TkpdResponse> response) {
        TrackingAwbReturProduct domainModel = new TrackingAwbReturProduct();
        if (response.isSuccessful()) {
            if (!response.body().isError()) {
                ResCenterTrackShipping pojo = response.body().convertDataObj(ResCenterTrackShipping.class);
                ResCenterTrackShipping.TrackShipping trackShipping = pojo.getTrackShipping();
                domainModel.setSuccess(true);
                domainModel.setShippingRefNum(trackShipping.getShippingRefNum());
                domainModel.setReceiverName(trackShipping.getReceiverName());
                domainModel.setDelivered(trackShipping.getDelivered() == 1);
                domainModel.setTrackingHistory(mappingTrackingHistory(trackShipping.getTrackHistory()));
            } else {
                domainModel.setSuccess(false);
                domainModel.setMessageError(generateMessageError(response));
            }
        } else {
            domainModel.setSuccess(false);
            domainModel.setErrorCode(response.code());
        }
        return domainModel;
    }

    private List<TrackingAwbReturProductHistory> mappingTrackingHistory(List<ResCenterTrackShipping.TrackHistory> trackHistorys) {
        List<TrackingAwbReturProductHistory> domainModels = new ArrayList<>();
        for (ResCenterTrackShipping.TrackHistory items : trackHistorys) {
            TrackingAwbReturProductHistory model = new TrackingAwbReturProductHistory();
            model.setCity(items.getCity());
            model.setDate(items.getDate());
            model.setStatus(items.getStatus());
            domainModels.add(model);
        }
        return domainModels;
    }

    private String generateMessageError(Response<TkpdResponse> response) {
        return response.body().getErrorMessageJoined();
    }
}
