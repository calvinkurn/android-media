package com.tokopedia.tkpdtrain.search.presentation.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainSearchPassDataViewModel;
import com.tokopedia.tkpdtrain.search.data.typedef.ScheduleTypeDef;
import com.tokopedia.tkpdtrain.search.presentation.activity.TrainReturnSearchActivity;
import com.tokopedia.tkpdtrain.search.presentation.activity.TrainSearchActivity;
import com.tokopedia.tkpdtrain.search.presentation.model.TrainScheduleViewModel;

/**
 * Created by nabillasabbaha on 3/19/18.
 */

public class TrainDepartureSearchFragment extends TrainSearchFragment {

    public static TrainDepartureSearchFragment newInstance(TrainSearchPassDataViewModel trainSearchPassDataViewModel) {
        TrainDepartureSearchFragment fragment = new TrainDepartureSearchFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TrainSearchActivity.EXTRA_SEARCH_PASS_DATA, trainSearchPassDataViewModel);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected void getDataFromFragment() {
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
    protected int getScheduleVariant() {
        return ScheduleTypeDef.DEPARTURE_SCHEDULE;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onItemClicked(TrainScheduleViewModel trainScheduleViewModel) {
        if (!trainSearchPassDataViewModel.isOneWay()) {
            startActivityForResult(TrainReturnSearchActivity.getCallingIntent(getActivity(),
                    trainSearchPassDataViewModel, trainScheduleViewModel.getIdSchedule()), 11);
        }
    }
}
