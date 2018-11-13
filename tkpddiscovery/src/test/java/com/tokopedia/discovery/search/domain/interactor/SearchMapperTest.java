package com.tokopedia.discovery.search.domain.interactor;

import com.google.gson.Gson;
import com.tokopedia.discovery.SearchDataAssetJson;
import com.tokopedia.discovery.UnitTestFileUtils;
import com.tokopedia.discovery.search.domain.model.SearchResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

@RunWith(BlockJUnit4ClassRunner.class)
public class SearchMapperTest {
    private SearchMapper searchMapper;
    private UnitTestFileUtils unitTestFileUtils;

    @Before
    public void setUp() {
        this.searchMapper = new SearchMapper();
        unitTestFileUtils = new UnitTestFileUtils();
    }

    @Test
    public void onSearchDataResponse_successResult_returnListSearchData() {
        Gson gson = new Gson();
        SearchResponse dataResponse =
                gson.fromJson(unitTestFileUtils.getJsonFromAsset(SearchDataAssetJson.SEARCH_DATA_SUCCESS),
                        SearchResponse.class);
        Response<SearchResponse> searchResponseSuccess = Response.success(dataResponse);
        assertThat(searchMapper.call(searchResponseSuccess), is(searchResponseSuccess.body().getData()));
    }

    @Test
    public void onSearchDataResponse_successResultWithNullData_returnListSearchData() {
        Gson gson = new Gson();
        SearchResponse dataResponse =
                gson.fromJson(unitTestFileUtils.getJsonFromAsset(SearchDataAssetJson.SEARCH_DATA_SUCCESS),
                        SearchResponse.class);
        dataResponse.setData(null);
        Response<SearchResponse> searchResponseSuccess = Response.success(dataResponse);
        assertThat(searchMapper.call(searchResponseSuccess), is(new ArrayList<>()));
    }

    @Test
    public void onSearchDataResponse_failedResult_returnListSearchData() {
        MediaType mediaType
                = MediaType.parse("application/json; charset=utf-8");
        ResponseBody responseBody = ResponseBody.create(
                mediaType, "error"
        );
        Response<SearchResponse> searchResponseFailed = Response.error(responseBody, new okhttp3.Response.Builder() //
                .code(403)
                .protocol(Protocol.HTTP_1_1)
                .message("")
                .request(new Request.Builder().url("http://localhost/").build())
                .build());
        assertThat(searchMapper.call(searchResponseFailed), is(new ArrayList<>()));
    }
}