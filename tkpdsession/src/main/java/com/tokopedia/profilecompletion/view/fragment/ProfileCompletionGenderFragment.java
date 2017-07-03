package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenter;
import com.tokopedia.session.R;


/**
 * Created by stevenfredian on 7/3/17.
 */

public class ProfileCompletionGenderFragment extends BasePresenterFragment<ProfileCompletionPresenter> {


    public static ProfileCompletionGenderFragment createInstance(ProfileCompletionFragment profileCompletionFragment) {
        return new ProfileCompletionGenderFragment();
    }

    @Override
    protected boolean isRetainInstance() {
        return false;
    }

    @Override
    protected void onFirstTimeLaunched() {

    }

    @Override
    public void onSaveState(Bundle state) {

    }

    @Override
    public void onRestoreState(Bundle savedState) {

    }

    @Override
    protected boolean getOptionsMenuEnable() {
        return false;
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void initialListener(Activity activity) {

    }

    @Override
    protected void setupArguments(Bundle arguments) {

    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_profile_completion_gender;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
