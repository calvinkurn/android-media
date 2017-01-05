package com.tokopedia.transaction.cart.interactor.data.source;

import com.tokopedia.core.network.apiservices.transaction.TXCartActService;
import com.tokopedia.core.network.apiservices.transaction.TXCartService;
import com.tokopedia.core.network.apiservices.user.PeopleActService;
import com.tokopedia.core.network.retrofit.response.ErrorHandler;
import com.tokopedia.core.network.retrofit.response.ErrorListener;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.ErrorNetMessage;
import com.tokopedia.transaction.cart.interactor.data.IShipmentCartDataStore;
import com.tokopedia.transaction.cart.interactor.data.entity.CalculateShipmentEntity;
import com.tokopedia.transaction.cart.interactor.data.entity.EditShipmentEntity;
import com.tokopedia.transaction.cart.interactor.data.entity.ShipmentEntity;
import com.tokopedia.transaction.cart.model.savelocation.SaveLocationData;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * @author by alvarisi on 11/30/16.
 */

public class CloudShipmentCartDataStore implements IShipmentCartDataStore {
    private static final String KEY_FLAG_IS_SUCCESS = "is_success";

    public CloudShipmentCartDataStore() {
    }

    public Observable<List<ShipmentEntity>> shipments(Map<String, String> param) {
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

    public Observable<EditShipmentEntity> editShipment(Map<String, String> param) {
        return Observable.just(param)
                .flatMap(new Func1<Map<String, String>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(Map<String, String> params) {
                        TXCartActService service = new TXCartActService();
                        return service
                                .getApi()
                                .editAddress(params);
                    }
                })
                .map(new Func1<Response<TkpdResponse>, EditShipmentEntity>() {
                    @Override
                    public EditShipmentEntity call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                if (!response.body().isNullData() &&
                                        !response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                                    int status = 0;
                                    try {
                                        status = response.body().getJsonData()
                                                .getInt(KEY_FLAG_IS_SUCCESS);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    String message;
                                    if (status == 1) {
                                        message = response.body()
                                                .getStatusMessages().get(0);
                                    } else {
                                        message = response.body().getErrorMessages().get(0);
                                    }
                                    EditShipmentEntity editShipmentCart = new EditShipmentEntity();
                                    editShipmentCart.setStatus(String.valueOf(status));
                                    editShipmentCart.setMessage(message);
                                    return editShipmentCart;
                                } else {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                                }
                            } else {
                                throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_DEFAULT);
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

    public Observable<SaveLocationData> editLocation(Map<String, String> param) {
        return Observable.just(param)
                .flatMap(new Func1<Map<String, String>, Observable<Response<TkpdResponse>>>() {
                    @Override
                    public Observable<Response<TkpdResponse>> call(Map<String, String> params) {
                        PeopleActService service = new PeopleActService();
                        return service
                                .getApi()
                                .editAddress(params);
                    }
                })
                .map(new Func1<Response<TkpdResponse>, SaveLocationData>() {
                    @Override
                    public SaveLocationData call(Response<TkpdResponse> response) {
                        if (response.isSuccessful()) {
                            if (!response.body().isError()) {
                                if (!response.body().isNullData() &&
                                        !response.body().getJsonData().isNull(KEY_FLAG_IS_SUCCESS)) {
                                    return response.body().convertDataObj(SaveLocationData.class);
                                } else {
                                    throw new RuntimeException(ErrorNetMessage.MESSAGE_ERROR_NULL_DATA);
                                }
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
