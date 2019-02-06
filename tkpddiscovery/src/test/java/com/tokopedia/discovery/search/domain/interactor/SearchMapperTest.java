package com.tokopedia.discovery.search.domain.interactor;

import com.tokopedia.discovery.search.domain.model.SearchData;
import com.tokopedia.discovery.search.domain.model.SearchResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SearchMapperTest {
    private SearchMapper searchMapper;

    @Mock
    SearchResponse searchResponseBody;

    @Before
    public void setUp() {
        this.searchMapper = new SearchMapper();
    }

    @Test
    public void onSearchDataResponse_networkNotSuccessAndNullResponseBody_returnEmptyResult() {
        MediaType mediaType
                = MediaType.parse("application/json; charset=utf-8");
        ResponseBody responseBody = ResponseBody.create(
                mediaType, "server failing..."
        );
        Response<SearchResponse> errorSearchResponse = Response.error(responseBody, new okhttp3.Response.Builder() //
                .code(500)
                .protocol(Protocol.HTTP_1_1)
                .message("")
                .request(new Request.Builder().url("http://localhost/").build())
                .build());

        List<SearchData> networkData = searchMapper.call(errorSearchResponse);

        assertEquals(networkData.size(), 0);
    }

    @Test
    public void onSearchDataResponse_networkNotSuccessAndResponseBodyNotNull_returnEmptyResult() {
        MediaType mediaType
                = MediaType.parse("application/json; charset=utf-8");
        ResponseBody responseBody = ResponseBody.create(
                mediaType, "server failing..."
        );
        Response<SearchResponse> errorSearchResponse = Response.error(responseBody, new okhttp3.Response.Builder() //
                .code(500)
                .protocol(Protocol.HTTP_1_1)
                .message("")
                .request(new Request.Builder().url("http://localhost/").build())
                .build());

        List<SearchData> networkData = searchMapper.call(errorSearchResponse);

        assertEquals(networkData.size(), 0);
    }

    @Test
    public void onSearchDataResponse_networkSuccessAndResponseBodyNull_returnEmptyResult() {
        Response<SearchResponse> searchResponse = Response.success(null);

        List<SearchData> networkData = searchMapper.call(searchResponse);

        assertEquals(networkData.size(), 0);
    }

    @Test
    public void onSearchDataResponse_successResultWithNullData_returnEmptyResult() {
        Response<SearchResponse> searchResponse = Response.success(searchResponseBody);
        when(searchResponseBody.getData()).thenReturn(null);

        List<SearchData> networkData = searchMapper.call(searchResponse);

        assertEquals(networkData.size(), 0);
    }

    @Test
    public void onSearchDataResponse_successResultWithDataExist_returnEmptyResult() {
        Response<SearchResponse> searchResponse = Response.success(searchResponseBody);

        searchMapper.call(searchResponse);
        verify(searchResponseBody, times(2)).getData();
    }
}