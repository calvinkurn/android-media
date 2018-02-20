package com.tokopedia.transaction.checkout.domain;

import com.tokopedia.abstraction.common.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.domain.response.rates.RatesResponse;
import com.tokopedia.transaction.checkout.view.data.ShipmentDetailData;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by Irfan Khoirul on 20/02/18.
 */

public class GetRatesUseCase extends UseCase<ShipmentDetailData> {

    private RatesRepository repository;
    private ShipmentDetailData shipmentDetailData;

    @Inject
    public GetRatesUseCase(RatesRepository repository) {
        this.repository = repository;
    }

    public void setShipmentDetailData(ShipmentDetailData shipmentDetailData) {
        this.shipmentDetailData = shipmentDetailData;
    }

    @Override
    public Observable<ShipmentDetailData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> mapParam = new TKPDMapParam<>();
        mapParam.putAll(requestParams.getParamsAllValueInString());
        return repository.getRates(shipmentDetailData, mapParam);
    }

    public RequestParams getParams() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(Params.SERVICE, "instant,sameday,regular");
        requestParams.putString(Params.NAMES, "jne,pos,gojek");
        requestParams.putString(Params.ORIGIN, "5573|11410|-6.222334,108.223112555");
        requestParams.putString(Params.DESTINATION, "2170|44233|-7.11000231,108.9210222");
        requestParams.putString(Params.WEIGHT, "1.0");
        requestParams.putString(Params.TYPE, "android");
        requestParams.putString(Params.FROM, "client");
        requestParams.putString(Params.TOKEN, "Tokopedia+Kero:dOONiKY6MpKDxXh1PZXbFzZVCXo=");
        requestParams.putString(Params.UT, "1518493640");
        requestParams.putString(Params.INSURANCE, "1");
        requestParams.putString(Params.PRODUCT_INSURANCE, "0");
        requestParams.putString(Params.ORDER_VALUE, "1990000");
        requestParams.putString(Params.CAT_ID, "35,20,60");
        return requestParams;
    }

    interface Params {
        String SERVICE = "service";
        String NAMES = "names";
        String ORIGIN = "origin";
        String DESTINATION = "destination";
        String WEIGHT = "weight";
        String TYPE = "type";
        String FROM = "from";
        String TOKEN = "token";
        String UT = "ut";
        String INSURANCE = "insurance";
        String PRODUCT_INSURANCE = "product_insurance";
        String ORDER_VALUE = "order_value";
        String CAT_ID = "cat_id";
    }

}
