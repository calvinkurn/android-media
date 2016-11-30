package com.tokopedia.transaction.cart.repository.source;

import com.tokopedia.core.network.apiservices.transaction.TXCartService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.cart.repository.entity.CalculateShipmentEntity;
import com.tokopedia.transaction.cart.repository.entity.ShipmentEntity;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author  by alvarisi on 11/30/16.
 */

public class CloudShipmentCartSource {
    public Observable<List<ShipmentEntity>> shipments(Map<String, String> param){
        return Observable.just(param)
                .flatMap(new Func1<Map<String, String>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(Map<String, String> params1) {
                        TXCartService service = new TXCartService();
                        return service
                                .getApi()
                                .calculateCart(params1);
                    }
                }).map(new Func1<Response<TkpdResponse>, List<ShipmentEntity>>() {
                    @Override
                    public List<ShipmentEntity> call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                CalculateShipmentEntity calculateShipmentData = response.body().convertDataObj(CalculateShipmentEntity.class);
                                return calculateShipmentData.getShipment();
                            } else {
                                throw new RuntimeException(response.body().getErrorMessages().get(0));
                            }
                        } else {
                            new ErrorHandler(new ErrorListener() {
                                @Override
                                public void onUnknown() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onTimeout() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_TIMEOUT);
                                }

                                @Override
                                public void onServerError() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onBadRequest() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }

                                @Override
                                public void onForbidden() {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                                }
                            }, response.code());
                        }
                        throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
                    }
                });
    }
}
