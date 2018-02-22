package com.tokopedia.tkpdtrain.homepage.presentation.view;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.common.presentation.TextInputView;
import com.tokopedia.tkpdtrain.common.util.KAIDateUtil;
import com.tokopedia.tkpdtrain.homepage.presentation.listener.ITrainHomepageView;
import com.tokopedia.tkpdtrain.homepage.presentation.model.KAIHomepageViewModel;
import com.tokopedia.tkpdtrain.homepage.presentation.model.KAIPassengerViewModel;
import com.tokopedia.tkpdtrain.homepage.presentation.presenter.TrainHomepagePresenter;

import java.util.Calendar;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainHomepageFragment extends Fragment implements ITrainHomepageView {

    private final int DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL = 2;

    private AppCompatButton buttonOneWayTrip;
    private AppCompatButton buttonRoundTrip;

    private LinearLayout layoutOriginStation;
    private LinearLayout layoutDestinationStation;

    private AppCompatTextView tvOriginStationLabel;
    private AppCompatTextView tvDestinationStationLabel;

    private AppCompatImageView imageReverseOriginDestitation;

    private TextInputView textInputViewDateDeparture;
    private TextInputView textInputViewDateReturn;

    private TextInputView textInputViewPassenger;

    private AppCompatButton buttonSearchTicket;

    private View separatorDateReturn;

    private TrainHomepagePresenter trainHomepagePresenter;

    public TrainHomepageFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_train_homepage, container, false);

        buttonOneWayTrip = view.findViewById(R.id.button_one_way_trip);
        buttonRoundTrip = view.findViewById(R.id.button_round_trip);
        layoutOriginStation = view.findViewById(R.id.layout_origin_station);
        layoutDestinationStation = view.findViewById(R.id.layout_destination_station);
        tvOriginStationLabel = view.findViewById(R.id.tv_origin_station_label);
        tvDestinationStationLabel = view.findViewById(R.id.tv_destination_station_label);
        imageReverseOriginDestitation = view.findViewById(R.id.image_reverse_origin_destination);
        textInputViewDateDeparture = view.findViewById(R.id.text_input_view_date_departure);
        textInputViewDateReturn = view.findViewById(R.id.text_input_view_date_return);
        textInputViewPassenger = view.findViewById(R.id.text_input_view_passenger);
        buttonSearchTicket = view.findViewById(R.id.button_search_ticket);
        separatorDateReturn = view.findViewById(R.id.separator_date_return);

        buttonOneWayTrip.setSelected(true);

        buttonOneWayTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainHomepagePresenter.singleTrip();
            }
        });

        buttonRoundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainHomepagePresenter.roundTrip();
            }
        });

        imageReverseOriginDestitation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation shake = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate);
                shake.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                imageReverseOriginDestitation.startAnimation(shake);
            }
        });

        textInputViewDateDeparture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainHomepagePresenter.onDepartureDateButtonClicked();
            }
        });

        textInputViewDateReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainHomepagePresenter.onReturnDateButtonClicked();
            }
        });

        KAIHomepageViewModel kaiHomepageViewModel = initializeViewModel();
        trainHomepagePresenter = new TrainHomepagePresenter(kaiHomepageViewModel);

        return view;
    }

    private KAIHomepageViewModel initializeViewModel() {
        Date departureDate = KAIDateUtil.addTimeToCurrentDate(Calendar.DATE, 1); // departure date = today + 1
        String departureDateString = KAIDateUtil.dateToString(departureDate, KAIDateUtil.DEFAULT_FORMAT);
        String departureDateFmtString = KAIDateUtil.dateToString(departureDate, KAIDateUtil.DEFAULT_VIEW_FORMAT);

        Date returnDate = KAIDateUtil.addTimeToSpesificDate(departureDate, Calendar.DATE, DEFAULT_RANGE_OF_DEPARTURE_AND_ARRIVAL); // return date = departure date + 2
        String returnDateString = KAIDateUtil.dateToString(returnDate, KAIDateUtil.DEFAULT_FORMAT);
        String returnDateFmtString = KAIDateUtil.dateToString(returnDate, KAIDateUtil.DEFAULT_VIEW_FORMAT);

        KAIPassengerViewModel passData = new KAIPassengerViewModel.Builder()
                .setAdult(1)
                .build();
        String passengerFmt = buildPassengerTextFormatted(passData);

        return new KAIHomepageViewModel.Builder()
                .setKAIPassengerViewModel(passData)
                .setIsOneWay(true)
                .setDepartureDate(departureDateString)
                .setReturnDate(returnDateString)
                .setDepartureDateFmt(departureDateFmtString)
                .setReturnDateFmt(returnDateFmtString)
                .setPassengerFmt(passengerFmt)
                .build();
    }

    @NonNull
    private String buildPassengerTextFormatted(KAIPassengerViewModel passData) {
        String passengerFmt = "";
        if (passData.getAdult() > 0) {
            passengerFmt = passData.getAdult() + " " + getString(R.string.kai_homepage_adult_passenger);
            if (passData.getInfant() > 0) {
                passengerFmt += ", " + passData.getInfant() + " " + getString(R.string.kai_homepage_adult_infant);
            }
        }
        return passengerFmt;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trainHomepagePresenter.takeView(this);
        buttonOneWayTrip.performClick();
    }

    @Override
    public void renderSingleTripView(KAIHomepageViewModel kaiHomepageViewModel) {
        buttonOneWayTrip.setTextColor(getResources().getColor(R.color.white));
        buttonRoundTrip.setTextColor(getResources().getColor(R.color.grey_400));
        buttonOneWayTrip.setSelected(true);
        buttonRoundTrip.setSelected(false);
        textInputViewDateReturn.setVisibility(View.GONE);
        separatorDateReturn.setVisibility(View.GONE);

        textInputViewDateDeparture.setText(kaiHomepageViewModel.getDepartureDateFmt());
        textInputViewPassenger.setText(kaiHomepageViewModel.getPassengerFmt());
        if (kaiHomepageViewModel.getOriginStation() != null) {
            tvOriginStationLabel.setText(kaiHomepageViewModel.getStationTextForView(getContext(), true));
        } else {
            tvOriginStationLabel.setText(null);
            tvOriginStationLabel.setHint("Kota/Stasiun Asal");
        }
        if (kaiHomepageViewModel.getDestinationStation() != null) {
            tvDestinationStationLabel.setText(kaiHomepageViewModel.getStationTextForView(getContext(), false));
        } else {
            tvDestinationStationLabel.setText(null);
            tvDestinationStationLabel.setHint("Kota/Stasiun Tujuan");
        }
    }

    @Override
    public void renderRoundTripView(KAIHomepageViewModel kaiHomepageViewModel) {
        buttonOneWayTrip.setTextColor(getResources().getColor(R.color.grey_400));
        buttonRoundTrip.setTextColor(getResources().getColor(R.color.white));
        buttonOneWayTrip.setSelected(false);
        buttonRoundTrip.setSelected(true);
        textInputViewDateReturn.setVisibility(View.VISIBLE);
        separatorDateReturn.setVisibility(View.VISIBLE);

        textInputViewDateDeparture.setText(kaiHomepageViewModel.getDepartureDateFmt());
        textInputViewDateReturn.setText(kaiHomepageViewModel.getReturnDateFmt());
        textInputViewPassenger.setText(kaiHomepageViewModel.getPassengerFmt());
        if (kaiHomepageViewModel.getOriginStation() != null) {
            tvOriginStationLabel.setText(kaiHomepageViewModel.getStationTextForView(getContext(), true));
        } else {
            tvOriginStationLabel.setText(null);
            tvOriginStationLabel.setHint("Kota/Stasiun Asal");
        }
        if (kaiHomepageViewModel.getDestinationStation() != null) {
            tvDestinationStationLabel.setText(kaiHomepageViewModel.getStationTextForView(getContext(), false));
        } else {
            tvDestinationStationLabel.setText(null);
            tvDestinationStationLabel.setHint("Kota/Stasiun Tujuan");
        }
    }

    @Override
    public void showDepartureDatePickerDialog(Date selectedDate, Date minDate, Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                trainHomepagePresenter.onDepartureDateChange(year, month, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
        datePicker1.setMaxDate(maxDate.getTime());
        datePicker.show();
    }

    @Override
    public void showReturnDatePickerDialog(Date selectedDate, Date minDate, Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                trainHomepagePresenter.onReturnDateChange(year, month, dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        DatePicker datePicker1 = datePicker.getDatePicker();
        datePicker1.setMinDate(minDate.getTime());
        datePicker1.setMaxDate(maxDate.getTime());
        datePicker.show();
    }

    @Override
    public void showDepartureDateShouldAtLeastToday(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showDepartureDateMax100Days(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showReturnDateShouldGreaterOrEqual(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @Override
    public void showReturnDateMax100Days(@StringRes int resId) {
        showMessageErrorInSnackBar(resId);
    }

    @SuppressWarnings("Range")
    private void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

}
