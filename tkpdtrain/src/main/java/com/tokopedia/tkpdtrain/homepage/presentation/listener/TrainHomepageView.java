package com.tokopedia.tkpdtrain.homepage.presentation.listener;

import android.app.Activity;
import android.support.annotation.StringRes;

import com.tokopedia.abstraction.base.view.listener.CustomerView;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainHomepageViewModel;

import java.util.Date;

/**
 * Created by Rizky on 21/02/18.
 */

public interface TrainHomepageView extends CustomerView{

    void renderSingleTripView(TrainHomepageViewModel trainHomepageViewModel);

    void renderRoundTripView(TrainHomepageViewModel trainHomepageViewModel);

    void showDepartureDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

    void showReturnDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

    void showDepartureDateShouldAtLeastToday(@StringRes int resId);

    void showDepartureDateMax100Days(@StringRes int resId);

    void showReturnDateShouldGreaterOrEqual(@StringRes int resId);

    void showReturnDateMax100Days(@StringRes int resId);

    TrainHomepageViewModel getHomepageViewModel();

    Activity getActivity();

    void setHomepageViewModel(TrainHomepageViewModel viewModel);
}
