package com.tokopedia.tkpdpdp;

import com.tokopedia.core.network.entity.affiliateProductData.Affiliate;
import com.tokopedia.core.product.interactor.CacheInteractor;
import com.tokopedia.core.product.interactor.RetrofitInteractor;
import com.tokopedia.tkpdpdp.domain.GetAffiliateProductDataUseCase;
import com.tokopedia.tkpdpdp.domain.GetWishlistCountUseCase;
import com.tokopedia.tkpdpdp.listener.ProductDetailView;
import com.tokopedia.tkpdpdp.presenter.ProductDetailPresenterImpl;
import com.tokopedia.wishlist.common.listener.WishListActionListener;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Scheduler;
import rx.android.plugins.RxAndroidPlugins;
import rx.android.plugins.RxAndroidSchedulersHook;
import rx.functions.Func1;
import rx.plugins.RxJavaHooks;
import rx.schedulers.Schedulers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class TestProductDetailPresenter {

    @Mock
    private GetWishlistCountUseCase getWishlistCountUseCase;

    @Mock
    private ProductDetailView viewListener;

    @Mock
    private WishListActionListener wishListActionListener;

    @Mock
    private RetrofitInteractor retrofitInteractor;

    @Mock
    private CacheInteractor cacheInteractor;

    @Mock
    private GetAffiliateProductDataUseCase getAffiliateProductDataUseCase;

    private ProductDetailPresenterImpl productDetailPresenter;

    @Before
    public void setUpProductDetailPresenter(){

        MockitoAnnotations.initMocks(this);

        productDetailPresenter = new ProductDetailPresenterImpl(
                getWishlistCountUseCase,
                viewListener,
                wishListActionListener,
                retrofitInteractor,
                cacheInteractor,
                getAffiliateProductDataUseCase
        );
    }

    @Test
    public void givenAffiliateData_whenPresenterRenderAffiliateButton_thenViewRenderAffiliateButton(){
        Affiliate affiliate = Mockito.mock(Affiliate.class);
        productDetailPresenter.renderAffiliateButton(affiliate);
        verify(viewListener, times(1)).renderAffiliateButton(affiliate);
    }
}
