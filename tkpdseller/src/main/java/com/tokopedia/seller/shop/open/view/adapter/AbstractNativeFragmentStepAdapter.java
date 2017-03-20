package com.tokopedia.seller.shop.open.view.adapter;

/**
 * Created by zulfikarrahman on 3/17/17.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.StepAdapter;
import com.stepstone.stepper.viewmodel.StepViewModel;
import com.tokopedia.seller.R;

/**
 * A base adapter class which returns step system fragments to use inside of the {@link com.stepstone.stepper.StepperLayout}.
 */
public abstract class AbstractNativeFragmentStepAdapter
        extends FragmentPagerAdapter
        implements StepAdapter {

    @NonNull
    private final FragmentManager mFragmentManager;

    @NonNull
    protected final Context context;

    public AbstractNativeFragmentStepAdapter(@NonNull FragmentManager fm, @NonNull Context context) {
        super(fm);
        this.mFragmentManager = fm;
        this.context = context;
    }

    @Override
    public final Fragment getItem(@IntRange(from = 0) int position) {
        return (Fragment) createStep(position);
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public Step findStep(@IntRange(from = 0) int position) {
        String fragmentTag =  "android:switcher:" + R.id.ms_stepPager + ":" + this.getItemId(position);
        return (Step) mFragmentManager.findFragmentByTag(fragmentTag);
    }

    /** {@inheritDoc} */
    @NonNull
    @Override
    public StepViewModel getViewModel(@IntRange(from = 0) int position) {
        return new StepViewModel.Builder(context).create();
    }

    /** {@inheritDoc} */
    @Override
    public final PagerAdapter getPagerAdapter() {
        return this;
    }
}