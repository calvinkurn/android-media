package com.tokopedia.profilecompletion.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.SessionRouter;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.session.R;


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
                Intent intent = ((SessionRouter) getActivity().getApplicationContext())
                        .getTopProfileIntent(getActivity(),
                                SessionHandler.getLoginID(getActivity()));
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                getActivity().startActivity(intent);
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
