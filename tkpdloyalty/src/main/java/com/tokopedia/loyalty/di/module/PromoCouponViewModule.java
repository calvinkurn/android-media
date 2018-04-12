package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.tokopedia.core.base.di.qualifier.ApplicationContext;
import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.domain.repository.TokoPointRepository;
import com.tokopedia.loyalty.domain.usecase.FlightCheckVoucherUseCase;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.interactor.IPromoCouponInteractor;
import com.tokopedia.loyalty.view.interactor.PromoCouponInteractor;
import com.tokopedia.loyalty.view.presenter.IPromoCouponPresenter;
import com.tokopedia.loyalty.view.presenter.PromoCouponPresenter;
import com.tokopedia.loyalty.view.view.IPromoCouponView;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 27/11/17.
 */
@Module(includes = {ServiceApiModule.class})
public class PromoCouponViewModule {

    private final IPromoCouponView view;

    public PromoCouponViewModule(IPromoCouponView view) {
        this.view = view;
    }

    @Provides
    @LoyaltyScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @LoyaltyScope
    IPromoCouponInteractor provideIPromoCouponInteractor(CompositeSubscription compositeSubscription,
                                                         TokoPointRepository loyaltyRepository) {
        return new PromoCouponInteractor(compositeSubscription, loyaltyRepository);
    }

    @Provides
    @LoyaltyScope
    IPromoCouponPresenter provideIPromoCouponPresenter(IPromoCouponInteractor promoCouponInteractor, FlightCheckVoucherUseCase flightCheckVoucherUseCase) {
        return new PromoCouponPresenter(view, promoCouponInteractor, flightCheckVoucherUseCase);
    }


    @Provides
    LoyaltyModuleRouter provideLoyaltyViewModule(@ApplicationContext Context context) {
        if (context instanceof LoyaltyModuleRouter) {
            return (LoyaltyModuleRouter) context;
        }
        throw new RuntimeException("Applicaton should implement LoyaltyModuleRouter");
    }


    @Provides
    FlightCheckVoucherUseCase provideFlightCheckVoucherUseCase(LoyaltyModuleRouter loyaltyModuleRouter) {
        return new FlightCheckVoucherUseCase(loyaltyModuleRouter);
    }


}
