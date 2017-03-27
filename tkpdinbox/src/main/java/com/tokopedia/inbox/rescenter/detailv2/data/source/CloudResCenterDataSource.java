package com.tokopedia.inbox.rescenter.detailv2.data.source;

import android.content.Context;

import com.tokopedia.core.network.apiservices.rescenter.ResolutionService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.network.retrofit.utils.TKPDMapParam;
import com.tokopedia.inbox.rescenter.detailv2.data.mapper.DetailResCenterMapper;
import com.tokopedia.inbox.rescenter.historyaction.data.mapper.HistoryActionMapper;
import com.tokopedia.inbox.rescenter.historyaction.domain.model.HistoryActionData;
import com.tokopedia.inbox.rescenter.historyaddress.data.mapper.HistoryAddressMapper;
import com.tokopedia.inbox.rescenter.historyaddress.domain.model.HistoryAddressData;
import com.tokopedia.inbox.rescenter.historyawb.data.mapper.HistoryAwbMapper;
import com.tokopedia.inbox.rescenter.detailv2.domain.model.DetailResCenter;
import com.tokopedia.inbox.rescenter.historyawb.domain.model.HistoryAwbData;

import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;

/**
 * Created by hangnadi on 3/9/17.
 */

public class CloudResCenterDataSource {

    private Context context;
    private ResolutionService resolutionService;
    private DetailResCenterMapper detailResCenterMapper;
    private HistoryAwbMapper historyAwbMapper;
    private HistoryAddressMapper historyAddressMapper;
    private HistoryActionMapper historyActionMapper;

    public CloudResCenterDataSource(Context context,
                                    ResolutionService resolutionService,
                                    DetailResCenterMapper detailResCenterMapper,
                                    HistoryAwbMapper historyAwbMapper,
                                    HistoryAddressMapper historyAddressMapper,
                                    HistoryActionMapper historyActionMapper) {
        super();
        this.context = context;
        this.resolutionService = resolutionService;
        this.detailResCenterMapper = detailResCenterMapper;
        this.historyAwbMapper = historyAwbMapper;
        this.historyAddressMapper = historyAddressMapper;
        this.historyActionMapper = historyActionMapper;
    }

    public Observable<DetailResCenter> getResCenterDetail(String resolutionID, TKPDMapParam<String, Object> parameters) {
        return resolutionService.getApi()
                .getResCenterDetail(
                        resolutionID,
                        AuthUtil.generateParamsNetwork2(context, parameters)
                )
                .map(detailResCenterMapper);
    }

    public Observable<DetailResCenter> getResCenterConversation(String resolutionID,
                                                                TKPDMapParam<String, Object> parameters) {
        return resolutionService.getApi()
                .getResCenterConversation(
                        resolutionID,
                        AuthUtil.generateParamsNetwork2(context, parameters)
                )
                .map(detailResCenterMapper);
    }

    public Observable<DetailResCenter> getResCenterConversationMore(String resolutionID,
                                                                    String conversationID,
                                                                    TKPDMapParam<String, Object> parameters) {
        return resolutionService.getApi()
                .getResCenterConversationMore(
                        resolutionID,
                        conversationID,
                        AuthUtil.generateParamsNetwork2(context, parameters)
                )
                .map(detailResCenterMapper);
    }

    public Observable<HistoryAwbData> getHistoryAwb(String resolutionID,
                                                    TKPDMapParam<String, Object> parameters) {
        return resolutionService.getApi()
                .getHistoryAwb(
                        resolutionID,
                        AuthUtil.generateParamsNetwork2(context, parameters)
                )
                .map(historyAwbMapper);
    }

    public Observable<HistoryActionData> getHistoryAction(String resolutionID,
                                                          TKPDMapParam<String, Object> parameters) {
        return resolutionService.getApi()
                .getHistoryAction(resolutionID, AuthUtil.generateParamsNetwork2(context, parameters))
                .map(historyActionMapper);
    }

    public Observable<HistoryAddressData> getHistoryAddress(String resolutionID,
                                                            TKPDMapParam<String, Object> parameters) {
        return resolutionService.getApi()
                .getHistoryAddress(
                        resolutionID,
                        AuthUtil.generateParamsNetwork2(context, parameters)
                )
                .map(historyAddressMapper);
    }

    public Observable<HistoryAwbData> getListProduct(String resolutionID,
                                                     TKPDMapParam<String, Object> parameters) {
        return resolutionService.getApi()
                .getListProduct(
                        resolutionID,
                        AuthUtil.generateParamsNetwork2(context, parameters)
                )
                .map(historyAwbMapper);
    }

    public Observable<HistoryAwbData> getProductDetail(String resolutionID,
                                                       String troubleID,
                                                       TKPDMapParam<String, Object> parameters) {
        return resolutionService.getApi()
                .getProductDetail(
                        resolutionID,
                        troubleID,
                        AuthUtil.generateParamsNetwork2(context, parameters)
                )
                .map(historyAwbMapper);
    }
}
