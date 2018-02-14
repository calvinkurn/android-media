package com.tokopedia.profilecompletion.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.core.app.BasePresenterFragment;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.people.activity.PeopleInfoDrawerActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.profilecompletion.view.presenter.ProfileCompletionContract;
import com.tokopedia.session.R;

import butterknife.ButterKnife;


/**
 * Created by stevenfredian on 7/3/17.
 */

public class ProfileCompletionFinishedFragment extends BaseDaggerFragment {

    public static final String TAG = "finished";
    private View finish;


    public static ProfileCompletionFinishedFragment createInstance() {
        return new ProfileCompletionFinishedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_profile_completion_finish, container, false);
        initView(parentView);
        setViewListener();
        return parentView;
    }

    protected void initView(View view) {
        finish = view.findViewById(R.id.done);
    }

    protected void setViewListener() {
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(
                        PeopleInfoDrawerActivity.createInstance(getActivity(), SessionHandler.getLoginID(getActivity()))
                );
                getActivity().finish();
            }
        });
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

    }
}
