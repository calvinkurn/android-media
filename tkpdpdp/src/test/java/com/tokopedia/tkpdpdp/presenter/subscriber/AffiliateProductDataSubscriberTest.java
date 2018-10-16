package com.tokopedia.tkpdpdp.presenter.subscriber;

import com.tokopedia.tkpdpdp.entity.TopAdsPdpAffiliateResponse;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AffiliateProductDataSubscriberTest {

    @Mock
    private ProductDetailView viewListener;

    private AffiliateProductDataSubscriber affiliateProductDataSubscriber;

    @Before
    public void setUpSubscriber(){
        MockitoAnnotations.initMocks(this);
        affiliateProductDataSubscriber = new AffiliateProductDataSubscriber(viewListener);
    }

    @Test
    public void givenAffiliateResponse_whenSubscriberOnNext_thenPresenterRenderAffiliateButton(){
        // Given
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate response = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.class);
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data data = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.class);
        List<TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate> affiliateList = Mockito.mock(List.class);
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate affiliate = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate.class);

        when(response.getData()).thenReturn(data);
        when(data.getAffiliate()).thenReturn(affiliateList);
        when(affiliateList.isEmpty()).thenReturn(false);
        when(affiliateList.get(0)).thenReturn(affiliate);
        when(affiliate.getAdId()).thenReturn(1);
        when(affiliate.getProductId()).thenReturn(1);

        // when
        affiliateProductDataSubscriber.onNext(response);

        // then
        verify(viewListener, times(1)).renderAffiliateButton(any());
    }

    @Test
    public void givenResponseNull_whenSubscriberOnNext_thenPresenterNotRenderAffiliateButton(){
        // given

        // when
        affiliateProductDataSubscriber.onNext(null);

        // then
        verify(viewListener, never()).renderAffiliateButton(any());
    }

    @Test
    public void givenDataNull_whenSubscriberOnNext_thenPresenterNotRenderAffiliateButton() {
        // given
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate response = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.class);

        when(response.getData()).thenReturn(null);

        // when
        affiliateProductDataSubscriber.onNext(response);

        // then
        verify(viewListener, never()).renderAffiliateButton(any());
    }

    @Test
    public void givenAffiliateNull_whenSubscriberOnNext_thenPresenterNotRenderAffiliateButton() {
        // given
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate response = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.class);
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data data = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.class);

        when(response.getData()).thenReturn(data);
        when(data.getAffiliate()).thenReturn(null);

        // when
        affiliateProductDataSubscriber.onNext(response);

        // then
        verify(viewListener, never()).renderAffiliateButton(any());
    }

    @Test
    public void givenAffiliateIsEmpty_whenSubscriberOnNext_thenPresenterNotRenderAffiliateButton() {
        // given
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate response = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.class);
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data data = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.class);
        List<TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate> affiliateList = Mockito.mock(List.class);

        when(response.getData()).thenReturn(data);
        when(data.getAffiliate()).thenReturn(affiliateList);
        when(affiliateList.isEmpty()).thenReturn(true);

        // when
        affiliateProductDataSubscriber.onNext(response);

        // then
        verify(viewListener, never()).renderAffiliateButton(any());
    }

    @Test
    public void givenAdIdInvalid_whenSubscriberOnNext_thenPresenterNotRenderAffiliateButton() {
        // given
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate response = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.class);
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data data = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.class);
        List<TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate> affiliateList = Mockito.mock(List.class);
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate affiliate = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate.class);

        when(response.getData()).thenReturn(data);
        when(data.getAffiliate()).thenReturn(affiliateList);
        when(affiliateList.isEmpty()).thenReturn(false);
        when(affiliateList.get(0)).thenReturn(affiliate);
        when(affiliate.getAdId()).thenReturn(0);

        // when
        affiliateProductDataSubscriber.onNext(response);

        // then
        verify(viewListener, never()).renderAffiliateButton(any());
    }

    @Test
    public void givenProductIdInvalid_whenSubscriberOnNext_thenPresenterNotRenderAffiliateButton() {
        // given
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate response = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.class);
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data data = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.class);
        List<TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate> affiliateList = Mockito.mock(List.class);
        TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate affiliate = Mockito.mock(TopAdsPdpAffiliateResponse.TopAdsPdpAffiliate.Data.PdpAffiliate.class);

        when(response.getData()).thenReturn(data);
        when(data.getAffiliate()).thenReturn(affiliateList);
        when(affiliateList.isEmpty()).thenReturn(false);
        when(affiliateList.get(0)).thenReturn(affiliate);
        when(affiliate.getAdId()).thenReturn(1);
        when(affiliate.getProductId()).thenReturn(0);

        // when
        affiliateProductDataSubscriber.onNext(response);

        // then
        verify(viewListener, never()).renderAffiliateButton(any());
    }
}