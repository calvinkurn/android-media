package com.tokopedia.tkpdtrain.homepage.presentation.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.tokopedia.tkpdtrain.common.presentation.TrainBaseActivity;
import com.tokopedia.tkpdtrain.homepage.presentation.fragment.TrainPassengerPickerFragment;
import com.tokopedia.tkpdtrain.homepage.presentation.model.TrainPassengerViewModel;

/**
 * @author Rizky on 13/03/18.
 */

public class TrainPassengerPickerActivity extends TrainBaseActivity implements TrainPassengerPickerFragment.OnFragmentInteractionListener {

    public static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";

    public static Intent getCallingIntent(Activity activity, TrainPassengerViewModel viewModel) {
        Intent intent = new Intent(activity, TrainPassengerPickerActivity.class);
        intent.putExtra(EXTRA_PASS_DATA, viewModel);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {
        TrainPassengerViewModel passData = getIntent().getParcelableExtra(EXTRA_PASS_DATA);
        return TrainPassengerPickerFragment.newInstance(passData);
    }

    @Override
    public void actionSavePassenger(TrainPassengerViewModel passData) {
        setIntent(getIntent().putExtra(EXTRA_PASS_DATA, passData));
        setResult(RESULT_OK, getIntent());
        finish();
    }

}
