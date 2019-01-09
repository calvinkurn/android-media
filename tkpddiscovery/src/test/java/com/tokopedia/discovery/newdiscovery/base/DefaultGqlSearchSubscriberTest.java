package com.tokopedia.discovery.newdiscovery.base;

import com.tokopedia.discovery.newdiscovery.domain.gql.SearchProductGqlResponse;
import com.tokopedia.discovery.newdiscovery.util.SearchParameter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DefaultGqlSearchSubscriberTest {

    DefaultGqlSearchSubscriber<BaseDiscoveryContract.View> defaultGqlSearchSubscriber;

    @Mock
    SearchParameter searchParameter;

    @Mock
    BaseDiscoveryContract.View discoveryView;

    @Mock
    boolean forSearch;

    @Mock
    boolean imageSearch;

    @Before
    public void setUp(){
        defaultGqlSearchSubscriber = new DefaultGqlSearchSubscriber<BaseDiscoveryContract.View>(
                searchParameter,
                forSearch,
                discoveryView,
                imageSearch
        );
    }

    @Test
    public void onError_givenThrowableError_handleResponseErrorOnView() {
        defaultGqlSearchSubscriber.onError(new Throwable());
        verify(discoveryView).onHandleResponseError();
    }

    @Test
    public void onHandleSearch_givenSearchProductGqlRespons_handleSearchOnView() {
        SearchProductGqlResponse searchProductGqlResponse = Mockito.mock(SearchProductGqlResponse.class);
        defaultGqlSearchSubscriber.onHandleSearch(searchProductGqlResponse);
        verify(discoveryView).onHandleResponseSearch(any());
    }

    @Test
    public void onNext() {
    }

    @Test
    public void onHandleApplink() {
    }

    @Test
    public void onHandleSearch() {
    }
}