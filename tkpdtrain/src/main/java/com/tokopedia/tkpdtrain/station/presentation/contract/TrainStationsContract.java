package com.tokopedia.tkpdtrain.station.presentation.contract;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;

/**
 * Created by alvarisi on 3/5/18.
 */

public interface TrainStationsContract {
    interface View extends CustomerView{

    }

    interface Presenter extends CustomerPresenter<View>{

    }
}
