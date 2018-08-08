package com.tokopedia.loyalty.di.module;

import android.content.Context;

import com.tokopedia.loyalty.di.LoyaltyScope;
import com.tokopedia.loyalty.domain.repository.TokoPointRepository;
import com.tokopedia.loyalty.domain.usecase.FlightCheckVoucherUseCase;
import com.tokopedia.loyalty.domain.usecase.TrainCheckVoucherUseCase;
import com.tokopedia.loyalty.router.LoyaltyModuleRouter;
import com.tokopedia.loyalty.view.interactor.IPromoCodeInteractor;
import com.tokopedia.loyalty.view.interactor.PromoCodeInteractor;
import com.tokopedia.loyalty.view.presenter.IPromoCodePresenter;
import com.tokopedia.loyalty.view.presenter.PromoCodePresenter;
import com.tokopedia.loyalty.view.view.IPromoCodeView;
import com.tokopedia.transactiondata.repository.ICartRepository;

import dagger.Module;
import dagger.Provides;
import rx.subscriptions.CompositeSubscription;

/**
 * @author anggaprasetiyo on 29/11/17.
 */

@Module(includes = {RouterModule.class, ServiceApiModule.class, TransactionApiServiceModule.class})
public class PromoCodeViewModule {

    private final IPromoCodeView view;

    public PromoCodeViewModule(IPromoCodeView view) {
        this.view = view;
    }

    @Provides
    @LoyaltyScope
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @LoyaltyScope
    IPromoCodeInteractor providePromoCodeInteractor(CompositeSubscription compositeSubscription,
                                                    TokoPointRepository loyaltyRepository,
                                                    ICartRepository cartRepository) {
        return new PromoCodeInteractor(compositeSubscription, loyaltyRepository, cartRepository);
    }

    @Provides
    @LoyaltyScope
    IPromoCodePresenter provideIPromoCodePresenter(IPromoCodeInteractor promoCodeInteractor,
                                                   FlightCheckVoucherUseCase flightCheckVoucherUseCase,
                                                   TrainCheckVoucherUseCase trainCheckVoucherUseCase) {
        return new PromoCodePresenter(view, promoCodeInteractor, flightCheckVoucherUseCase,
                trainCheckVoucherUseCase);
    }

    @Provides
    LoyaltyModuleRouter provideLoyaltyViewModule(Context context) {
        if (context instanceof LoyaltyModuleRouter) {
            return (LoyaltyModuleRouter) context;
        }
        throw new RuntimeException("Applicaton should implement LoyaltyModuleRouter");
    }

    @Provides
    FlightCheckVoucherUseCase provideFlightCheckVoucherUseCase(LoyaltyModuleRouter loyaltyModuleRouter) {
        return new FlightCheckVoucherUseCase(loyaltyModuleRouter);
    }

    @Provides
    TrainCheckVoucherUseCase provideTrainCheckVoucherUseCase(LoyaltyModuleRouter loyaltyModuleRouter) {
        return new TrainCheckVoucherUseCase(loyaltyModuleRouter);
    }

}
