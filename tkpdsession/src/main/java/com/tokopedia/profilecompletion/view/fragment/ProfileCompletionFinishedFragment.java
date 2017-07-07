package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.core.people.activity.PeopleInfoDrawerActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionPresenter;
import com.tokopedia.session.R;


/**
 * Created by stevenfredian on 7/3/17.
 */

public class ProfileCompletionFinishedFragment extends BasePresenterFragment<ProfileCompletionPresenter> {

    public static final String TAG = "finished";
    private View finish;


    public static ProfileCompletionFinishedFragment createInstance() {
        return new ProfileCompletionFinishedFragment();
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
        return R.layout.fragment_profile_completion_finish;
    }

    @Override
    protected void initView(View view) {
        finish = view.findViewById(R.id.done);
    }

    @Override
    protected void setViewListener() {
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(
                        PeopleInfoDrawerActivity.createInstance(context, SessionHandler.getLoginID(context))
                );
                getActivity().finish();
            }
        });
    }

    @Override
    protected void initialVar() {

    }

    @Override
    protected void setActionVar() {

    }
}
