package com.tokopedia.tkpdtrain.homepage.presentation.view;


import android.app.DatePickerDialog;
import android.os.Bundle;
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

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.common.constant.TrainAppScreen;
import com.tokopedia.tkpdtrain.common.presentation.TextInputView;
import com.tokopedia.tkpdtrain.homepage.di.TrainHomepageComponent;
import com.tokopedia.tkpdtrain.homepage.presentation.listener.TrainHomepageView;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainHomepageViewModel;
import com.tokopedia.tkpdtrain.homepage.presentation.presenter.TrainHomepagePresenterImpl;
import com.tokopedia.tkpdtrain.station.presentation.TrainStationsActivity;

import java.util.Calendar;
import java.util.Date;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainHomepageFragment extends BaseDaggerFragment implements TrainHomepageView {

    private static final int ORIGIN_STATION_REQUEST_CODE = 1001;
    private static final int DESTINATION_STATION_REQUEST_CODE = 1002;
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


    private TrainHomepageViewModel viewModel;

    @Inject
    TrainHomepagePresenterImpl trainHomepagePresenterImpl;

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

        layoutOriginStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(TrainStationsActivity.getCallingIntent(getActivity()), ORIGIN_STATION_REQUEST_CODE);
            }
        });
        layoutOriginStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(TrainStationsActivity.getCallingIntent(getActivity()), DESTINATION_STATION_REQUEST_CODE);
            }
        });

        buttonOneWayTrip.setSelected(true);

        buttonOneWayTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainHomepagePresenterImpl.singleTrip();
            }
        });

        buttonRoundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainHomepagePresenterImpl.roundTrip();
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
                trainHomepagePresenterImpl.onDepartureDateButtonClicked();
            }
        });

        textInputViewDateReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trainHomepagePresenterImpl.onReturnDateButtonClicked();
            }
        });

        trainHomepagePresenterImpl = new TrainHomepagePresenterImpl();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        trainHomepagePresenterImpl.attachView(this);
        trainHomepagePresenterImpl.initialize();
    }

    @Override
    public void renderSingleTripView(TrainHomepageViewModel trainHomepageViewModel) {
        buttonOneWayTrip.setTextColor(getResources().getColor(R.color.white));
        buttonRoundTrip.setTextColor(getResources().getColor(R.color.grey_400));
        buttonOneWayTrip.setSelected(true);
        buttonRoundTrip.setSelected(false);
        textInputViewDateReturn.setVisibility(View.GONE);
        separatorDateReturn.setVisibility(View.GONE);

        textInputViewDateDeparture.setText(trainHomepageViewModel.getDepartureDateFmt());
        textInputViewPassenger.setText(trainHomepageViewModel.getPassengerFmt());
        if (trainHomepageViewModel.getOriginStation() != null) {
            tvOriginStationLabel.setText(trainHomepageViewModel.getStationTextForView(getContext(), true));
        } else {
            tvOriginStationLabel.setText(null);
            tvOriginStationLabel.setHint(R.string.train_homepage_origin_station_hint);
        }
        if (trainHomepageViewModel.getDestinationStation() != null) {
            tvDestinationStationLabel.setText(trainHomepageViewModel.getStationTextForView(getContext(), false));
        } else {
            tvDestinationStationLabel.setText(null);
            tvDestinationStationLabel.setHint(R.string.train_homepage_destination_station_hint);
        }
    }

    @Override
    public void renderRoundTripView(TrainHomepageViewModel trainHomepageViewModel) {
        buttonOneWayTrip.setTextColor(getResources().getColor(R.color.grey_400));
        buttonRoundTrip.setTextColor(getResources().getColor(R.color.white));
        buttonOneWayTrip.setSelected(false);
        buttonRoundTrip.setSelected(true);
        textInputViewDateReturn.setVisibility(View.VISIBLE);
        separatorDateReturn.setVisibility(View.VISIBLE);

        textInputViewDateDeparture.setText(trainHomepageViewModel.getDepartureDateFmt());
        textInputViewDateReturn.setText(trainHomepageViewModel.getReturnDateFmt());
        textInputViewPassenger.setText(trainHomepageViewModel.getPassengerFmt());
        if (trainHomepageViewModel.getOriginStation() != null) {
            tvOriginStationLabel.setText(trainHomepageViewModel.getStationTextForView(getContext(), true));
        } else {
            tvOriginStationLabel.setText(null);
            tvOriginStationLabel.setHint(R.string.train_homepage_origin_station_hint);
        }
        if (trainHomepageViewModel.getDestinationStation() != null) {
            tvDestinationStationLabel.setText(trainHomepageViewModel.getStationTextForView(getContext(), false));
        } else {
            tvDestinationStationLabel.setText(null);
            tvDestinationStationLabel.setHint(R.string.train_homepage_destination_station_hint);
        }
    }

    @Override
    public void showDepartureDatePickerDialog(Date selectedDate, Date minDate, Date maxDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(selectedDate);
        DatePickerDialog datePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                trainHomepagePresenterImpl.onDepartureDateChange(year, month, dayOfMonth);
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
                trainHomepagePresenterImpl.onReturnDateChange(year, month, dayOfMonth);
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

    @Override
    public void setHomepageViewModel(TrainHomepageViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public TrainHomepageViewModel getHomepageViewModel() {
        return viewModel;
    }

    @SuppressWarnings("Range")
    private void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    protected String getScreenName() {
        return TrainAppScreen.HOMEPAGE;
    }

    @Override
    protected void initInjector() {
        getComponent(TrainHomepageComponent.class).inject(this);
    }
}
