package com.tokopedia.tkpdtrain.homepage.presentation.presenter;

import com.tokopedia.abstraction.base.view.presenter.CustomerPresenter;
import com.tokopedia.tkpdtrain.homepage.presentation.listener.TrainHomepageView;

/**
 * Created by Rizky on 21/02/18.
 */

public interface TrainHomepagePresenter extends CustomerPresenter<TrainHomepageView> {

    void roundTrip();

    void singleTrip();

    void onDepartureDateButtonClicked();

    void onReturnDateButtonClicked();

    void onDepartureDateChange(int year, int month, int dayOfMonth);

    void onReturnDateChange(int year, int month, int dayOfMonth);

    void initialize();
}
