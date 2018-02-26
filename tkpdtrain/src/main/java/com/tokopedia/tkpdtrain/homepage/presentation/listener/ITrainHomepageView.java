package com.tokopedia.tkpdtrain.homepage.presentation.listener;

import android.support.annotation.StringRes;

import com.tokopedia.tkpdtrain.homepage.presentation.model.KAIHomepageViewModel;

import java.util.Date;

/**
 * Created by Rizky on 21/02/18.
 */

public interface ITrainHomepageView {

    void renderSingleTripView(KAIHomepageViewModel kaiHomepageViewModel);

    void renderRoundTripView(KAIHomepageViewModel kaiHomepageViewModel);

    void showDepartureDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

    void showReturnDatePickerDialog(Date selectedDate, Date minDate, Date maxDate);

    void showDepartureDateShouldAtLeastToday(@StringRes int resId);

    void showDepartureDateMax100Days(@StringRes int resId);

    void showReturnDateShouldGreaterOrEqual(@StringRes int resId);

    void showReturnDateMax100Days(@StringRes int resId);

}
