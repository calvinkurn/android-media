package com.tokopedia.tkpdtrain.search.presentation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.design.bottomsheet.BottomSheetBuilder;
import com.tokopedia.design.bottomsheet.adapter.BottomSheetItemClickListener;
import com.tokopedia.design.bottomsheet.custom.CheckedBottomSheetBuilder;
import com.tokopedia.tkpdtrain.R;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.tkpdtrain.search.constant.TrainSortOption;
import com.tokopedia.tkpdtrain.search.di.TrainSearchComponent;
import com.tokopedia.tkpdtrain.search.domain.GetScheduleUseCase;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainSchedule;
import com.tokopedia.usecase.RequestParams;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class TrainSearchFragment extends BaseDaggerFragment implements TrainSearchContract.View {

    private static final String TAG = TrainSearchFragment.class.getSimpleName();

    private TrainSearchPassDataViewModel trainSearchPassDataViewModel;
    private String dateDeparture;
    private int adultPassanger;
    private int infantPassanger;
    private String originCode;
    private String originCity;
    private String destinationCode;
    private String destinationCity;

    @Inject
    TrainSearchPresenter presenter;

    public static TrainSearchFragment newInstance(TrainSearchPassDataViewModel trainSearchPassDataViewModel) {
        TrainSearchFragment fragment = new TrainSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TrainSearchActivity.EXTRA_SEARCH_PASS_DATA, trainSearchPassDataViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    public TrainSearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        presenter.attachView(this);
        return inflater.inflate(R.layout.fragment_train_search, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromFragment();
        showSortBottomSheets();
        presenter.getTrainSchedules();
    }

    private void getDataFromFragment() {
        trainSearchPassDataViewModel = getArguments().getParcelable(TrainSearchActivity.EXTRA_SEARCH_PASS_DATA);
        dateDeparture = trainSearchPassDataViewModel.getDepartureDate();
        adultPassanger = trainSearchPassDataViewModel.getAdult();
        infantPassanger = trainSearchPassDataViewModel.getInfant();
        originCode = trainSearchPassDataViewModel.getOriginStationCode();
        originCity = trainSearchPassDataViewModel.getOriginCityName();
        destinationCode = trainSearchPassDataViewModel.getDestinationStationCode();
        destinationCity = trainSearchPassDataViewModel.getDestinationCityName();
    }

    @Override
    public RequestParams getRequestParam() {
        RequestParams requestParams = RequestParams.create();
        requestParams.putString(GetScheduleUseCase.DATE_SCHEDULE, dateDeparture);
        requestParams.putInt(GetScheduleUseCase.TOTAL_ADULT, adultPassanger);
        requestParams.putInt(GetScheduleUseCase.TOTAL_INFANT, infantPassanger);
        requestParams.putString(GetScheduleUseCase.ORIGIN_CODE, originCode);
        requestParams.putString(GetScheduleUseCase.ORIGIN_CITY, originCity);
        requestParams.putString(GetScheduleUseCase.DEST_CODE, destinationCode);
        requestParams.putString(GetScheduleUseCase.DEST_CITY, destinationCity);
        return requestParams;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TrainSearchComponent.class).inject(this);
    }

    @Override
    public void showSearchResult(List<TrainSchedule> schedules) {
        String trainScheduleStr = "";
        for (TrainSchedule trainSchedule : schedules) {
            trainScheduleStr = trainScheduleStr + trainSchedule.toString() + "\n";
        }
        Log.d(TAG, trainScheduleStr);
    }

    public void showSortBottomSheets() {
        BottomSheetBuilder bottomSheetBuilder = new CheckedBottomSheetBuilder(getActivity())
                .setMode(BottomSheetBuilder.MODE_LIST)
                .addTitleItem("Urutkan");

        bottomSheetBuilder.addItem(TrainSortOption.EARLIEST_DEPARTURE, "Waktu Keberangkatan Terpagi", null);
        bottomSheetBuilder.addItem(TrainSortOption.LATEST_DEPARTURE, "Waktu Keberangkatan Termalam", null);
        bottomSheetBuilder.addItem(TrainSortOption.SHORTEST_DURATION, "Durasi Terpendek", null);
        bottomSheetBuilder.addItem(TrainSortOption.LONGEST_DURATION, "Durasi Terlama", null);
        bottomSheetBuilder.addItem(TrainSortOption.EARLIEST_ARRIVAL, "Waktu Tiba Terpagi", null);
        bottomSheetBuilder.addItem(TrainSortOption.LATEST_ARRIVAL, "Waktu Tiba Termalam", null);
        bottomSheetBuilder.addItem(TrainSortOption.CHEAPEST, "Harga Termurah", null);
        bottomSheetBuilder.addItem(TrainSortOption.MOST_EXPENSIVE, "Harga Termahal", null);

        BottomSheetDialog bottomSheetDialog = bottomSheetBuilder.expandOnStart(true)
                .setItemClickListener(new BottomSheetItemClickListener() {
                    @SuppressWarnings("WrongConstant")
                    @Override
                    public void onBottomSheetItemClick(MenuItem item) {
                        List<String> trains = new ArrayList<>();
                        presenter.getFilteredAndSortedSchedules(0, 200000, "", trains, item.getItemId());
                    }
                })
                .createDialog();
        bottomSheetDialog.show();
    }

}