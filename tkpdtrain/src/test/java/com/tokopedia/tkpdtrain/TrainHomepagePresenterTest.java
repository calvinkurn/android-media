package com.tokopedia.tkpdtrain;

import com.tokopedia.tkpdtrain.common.util.KAIDateUtil;
import com.tokopedia.tkpdtrain.homepage.presentation.listener.ITrainHomepageView;
import com.tokopedia.tkpdtrain.homepage.presentation.model.KAIHomepageViewModel;
import com.tokopedia.tkpdtrain.homepage.presentation.presenter.TrainHomepagePresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.Calendar;
import java.util.Date;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by Rizky on 22/02/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest(KAIDateUtil.class)
public class TrainHomepagePresenterTest {

    private Calendar fakeCurrentCalendar;

    private KAIHomepageViewModel kaiHomepageViewModel;
    private ITrainHomepageView trainHomepageView;

    private TrainHomepagePresenter trainHomepagePresenter;

    @Before
    public void setupTrainHomepagePresenterTest() {
        kaiHomepageViewModel = mock(KAIHomepageViewModel.class);
        trainHomepageView = mock(ITrainHomepageView.class);

        PowerMockito.mockStatic(KAIDateUtil.class);

        // setup today's date
        int fakeYear = 1994;
        int fakeMonth = 9;
        int fakeDate = 17;
        fakeCurrentCalendar = setupFakeCalendar(fakeYear, fakeMonth, fakeDate);
        when(KAIDateUtil.getCurrentCalendar()).thenReturn(fakeCurrentCalendar);
        Date fakeCurrentDate = fakeCurrentCalendar.getTime();
        when(KAIDateUtil.getCurrentDate()).thenReturn(fakeCurrentDate); // 17-9-1994

        trainHomepagePresenter = new TrainHomepagePresenter(kaiHomepageViewModel);
        trainHomepagePresenter.takeView(trainHomepageView);
    }

    @Test
    public void singleTrip() {
        trainHomepagePresenter.singleTrip();

        verify(kaiHomepageViewModel).setOneWay(true);
        verify(trainHomepageView).renderSingleTripView(kaiHomepageViewModel);
    }

    @Test
    public void roundTrip() {
        trainHomepagePresenter.roundTrip();

        verify(kaiHomepageViewModel).setOneWay(false);
        verify(trainHomepageView).renderRoundTripView(kaiHomepageViewModel);
    }

    @Test
    public void onDepartureDateButtonClicked() {
        Date departureDate = KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 1);
        String departureDateString = KAIDateUtil.dateToString(departureDate, KAIDateUtil.DEFAULT_FORMAT);

        when(kaiHomepageViewModel.getDepartureDate()).thenReturn(departureDateString);

