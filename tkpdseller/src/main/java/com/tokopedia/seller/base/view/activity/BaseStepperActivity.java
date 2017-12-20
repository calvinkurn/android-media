package com.tokopedia.seller.base.view.activity;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.tokopedia.seller.R;
import com.tokopedia.seller.base.view.listener.StepperListener;
import com.tokopedia.seller.base.view.model.StepperModel;

import java.util.List;

/**
 * Created by zulfikarrahman on 7/27/17.
 */

public abstract class BaseStepperActivity<T extends StepperModel> extends BaseToolbarActivity implements StepperListener<T> {
    public static final String STEPPER_MODEL_EXTRA = "STEPPER_MODEL_EXTRA";
    private static final int START_PAGE_POSITION = 1;

    private RoundCornerProgressBar progressStepper;

    private int currentPosition = START_PAGE_POSITION;
    protected T stepperModel;

    @NonNull
    protected abstract List<Fragment> getListFragment();

    @CallSuper
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            stepperModel = createNewStepperModel();
        } else {
            stepperModel = savedInstanceState.getParcelable(STEPPER_MODEL_EXTRA);
        }
        progressStepper = (RoundCornerProgressBar) findViewById(R.id.stepper_progress);
        progressStepper.setMax(getListFragment().size());
        progressStepper.setProgress(currentPosition);
        updateToolbarTitle(START_PAGE_POSITION);
    }

    public abstract T createNewStepperModel();

    @Override
    protected void setupFragment(Bundle savedinstancestate) {
        if (getListFragment().size() >= currentPosition) {
            Fragment fragment = getListFragment().get(currentPosition - 1);
            Bundle bundle = new Bundle();
            bundle.putParcelable(STEPPER_MODEL_EXTRA, stepperModel);
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.parent_view, fragment, fragment.getClass().getSimpleName())
                    .commit();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_base_stepper_toolbar;
    }

    @Override
    public void goToNextPage(T stepperModel) {
        if (stepperModel!= null) {
            this.stepperModel = stepperModel;
        }
        currentPosition++;
        goToPosition(currentPosition);
    }

    private void goToPreviousPage() {
        currentPosition--;
        goToPosition(currentPosition);
    }

    private void goToPosition(int position) {
        progressStepper.setProgress(position);
        updateToolbarTitle(position);
        setupFragment(null);
    }

    @Override
    public void onBackPressed() {
        onBackEvent();
    }

    private void onBackEvent() {
        if (currentPosition > 1) {
            goToPreviousPage();
        } else {
            super.onBackPressed();
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == android.R.id.home) {
            onBackPressed();
            return false;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateToolbarTitle(int position) {
        getSupportActionBar().setTitle(getString(R.string.top_ads_label_stepper, position, getListFragment().size()));
    }

    @Override
    public T getStepperModel() {
        return stepperModel;
    }

    @Override
    public void finishPage() {
        finish();
    }

    @Override
    protected boolean isToolbarWhite() {
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEPPER_MODEL_EXTRA, stepperModel);
    }
}