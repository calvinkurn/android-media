package com.tokopedia.tkpdtrain.station.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter;
import com.tokopedia.tkpdtrain.station.presentation.contract.TrainStationsContract;

import javax.inject.Inject;

/**
 * @author  by alvarisi on 3/5/18.
 */

public class TrainStationsPresenter extends BaseDaggerPresenter<TrainStationsContract.View> implements TrainStationsContract.Presenter {

    @Inject
    public TrainStationsPresenter() {
    }
}
