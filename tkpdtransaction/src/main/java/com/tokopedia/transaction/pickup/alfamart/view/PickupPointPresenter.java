package com.tokopedia.transaction.pickup.alfamart.view;

import com.tokopedia.core.base.presentation.BaseDaggerPresenter;
import com.tokopedia.transaction.pickup.alfamart.domain.usecase.GetPickupPointsUseCase;

import javax.inject.Inject;

/**
 * Created by Irfan Khoirul on 22/12/17.
 */

public class PickupPointPresenter extends BaseDaggerPresenter<PickupPointContract.View>
        implements PickupPointContract.Presenter {

    private GetPickupPointsUseCase getPickupPointsUseCase;

    @Inject
    public PickupPointPresenter(GetPickupPointsUseCase getPickupPointsUseCase) {
        this.getPickupPointsUseCase = getPickupPointsUseCase;
    }

    @Override
    public void attachView(PickupPointContract.View view) {
        super.attachView(view);
    }

    @Override
    public void detachView() {
        super.detachView();
    }

}
