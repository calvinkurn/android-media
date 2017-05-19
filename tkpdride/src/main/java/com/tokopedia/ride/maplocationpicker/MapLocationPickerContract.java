package com.tokopedia.ride.maplocationpicker;

import com.tokopedia.core.base.presentation.CustomerPresenter;
import com.tokopedia.core.base.presentation.CustomerView;

/**
 * Created by alvarisi on 5/19/17.
 */

public interface MapLocationPickerContract {
    interface View extends CustomerView{

    }

    interface Presenter extends CustomerPresenter<View>{

    }
}
