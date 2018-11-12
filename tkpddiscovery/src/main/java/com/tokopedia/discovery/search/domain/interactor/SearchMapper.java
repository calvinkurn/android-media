package com.tokopedia.discovery.search.domain.interactor;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.tokopedia.discovery.R;
import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.domain.model.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;
import rx.functions.Func1;

/**
 * @author erry on 23/02/17.
 */

public class SearchMapper implements Func1<Response<SearchResponse>, List<SearchData>> {
    private String mDefaultErrorMessage;
    private Context context;
    private Gson gson;

    public SearchMapper(Context context, Gson gson) {
        this.context = context;
        this.gson = gson;
        mDefaultErrorMessage = context.getString(R.string.msg_network_error);
    }

    public SearchMapper(Gson gson) {
        this.gson = gson;
    }

    @Override
    public List<SearchData> call(Response<SearchResponse> response) {
        if(response.isSuccessful() && response.body() != null){
            SearchResponse searchResponse = response.body();

            if (searchResponse.getData() != null) {
                return searchResponse.getData();
            }
        }
        return new ArrayList<>();
    }
}
