package com.tokopedia.discovery.search.domain.interactor;

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

    public SearchMapper() {
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
