package com.tokopedia.flight.orderlist.data.cloud;

import com.tokopedia.abstraction.common.data.model.response.DataResponse;
import com.tokopedia.flight.common.data.source.cloud.api.FlightApi;
import com.tokopedia.flight.orderlist.data.cloud.entity.OrderEntity;
import com.tokopedia.flight.orderlist.data.cloud.entity.SendEmailEntity;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by alvarisi on 12/6/17.
 */

public class FlightOrderDataSource {
    private FlightApi flightApi;

    @Inject
    public FlightOrderDataSource(FlightApi flightApi) {
        this.flightApi = flightApi;
    }

    public Observable<List<OrderEntity>> getOrders(Map<String, Object> maps) {
        return flightApi.getOrders(maps).map(new Func1<Response<DataResponse<List<OrderEntity>>>, List<OrderEntity>>() {
            @Override
            public List<OrderEntity> call(Response<DataResponse<List<OrderEntity>>> dataResponseResponse) {
                return dataResponseResponse.body().getData();
            }
        });
    }

    public Observable<OrderEntity> getOrder(String id) {
        return flightApi.getOrder(id).map(new Func1<Response<DataResponse<OrderEntity>>, OrderEntity>() {
            @Override
            public OrderEntity call(Response<DataResponse<OrderEntity>> dataResponseResponse) {
                return dataResponseResponse.body().getData();
            }
        });
    }

    public Observable<SendEmailEntity> sendEmail(Map<String, Object> maps) {
        return flightApi.sendEmail(maps).map(new Func1<Response<SendEmailEntity>, SendEmailEntity>() {
            @Override
            public SendEmailEntity call(Response<SendEmailEntity> emailResponse) {
                return emailResponse.body();
            }
        });
    }
}
