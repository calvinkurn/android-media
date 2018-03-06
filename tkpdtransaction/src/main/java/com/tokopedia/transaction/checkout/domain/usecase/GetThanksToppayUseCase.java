package com.tokopedia.transaction.checkout.domain.usecase;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.transaction.checkout.data.repository.ITopPayRepository;
import com.tokopedia.transaction.checkout.domain.datamodel.toppay.ThanksTopPayData;
import com.tokopedia.transaction.checkout.domain.mapper.ITopPayMapper;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import javax.inject.Inject;

import rx.Observable;
import rx.functions.Func1;

/**
 * @author anggaprasetiyo on 06/03/18.
 */

public class GetThanksToppayUseCase extends UseCase<ThanksTopPayData> {
    public static final String PARAM_TRANSACTION_ID = "id";
    private final ITopPayRepository topPayRepository;
    private final ITopPayMapper topPayMapper;

    @Inject
    public GetThanksToppayUseCase(ITopPayRepository topPayRepository, ITopPayMapper topPayMapper) {
        this.topPayRepository = topPayRepository;
        this.topPayMapper = topPayMapper;
    }

    @Override
    public Observable<ThanksTopPayData> createObservable(RequestParams requestParams) {
        TKPDMapParam<String, String> param = new TKPDMapParam<>();
        param.put(PARAM_TRANSACTION_ID, requestParams.getString(PARAM_TRANSACTION_ID, ""));
        return topPayRepository.getThanksTopPay(
                AuthUtil.generateParamsNetwork(MainApplication.getAppContext(), param)
        ).map(new Func1<com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData, ThanksTopPayData>() {
            @Override
            public ThanksTopPayData call(
                    com.tokopedia.transaction.cart.model.thankstoppaydata.ThanksTopPayData
                            thanksTopPayData) {
                return topPayMapper.convertThanksTopPayData(thanksTopPayData);
            }
        });
    }
}
