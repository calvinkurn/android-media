package com.tokopedia.tkpdpdp;

import com.tokopedia.core.base.common.service.TopAdsService;
import com.tokopedia.tkpdpdp.domain.GetAffiliateProductDataUseCase;
import com.tokopedia.usecase.RequestParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestGetAffiliateProductDataUseCase {

    private GetAffiliateProductDataUseCase getAffiliateProductDataUseCase;

    @Mock
    private TopAdsService topAdsService;

    @Before
    public void setUpUseCase(){
        getAffiliateProductDataUseCase = new GetAffiliateProductDataUseCase(
                topAdsService
        );
    }

    @Test
    public void givenRequestParams_whenUseCaseCreateObservable_thenUseCaseGetDataFromTopAdsService(){
        RequestParams requestParams = Mockito.mock(RequestParams.class);
        getAffiliateProductDataUseCase.createObservable(requestParams);
        verify(topAdsService).getPdpAffiliateData(
                anyString(),
                anyString(),
                anyString(),
                anyString()
        );
    }
}
