package com.tokopedia.tkpdtrain.homepage.presentation.presenter;

import com.tokopedia.tkpdtrain.homepage.presentation.listener.ITrainHomepageView;

/**
 * Created by Rizky on 21/02/18.
 */

public interface ITrainHomepagePresenter {

    void takeView(ITrainHomepageView trainHomepageView);

    void roundTrip();

    void singleTrip();

    void onDepartureDateButtonClicked();

    void onReturnDateButtonClicked();

    void onDepartureDateChange(int year, int month, int dayOfMonth);

    void onReturnDateChange(int year, int month, int dayOfMonth);

}
