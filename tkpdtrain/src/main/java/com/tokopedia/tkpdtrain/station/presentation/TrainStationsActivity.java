package com.tokopedia.tkpdtrain.station.presentation;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.tkpdtrain.common.di.utils.TrainComponentUtils;
import com.tokopedia.tkpdtrain.station.di.DaggerTrainStationsComponent;
import com.tokopedia.tkpdtrain.station.di.TrainStationsComponent;
import com.tokopedia.tkpdtrain.station.presentation.adapter.viewmodel.TrainStationViewModel;

/**
 * Created by alvarisi on 3/5/18.
 */

public class TrainStationsActivity extends BaseSimpleActivity implements HasComponent<TrainStationsComponent>, TrainStationsFragment.OnFragmentInteractionListener {
    public static final String EXTRA_SELECTED_STATION  = "EXTRA_SELECTED_STATION";
    private TrainStationsComponent trainStationsComponent;

    public static Intent getCallingIntent(Activity activity) {
        Intent intent = new Intent(activity, TrainStationsActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        return TrainStationsFragment.newInstance();
    }

    @Override
    public TrainStationsComponent getComponent() {
        if (trainStationsComponent == null) {
            trainStationsComponent = DaggerTrainStationsComponent.builder().trainComponent(TrainComponentUtils.getTrainComponent(getApplication())).build();
        }
        return trainStationsComponent;
    }

    @Override
    public void onStationClicked(TrainStationViewModel viewModel) {
        Intent intent = getIntent();
        intent.putExtra(EXTRA_SELECTED_STATION, viewModel);
        setResult(RESULT_OK, intent);
        finish();
    }
}
