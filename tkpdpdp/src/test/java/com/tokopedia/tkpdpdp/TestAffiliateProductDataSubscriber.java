package com.tokopedia.tkpdpdp;

import com.tokopedia.core.network.entity.affiliateProductData.Affiliate;
import com.tokopedia.core.network.entity.affiliateProductData.AffiliateProductDataResponse;
import com.tokopedia.core.network.entity.affiliateProductData.Data;
import com.tokopedia.tkpdpdp.presenter.ProductDetailPresenter;
import com.tokopedia.tkpdpdp.presenter.subscriber.AffiliateProductDataSubscriber;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;

import retrofit2.Response;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestAffiliateProductDataSubscriber {

    @Mock
    private ProductDetailPresenter productDetailPresenter;

    private AffiliateProductDataSubscriber affiliateProductDataSubscriber;

    @Before
    public void setUpSubscriber(){
        MockitoAnnotations.initMocks(this);
        affiliateProductDataSubscriber = new AffiliateProductDataSubscriber(productDetailPresenter);
    }

    @Test
    public void givenAffiliateResponse_whenSubscriberOnNext_thenPresenterRenderAffiliateButton(){
        Response<AffiliateProductDataResponse> response = mockAffiliateProductDataResponse();
        affiliateProductDataSubscriber.onNext(response);
        verify(productDetailPresenter, times(1)).renderAffiliateButton(any());
    }

    @Test
    public void givenNullAffiliateResponse_whenSubscriberOnNext_thenPresenterNotRenderAffiliateButton(){
        Response<AffiliateProductDataResponse> response = mockAffiliateProductDataResponseWithNullAffiliate();
        affiliateProductDataSubscriber.onNext(response);
        verify(productDetailPresenter, never()).renderAffiliateButton(any());
    }

    private Response<AffiliateProductDataResponse> mockAffiliateProductDataResponse() {
        AffiliateProductDataResponse affiliateProductDataResponse =
                Mockito.mock(AffiliateProductDataResponse.class);
        Affiliate affiliate =
                Mockito.mock(Affiliate.class);
        Data data =
                Mockito.mock(Data.class);
        List<Affiliate> affiliateList =
                Mockito.mock(List.class);

        when(affiliateProductDataResponse.getData())
                .thenReturn(data);
        when(affiliateProductDataResponse.getData().getAffiliate())
                .thenReturn(affiliateList);
        when(affiliateProductDataResponse.getData().getAffiliate().get(0))
                .thenReturn(affiliate);

        Response<AffiliateProductDataResponse> response =
                Response.success(affiliateProductDataResponse);
        return response;
    }

    private Response<AffiliateProductDataResponse> mockAffiliateProductDataResponseWithNullAffiliate() {
        AffiliateProductDataResponse affiliateProductDataResponse =
                Mockito.mock(AffiliateProductDataResponse.class);
        Affiliate affiliate =
                null;
        Data data =
                Mockito.mock(Data.class);
        List<Affiliate> affiliateList =
                Mockito.mock(List.class);

        when(affiliateProductDataResponse.getData())
                .thenReturn(data);
        when(affiliateProductDataResponse.getData().getAffiliate())
                .thenReturn(affiliateList);
        when(affiliateProductDataResponse.getData().getAffiliate().get(0))
                .thenReturn(affiliate);

        Response<AffiliateProductDataResponse> response =
                Response.success(affiliateProductDataResponse);
        return response;
    }
}