        Date minDate = KAIDateUtil.getCurrentDate();
        Date maxDate = KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 100);
        Date selectedDate = KAIDateUtil.stringToDate(kaiHomepageViewModel.getDepartureDate());

        trainHomepagePresenter.onDepartureDateButtonClicked();

        verify(trainHomepageView).showDepartureDatePickerDialog(selectedDate, minDate, maxDate);
    }

    @Test
    public void onReturnDateButtonClicked() {
        Date departureDate = KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 1);
        String departureDateString = KAIDateUtil.dateToString(departureDate, KAIDateUtil.DEFAULT_FORMAT);
        Date returnDate = KAIDateUtil.addTimeToSpesificDate(departureDate, Calendar.DATE, 2);
        String returnDateString = KAIDateUtil.dateToString(returnDate, KAIDateUtil.DEFAULT_FORMAT);

        when(kaiHomepageViewModel.getReturnDate()).thenReturn(returnDateString);
        when(kaiHomepageViewModel.getDepartureDate()).thenReturn(departureDateString);

        Date selectedDate = KAIDateUtil.stringToDate(kaiHomepageViewModel.getReturnDate());
        Date minDate = KAIDateUtil.stringToDate(kaiHomepageViewModel.getDepartureDate());
        Date maxDate = KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 100);

        trainHomepagePresenter.onReturnDateButtonClicked();

        verify(trainHomepageView).showReturnDatePickerDialog(selectedDate, minDate, maxDate);
    }

    // today's date   = 17 Oct 1994
    // departure date = 18 Oct 1994
    @Test
    public void onDepartureDateChange_SelectedDateValid_OneWayTrip_RenderSingleTripView() {
        // set limit date: today's date + 100
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 25-1-1995

        when(kaiHomepageViewModel.isOneWay()).thenReturn(true);

        int newFakeDepartureYear = 1994;
        int newFakeDepartureMonth = 9;
        int newFakeDepartureDate = 18;

        trainHomepagePresenter.onDepartureDateChange(newFakeDepartureYear, newFakeDepartureMonth, newFakeDepartureDate);

        verify(trainHomepageView).renderSingleTripView(kaiHomepageViewModel);
    }

    // today's date   = 17 Oct 1994
    // departure date = 18 Oct 1994
    // return date    = 22 Oct 1994
    @Test
    public void onDepartureDateChange_SelectedDateValid_RoundTrip_RenderRoundTripView() {
        // set limit date: today's date + 100
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 25-1-1995

        // set return date
        int fakeReturnYear = 1994;
        int fakeReturnMonth = 9;
        int fakeReturnDate = 22;
        Calendar returnCalendar = setupFakeCalendar(fakeReturnYear, fakeReturnMonth, fakeReturnDate);
        Date returnDate = returnCalendar.getTime();
        when(kaiHomepageViewModel.getReturnDate()).thenReturn("22-09-1994");
        when(KAIDateUtil.stringToDate("22-09-1994")).thenReturn(returnDate); // 22-9-1994

        when(kaiHomepageViewModel.isOneWay()).thenReturn(false);

        int newFakeDepartureYear = 1994;
        int newFakeDepartureMonth = 9;
        int newFakeDepartureDate = 18;

        trainHomepagePresenter.onDepartureDateChange(newFakeDepartureYear, newFakeDepartureMonth, newFakeDepartureDate);

        verify(trainHomepageView).renderRoundTripView(kaiHomepageViewModel);
    }

    // today's date   = 17 Oct 1994
    // departure date = 26 Feb 1995
    @Test
    public void onDepartureDateChange_SelectedDepartureDateGreaterThan100Days_ShowError() {
        // set limit date: today's date + 100
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 25-1-1995

        int newFakeDepartureYear = 1995;
        int newFakeDepartureMonth = 1;
        int newFakeDepartureDate = 26;

        trainHomepagePresenter.onDepartureDateChange(newFakeDepartureYear, newFakeDepartureMonth, newFakeDepartureDate);

        verify(trainHomepageView).showDepartureDateMax100Days(R.string.kai_homepage_departure_max_100_days_from_today_error);
    }

    // today's date   = 17 Oct 1994
    // departure date = 16 Oct 1994
    @Test
    public void onDepartureDateChange_SelecedDepartureDateSmallerThanToday_ShowError() {
        // set limit date
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date fakeLimitDate = fakeCurrentCalendar.getTime();
        when(KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(fakeLimitDate); // 25-1-1995

        int newFakeDepartureYear = 1994;
        int newFakeDepartureMonth = 9;
        int newFakeDepartureDate = 16;

        trainHomepagePresenter.onDepartureDateChange(newFakeDepartureYear, newFakeDepartureMonth, newFakeDepartureDate);

        verify(trainHomepageView).showDepartureDateShouldAtLeastToday(R.string.kai_homepage_departure_should_atleast_today_error);
    }

    // today's date   = 17 Oct 1994
    // departure date = 19 Oct 1994
    // return date    = 21 Oct 1994
    @Test
    public void onReturnDateChange_SelectedDateValid_RenderRoundTripView() {
        // set limit date
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 17-9-1994

        // set departure date
        Calendar fakeDepartureCalendar = setupFakeCalendar(1994, 9, 19);
        Date fakeDepartureDate = fakeDepartureCalendar.getTime();
        when(kaiHomepageViewModel.getDepartureDate()).thenReturn("19-09-1994");
        when(KAIDateUtil.stringToDate("19-09-1994")).thenReturn(fakeDepartureDate);

        int newFakeReturnYear = 1994;
        int newFakeReturnMonth = 9;
        int newFakeReturnDate = 21;

        trainHomepagePresenter.onReturnDateChange(newFakeReturnYear, newFakeReturnMonth, newFakeReturnDate);

        verify(trainHomepageView).renderRoundTripView(kaiHomepageViewModel);
    }

    // today's date   = 17 Oct 1994
    // departure date = 19 Oct 1994
    // return date    = 26 Feb 1995
    @Test
    public void onReturnDateChange_SelectedReturnDateGreaterThan100Days_ShowError() {
        // set limit date
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 17-9-1994

        // set departure date
        Calendar fakeDepartureCalendar = setupFakeCalendar(1994, 9, 19);
        Date fakeDepartureDate = fakeDepartureCalendar.getTime();
        when(kaiHomepageViewModel.getDepartureDate()).thenReturn("19-09-1994");
        when(KAIDateUtil.stringToDate("19-09-1994")).thenReturn(fakeDepartureDate);

        int newFakeReturnYear = 1995;
        int newFakeReturnMonth = 9;
        int newFakeReturnDate = 26;

        trainHomepagePresenter.onReturnDateChange(newFakeReturnYear, newFakeReturnMonth, newFakeReturnDate);

        verify(trainHomepageView).showReturnDateMax100Days(R.string.kai_homepage_return_max_100_days_from_today_error);
    }

    // today's date   = 17 Oct 1994
    // departure date = 19 Oct 1994
    // return date    = 18 Oct 1994
    @Test
    public void onReturnDateChange_SelectedReturnDateSmallerThanDepartureDate_ShowError() {
        // set limit date
        fakeCurrentCalendar.add(Calendar.DATE, 100);
        Date limitDate = fakeCurrentCalendar.getTime();
        when(KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 100)).thenReturn(limitDate); // 17-9-1994

        // set departure date
        Calendar fakeDepartureCalendar = setupFakeCalendar(1994, 9, 19);
        Date fakeDepartureDate = fakeDepartureCalendar.getTime();
        when(kaiHomepageViewModel.getDepartureDate()).thenReturn("19-09-1994");
        when(KAIDateUtil.stringToDate("19-09-1994")).thenReturn(fakeDepartureDate);

        int newFakeReturnYear = 1994;
        int newFakeReturnMonth = 9;
        int newFakeReturnDate = 18;

        trainHomepagePresenter.onReturnDateChange(newFakeReturnYear, newFakeReturnMonth, newFakeReturnDate);

        verify(trainHomepageView).showReturnDateShouldGreaterOrEqual(R.string.kai_homepage_return_should_greater_equal_error);
    }

    private Calendar setupFakeCalendar(int year, int month, int date) {
        Calendar fakeCalendar = Calendar.getInstance();
        fakeCalendar.set(Calendar.YEAR, year);
        fakeCalendar.set(Calendar.MONTH, month);
        fakeCalendar.set(Calendar.DATE, date);
        return fakeCalendar;
    }

}
