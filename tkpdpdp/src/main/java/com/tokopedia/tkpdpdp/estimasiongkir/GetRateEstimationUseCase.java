package com.tokopedia.tkpdpdp.estimasiongkir;

import android.content.Context;

import com.google.gson.Gson;
import com.tokopedia.graphql.data.model.GraphqlRequest;
import com.tokopedia.graphql.domain.GraphqlUseCase;
import com.tokopedia.usecase.RequestParams;
import com.tokopedia.usecase.UseCase;

import java.io.IOException;
import java.io.InputStream;

import com.tokopedia.tkpdpdp.R;

import rx.Observable;

public class GetRateEstimationUseCase extends UseCase<RatesModel> {
    private static final String PARAM_QUERY = "query";
    private static final String PARAM_PRODUCT_ID = "product_id";
    private static final String PARAM_USER_ID = "user_id";
    private final GraphqlUseCase graphqlUseCase;

    // for testing
    private final Context context;
    private final Gson gson = new Gson();

    public GetRateEstimationUseCase(GraphqlUseCase graphqlUseCase, Context context) {
        this.graphqlUseCase = graphqlUseCase;
        this.context = context;
    }

    @Override
    public Observable<RatesModel> createObservable(RequestParams requestParams) {
        String query = requestParams.getString(PARAM_QUERY, "");
        requestParams.clearValue(PARAM_QUERY);
        GraphqlRequest graphqlRequest = new GraphqlRequest(query, RatesEstimationModel.Response.class, requestParams.getParameters());
        graphqlUseCase.clearRequest();
        graphqlUseCase.addRequest(graphqlRequest);
        return graphqlUseCase.createObservable(null).map(graphqlResponse -> {
            RatesEstimationModel.Response response = graphqlResponse.getData(RatesEstimationModel.Response.class);
            return response.getRatesEstimation().getRates();
        }).onErrorReturn(throwable ->
                gson.fromJson(inputStreamToString(context.getResources().openRawResource(R.raw.raw_json_est_shipping)),
                        RatesEstimationModel.Response.class).getRatesEstimation().getRates());
    }

    // temporary for testing
    private String inputStreamToString(InputStream inputStream) {
        try {
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes, 0, bytes.length);
            return new String(bytes);
        } catch (IOException e) {
            return null;
        }
    }

    public static RequestParams createRequestParams(String query, String productId, String userId){
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(PARAM_QUERY, query);
        requestParams.putString(PARAM_PRODUCT_ID, productId);
        requestParams.putString(PARAM_USER_ID, userId);
        return requestParams;
    }
}
