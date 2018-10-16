package com.tokopedia.tkpdpdp.affiliate;

import com.tokopedia.tkpdpdp.RxAndroidTestPlugins;
import com.tokopedia.tkpdpdp.RxJavaTestPlugins;
import com.tokopedia.usecase.RequestParams;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductAffiliatePresenterTest {

    private static final int WANTED_NUMBER_OF_INVOCATIONS_1 = 1;

    private ProductAffiliatePresenter presenter;

    @Mock
    public ProductAffiliateContract.View view;
    @Mock
    public GetProductAffiliateUseCase getProductAffiliateUseCase;

    @Before
    public void setup() {
        RxJavaTestPlugins.setImmediateScheduler();
        RxAndroidTestPlugins.setImmediateScheduler();
        MockitoAnnotations.initMocks(this);
        presenter = new ProductAffiliatePresenter(
                getProductAffiliateUseCase
        );
        presenter.attachView(view);
    }

    @After
    public void tearDown() throws Exception {
        RxJavaTestPlugins.resetJavaTestPlugins();
        RxAndroidTestPlugins.resetAndroidTestPlugins();
    }

    @Test
    public void whenIsLoading_TRUE_requestProductDetail_NEVER() {
        //given
        ProductAffiliatePresenter spyPresenter = spy(presenter);
        when(view.isOnRequestingProductDetail()).thenReturn(true);

        //when
        spyPresenter.requestProductDetail(anyString());

        //then
        verify(getProductAffiliateUseCase, never())
                .execute(any(RequestParams.class), any(GetProductDetailSubscriber.class));

    }

    @Test
    public void whenIsLoading_FALSE_requestProductDetail_ONCE() {
        //given
        ProductAffiliatePresenter spyPresenter = spy(presenter);
        when(view.isOnRequestingProductDetail()).thenReturn(false);

        //when
        spyPresenter.requestProductDetail(anyString());

        //then
        verify(getProductAffiliateUseCase, atLeastOnce())
                .execute(any(RequestParams.class), any(GetProductDetailSubscriber.class));
        verify(getProductAffiliateUseCase, atMost(WANTED_NUMBER_OF_INVOCATIONS_1))
                .execute(any(RequestParams.class), any(GetProductDetailSubscriber.class));
    }

}
